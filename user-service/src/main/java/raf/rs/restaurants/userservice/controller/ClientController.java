package raf.rs.restaurants.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.service.UserService;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final UserService userService;

    public ClientController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerClient(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(this.userService.createClient(userCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/increaseReservations/{id}")
    public ResponseEntity<Void> increaseReservationCount(@PathVariable Long id) {
        this.userService.increaseReservationCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/decreaseReservations/{id}")
    public ResponseEntity<Void> decreaseReservationCount(@PathVariable Long id) {
        this.userService.decreaseReservationCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
