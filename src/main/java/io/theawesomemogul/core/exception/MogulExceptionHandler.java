package io.theawesomemogul.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Global exception handler for Mogul Audio REST APIs.
 *
 * This controller advice intercepts all exceptions thrown in Mogul Audio services
 * and converts them into standardized error responses. It ensures consistent error
 * handling, logging, and API response formats across the entire ecosystem.
 *
 * Handled exceptions:
 * - MogulException and all subclasses (application errors)
 * - IllegalArgumentException (validation errors)
 * - General exceptions (unexpected errors)
 *
 * All error responses include:
 * - Timestamp and trace ID for correlation
 * - HTTP status code and application error code
 * - Human-readable message and contextual details
 * - Request path for debugging
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see MogulException
 * @see ErrorResponse
 */
@Slf4j
@RestControllerAdvice
public class MogulExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all MogulException instances and subclasses.
     *
     * Converts Mogul exceptions into structured error responses with appropriate
     * HTTP status codes and error details.
     *
     * @param ex the caught MogulException
     * @param request the current request
     * @return ResponseEntity with error response and appropriate HTTP status
     * @since 1.0.0
     */
    @ExceptionHandler(MogulException.class)
    public ResponseEntity<ErrorResponse> handleMogulException(
            MogulException ex,
            WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");
        String traceId = UUID.randomUUID().toString();

        log.warn("Mogul application exception: {} [{}] - {}",
                ex.getClass().getSimpleName(),
                ex.getErrorCode(),
                ex.getMessage());

        if (log.isDebugEnabled()) {
            log.debug("Exception details: {}", ex.getDetails(), ex);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getHttpStatus().value(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails().isEmpty() ? null : ex.getDetails(),
                path,
                traceId
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handles IllegalArgumentException instances.
     *
     * Converts validation exceptions into 400 Bad Request responses.
     *
     * @param ex the caught IllegalArgumentException
     * @param request the current request
     * @return ResponseEntity with error response and 400 status
     * @since 1.0.0
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");
        String traceId = UUID.randomUUID().toString();

        log.warn("Validation error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "MOG-BAD-REQUEST",
                ex.getMessage(),
                null,
                path,
                traceId
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * Converts uncaught exceptions into 500 Internal Server Error responses
     * with generic messaging to avoid exposing internal implementation details.
     *
     * @param ex the caught exception
     * @param request the current request
     * @return ResponseEntity with error response and 500 status
     * @since 1.0.0
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");
        String traceId = UUID.randomUUID().toString();

        log.error("Unexpected exception occurred. TraceId: {}", traceId, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "MOG-SERVICE-ERROR",
                "An unexpected error occurred. Please contact support with trace ID: " + traceId,
                null,
                path,
                traceId
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
