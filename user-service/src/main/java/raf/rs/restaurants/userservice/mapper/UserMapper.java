package raf.rs.restaurants.userservice.mapper;

import java.sql.Date;
import org.springframework.stereotype.Component;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;

@Component
public class UserMapper {

    public User createDtoToUser(UserCreateDto dto) {
        final User user = new User();
        user.setEmail(dto.getEmail());
        System.out.println(dto.getDateOfBirth());
        user.setBirthDate(Date.valueOf(dto.getDateOfBirth()));
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public UserDto createUserToUserDto(User user) {
        final UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }

}
