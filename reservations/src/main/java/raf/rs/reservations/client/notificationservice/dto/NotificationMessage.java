package raf.rs.reservations.client.notificationservice.dto;

public record NotificationMessage(NotificationCategory category, Object[] args, String email) {

    public static NotificationMessage of(NotificationCategory category, String email, Object... args) {
        return new NotificationMessage(category, args, email);
    }
}
