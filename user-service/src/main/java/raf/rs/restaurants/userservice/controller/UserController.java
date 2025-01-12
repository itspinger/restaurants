package raf.rs.restaurants.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raf.rs.restaurants.userservice.dto.*;
import raf.rs.restaurants.userservice.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> findAll(Pageable pageable) {
        return new ResponseEntity<>(this.userService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(this.userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(this.userService.login(tokenRequestDto), HttpStatus.OK);
    }

    @PreAuthorize("@authorizationService.canEditProfile(authentication, #id)")
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patch(@RequestBody @Valid UserPatchDto userDto, @PathVariable Long id) {
        return new ResponseEntity<>(this.userService.patchUser(userDto, id), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessMessageDto> changePassword(@RequestParam("email") String email) {
        return new ResponseEntity<>(this.userService.resetPassword(email), HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<SuccessMessageDto> changePassword(@RequestBody @Valid PasswordDto passwordDto, @RequestParam("token") String token) {
        return new ResponseEntity<>(this.userService.changePassword(passwordDto, token), HttpStatus.OK);
    }
}
