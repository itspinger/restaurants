package raf.rs.notification.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.rs.notification.dto.NotificationDto;
import raf.rs.notification.dto.NotificationFilterDto;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<NotificationDto>> getNotifications(NotificationFilterDto dto, Pageable pageable) {
        return ResponseEntity.ok(this.notificationService.findAll(null, dto, pageable));
    }

    @PreAuthorize("@authorizationService.canViewNotifications(authentication, #userId)")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<NotificationDto>> getNotifications(@PathVariable Long userId, NotificationFilterDto dto, Pageable pageable) {
        return ResponseEntity.ok(this.notificationService.findAll(userId, dto, pageable));
    }

    @GetMapping("/types")
    public ResponseEntity<List<NotificationTypeDto>> getNotificationTypes() {
        return ResponseEntity.ok(this.notificationTypeService.findAll());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/type/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        this.notificationTypeService.deleteById(notificationId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/type/{notificationId}")
    public ResponseEntity<NotificationTypeDto> updateNotification(@RequestBody NotificationTypeDto dto, @PathVariable Long notificationId) {
        return new ResponseEntity<>(this.notificationTypeService.saveNotificationType(dto, notificationId), HttpStatus.ACCEPTED);
    }
}
