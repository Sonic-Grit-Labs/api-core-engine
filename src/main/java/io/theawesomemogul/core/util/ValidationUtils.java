package io.theawesomemogul.core.util;

import io.theawesomemogul.core.exception.MogulBadRequestException;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations in Mogul Audio applications.
 *
 * Provides convenience methods for validating input, throwing standardized exceptions
 * when validation fails. Used throughout the Mogul Audio services for input validation.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    private static final Pattern UUID_PATTERN =
            Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                    Pattern.CASE_INSENSITIVE);

    /**
     * Requires a string value to be non-blank (not null, not empty, not whitespace only).
     *
     * Throws MogulBadRequestException if the value is blank.
     *
     * @param value the value to check
     * @param fieldName the name of the field (for error message)
     * @throws MogulBadRequestException if the value is blank
     * @since 1.0.0
     */
    public static void requireNonBlank(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            throw new MogulBadRequestException(fieldName + " cannot be blank")
                    .withDetail("field", fieldName);
        }
    }

    /**
     * Requires a numeric value to be positive (greater than zero).
     *
     * Throws MogulBadRequestException if the value is not positive.
     *
     * @param value the value to check
     * @param fieldName the name of the field (for error message)
     * @throws MogulBadRequestException if the value is not positive
     * @since 1.0.0
     */
    public static void requirePositive(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new MogulBadRequestException(fieldName + " must be greater than zero")
                    .withDetail("field", fieldName)
                    .withDetail("value", value);
        }
    }

    /**
     * Requires a numeric value to be non-negative (greater than or equal to zero).
     *
     * Throws MogulBadRequestException if the value is negative.
     *
     * @param value the value to check
     * @param fieldName the name of the field (for error message)
     * @throws MogulBadRequestException if the value is negative
     * @since 1.0.0
     */
    public static void requireNonNegative(Number value, String fieldName) {
        if (value == null || value.doubleValue() < 0) {
            throw new MogulBadRequestException(fieldName + " cannot be negative")
                    .withDetail("field", fieldName)
                    .withDetail("value", value);
        }
    }

    /**
     * Requires a string value to be a valid email address.
     *
     * Throws MogulBadRequestException if the value is not a valid email.
     *
     * @param email the email address to validate
     * @throws MogulBadRequestException if the email is invalid
     * @since 1.0.0
     */
    public static void requireValidEmail(String email) {
        if (StringUtils.isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new MogulBadRequestException("Invalid email address")
                    .withDetail("email", email);
        }
    }

    /**
     * Requires a string value to be a valid UUID.
     *
     * Throws MogulBadRequestException if the value is not a valid UUID.
     *
     * @param uuidString the UUID string to validate
     * @throws MogulBadRequestException if the UUID is invalid
     * @since 1.0.0
     */
    public static void requireValidUUID(String uuidString) {
        if (StringUtils.isBlank(uuidString) || !UUID_PATTERN.matcher(uuidString).matches()) {
            throw new MogulBadRequestException("Invalid UUID format")
                    .withDetail("value", uuidString);
        }
        // Also try to parse it
        try {
            UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new MogulBadRequestException("Invalid UUID format", e)
                    .withDetail("value", uuidString);
        }
    }

    /**
     * Requires a value to be non-null.
     *
     * Throws MogulBadRequestException if the value is null.
     *
     * @param value the value to check
     * @param fieldName the name of the field (for error message)
     * @throws MogulBadRequestException if the value is null
     * @since 1.0.0
     */
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new MogulBadRequestException(fieldName + " cannot be null")
                    .withDetail("field", fieldName);
        }
    }

    /**
     * Checks if a string is a valid email address.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     * @since 1.0.0
     */
    public static boolean isValidEmail(String email) {
        return !StringUtils.isBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Checks if a string is a valid UUID.
     *
     * @param uuidString the UUID string to validate
     * @return true if the UUID is valid, false otherwise
     * @since 1.0.0
     */
    public static boolean isValidUUID(String uuidString) {
        if (StringUtils.isBlank(uuidString) || !UUID_PATTERN.matcher(uuidString).matches()) {
            return false;
        }
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
