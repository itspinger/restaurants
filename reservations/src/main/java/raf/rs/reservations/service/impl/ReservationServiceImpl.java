package raf.rs.reservations.service.impl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
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
    private final ReservationRepository reservationRepository;
    private final RestTemplate userServiceRestTemplate;
    private final JmsTemplate jmsTemplate;
    private final String destination;

    public ReservationServiceImpl(
        ReservationRepository reservationRepository,
        RestTemplate userServiceRestTemplate,
        JmsTemplate jmsTemplate,
        @Value("${destination.sendMail}") String destination
    ) {
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

        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/user/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to retrieve user by id %s".formatted(reservationCreateDto.getUserId()));
        }

        final UserDto userDto = responseEntity.getBody();
        if (userDto == null) {
            throw new NotFoundException("Failed to retrieve user by id %s".formatted(reservationCreateDto.getUserId()));
        }

        final RestaurantDto restaurantDto = appointment.getTablesDto().getRestaurantDto();
        final Reservation reservation = ReservationMapper.toEntity(reservationCreateDto);
        final boolean benefits = this.hasAppliedBenefits(userDto.getReservationsNum(), restaurantDto);

        this.reservationRepository.save(reservation);
        this.sendNotifications(userDto, benefits, restaurantDto, appointment);

        // After the reservation was saved we need to increase
        // The reservation count for the user
        try {
            this.userServiceRestTemplate.exchange("/user/increaseReservations/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.POST, null, Void.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Failed to increase reservation count for user " + e.getMessage());
        }
    }

    @Override
    public void cancelReservation(Long reservationId, boolean userCancelled) {
        final Reservation reservation = this.findReservationById(reservationId);

        this.reservationRepository.delete(reservation);

        // If user cancelled, we need to contact the user service
        // To decrease the reservation number
        // Otherwise just send out the emails
        if (userCancelled) {
            // If manager cancelled, synchronously contact the user service to decrease the reservation number
            try {
                this.userServiceRestTemplate.exchange("/user/decreaseReservations/%s".formatted(reservationId), HttpMethod.POST, null, Void.class);
            } catch (HttpClientErrorException e) {
                throw new NotFoundException("Failed to decrease reservation count for user " + e.getMessage());
            }

            final NotificationRequestDto managerMesage = NotificationRequestDto.of(
                "Client cancelled a reservation",
                "",
                reservation.getAppointment().getTime(),
                reservation.getAppointment().getTable().getId()
            );

            this.jmsTemplate.convertAndSend(this.destination, managerMesage);
            return;
        }

        final NotificationRequestDto clientMessage = NotificationRequestDto.of(
            "Manager cancelled your reservation",
            "",
            reservation.getAppointment().getTime(),
            reservation.getAppointment().getTable().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
    }

    private boolean hasAppliedBenefits(Integer reservationCount, RestaurantDto restaurantDto) {
        return reservationCount >= restaurantDto.getDiscountAfterXReservations() || restaurantDto.getFreeItemEachXReservations() % reservationCount == 0;
    }

    private void sendNotifications(UserDto dto, boolean benefits, RestaurantDto restaurantDto, AppointmentDto appointmentDto) {
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
                "",
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
            "",
            LocalDateTime.now(),
            appointmentDto.getTablesDto().getId()
        );

        this.jmsTemplate.convertAndSend(this.destination, clientMessage);
        this.jmsTemplate.convertAndSend(this.destination, managerMessage);
    }
}
