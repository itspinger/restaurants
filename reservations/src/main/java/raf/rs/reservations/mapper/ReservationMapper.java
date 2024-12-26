package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.ReservationCreateDto;

public class ReservationMapper {

    public static ReservationCreateDto toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return new ReservationCreateDto(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getAppointment() != null ? AppointmentMapper.toDTO(reservation.getAppointment()) : null
        );
    }

    public static Reservation toEntity(ReservationCreateDto reservationDTO) {
        if (reservationDTO == null) {
            return null;
        }

        final Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getReservationId());
        reservation.setAppointment(AppointmentMapper.toEntity());
        return reservation;
    }
}
