package raf.rs.reservations.service;

import raf.rs.reservations.dto.ReservationCreateDto;

public interface ReservationService {

    void createReservation(ReservationCreateDto reservation);

}
