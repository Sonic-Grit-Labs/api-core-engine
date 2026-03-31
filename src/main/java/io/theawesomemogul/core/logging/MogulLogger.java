package io.theawesomemogul.core.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * Structured logging utility for Mogul Audio applications.
 *
 * This utility wraps SLF4J with convenience methods for structured logging,
 * supporting JSON-formatted logs for better parsing and analytics. It provides
 * specialized methods for different log types: business events, security events,
 * and performance metrics.
 *
 * Features:
 * - Structured logging with context fields
 * - JSON serialization support
 * - Audit trail logging for business events
 * - Performance monitoring (operation duration tracking)
 * - Security event logging with isolation
 * - Thread-safe MDC (Mapped Diagnostic Context) support
 *
 * Usage:
 * <pre>
 * private static final MogulLogger logger = MogulLogger.of(UserService.class);
 *
 * public void createUser(String email) {
 *     logger.audit(userId, "USER_CREATED", Map.of("email", email));
 *     logger.perf("createUser", duration);
 *     logger.security("LOGIN_SUCCESS", Map.of("userId", userId, "ip", clientIp));
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
public class MogulLogger {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger;

    /**
     * Context fields that can be added to all logs via MDC.
     */
    private final Map<String, String> contextFields = new HashMap<>();

    /**
     * Private constructor for MogulLogger.
     *
     * @param logger the underlying SLF4J logger
     * @since 1.0.0
     */
    private MogulLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Factory method to create a MogulLogger for a given class.
     *
     * @param clazz the class for which to create a logger
     * @param <T> the class type
     * @return a new MogulLogger instance
     * @since 1.0.0
     */
    public static <T> MogulLogger of(Class<T> clazz) {
        return new MogulLogger(LoggerFactory.getLogger(clazz));
    }

    /**
     * Sets a context field that will be included in all subsequent logs.
     *
     * @param key the field name
     * @param value the field value
     * @return this logger for method chaining
     * @since 1.0.0
     */
    public MogulLogger withContext(String key, String value) {
        this.contextFields.put(key, value);
        MDC.put(key, value);
        return this;
    }

    /**
     * Sets the user ID in the logging context.
     *
     * @param userId the user ID
     * @return this logger for method chaining
     * @since 1.0.0
     */
    public MogulLogger withUserId(String userId) {
        return withContext("userId", userId);
    }

    /**
     * Sets the request ID in the logging context.
     *
     * @param requestId the request ID
     * @return this logger for method chaining
     * @since 1.0.0
     */
    public MogulLogger withRequestId(String requestId) {
        return withContext("requestId", requestId);
    }

    /**
     * Sets the service name in the logging context.
     *
     * @param serviceName the service name
     * @return this logger for method chaining
     * @since 1.0.0
     */
    public MogulLogger withService(String serviceName) {
        return withContext("service", serviceName);
    }

    /**
     * Clears all context fields from the MDC.
     *
     * @since 1.0.0
     */
    public void clearContext() {
        MDC.clear();
        contextFields.clear();
    }

    /**
     * Logs a business audit event with action and details.
     *
     * Useful for tracking significant business operations like user creation,
     * subscription changes, license activation, etc.
     *
     * @param userId the user performing the action
     * @param action the action performed (e.g., "USER_CREATED", "SUBSCRIPTION_UPGRADED")
     * @param details additional context about the action
     * @since 1.0.0
     */
    public void audit(String userId, String action, Map<String, Object> details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "AUDIT");
        logEntry.put("userId", userId);
        logEntry.put("action", action);
        logEntry.putAll(details);

        try {
            String json = objectMapper.writeValueAsString(logEntry);
            logger.info(json);
        } catch (Exception e) {
            logger.info("AUDIT [{}] action={} userId={} details={}", action, userId, details);
        }
    }

    /**
     * Logs a performance metric for an operation.
     *
     * Useful for monitoring operation latency and identifying bottlenecks.
     *
     * @param operation the operation name (e.g., "getUserById", "queryDatabase")
     * @param durationMs the operation duration in milliseconds
     * @since 1.0.0
     */
    public void perf(String operation, long durationMs) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "PERF");
        logEntry.put("operation", operation);
        logEntry.put("durationMs", durationMs);
        logEntry.putAll(contextFields);

        try {
            String json = objectMapper.writeValueAsString(logEntry);
            logger.info(json);
        } catch (Exception e) {
            logger.info("PERF [{}] duration={}ms", operation, durationMs);
        }
    }

    /**
     * Logs a security-relevant event.
     *
     * Used for tracking authentication, authorization, and suspicious activities.
     *
     * @param event the security event (e.g., "LOGIN_SUCCESS", "INVALID_TOKEN", "UNAUTHORIZED_ACCESS")
     * @param details additional security context
     * @since 1.0.0
     */
    public void security(String event, Map<String, Object> details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "SECURITY");
        logEntry.put("event", event);
        logEntry.putAll(details);

        try {
            String json = objectMapper.writeValueAsString(logEntry);
            logger.warn(json);
        } catch (Exception e) {
            logger.warn("SECURITY [{}] details={}", event, details);
        }
    }

    /**
     * Logs an info-level message.
     *
     * @param message the message
     * @since 1.0.0
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Logs an info-level message with arguments.
     *
     * @param message the message template
     * @param args message arguments
     * @since 1.0.0
     */
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    /**
     * Logs a debug-level message.
     *
     * @param message the message
     * @since 1.0.0
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /**
     * Logs a debug-level message with arguments.
     *
     * @param message the message template
     * @param args message arguments
     * @since 1.0.0
     */
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    /**
     * Logs a warning-level message.
     *
     * @param message the message
     * @since 1.0.0
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs a warning-level message with arguments.
     *
     * @param message the message template
     * @param args message arguments
     * @since 1.0.0
     */
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    /**
     * Logs an error-level message.
     *
     * @param message the message
     * @since 1.0.0
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * Logs an error-level message with arguments.
     *
     * @param message the message template
     * @param args message arguments
     * @since 1.0.0
     */
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    /**
     * Logs an error-level message with exception.
     *
     * @param message the message
     * @param throwable the exception
     * @since 1.0.0
     */
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
