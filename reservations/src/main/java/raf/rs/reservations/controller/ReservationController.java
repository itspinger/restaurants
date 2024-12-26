package raf.rs.reservations.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createReservation(@RequestBody @Valid ReservationCreateDto reservation) {
        this.reservationService.createReservation(reservation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
