package raf.rs.reservations.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {
    private final Validator validator;
    private final ObjectMapper objectMapper;

    public MessageHelper(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    public <T> T getMessage(Message message, Class<T> clazz) throws RuntimeException, JMSException {
        try {
            final String json = ((TextMessage) message).getText();
            final T data = this.objectMapper.readValue(json, clazz);

            final Set<ConstraintViolation<T>> violations = this.validator.validate(data);
            if (violations.isEmpty()) {
                return data;
            }

            this.printViolationsAndThrowException(violations);
            return null;
        } catch (IOException exception) {
            throw new RuntimeException("Message parsing fails.", exception);
        }
    }

    public String createTextMessage(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Problem with creating text message");
        }
    }

    private <T> void printViolationsAndThrowException(Set<ConstraintViolation<T>> violations) {
        final String concatenatedViolations = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
        throw new RuntimeException(concatenatedViolations);
    }
}
