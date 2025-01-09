package raf.rs.reservations.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.reservations.dto.AppointmentCreateDto;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.AppointmentFilterDto;
import raf.rs.reservations.service.AppointmentService;
import raf.rs.reservations.service.RestaurantService;

@RestController
@RequestMapping("/appointments")
public class AppointmentsController {
    private final RestaurantService restaurantService;
    private final AppointmentService appointmentService;

    public AppointmentsController(RestaurantService restaurantService, AppointmentService appointmentService) {
        this.restaurantService = restaurantService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody @Valid AppointmentCreateDto dto) {
        return new ResponseEntity<>(this.restaurantService.createAppointment(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentDto>> getAllAppointments(AppointmentFilterDto filterDto, Pageable pageable) {
        return new ResponseEntity<>(this.appointmentService.findAvailableAppointments(filterDto, pageable), HttpStatus.OK);
    }
}
