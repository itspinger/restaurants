package raf.rs.reservations.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import raf.rs.reservations.client.notificationservice.dto.NotificationRequestDto;
import raf.rs.reservations.client.userservice.dto.UserDto;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.domain.Table;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.exception.AlreadyExistsException;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.repository.ReservationRepository;
import raf.rs.reservations.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final TaskScheduler scheduler;
    private final AppointmentRepository appointmentRepository;
    private final ReservationRepository reservationRepository;
    private final RestTemplate userServiceRestTemplate;
    private final JmsTemplate jmsTemplate;
    private final String destination;

    public ReservationServiceImpl(
        TaskScheduler scheduler,
        AppointmentRepository appointmentRepository,
        ReservationRepository reservationRepository,
        RestTemplate userServiceRestTemplate,
        JmsTemplate jmsTemplate,
        @Value("${destination.sendMail}") String destination
    ) {
        this.scheduler = scheduler;
        this.appointmentRepository = appointmentRepository;
        this.reservationRepository = reservationRepository;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @Override
    public Reservation findReservationById(Long id) {
        return this.reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find reservation with this id"));
    }

    @Override
    public void createReservation(ReservationCreateDto reservationCreateDto) {
        final Long appointmentId = reservationCreateDto.getAppointmentId();
        final Appointment appointment = this.appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new NotFoundException("Appointment with this id does not exist"));

        // After we've fetched the appointment
        // We have the data for needed reservation
        // But first we need to check whether a reservation was already created
        // For this appointment
        // If so, disallow the user to create a reservation
        final boolean appointmentExists = this.reservationRepository.existsByAppointment_Id(appointment.getId());
        if (appointmentExists) {
            throw new AlreadyExistsException("Reservation already exists");
        }

        // Try to find the info of the user which created the reservation
        // Check if the user is banned here maybe??
        final UserDto userDto = this.findById(reservationCreateDto.getUserId());
        if (userDto == null) {
            throw new NotFoundException("User does not exist");
        }

        final Restaurant restaurant = appointment.getTable().getRestaurant();
        final Reservation reservation = this.createReservation(reservationCreateDto, appointment);
        final boolean benefits = this.hasAppliedBenefits(userDto.getReservationCount() + 1, restaurant);

        this.reservationRepository.save(reservation);

        final UserDto managerDto = this.findByRestaurantId(restaurant.getId());
        this.sendNotifications(userDto, managerDto, benefits, restaurant, appointment);

        // After the reservation was saved we need to increase
        // The reservation count for the user
        try {
            this.userServiceRestTemplate.exchange("/admin/increaseReservations/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.POST, null, Void.class);
        } catch (HttpClientErrorException e) {
            throw new InternalError();
        }

        this.scheduleReminder(userDto, reservation);
    }

    private Reservation createReservation(ReservationCreateDto dto, Appointment appointment) {
        final Reservation reservation = new Reservation();
        reservation.setUserId(dto.getUserId());
        reservation.setAppointment(appointment);
        return reservation;
    }

    private void scheduleReminder(UserDto userDto, Reservation reservation) {
        final LocalDateTime scheduledTime = reservation.getAppointment().getTime();
        final LocalDateTime reminderTime = scheduledTime.minusHours(1);
        final Instant reminderInstant = reminderTime.atZone(ZoneId.systemDefault()).toInstant();

        // Trenutno vreme je LocalDateTime.now()
        // Ako je reminderTime after LocalDateTime.now()
        // Onda zakazati, u suprotnom ne raditi nista
        if (reminderTime.isAfter(LocalDateTime.now())) {
            this.scheduler.schedule(() -> {
                final NotificationRequestDto requestDto = NotificationRequestDto.of(
                    "Reservation Reminder",
                    userDto.getEmail(),
                    userDto.getFirstName(),
                    reservation.getAppointment().getTable().getId()
                );

                this.jmsTemplate.convertAndSend(this.destination, requestDto);
            }, reminderInstant);
        }
    }

    @Override
    public void cancelReservation(Long reservationId, boolean userCancelled) {
        final Reservation reservation = this.findReservationById(reservationId);
        final Appointment appointment = reservation.getAppointment();
        final Table table = appointment.getTable();
        final Restaurant restaurant = table.getRestaurant();
        final Long userId = reservation.getUserId();

        final UserDto clientDto = this.findById(userId);
        final UserDto managerDto = this.findByRestaurantId(restaurant.getId());

        this.reservationRepository.delete(reservation);

        // If user cancelled, we need to contact the user service
        // To decrease the reservation number
        // Otherwise just send out the emails
        if (userCancelled) {
            // If manager cancelled, synchronously contact the user service to decrease the reservation number
            try {
                this.userServiceRestTemplate.exchange("/admin/decreaseReservations/%s".formatted(userId), HttpMethod.POST, null, Void.class);
            } catch (HttpClientErrorException e) {
                throw new InternalError();
            }

            final NotificationRequestDto managerMesage = NotificationRequestDto.of(
                "Client cancelled a reservation",
                managerDto.getEmail(),
                appointment.getTime(),
                table.getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, managerMesage);
            return;
        }

        final NotificationRequestDto clientMessage = NotificationRequestDto.of(
            "Manager cancelled your reservation",
            clientDto.getEmail(),
            appointment.getTime(),
            table.getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
    }

    private boolean hasAppliedBenefits(int reservationCount, Restaurant restaurant) {
        final boolean discount = restaurant.getFreeItemEachXReservations() != -1 && reservationCount >= restaurant.getDiscountAfterXReservations();
        final boolean freeItem = restaurant.getFreeItemEachXReservations() != -1 && restaurant.getFreeItemEachXReservations() % reservationCount == 0;
        return discount || freeItem;
    }

    private void sendNotifications(UserDto dto, UserDto managerDto, boolean benefits, Restaurant restaurant, Appointment appointment) {
        if (benefits) {
            final NotificationRequestDto clientMessage = NotificationRequestDto.of(
                "Reservation Confirmed (With Benefits)",
                dto.getEmail(),
                restaurant.getName(),
                LocalDateTime.now(),
                appointment.getTable().getId()
            );

            final NotificationRequestDto managerMessage = NotificationRequestDto.of(
                "Client created a reservation with benefits",
                managerDto.getEmail(),
                LocalDateTime.now(),
                appointment.getTable().getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, clientMessage);
            this.jmsTemplate.convertAndSend(this.destination, managerMessage);
            return;
        }

        final NotificationRequestDto clientMessage = NotificationRequestDto.of(
            "Reservation Confirmed",
            dto.getEmail(),
            restaurant.getName(),
            LocalDateTime.now(),
            appointment.getTable().getId()
        );

        final NotificationRequestDto managerMessage = NotificationRequestDto.of(
            "Client created a reservation",
            managerDto.getEmail(),
            LocalDateTime.now(),
            appointment.getTable().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
        this.jmsTemplate.convertAndSend(this.destination, managerMessage);
    }

    private UserDto findById(Long userId) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/user/%s".formatted(userId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new InternalError();
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("This user does not exist");
        }

        return responseEntity.getBody();
    }

    private UserDto findByRestaurantId(Long restaurantId) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/manager/%s".formatted(restaurantId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new InternalError();
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("This restaurant does not have a manager");
        }

        return responseEntity.getBody();
    }
}
