package raf.rs.notification.service;

import java.util.List;
import raf.rs.notification.domain.NotificationCategory;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.dto.NotificationTypeDto;

public interface NotificationTypeService {

    NotificationType findByCategory(NotificationCategory category);

    NotificationTypeDto saveNotificationType(NotificationTypeDto notificationDto, Long id);

    List<NotificationTypeDto> findAll();

    NotificationTypeDto findById(Long id);
}
