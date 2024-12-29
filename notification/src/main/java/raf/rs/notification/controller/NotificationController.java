package raf.rs.notification.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.dto.NotificationTypeDto;
import raf.rs.notification.service.NotificationService;
import raf.rs.notification.service.NotificationTypeService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public NotificationController(NotificationService notificationService, NotificationTypeService notificationTypeService) {
        this.notificationService = notificationService;
        this.notificationTypeService = notificationTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        return ResponseEntity.ok(this.notificationService.findAll());
    }

    @GetMapping("/{mail}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable String mail) {
        return ResponseEntity.ok(this.notificationService.findByEmail(mail));
    }

    @DeleteMapping("/type/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        this.notificationTypeService.deleteById(notificationId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/type/{notificationId}")
    public ResponseEntity<NotificationTypeDto> updateNotification(@RequestBody NotificationTypeDto dto, @PathVariable Long notificationId) {
        return new ResponseEntity<>(this.notificationTypeService.saveNotificationType(dto, notificationId), HttpStatus.ACCEPTED);
    }
}
