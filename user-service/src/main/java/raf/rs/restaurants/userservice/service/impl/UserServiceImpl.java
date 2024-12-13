package raf.rs.restaurants.userservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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
    public UserDto createUser(UserCreateDto userCreateDto) {
        return null;
    }
}
