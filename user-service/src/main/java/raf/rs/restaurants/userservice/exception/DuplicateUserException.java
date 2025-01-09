package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateUserException extends CustomException {

    public DuplicateUserException() {
        super("User already exists with this email or username", ErrorCode.ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}
