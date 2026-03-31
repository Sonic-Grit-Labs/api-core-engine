package io.theawesomemogul.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Standardized error response record for Mogul Audio API errors.
 *
 * This record provides a consistent structure for all error responses returned
 * by Mogul Audio services. It includes comprehensive information for client-side
 * error handling, logging, and debugging.
 *
 * Fields:
 * - timestamp: When the error occurred
 * - status: HTTP status code
 * - errorCode: Application-specific error code
 * - message: Human-readable error message
 * - details: Optional map of additional error context
 * - path: The request path that caused the error
 * - traceId: Unique identifier for request tracing and correlation
 *
 * Example JSON response (HTTP 404):
 * <pre>
 * {
 *   "timestamp": "2026-03-31T10:30:00",
 *   "status": 404,
 *   "errorCode": "MOG-NOT-FOUND",
 *   "message": "User not found",
 *   "details": {
 *     "userId": "550e8400-e29b-41d4-a716-446655440000"
 *   },
 *   "path": "/api/users/550e8400-e29b-41d4-a716-446655440000",
 *   "traceId": "550e8400-e29b-41d4-a716-446655440001"
 * }
 * </pre>
 *
 * @param timestamp the timestamp when the error occurred
 * @param status the HTTP status code
 * @param errorCode the application error code
 * @param message the error message
 * @param details optional contextual details (omitted in JSON if null)
 * @param path the request path
 * @param traceId the request trace ID for correlation
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String message,
        Map<String, Object> details,
        String path,
        String traceId
) {
    /**
     * Creates a new ErrorResponse with current timestamp.
     *
     * @param status the HTTP status code
     * @param errorCode the application error code
     * @param message the error message
     * @param path the request path
     * @return a new ErrorResponse with current timestamp and generated trace ID
     * @since 1.0.0
     */
    public static ErrorResponse now(int status, String errorCode, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                errorCode,
                message,
                null,
                path,
                UUID.randomUUID().toString()
        );
    }

    /**
     * Creates a new ErrorResponse with current timestamp and details.
     *
     * @param status the HTTP status code
     * @param errorCode the application error code
     * @param message the error message
     * @param details contextual error details
     * @param path the request path
     * @return a new ErrorResponse with current timestamp and generated trace ID
     * @since 1.0.0
     */
    public static ErrorResponse now(int status, String errorCode, String message,
                                    Map<String, Object> details, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                errorCode,
                message,
                details,
                path,
                UUID.randomUUID().toString()
        );
    }
}
