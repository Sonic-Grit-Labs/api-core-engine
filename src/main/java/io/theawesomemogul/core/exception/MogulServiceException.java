package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an internal service error occurs.
 *
 * This exception corresponds to HTTP 500 Internal Server Error and HTTP 503 Service
 * Unavailable responses. It should be thrown when the server encounters an unexpected
 * condition, such as database errors, external service failures, or system resource issues.
 *
 * Example usage:
 * <pre>
 * try {
 *     // risky operation
 * } catch (DatabaseException e) {
 *     throw new MogulServiceException("Database operation failed", e)
 *         .withDetail("operation", "getUserById");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulServiceException extends MogulException {

    /**
     * Constructs a MogulServiceException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "MOG-SERVICE-ERROR");
    }

    /**
     * Constructs a MogulServiceException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulServiceException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "MOG-SERVICE-ERROR", cause);
    }

    /**
     * Constructs a MogulServiceException with a message, cause, and HTTP status.
     *
     * Allows specifying a different HTTP status, such as 503 Service Unavailable.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @param httpStatus the HTTP status code
     * @since 1.0.0
     */
    public MogulServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, httpStatus, "MOG-SERVICE-ERROR", cause);
    }
}
