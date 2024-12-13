package raf.rs.restaurants.userservice.service;

import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;

public interface UserService {

    UserDto createUser(UserCreateDto userCreateDto);

}
