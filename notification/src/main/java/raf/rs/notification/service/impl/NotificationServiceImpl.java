package raf.rs.notification.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.mapper.NotificationMapper;
import raf.rs.notification.repository.NotificationRepository;
import raf.rs.notification.service.EmailService;
import raf.rs.notification.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final EmailService emailService;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, EmailService emailService, NotificationMapper mapper) {
        this.repository = repository;
        this.emailService = emailService;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationDto> findAll() {
        return this.repository.findAll()
            .stream()
            .map(this.mapper::notificationDtoFromNotification)
            .toList();
    }

    @Override
    public List<NotificationDto> findByEmail(String email) {
        final String decodedMail = Arrays.toString(Base64.getDecoder().decode(email));

        return this.repository.findByEmail(decodedMail)
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
            notification.getNotificationType().getText()
        );

        this.repository.save(notification);
    }

}
