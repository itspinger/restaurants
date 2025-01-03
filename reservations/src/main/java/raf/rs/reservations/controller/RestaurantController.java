package raf.rs.reservations.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return new ResponseEntity<>(this.restaurantService.findAll(), HttpStatus.OK);
    }

    @PatchMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@RequestBody RestaurantDto dto, @PathVariable Long restaurantId) {
        return new ResponseEntity<>(this.restaurantService.updateRestaurant(dto, restaurantId), HttpStatus.ACCEPTED);
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDto dto) {
        return new ResponseEntity<>(this.restaurantService.createAppointment(dto), HttpStatus.CREATED);
    }

}
