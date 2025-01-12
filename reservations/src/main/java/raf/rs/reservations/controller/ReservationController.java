package raf.rs.reservations.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.reservations.auth.AuthorizationService;
import raf.rs.reservations.dto.ReservationDto;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.dto.ReservationFilterDto;
import raf.rs.reservations.dto.SuccessMessageDto;
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

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<SuccessMessageDto> createReservation(@RequestBody @Valid ReservationCreateDto reservation) {
        return new ResponseEntity<>(this.reservationService.createReservation(reservation), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ReservationDto>> getReservations(ReservationFilterDto filterDto, Pageable pageable) {
        return new ResponseEntity<>(this.reservationService.findAll(filterDto, pageable), HttpStatus.OK);
    }

    @PreAuthorize("@authorizationService.canCancelReservation(authentication, #reservationId)")
    @PostMapping("/cancel/{reservationId}")
    public ResponseEntity<SuccessMessageDto> cancelReservation(@PathVariable Long reservationId) {
        return new ResponseEntity<>(this.reservationService.cancelReservation(reservationId, !this.authorizationService.isManager()), HttpStatus.OK);
    }

}
