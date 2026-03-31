package io.theawesomemogul.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a usage quota or rate limit has been exceeded.
 *
 * This exception corresponds to HTTP 429 Too Many Requests responses. It should be
 * thrown when a user has exceeded their allocated quota for a resource, API calls,
 * storage, or any metered feature in the Mogul Audio platform.
 *
 * Example usage:
 * <pre>
 * if (userQuotaExceeded(user, feature)) {
 *     throw new MogulQuotaExceededException("You've reached the limit for this feature")
 *         .withDetail("limit", 1000)
 *         .withDetail("used", 1000)
 *         .withDetail("resetAt", "2026-04-01T00:00:00Z");
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class MogulQuotaExceededException extends MogulException {

    /**
     * Constructs a MogulQuotaExceededException with a message.
     *
     * @param message the exception message
     * @since 1.0.0
     */
    public MogulQuotaExceededException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS, "MOG-QUOTA-EXCEEDED");
    }

    /**
     * Constructs a MogulQuotaExceededException with a message and cause.
     *
     * @param message the exception message
     * @param cause the underlying cause
     * @since 1.0.0
     */
    public MogulQuotaExceededException(String message, Throwable cause) {
        super(message, HttpStatus.TOO_MANY_REQUESTS, "MOG-QUOTA-EXCEEDED", cause);
    }
}
