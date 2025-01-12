package raf.rs.reservations.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends CustomException {

    public InvalidDataException(String message) {
        super(message, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
    }
}
