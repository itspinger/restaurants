package raf.rs.reservations.dto;

public class SuccessMessageDto {
    private final String message;
    private final boolean success;

    public SuccessMessageDto(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static SuccessMessageDto success(String message) {
        return new SuccessMessageDto(message, true);
    }

    public static SuccessMessageDto unsuccess(String message) {
        return new SuccessMessageDto(message, false);
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
