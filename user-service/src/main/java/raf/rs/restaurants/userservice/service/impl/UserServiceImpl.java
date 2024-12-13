package raf.rs.restaurants.userservice.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.mapper.UserMapper;
import raf.rs.restaurants.userservice.repository.UserRepository;
import raf.rs.restaurants.userservice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findAll() {
        return this.userRepository.findAll().stream().map(this.userMapper::createUserToUserDto).toList();
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        final User newUser = this.userMapper.createDtoToUser(userCreateDto);
        this.userRepository.save(newUser);
        return this.userMapper.createUserToUserDto(newUser);
    }
}
