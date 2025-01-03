package raf.rs.reservations.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.reservations.auth.AuthorizationService;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final AuthorizationService authorizationService;
    private final ReservationService reservationService;

    public ReservationController(AuthorizationService authorizationService, ReservationService reservationService) {
        this.authorizationService = authorizationService;
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createReservation(@RequestBody @Valid ReservationCreateDto reservation) {
        this.reservationService.createReservation(reservation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("@authorizationService.canCancelReservation(authentication, #reservationId)")
    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        this.reservationService.cancelReservation(reservationId, !this.authorizationService.isManager());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
