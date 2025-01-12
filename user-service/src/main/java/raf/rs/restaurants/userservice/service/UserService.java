package raf.rs.restaurants.userservice.service;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.rs.restaurants.userservice.dto.PasswordDto;
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

    SuccessMessageDto changePassword(PasswordDto passwordDto, String token);

    SuccessMessageDto resetPassword(String email);

    SuccessMessageDto ban(Long userId);

    SuccessMessageDto unban(Long userId);

    UserDto patchUser(@Valid UserPatchDto userDto, Long id);
}
