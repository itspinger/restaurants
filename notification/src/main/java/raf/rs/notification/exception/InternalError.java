package raf.rs.notification.exception;

import org.springframework.http.HttpStatus;

public class InternalError extends CustomException {

    public InternalError(String message) {
        super(message, ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
