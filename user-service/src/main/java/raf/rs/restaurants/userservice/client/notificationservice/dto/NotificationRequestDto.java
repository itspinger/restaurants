package raf.rs.restaurants.userservice.client.notificationservice.dto;

public record NotificationRequestDto(String type, Object[] args, String email) {

    public static NotificationRequestDto of(String type, String email, Object... args) {
        return new NotificationRequestDto(type, args, email);
    }

}

