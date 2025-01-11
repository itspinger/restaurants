package raf.rs.notification.exception;

import org.springframework.http.HttpStatus;

public class LockedException extends CustomException {

    public LockedException(String message) {
        super(message, ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
    }
}
