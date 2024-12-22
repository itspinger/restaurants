package raf.rs.restaurants.userservice.mapper;

import raf.rs.restaurants.userservice.domain.Appointment;
import raf.rs.restaurants.userservice.domain.Client;
import raf.rs.restaurants.userservice.domain.Reservation;
import raf.rs.restaurants.userservice.domain.UserType;
import raf.rs.restaurants.userservice.dto.ReservationDto;

public class ReservationMapper {
    public static ReservationDto toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationDto(
                reservation.getId(),
                reservation.getAppointment() != null ? reservation.getAppointment().getId() : null,
                reservation.getClient() != null ? reservation.getClient().getId() : null,
                reservation.getNote(),
                reservation.isActive(),
                reservation.getDeclinedBy() != null ? reservation.getDeclinedBy().name() : null
        );
    }

    public static Reservation toEntity(ReservationDto reservationDTO, Appointment appointment, Client client) {
        if (reservationDTO == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setAppointment(appointment);
        reservation.setClient(client);
        reservation.setNote(reservationDTO.getNote());
        reservation.setActive(reservationDTO.isActive());
        reservation.setDeclinedBy(reservationDTO.getDeclinedBy() != null ? UserType.valueOf(reservationDTO.getDeclinedBy()) : null);
        return reservation;
    }
}
