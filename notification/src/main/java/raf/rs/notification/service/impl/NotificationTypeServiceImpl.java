package raf.rs.notification.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import raf.rs.notification.domain.NotificationCategory;
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
    public NotificationType findByCategory(NotificationCategory category) {
        return this.repository.findByCategory(category)
            .orElseThrow(() -> new NotFoundException("Notification does not exist with this category"));
    }

    @Override
    public NotificationTypeDto saveNotificationType(NotificationTypeDto notificationDto, Long id) {
        final NotificationType type = this.repository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Notification type with id not found"));

        type.setName(notificationDto.getName());
        type.setText(notificationDto.getText());

        final NotificationType saved = this.repository.save(type);
        return this.notificationMapper.notificationTypeDtoFromNotificationType(saved);
    }

    @Override
    public List<NotificationTypeDto> findAll() {
        return this.repository.findAll()
            .stream()
            .map(this.notificationMapper::notificationTypeDtoFromNotificationType)
            .toList();
    }

    @Override
    public NotificationTypeDto findById(Long id) {
        return this.repository.findById(id)
            .map(this.notificationMapper::notificationTypeDtoFromNotificationType)
            .orElseThrow(() -> new NotFoundException("Notification does not exist with this name"));
    }
}
