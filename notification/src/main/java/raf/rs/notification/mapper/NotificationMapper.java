package raf.rs.notification.mapper;

import org.springframework.stereotype.Component;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.dto.NotificationTypeDto;

@Component
public class NotificationMapper {

    public NotificationDto notificationDtoFromNotification(Notification notification) {
        final NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setEmail(notification.getEmail());
        notificationDto.setText(notification.getText());
        notificationDto.setTimestamp(notification.getTimestamp());
        notificationDto.setType(this.notificationTypeDtoFromNotificationType(notification.getNotificationType()));
        return notificationDto;
    }

    public NotificationTypeDto notificationTypeDtoFromNotificationType(NotificationType notificationType) {
        final NotificationTypeDto notificationTypeDto = new NotificationTypeDto();
        notificationTypeDto.setId(notificationType.getId());
        notificationTypeDto.setName(notificationType.getName());
        notificationTypeDto.setText(notificationType.getText());
        return notificationTypeDto;
    }

    public NotificationType notificationTypeFromNotificationTypeDto(NotificationTypeDto notificationTypeDto) {
        final NotificationType notificationType = new NotificationType();
        notificationType.setId(notificationTypeDto.getId());
        notificationType.setName(notificationTypeDto.getName());
        notificationType.setText(notificationTypeDto.getText());
        return notificationType;
    }
}
