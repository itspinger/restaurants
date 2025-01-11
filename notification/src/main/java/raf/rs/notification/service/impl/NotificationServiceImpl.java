package raf.rs.notification.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import raf.rs.notification.client.userservice.dto.UserDto;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.dto.NotificationFilterDto;
import raf.rs.notification.exception.InternalError;
import raf.rs.notification.exception.NotFoundException;
import raf.rs.notification.mapper.NotificationMapper;
import raf.rs.notification.repository.NotificationRepository;
import raf.rs.notification.service.EmailService;
import raf.rs.notification.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final EmailService emailService;
    private final NotificationMapper mapper;
    private final RestTemplate restTemplate;

    public NotificationServiceImpl(NotificationRepository repository, EmailService emailService, NotificationMapper mapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.emailService = emailService;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<NotificationDto> findAll() {
        return this.repository.findAll()
            .stream()
            .map(this.mapper::notificationDtoFromNotification)
            .toList();
    }

    @Override
    public List<NotificationDto> findByUserId(Long userId) {
        // First we need to communicate with the user service to even get the UserDto
        // That we're supposed to retrieve the notifications for
        final UserDto user = this.findById(userId);

        // Now we need to get the users email
        // And find all notifications which contain this email
        // Which is a very simple operation
        return this.repository.findByEmail(user.getEmail())
            .stream()
            .map(this.mapper::notificationDtoFromNotification)
            .toList();
    }

    @Override
    public void sendNotification(Notification notification) {
        // Posalji notifikaciju na mejl
        this.emailService.sendMessage(
            notification.getEmail(),
            notification.getNotificationType().getName(),
            notification.getText()
        );

        this.repository.save(notification);
    }

    @Override
    public Page<NotificationDto> findAll(Long userId, NotificationFilterDto dto, Pageable pageable) {
        // Take the id from the user
        // If this method was called
        // Then this must mean this user is a manager or a client
        // So check if email differs from his own email
        // If so, return 0 notifications
        if (userId != null) {
            final UserDto user = this.findById(userId);
            if (dto.getEmail() != null && !user.getEmail().startsWith(dto.getEmail())) {
                return Page.empty(pageable);
            }

            dto.setEmail(user.getEmail());
        }

        return this.repository.findAll(dto, pageable).map(this.mapper::notificationDtoFromNotification);
    }

    private UserDto findById(Long userId) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.restTemplate.exchange("/user/%s".formatted(userId), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new InternalError("Error while communicating with user service");
        }

        if (responseEntity.getBody() == null) {
            throw new NotFoundException("This user does not exist");
        }

        return responseEntity.getBody();
    }

}
