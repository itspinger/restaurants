package raf.rs.restaurants.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raf.rs.restaurants.userservice.dto.SuccessMessageDto;
import raf.rs.restaurants.userservice.service.UserService;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<SuccessMessageDto> verifyEmail(@RequestParam("token") String token) {
        return new ResponseEntity<>(this.userService.validateVerificationToken(token), HttpStatus.OK);
    }
}
