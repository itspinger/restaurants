package raf.rs.restaurants.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.service.UserService;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final UserService userService;

    public ManagerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerManager(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(this.userService.createManager(userCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<UserDto> getManagerByRestaurantId(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(this.userService.findManagerByRestaurantId(restaurantId), HttpStatus.OK);
    }
}
