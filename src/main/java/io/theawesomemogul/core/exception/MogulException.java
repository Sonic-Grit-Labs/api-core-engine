package io.theawesomemogul.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Base exception class for all Mogul Audio application exceptions.
 *
 * This abstract exception provides a standardized way to handle errors across the
 * Mogul Audio ecosystem. All application-specific exceptions should extend this class
 * to ensure consistent error handling, logging, and API responses.
 *
 * Key features:
 * - Error codes for categorization (e.g., "MOG-001", "MOG-NOT-FOUND")
 * - HTTP status mapping for REST API responses
 * - Contextual details as a flexible Map for rich error information
 * - Structured logging and monitoring support
 *
 * Example usage:
 * <pre>
 * throw new MogulNotFoundException("User not found")
 *     .withCode("MOG-USER-NOT-FOUND")
 *     .withDetail("userId", 123);
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Getter
public abstract class MogulException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    /**
     * Constructs a MogulException with message and HTTP status.
     *
     * @param message the exception message
     * @param httpStatus the HTTP status code to return in API responses
     * @since 1.0.0
     */
    public MogulException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    /**
     * Constructs a MogulException with message, HTTP status, and error code.
     *
     * @param message the exception message
     * @param httpStatus the HTTP status code to return in API responses
     * @param errorCode the error code for categorization
     * @since 1.0.0
     */
    public MogulException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode != null ? errorCode : "MOG-ERROR";
        this.details = new HashMap<>();
    }

    /**
     * Constructs a MogulException with message, HTTP status, error code, and cause.
     *
     * @param message the exception message
     * @param httpStatus the HTTP status code to return in API responses
     * @param errorCode the error code for categorization
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulException(String message, HttpStatus httpStatus, String errorCode, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode != null ? errorCode : "MOG-ERROR";
        this.details = new HashMap<>();
    }

    /**
     * Adds a detail entry to this exception.
     *
     * @param key the detail key
     * @param value the detail value
     * @return this exception for method chaining
     * @since 1.0.0
     */
    public MogulException withDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

    /**
     * Adds multiple detail entries to this exception.
     *
     * @param detailMap the map of details to add
     * @return this exception for method chaining
     * @since 1.0.0
     */
    public MogulException withDetails(Map<String, Object> detailMap) {
        if (detailMap != null) {
            this.details.putAll(detailMap);
        }
        return this;
    }

    /**
     * Returns a string representation including error code and details.
     *
     * @return formatted exception string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return String.format("%s [%s]: %s. Details: %s",
                this.getClass().getSimpleName(),
                errorCode,
                getMessage(),
                details);
    }
}
