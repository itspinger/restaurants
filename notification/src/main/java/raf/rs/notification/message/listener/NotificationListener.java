package raf.rs.notification.message.listener;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.dto.NotificationRequestDto;
import raf.rs.notification.message.MessageHelper;
import raf.rs.notification.service.NotificationService;
import raf.rs.notification.service.NotificationTypeService;

@Component
public class NotificationListener {
    private final MessageHelper helper;
    private final NotificationTypeService notificationTypeService;
    private final NotificationService notificationService;

    public NotificationListener(MessageHelper helper, NotificationTypeService service, NotificationService notificationService) {
        this.helper = helper;
        this.notificationTypeService = service;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${destination.sendMail}", concurrency = "5-10")
    public void sendMail(Message message) throws JMSException {
        this.handleNotificationRequest(message);
    }

    private void handleNotificationRequest(Message message) throws JMSException {
        final NotificationRequestDto notificationDto = this.helper.getMessage(message, NotificationRequestDto.class);
        final NotificationType type = this.notificationTypeService.findByName(notificationDto.getType());
        if (type == null) {
            return;
        }

        final Notification notification = new Notification();
        notification.setEmail(notificationDto.getEmail());
        notification.setTimestamp(LocalDateTime.now());
        notification.setNotificationType(type);
        notification.setText(type.getText().formatted(notificationDto.getArgs()));

        this.notificationService.sendNotification(notification);
    }

}
