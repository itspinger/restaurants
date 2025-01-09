package raf.rs.restaurants.userservice.service;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import raf.rs.restaurants.userservice.dto.SuccessMessageDto;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.dto.UserPatchDto;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto createClient(UserCreateDto userCreateDto);

    UserDto createManager(UserCreateDto userCreateDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    void increaseReservationCount(Long id);

    void decreaseReservationCount(Long id);

    UserDto findManagerByRestaurantId(Long restaurantId);

    SuccessMessageDto validateVerificationToken(String token);

    SuccessMessageDto ban(Long userId);

    SuccessMessageDto unban(Long userId);

    UserDto patchUser(@Valid UserPatchDto userDto, Long id);
}
