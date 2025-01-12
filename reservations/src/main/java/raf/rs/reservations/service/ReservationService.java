package raf.rs.reservations.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationDto;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.dto.ReservationFilterDto;
import raf.rs.reservations.dto.SuccessMessageDto;

public interface ReservationService {

    Reservation findReservationById(Long id);

    SuccessMessageDto createReservation(ReservationCreateDto reservation);

    SuccessMessageDto cancelReservation(Long reservationId, boolean userCancelled);

    Page<ReservationDto> findAll(ReservationFilterDto filter, Pageable pageable);
}
