package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class NotYoursException extends CustomException {

    public NotYoursException(String message) {
        super(message, ErrorCode.NOT_YOURS, HttpStatus.UNAUTHORIZED);
    }

}
