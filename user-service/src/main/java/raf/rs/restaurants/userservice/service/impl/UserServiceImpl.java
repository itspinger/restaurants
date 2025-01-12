package raf.rs.restaurants.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raf.rs.restaurants.userservice.client.notificationservice.dto.NotificationCategory;
import raf.rs.restaurants.userservice.client.notificationservice.dto.NotificationMessage;
import raf.rs.restaurants.userservice.domain.Client;
import raf.rs.restaurants.userservice.domain.Manager;
import raf.rs.restaurants.userservice.domain.User;
import raf.rs.restaurants.userservice.dto.PasswordDto;
import raf.rs.restaurants.userservice.dto.SuccessMessageDto;
import raf.rs.restaurants.userservice.dto.TokenRequestDto;
import raf.rs.restaurants.userservice.dto.TokenResponseDto;
import raf.rs.restaurants.userservice.dto.UserCreateDto;
import raf.rs.restaurants.userservice.dto.UserDto;
import raf.rs.restaurants.userservice.dto.UserPatchDto;
import raf.rs.restaurants.userservice.exception.AlreadyExistsException;
import raf.rs.restaurants.userservice.exception.DuplicateUserException;
import raf.rs.restaurants.userservice.exception.IncorrectCredentialsException;
import raf.rs.restaurants.userservice.exception.NotClientException;
import raf.rs.restaurants.userservice.exception.NotFoundException;
import raf.rs.restaurants.userservice.repository.ManagerRepository;
import raf.rs.restaurants.userservice.repository.UserRepository;
import raf.rs.restaurants.userservice.security.service.TokenService;
import raf.rs.restaurants.userservice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticatorManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JmsTemplate jmsTemplate;
    private final String destination;

    public UserServiceImpl(
        ModelMapper modelMapper,
        ManagerRepository managerRepository,
        UserRepository userRepository,
        AuthenticationManager authenticatorManager,
        TokenService tokenService,
        PasswordEncoder passwordEncoder,
        JmsTemplate jmsTemplate,
        @Value("${destination.sendMail}") String destination
    ) {
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
        this.authenticatorManager = authenticatorManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable)
            .map((user) -> {
                System.out.println(user.getAuthorities());
                final UserDto map = this.modelMapper.map(user, UserDto.class);
                System.out.println(map.getRoles());
                return map;
            });
    }

    @Override
    public UserDto findById(Long id) {
        return this.userRepository.findById(id)
            .map((user) -> this.modelMapper.map(user, UserDto.class))
            .orElseThrow(() -> new NotFoundException("User does not exist"));
    }

    @Override
    public UserDto createClient(UserCreateDto userCreateDto) {
        this.checkExists(userCreateDto);

        final Client client = this.modelMapper.map(userCreateDto, Client.class);
        final String token = UUID.randomUUID().toString();
        client.setPassword(this.passwordEncoder.encode(userCreateDto.getPassword()));
        client.setVerificationToken(token);

        this.userRepository.save(client);
        this.sendVerificationEmail(client);

        return this.modelMapper.map(client, UserDto.class);
    }

    @Override
    public UserDto createManager(UserCreateDto userCreateDto) {
        this.checkExists(userCreateDto);

        System.out.println(userCreateDto.getBirthDate());

        final Manager manager = this.modelMapper.map(userCreateDto, Manager.class);
        final String token = UUID.randomUUID().toString();
        manager.setPassword(this.passwordEncoder.encode(userCreateDto.getPassword()));
        manager.setVerificationToken(token);

        this.userRepository.save(manager);
        this.sendVerificationEmail(manager);

        return this.modelMapper.map(manager, UserDto.class);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto request) {
        this.authenticatorManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final User user = this.userRepository
            .findByUsername(request.getUsername())
            .or(() -> this.userRepository.findByEmail(request.getUsername()))
            .orElseThrow(IncorrectCredentialsException::new);

        final Claims claims = Jwts.claims();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        final TokenResponseDto dto = new TokenResponseDto();
        dto.setToken(this.tokenService.generate(claims));
        dto.setUser(this.modelMapper.map(user, UserDto.class));
        return dto;
    }

    @Override
    public void increaseReservationCount(Long id) {
        final User user = this.findUserById(id);
        if (!(user instanceof Client client)) {
            throw new NotClientException();
        }

        client.setReservationCount(client.getReservationCount() + 1);
        this.userRepository.save(user);
    }

    @Override
    public void decreaseReservationCount(Long id) {
        final User user = this.findUserById(id);
        if (!(user instanceof Client client)) {
            throw new NotClientException();
        }

        client.setReservationCount(client.getReservationCount() - 1);
        this.userRepository.save(user);
    }

    @Override
    public SuccessMessageDto validateVerificationToken(String token) {
        final Optional<User> valid = this.userRepository.findByVerificationToken(token);
        if (valid.isEmpty()) {
            throw new NotFoundException("Verification token does not exist");
        }

        final User user = valid.get();
        user.setActivated(true);
        user.setVerificationToken(null);
        this.userRepository.save(user);
        return SuccessMessageDto.success("Successfully verified your email!");
    }

    @Override
    public SuccessMessageDto changePassword(PasswordDto passwordDto, String token) {
        final Optional<User> valid = this.userRepository.findByResetToken(token);
        if (valid.isEmpty()) {
            throw new NotFoundException("Password reset token does not exist");
        }

        final User user = valid.get();
        if (passwordDto.getPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(passwordDto.getPassword()));
        }

        user.setResetToken(null);
        this.userRepository.save(user);
        return SuccessMessageDto.success("Successfully reset your password!");
    }

    @Override
    public SuccessMessageDto ban(Long userId) {
        final User user = this.findUserById(userId);
        user.setBanned(true);
        this.userRepository.save(user);
        return SuccessMessageDto.success("Successfully banned the account!");
    }

    @Override
    public SuccessMessageDto unban(Long userId) {
        final User user = this.findUserById(userId);
        user.setBanned(false);
        this.userRepository.save(user);
        return SuccessMessageDto.success("Successfully unbanned the account!");
    }

    @Override
    public UserDto patchUser(UserPatchDto userDto, Long id) {
        final User user = this.findUserById(id);
        this.checkExistsUser(user, userDto);

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }

        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.validateDate(userDto.getBirthDate()));
        }

        if (user instanceof Manager manager) {
            if (userDto.getStartDate() != null) {
                final LocalDate startDate = userDto.validateDate(userDto.getStartDate());
                manager.setStartDate(startDate);
            }
        }

        return this.modelMapper.map(this.userRepository.save(user), UserDto.class);
    }

    private void checkExistsUser(User user, UserPatchDto userDto) {
        if (userDto.getUsername() != null && !user.getUsername().equals(userDto.getUsername()) && this.userRepository.existsByUsername(userDto.getUsername())) {
            throw new AlreadyExistsException("User with this username already exists");
        }

        if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail()) && this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistsException("User with this email already exists");
        }
    }

    private User findUserById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User does not exist"));
    }

    private void sendVerificationEmail(User user) {
        final NotificationMessage dto = NotificationMessage.of(
            NotificationCategory.EMAIL_VERIFICATION,
            user.getEmail(),
            user.getUsername(),
            "http://localhost:8084/user-service/api/verify-email?token=%s".formatted(user.getVerificationToken())
        );

        this.jmsTemplate.convertAndSend(this.destination, dto);
    }

    @Override
    public SuccessMessageDto resetPassword(String email) {
        final User user = this.userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Email does not exist"));

        user.setResetToken(UUID.randomUUID().toString());
        this.userRepository.save(user);

        final NotificationMessage dto = NotificationMessage.of(
            NotificationCategory.RESET_PASSWORD,
            user.getEmail(),
            user.getUsername(),
            "http://localhost:5173/changePassword?token=%s".formatted(user.getResetToken())
        );

        this.jmsTemplate.convertAndSend(this.destination, dto);
        return SuccessMessageDto.success("Successfully sent the verification link!");
    }

    private void checkExists(UserCreateDto dto) {
        final boolean invalid = this.userRepository.existsByUsername(dto.getUsername()) || this.userRepository.existsByEmail(dto.getEmail());
        if (invalid) {
            throw new DuplicateUserException();
        }
    }
}
