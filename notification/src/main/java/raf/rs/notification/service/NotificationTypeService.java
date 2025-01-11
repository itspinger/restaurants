package raf.rs.notification.service;

import java.util.List;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.dto.NotificationTypeDto;

public interface NotificationTypeService {

    NotificationType findById(Long id);

    NotificationType findByName(String name);

    void deleteById(Long id);

    NotificationTypeDto saveNotificationType(NotificationTypeDto notificationDto, Long id);

    List<NotificationTypeDto> findAll();

}
