package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class IncorrectCredentialsException extends CustomException {

    public IncorrectCredentialsException() {
        super("Incorrect credentials", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
