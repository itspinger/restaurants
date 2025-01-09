package raf.rs.restaurants.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.restaurants.userservice.dto.SuccessMessageDto;
import raf.rs.restaurants.userservice.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/ban/{userId}")
    public ResponseEntity<SuccessMessageDto> ban(@PathVariable Long userId) {
        return new ResponseEntity<>(this.userService.ban(userId), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/unban/{userId}")
    public ResponseEntity<SuccessMessageDto> unban(@PathVariable Long userId) {
        return new ResponseEntity<>(this.userService.unban(userId), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/increaseReservations/{id}")
    public ResponseEntity<Void> increaseReservationCount(@PathVariable Long id) {
        this.userService.increaseReservationCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/decreaseReservations/{id}")
    public ResponseEntity<Void> decreaseReservationCount(@PathVariable Long id) {
        this.userService.decreaseReservationCount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
