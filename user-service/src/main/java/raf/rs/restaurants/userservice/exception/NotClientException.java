package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class NotClientException extends CustomException {

    public NotClientException(String message) {
        super(message, ErrorCode.NOT_CLIENT, HttpStatus.FORBIDDEN);
    }

}
