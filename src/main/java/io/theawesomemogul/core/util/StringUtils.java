package io.theawesomemogul.core.util;

import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Utility class for common string operations used in Mogul Audio applications.
 *
 * Provides string manipulation utilities including slugification, truncation,
 * and masking of sensitive information.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
public class StringUtils {

    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Pattern NON_ALPHANUM_PATTERN = Pattern.compile("[^a-z0-9-_]");

    /**
     * Converts a string to a URL-friendly slug.
     *
     * Removes diacritical marks, converts to lowercase, and replaces spaces and
     * special characters with hyphens.
     *
     * Example: "Hello World!" -> "hello-world"
     *
     * @param input the input string
     * @return the slugified string
     * @since 1.0.0
     */
    public static String slugify(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Normalize to NFD and remove diacritical marks
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutDiacritics = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");

        // Convert to lowercase
        String lowercase = withoutDiacritics.toLowerCase();

        // Replace spaces with hyphens
        String withHyphens = lowercase.replaceAll("\\s+", "-");

        // Remove non-alphanumeric characters except hyphens and underscores
        String slug = NON_ALPHANUM_PATTERN.matcher(withHyphens).replaceAll("");

        // Remove consecutive hyphens
        slug = slug.replaceAll("-+", "-");

        // Remove leading and trailing hyphens
        return slug.replaceAll("^-|-$", "");
    }

    /**
     * Truncates a string to a specified length with ellipsis.
     *
     * If the string is longer than the specified length, it is truncated and
     * "..." is appended.
     *
     * Example: truncate("Hello World", 8) -> "Hello..."
     *
     * @param input the input string
     * @param maxLength the maximum length including ellipsis
     * @return the truncated string
     * @since 1.0.0
     */
    public static String truncate(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }

        if (maxLength < 3) {
            return input.substring(0, maxLength);
        }

        return input.substring(0, maxLength - 3) + "...";
    }

    /**
     * Masks an email address to protect privacy.
     *
     * Shows the first 2 characters and the domain, masks the rest.
     *
     * Example: maskEmail("john.doe@example.com") -> "jo***@example.com"
     *
     * @param email the email address to mask
     * @return the masked email address
     * @since 1.0.0
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 2) {
            return localPart + "***@" + domain;
        }

        return localPart.substring(0, 2) + "***@" + domain;
    }

    /**
     * Masks a license key or token to protect sensitive information.
     *
     * Shows only the first 4 and last 4 characters, masks the rest.
     *
     * Example: maskLicenseKey("ABC123DEF456GHI789") -> "ABC1...I789"
     *
     * @param licenseKey the license key to mask
     * @return the masked license key
     * @since 1.0.0
     */
    public static String maskLicenseKey(String licenseKey) {
        if (licenseKey == null || licenseKey.length() <= 8) {
            return "***";
        }

        String first = licenseKey.substring(0, 4);
        String last = licenseKey.substring(licenseKey.length() - 4);
        return first + "..." + last;
    }

    /**
     * Checks if a string is null or empty (whitespace only).
     *
     * @param input the string to check
     * @return true if the string is null or empty
     * @since 1.0.0
     */
    public static boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty (whitespace only).
     *
     * @param input the string to check
     * @return true if the string is not null and not empty
     * @since 1.0.0
     */
    public static boolean isNotBlank(String input) {
        return !isBlank(input);
    }

    /**
     * Capitalizes the first character of a string.
     *
     * @param input the input string
     * @return the capitalized string
     * @since 1.0.0
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
