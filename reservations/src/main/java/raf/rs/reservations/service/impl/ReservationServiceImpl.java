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
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.exception.AlreadyExistsException;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.mapper.ReservationMapper;
import raf.rs.reservations.repository.ReservationRepository;
import raf.rs.reservations.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final TaskScheduler scheduler;
    private final ReservationRepository reservationRepository;
    private final RestTemplate userServiceRestTemplate;
    private final JmsTemplate jmsTemplate;
    private final String destination;

    public ReservationServiceImpl(
        TaskScheduler scheduler, ReservationRepository reservationRepository,
        RestTemplate userServiceRestTemplate,
        JmsTemplate jmsTemplate,
        @Value("${destination.sendMail}") String destination
    ) {
        this.scheduler = scheduler;
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
        final AppointmentDto appointment = reservationCreateDto.getAppointment();
        final boolean appointmentExists = this.reservationRepository.existsByAppointment_Id(appointment.getId());
        if (appointmentExists) {
            throw new AlreadyExistsException("Reservation already exists");
        }

        final UserDto userDto = this.findById(reservationCreateDto.getUserId());
        if (userDto == null) {
            throw new NotFoundException("Failed to retrieve user by id %s".formatted(reservationCreateDto.getUserId()));
        }

        final RestaurantDto restaurantDto = appointment.getTablesDto().getRestaurantDto();
        final Reservation reservation = ReservationMapper.toEntity(reservationCreateDto);
        final boolean benefits = this.hasAppliedBenefits(userDto.getReservationsNum(), restaurantDto);

        this.reservationRepository.save(reservation);

        final UserDto managerDto = this.findByRestaurantId(restaurantDto.getId());
        this.sendNotifications(userDto, managerDto, benefits, restaurantDto, appointment);

        // After the reservation was saved we need to increase
        // The reservation count for the user
        try {
            this.userServiceRestTemplate.exchange("/user/increaseReservations/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.POST, null, Void.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to increase reservation count for user " + e.getMessage());
        }

        this.scheduleReminder(userDto, reservation);
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

        final Long userId = reservation.getUserId();
        final Long restaurantId = reservation.getAppointment().getTable().getRestaurant().getId();

        final UserDto clientDto = this.findById(userId);
        final UserDto managerDto = this.findByRestaurantId(restaurantId);

        this.reservationRepository.delete(reservation);

        // If user cancelled, we need to contact the user service
        // To decrease the reservation number
        // Otherwise just send out the emails
        if (userCancelled) {
            // If manager cancelled, synchronously contact the user service to decrease the reservation number
            try {
                this.userServiceRestTemplate.exchange("/user/decreaseReservations/%s".formatted(userId), HttpMethod.POST, null, Void.class);
            } catch (HttpClientErrorException e) {
                throw new NotFoundException("Failed to decrease reservation count for user " + e.getMessage());
            }

            final NotificationRequestDto managerMesage = NotificationRequestDto.of(
                "Client cancelled a reservation",
                managerDto.getEmail(),
                reservation.getAppointment().getTime(),
                reservation.getAppointment().getTable().getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, managerMesage);
            return;
        }

        final NotificationRequestDto clientMessage = NotificationRequestDto.of(
            "Manager cancelled your reservation",
            clientDto.getEmail(),
            reservation.getAppointment().getTime(),
            reservation.getAppointment().getTable().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
    }

    private boolean hasAppliedBenefits(Integer reservationCount, RestaurantDto restaurantDto) {
        return reservationCount >= restaurantDto.getDiscountAfterXReservations() || restaurantDto.getFreeItemEachXReservations() % reservationCount == 0;
    }

    private void sendNotifications(UserDto dto, UserDto managerDto, boolean benefits, RestaurantDto restaurantDto, AppointmentDto appointmentDto) {
        if (benefits) {
            final NotificationRequestDto clientMessage = NotificationRequestDto.of(
                "Reservation Confirmed (With Benefits)",
                dto.getEmail(),
                restaurantDto.getName(),
                LocalDateTime.now(),
                appointmentDto.getTablesDto().getId()
            );

            final NotificationRequestDto managerMessage = NotificationRequestDto.of(
                "Client created a reservation with benefits",
                managerDto.getEmail(),
                LocalDateTime.now(),
                appointmentDto.getTablesDto().getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, clientMessage);
            this.jmsTemplate.convertAndSend(this.destination, managerMessage);
            return;
        }

        final NotificationRequestDto clientMessage = NotificationRequestDto.of(
            "Reservation Confirmed",
            dto.getEmail(),
            restaurantDto.getName(),
            LocalDateTime.now(),
            appointmentDto.getTablesDto().getId()
        );

        final NotificationRequestDto managerMessage = NotificationRequestDto.of(
            "Client created a reservation",
            managerDto.getEmail(),
            LocalDateTime.now(),
            appointmentDto.getTablesDto().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
        this.jmsTemplate.convertAndSend(this.destination, managerMessage);
    }

    private String findUserEmail(Long userId) {
        return this.findById(userId).getEmail();
    }

    private UserDto findById(Long userId) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/user/%s".formatted(userId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to retrieve user by id %s".formatted(userId));
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("Failed to retrieve user by id %s".formatted(userId));
        }

        return responseEntity.getBody();
    }

    private UserDto findByRestaurantId(Long restaurantId) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/user/manager/%s".formatted(restaurantId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to manager of restaurant %s".formatted(restaurantId));
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("Failed to manager of restaurant %s".formatted(restaurantId));
        }

        return responseEntity.getBody();
    }
}
