package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class InternalError extends CustomException {

    public InternalError() {
        super("Internal Error", ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
