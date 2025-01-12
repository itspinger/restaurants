package raf.rs.notification.dto;

import raf.rs.notification.domain.NotificationCategory;

public record NotificationMessage(NotificationCategory category, Object[] args, String email) {

}
