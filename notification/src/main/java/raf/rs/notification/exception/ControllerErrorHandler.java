package raf.rs.notification.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException exception) {
        final ErrorDetails errorDetails = new ErrorDetails(exception.getErrorCode(), exception.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        final ErrorDetails error = new ErrorDetails(ErrorCode.INVALID_DATA, "Validation failed for one or more fields", Instant.now(), errors);
        return new ResponseEntity<>(error, exception.getStatusCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(BadCredentialsException exception) {
        final ErrorDetails error = new ErrorDetails(ErrorCode.INCORRECT_CREDENTIALS, "Incorrect credentials", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleException(DisabledException exception) {
        final ErrorDetails errorDetails = new ErrorDetails(ErrorCode.ACCOUNT_LOCKED, exception.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleException(AuthenticationException exception) {
        exception.printStackTrace();
        final ErrorDetails errorDetails = new ErrorDetails(ErrorCode.UNATHORIZED, exception.getMessage(), Instant.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}