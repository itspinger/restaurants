package raf.rs.notification.service;

import java.util.List;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationDto;

public interface NotificationService {

    List<NotificationDto> findAll();

    List<NotificationDto> findByEmail(String email);

    void sendNotification(Notification notification);

}
