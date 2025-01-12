package raf.rs.restaurants.userservice.service;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.dto.SuccessMessageDto;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.dto.UserPatchDto;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    UserDto findById(Long id);


    UserDto createClient(UserCreateDto userCreateDto);

    UserDto createManager(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    void increaseReservationCount(Long id);

    void decreaseReservationCount(Long id);

    SuccessMessageDto validateVerificationToken(String token);

    SuccessMessageDto validatePasswordResetToken(String token);

    void sendPasswordResetEmail(User user);

    SuccessMessageDto ban(Long userId);

    SuccessMessageDto unban(Long userId);

    UserDto patchUser(@Valid UserPatchDto userDto, Long id);
}
