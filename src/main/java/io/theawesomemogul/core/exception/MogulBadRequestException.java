package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request is invalid or malformed.
 *
 * This exception corresponds to HTTP 400 Bad Request responses. It should be thrown
 * when an API request contains invalid parameters, missing required fields, or
 * violates business logic constraints.
 *
 * Example usage:
 * <pre>
 * if (email == null || !email.contains("@")) {
 *     throw new MogulBadRequestException("Invalid email format");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulBadRequestException extends MogulException {

    /**
     * Constructs a MogulBadRequestException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulBadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "MOG-BAD-REQUEST");
    }

    /**
     * Constructs a MogulBadRequestException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulBadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, "MOG-BAD-REQUEST", cause);
    }
}
