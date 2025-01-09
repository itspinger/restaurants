package raf.rs.reservations.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import lombok.Setter;

/**
 * This class represents error response.
 */
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {

    @JsonProperty("code")
    private ErrorCode errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    private Instant timestamp;

    @JsonProperty("extra")
    private Map<String, String> extra;

    public ErrorDetails() {

    }

    public ErrorDetails(ErrorCode errorCode, String errorMessage, Instant timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public ErrorDetails(ErrorCode errorCode, String errorMessage, Instant timestamp, Map<String, String> extra) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
        this.extra = extra;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Map<String, String> getExtra() {
        return this.extra;
    }
}
