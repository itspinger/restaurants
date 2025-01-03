package raf.rs.restaurants.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import java.util.List;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import raf.rs.restaurants.userservice.domain.Client;
import raf.rs.restaurants.userservice.domain.Manager;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.domain.UserType;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.exception.NotClientException;
import raf.rs.restaurants.userservice.exception.NotFoundException;
import raf.rs.restaurants.userservice.mapper.UserMapper;
import raf.rs.restaurants.userservice.repository.UserRepository;
import raf.rs.restaurants.userservice.security.service.TokenService;
import raf.rs.restaurants.userservice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticatorManager;
    private final TokenService tokenService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AuthenticationManager authenticatorManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticatorManager = authenticatorManager;
        this.tokenService = tokenService;
    }

    @Override
    public List<UserDto> findAll() {
        return this.userRepository.findAll().stream().map(this.userMapper::createUserToUserDto).toList();
    }

    @Override
    public UserDto findById(Long id) {
        return this.userRepository.findById(id)
            .map(this.userMapper::createUserToUserDto)
            .orElseThrow(() -> new NotFoundException("User with id %s not found".formatted(id)));
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto, UserType userType) {
        final User newUser = this.userMapper.createDtoToUser(userCreateDto, userType);
        this.userRepository.save(newUser);
        return this.userMapper.createUserToUserDto(newUser);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        this.authenticatorManager.authenticate(new UsernamePasswordAuthenticationToken(tokenRequestDto.getUsername(), tokenRequestDto.getPassword()));

        final User user = this.userRepository.findByUsername(tokenRequestDto.getUsername()).or(()->this.userRepository.findByEmail(tokenRequestDto.getUsername())).orElseThrow();

        final Claims claims = Jwts.claims();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("roles", user.getAuthorities());

        if (user instanceof Manager manager) {
            claims.put("restaurantId", manager.getRestaurantId());
        }

        return new TokenResponseDto(this.tokenService.generate(claims));
    }

    @Override
    public void increaseReservationCount(Long id) {
        final User user = this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("User with id %s not found".formatted(id)));

        if (!(user instanceof Client client)) {
            throw new NotClientException("User with id %s is not a client".formatted(id));
        }

        client.setReservations_num(client.getReservations_num() + 1);
        this.userRepository.save(user);
    }

    @Override
    public void decreaseReservationCount(Long id) {
        final User user = this.userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("User with id %s not found".formatted(id)));

        if (!(user instanceof Client client)) {
            throw new NotClientException("User with id %s is not a client".formatted(id));
        }

        client.setReservations_num(client.getReservations_num() + 1);
        this.userRepository.save(user);
    }
}
