package raf.rs.notification.service.impl;

import org.springframework.stereotype.Service;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.dto.NotificationTypeDto;
import raf.rs.notification.exception.NotFoundException;
import raf.rs.notification.mapper.NotificationMapper;
import raf.rs.notification.repository.NotificationTypeRepository;
import raf.rs.notification.service.NotificationTypeService;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {
    private final NotificationMapper notificationMapper;
    private final NotificationTypeRepository repository;

    public NotificationTypeServiceImpl(NotificationMapper notificationMapper, NotificationTypeRepository repository) {
        this.notificationMapper = notificationMapper;
        this.repository = repository;
    }

    @Override
    public NotificationType findById(Long id) {
        return this.repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Notification type with id %s not found".formatted(id)));
    }

    @Override
    public NotificationType findByName(String name) {
        return this.repository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Notification type with name %s not found".formatted(name)));
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public NotificationTypeDto saveNotificationType(NotificationTypeDto notificationDto, Long id) {
        final NotificationType type = this.repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Notification type with id %s not found".formatted(id)));

        type.setName(notificationDto.getName());
        type.setText(notificationDto.getText());

        final NotificationType saved = this.repository.save(type);
        return this.notificationMapper.notificationTypeDtoFromNotificationType(saved);
    }
}
