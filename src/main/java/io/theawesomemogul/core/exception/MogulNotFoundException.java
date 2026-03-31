package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 *
 * This exception corresponds to HTTP 404 Not Found responses. It should be thrown
 * when an application attempts to access a resource that does not exist or has been deleted.
 *
 * Example usage:
 * <pre>
 * if (user == null) {
 *     throw new MogulNotFoundException("User not found")
 *         .withDetail("userId", userId);
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulNotFoundException extends MogulException {

    /**
     * Constructs a MogulNotFoundException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "MOG-NOT-FOUND");
    }

    /**
     * Constructs a MogulNotFoundException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulNotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, "MOG-NOT-FOUND", cause);
    }
}
