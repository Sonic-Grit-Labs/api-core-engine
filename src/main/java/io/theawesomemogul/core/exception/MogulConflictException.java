package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request conflicts with the current state of a resource.
 *
 * This exception corresponds to HTTP 409 Conflict responses. It should be thrown
 * when an operation cannot be completed due to a conflict, such as attempting to
 * create a duplicate resource or violating a unique constraint.
 *
 * Example usage:
 * <pre>
 * if (userAlreadyExists(email)) {
 *     throw new MogulConflictException("User with this email already exists");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulConflictException extends MogulException {

    /**
     * Constructs a MogulConflictException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulConflictException(String message) {
        super(message, HttpStatus.CONFLICT, "MOG-CONFLICT");
    }

    /**
     * Constructs a MogulConflictException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulConflictException(String message, Throwable cause) {
        super(message, HttpStatus.CONFLICT, "MOG-CONFLICT", cause);
    }
}
