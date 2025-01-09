package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends CustomException {

    public AlreadyExistsException(String message) {
        super(message, ErrorCode.ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
