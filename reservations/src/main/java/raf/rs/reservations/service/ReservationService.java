package raf.rs.reservations.service;

import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationCreateDto;

public interface ReservationService {

    Reservation findReservationById(Long id);

    void createReservation(ReservationCreateDto reservation);

    void cancelReservation(Long reservationId, boolean userCancelled);

}
