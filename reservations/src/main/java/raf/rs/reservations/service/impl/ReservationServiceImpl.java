package raf.rs.reservations.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import io.github.resilience4j.retry.Retry;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import raf.rs.reservations.client.notificationservice.dto.NotificationCategory;
import raf.rs.reservations.client.notificationservice.dto.NotificationMessage;
import raf.rs.reservations.client.userservice.dto.UserDto;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.ReservationDto;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.domain.Table;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.dto.ReservationFilterDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.dto.SuccessMessageDto;
import raf.rs.reservations.exception.AlreadyExistsException;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.repository.ReservationRepository;
import raf.rs.reservations.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ModelMapper modelMapper;
    private final TaskScheduler scheduler;
    private final AppointmentRepository appointmentRepository;
    private final ReservationRepository reservationRepository;
    private final RestTemplate userServiceRestTemplate;
    private final JmsTemplate jmsTemplate;
    private final String destination;
    private final Retry userRetry;

    public ReservationServiceImpl(
        ModelMapper modelMapper, TaskScheduler scheduler,
        AppointmentRepository appointmentRepository,
        ReservationRepository reservationRepository,
        RestTemplate userServiceRestTemplate,
        JmsTemplate jmsTemplate,
        @Value("${destination.sendMail}") String destination, Retry userRetry
    ) {
        this.modelMapper = modelMapper;
        this.scheduler = scheduler;
        this.appointmentRepository = appointmentRepository;
        this.reservationRepository = reservationRepository;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.userRetry = userRetry;
    }

    @Override
    public Reservation findReservationById(Long id) {
        return this.reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Could not find reservation with this id"));
    }

    @Override
    public SuccessMessageDto createReservation(ReservationCreateDto reservationCreateDto) {
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
      //  final UserDto userDto = this.findById(reservationCreateDto.getUserId());
        final UserDto userDto = Retry.decorateSupplier(userRetry,()->this.findById(reservationCreateDto.getUserId())).get();
        if (userDto == null) {
            throw new NotFoundException("User does not exist");
        }

        final Restaurant restaurant = appointment.getTable().getRestaurant();
        final Reservation reservation = this.createReservation(reservationCreateDto, appointment);
        final boolean benefits = this.hasAppliedBenefits(userDto.getReservationCount() + 1, restaurant);

        this.reservationRepository.save(reservation);

        final UserDto managerDto = this.findById(restaurant.getManagerId());
        this.sendNotifications(userDto, managerDto, benefits, restaurant, appointment);

        // After the reservation was saved we need to increase
        // The reservation count for the user
        try {
            this.userServiceRestTemplate.exchange("/admin/increaseReservations/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.POST, null, Void.class);
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            System.out.println("Headers: " + e.getResponseHeaders());
            e.printStackTrace();
            throw new InternalError("admin increse reservation num");
        }

        this.scheduleReminder(userDto, reservation);
        return SuccessMessageDto.success("Successfully created a reservation for you");
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
        final Table table = reservation.getAppointment().getTable();

        // Trenutno vreme je LocalDateTime.now()
        // Ako je reminderTime after LocalDateTime.now()
        // Onda zakazati, u suprotnom ne raditi nista
        if (reminderTime.isAfter(LocalDateTime.now())) {
            this.scheduler.schedule(() -> {
                final NotificationMessage reminder = NotificationMessage.of(
                    NotificationCategory.RESERVATION_REMINDER,
                    userDto.getEmail(),
                    userDto.getFirstName(),
                    table.getId(),
                    table.getRestaurant().getName()
                );

                this.jmsTemplate.convertAndSend(this.destination, reminder);
            }, reminderInstant);
        }
    }

    @Override
    public SuccessMessageDto cancelReservation(Long reservationId, boolean userCancelled) {
        final Reservation reservation = this.findReservationById(reservationId);
        final Appointment appointment = reservation.getAppointment();
        final Table table = appointment.getTable();
        final Restaurant restaurant = table.getRestaurant();
        final Long userId = reservation.getUserId();

        final UserDto clientDto = this.findById(userId);
        final UserDto managerDto = this.findById(restaurant.getManagerId());

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

            final NotificationMessage managerMesage = NotificationMessage.of(
                NotificationCategory.CLIENT_CANCELLATION,
                managerDto.getEmail(),
                managerDto.getFirstName(),
                table.getId(),
                FORMATTER.format(appointment.getTime())
            );

            this.jmsTemplate.convertAndSend(this.destination, managerMesage);
            return SuccessMessageDto.success("Successfully cancelled the reservation");
        }

        final NotificationMessage clientMessage = NotificationMessage.of(
            NotificationCategory.MANAGER_CANCELLATION,
            clientDto.getEmail(),
            restaurant.getName(),
            FORMATTER.format(appointment.getTime())
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
        return SuccessMessageDto.success("Successfully cancelled the reservation");
    }

    @Override
    public Page<ReservationDto> findAll(ReservationFilterDto filterDto, Pageable pageable) {
        final Page<Reservation> reservations = this.reservationRepository.findAvailableReservations(filterDto, pageable);

        return reservations.map(reservation -> {
            final ReservationDto dto = new ReservationDto();
            dto.setId(reservation.getId());
            dto.setUserId(reservation.getUserId());
            dto.setRestaurant(this.shallowMap(reservation.getAppointment().getTable().getRestaurant()));
            dto.setAppointment(this.shallowMap(reservation.getAppointment()));
            return dto;
        });
    }

    private RestaurantDto shallowMap(Restaurant restaurant) {
        // Ignore the tables, we will not serialize it
        restaurant.setTables(List.of());
        return this.modelMapper.map(restaurant, RestaurantDto.class);
    }

    private AppointmentDto shallowMap(Appointment appointment) {
        return this.modelMapper.map(appointment, AppointmentDto.class);
    }

    private boolean hasAppliedBenefits(int reservationCount, Restaurant restaurant) {
        final boolean discount = restaurant.getFreeItemEachXReservations() != -1 && reservationCount >= restaurant.getDiscountAfterXReservations();
        final boolean freeItem = restaurant.getFreeItemEachXReservations() == 0 || (restaurant.getFreeItemEachXReservations() != -1 && restaurant.getFreeItemEachXReservations() % reservationCount == 0);
        return discount || freeItem;
    }

    private void sendNotifications(UserDto dto, UserDto managerDto, boolean benefits, Restaurant restaurant, Appointment appointment) {
        if (benefits) {
            final NotificationMessage clientMessage = NotificationMessage.of(
                NotificationCategory.RESERVATION_CONFIRM_BENEFITS,
                dto.getEmail(),
                dto.getFirstName(),
                restaurant.getName(),
                FORMATTER.format(appointment.getTime()),
                appointment.getTable().getId()
            );

            final NotificationMessage managerMessage = NotificationMessage.of(
                NotificationCategory.CLIENT_RESERVATION_CREATE_BENEFITS,
                managerDto.getEmail(),
                managerDto.getFirstName(),
                FORMATTER.format(appointment.getTime()),
                appointment.getTable().getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, clientMessage);
            this.jmsTemplate.convertAndSend(this.destination, managerMessage);
            return;
        }

        final NotificationMessage clientMessage = NotificationMessage.of(
            NotificationCategory.RESERVATION_CONFIRM,
            dto.getEmail(),
            dto.getFirstName(),
            restaurant.getName(),
            FORMATTER.format(appointment.getTime()),
            appointment.getTable().getId()
        );

        final NotificationMessage managerMessage = NotificationMessage.of(
            NotificationCategory.CLIENT_RESERVATION_CREATE,
            managerDto.getEmail(),
            FORMATTER.format(appointment.getTime()),
            appointment.getTable().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
        this.jmsTemplate.convertAndSend(this.destination, managerMessage);
    }

    private UserDto findById(Long userId) {
        final ResponseEntity<UserDto> responseEntity;
        System.out.println("[" + System.currentTimeMillis() / 1000 + "] Getting user for id: " +userId );
        try {
            Thread.sleep(4000);
            responseEntity = this.userServiceRestTemplate.exchange("/user/%s".formatted(userId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Status: " + e.getStatusCode());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            System.out.println("Headers: " + e.getResponseHeaders());
            e.printStackTrace();

            throw new InternalError("Error while communicating with user service ");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("This user does not exist");
        }

        return responseEntity.getBody();
    }


}
