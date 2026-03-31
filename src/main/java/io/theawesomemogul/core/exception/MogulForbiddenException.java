package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is authenticated but lacks permission for an action.
 *
 * This exception corresponds to HTTP 403 Forbidden responses. It should be thrown
 * when an authenticated user attempts to access a resource or perform an action
 * that their role or permissions do not allow.
 *
 * Example usage:
 * <pre>
 * if (!userHasPermission(user, resource)) {
 *     throw new MogulForbiddenException("You do not have permission to access this resource");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulForbiddenException extends MogulException {

    /**
     * Constructs a MogulForbiddenException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "MOG-FORBIDDEN");
    }

    /**
     * Constructs a MogulForbiddenException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulForbiddenException(String message, Throwable cause) {
        super(message, HttpStatus.FORBIDDEN, "MOG-FORBIDDEN", cause);
    }
}
