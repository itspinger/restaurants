package raf.rs.notification.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.dto.NotificationFilterDto;

public interface NotificationService {

    List<NotificationDto> findAll();

    List<NotificationDto> findByUserId(Long userId);

    void sendNotification(Notification notification);

    Page<NotificationDto> findAll(Long userId, NotificationFilterDto dto, Pageable pageable);
}
