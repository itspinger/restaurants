package raf.rs.restaurants.userservice.service;

import java.util.List;

import raf.rs.restaurants.userservice.domain.UserType;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;

public interface UserService {

    List<UserDto> findAll();

    UserDto createUser(UserCreateDto userCreateDto, UserType userType);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

}
