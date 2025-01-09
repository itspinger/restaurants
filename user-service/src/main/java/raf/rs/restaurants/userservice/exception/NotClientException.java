package raf.rs.restaurants.userservice.exception;

import org.springframework.http.HttpStatus;

public class NotClientException extends CustomException {

    public NotClientException() {
        super("This user is not a client", ErrorCode.NOT_CLIENT, HttpStatus.FORBIDDEN);
    }

}
