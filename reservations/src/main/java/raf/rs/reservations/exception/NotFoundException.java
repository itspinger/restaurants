package raf.rs.reservations.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
