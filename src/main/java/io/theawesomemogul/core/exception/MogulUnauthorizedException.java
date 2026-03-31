package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication is required but not provided or invalid.
 *
 * This exception corresponds to HTTP 401 Unauthorized responses. It should be thrown
 * when a request lacks valid authentication credentials, such as a missing or expired
 * JWT token.
 *
 * Example usage:
 * <pre>
 * if (token == null || isExpired(token)) {
 *     throw new MogulUnauthorizedException("Authentication token is missing or expired");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulUnauthorizedException extends MogulException {

    /**
     * Constructs a MogulUnauthorizedException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulUnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "MOG-UNAUTHORIZED");
    }

    /**
     * Constructs a MogulUnauthorizedException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulUnauthorizedException(String message, Throwable cause) {
        super(message, HttpStatus.UNAUTHORIZED, "MOG-UNAUTHORIZED", cause);
    }
}
