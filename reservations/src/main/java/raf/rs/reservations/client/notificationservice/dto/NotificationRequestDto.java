package raf.rs.reservations.client.notificationservice.dto;

public class NotificationRequestDto {
    private final String type;
    private final Object[] args;
    private final String email;

    public NotificationRequestDto(String type, Object[] args, String email) {
        this.type = type;
        this.args = args;
        this.email = email;
    }

    public static NotificationRequestDto of(String type, String email, Object... args) {
        return new NotificationRequestDto(type, args, email);
    }

    public String getType() {
        return this.type;
    }

    public String getEmail() {
        return this.email;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
