package io.theawesomemogul.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time operations in Mogul Audio applications.
 *
 * Provides convenience methods for working with Java 8+ time types, including
 * formatting, conversion, and expiration checking.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class DateUtils {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final ZoneId UTC = ZoneId.of("UTC");

    /**
     * Gets the current date and time in UTC.
     *
     * @return the current LocalDateTime in UTC
     * @since 1.0.0
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(UTC);
    }

    /**
     * Gets the current date and time as a ZonedDateTime in UTC.
     *
     * @return the current ZonedDateTime in UTC
     * @since 1.0.0
     */
    public static ZonedDateTime nowZoned() {
        return ZonedDateTime.now(UTC);
    }

    /**
     * Gets the current date and time as an Instant.
     *
     * @return the current Instant
     * @since 1.0.0
     */
    public static Instant nowInstant() {
        return Instant.now();
    }

    /**
     * Converts a LocalDateTime to an Instant.
     *
     * @param dateTime the LocalDateTime to convert
     * @return the corresponding Instant
     * @since 1.0.0
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(UTC).toInstant();
    }

    /**
     * Converts an Instant to a LocalDateTime in UTC.
     *
     * @param instant the Instant to convert
     * @return the corresponding LocalDateTime in UTC
     * @since 1.0.0
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, UTC);
    }

    /**
     * Formats a LocalDateTime as an ISO 8601 string.
     *
     * @param dateTime the LocalDateTime to format
     * @return ISO 8601 formatted string
     * @since 1.0.0
     */
    public static String formatIso(LocalDateTime dateTime) {
        return dateTime.format(ISO_FORMATTER);
    }

    /**
     * Formats a LocalDateTime as an ISO 8601 UTC string.
     *
     * @param dateTime the LocalDateTime to format
     * @return ISO 8601 UTC formatted string
     * @since 1.0.0
     */
    public static String formatIsoUtc(LocalDateTime dateTime) {
        return dateTime.atZone(UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Checks if a LocalDateTime has passed (is before now).
     *
     * @param dateTime the LocalDateTime to check
     * @return true if the datetime is before now, false otherwise
     * @since 1.0.0
     */
    public static boolean isExpired(LocalDateTime dateTime) {
        return dateTime.isBefore(now());
    }

    /**
     * Checks if a LocalDateTime is in the future.
     *
     * @param dateTime the LocalDateTime to check
     * @return true if the datetime is after now, false otherwise
     * @since 1.0.0
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime.isAfter(now());
    }

    /**
     * Calculates the duration between two LocalDateTimes in milliseconds.
     *
     * @param start the start LocalDateTime
     * @param end the end LocalDateTime
     * @return the duration in milliseconds
     * @since 1.0.0
     */
    public static long durationMs(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    /**
     * Calculates the duration between two LocalDateTimes in seconds.
     *
     * @param start the start LocalDateTime
     * @param end the end LocalDateTime
     * @return the duration in seconds
     * @since 1.0.0
     */
    public static long durationSeconds(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    /**
     * Gets the start of today in UTC.
     *
     * @return today at 00:00:00 UTC
     * @since 1.0.0
     */
    public static LocalDateTime startOfToday() {
        return now().toLocalDate().atStartOfDay();
    }

    /**
     * Gets the end of today in UTC.
     *
     * @return today at 23:59:59 UTC
     * @since 1.0.0
     */
    public static LocalDateTime endOfToday() {
        return now().toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
    }
}
