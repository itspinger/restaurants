package raf.rs.restaurants.userservice.mapper;

import java.sql.Date;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.rs.restaurants.userservice.domain.Client;
import raf.rs.restaurants.userservice.domain.Manager;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.domain.UserType;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
@AllArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User createDtoToUser(UserCreateDto dto, UserType userType) {
        User user = null;
        if (userType == UserType.CLIENT) {
            user = new Client();
        } else {
            user = new Manager();
        }

        user.setEmail(dto.getEmail());
        user.setBirthDate(Date.valueOf(dto.getDateOfBirth()));
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
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

        if (user instanceof Client) {
            Client client = (Client) user;
            userDto.setReservationsNum(client.getReservations().size());  // Set the reservationsNum for Client
        }

        return userDto;
    }

}
