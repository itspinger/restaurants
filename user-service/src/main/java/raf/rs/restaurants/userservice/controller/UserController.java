package raf.rs.restaurants.userservice.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.restaurants.userservice.domain.UserType;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.mapper.UserMapper;
import raf.rs.restaurants.userservice.security.service.TokenService;
import raf.rs.restaurants.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public UserController(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(this.userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/manager/{restaurantId}")
    public ResponseEntity<UserDto> getManagerByRestaurantId(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(this.userService.findManagerByRestaurantId(restaurantId), HttpStatus.OK);
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

    @PostMapping("/register/manager")
    public ResponseEntity<UserDto> registerManager(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(this.userService.createUser(userCreateDto, UserType.MANAGER), HttpStatus.CREATED);
    }

    @PostMapping("/register/client")
    public ResponseEntity<UserDto> registerClient(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(this.userService.createUser(userCreateDto, UserType.CLIENT), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(this.userService.login(tokenRequestDto), HttpStatus.OK);
    }

}
