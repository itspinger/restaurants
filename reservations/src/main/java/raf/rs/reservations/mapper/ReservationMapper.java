package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationDto;

public class ReservationMapper {
    public static ReservationDto toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return new ReservationDto(
                reservation.getId(),
                reservation.getAppointment() != null ? reservation.getAppointment().getId() : null,
                reservation.getNote(),
                reservation.isActive()
        );
    }

    public static Reservation toEntity(ReservationDto reservationDTO, Appointment appointment) {
        if (reservationDTO == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setAppointment(appointment);

        reservation.setNote(reservationDTO.getNote());
        reservation.setActive(reservationDTO.isActive());

        return reservation;
    }
}
