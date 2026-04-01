package io.theawesomemogul.core;

import io.theawesomemogul.core.exception.MogulBadRequestException;
import io.theawesomemogul.core.exception.MogulNotFoundException;
import io.theawesomemogul.core.util.JsonUtils;
import io.theawesomemogul.core.util.StringUtils;
import io.theawesomemogul.core.util.ValidationUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Mogul Audio Core library.
 *
 * Verifies that core utilities and exception handling work correctly.
 * This test class is executed as part of the library build process to ensure
 * the library can be loaded and basic functionality is operational.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
class MogulAudioCoreTests {

    @Test
    void testLibraryLoads() {
        // Verify the library loads without errors
        assertNotNull(StringUtils.class);
        assertNotNull(JsonUtils.class);
        assertNotNull(ValidationUtils.class);
    }

    @Test
    void testStringUtilsSlugify() {
        String result = StringUtils.slugify("Hello World!");
        assertEquals("hello-world", result);
    }

    @Test
    void testStringUtilsMaskEmail() {
        String result = StringUtils.maskEmail("john.doe@example.com");
        assertEquals("jo***@example.com", result);
    }

    @Test
    void testStringUtilsIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank("hello"));
    }

    @Test
    void testJsonUtilsRoundTrip() {
        Map<String, Object> original = Map.of(
                "name", "Mogul Audio",
                "version", "1.0.0",
                "enabled", true
        );

        String json = JsonUtils.toJson(original);
        assertNotNull(json);
        assertTrue(JsonUtils.isValidJson(json));

        Map<String, Object> deserialized = JsonUtils.fromJson(json, Map.class);
        assertEquals("Mogul Audio", deserialized.get("name"));
        assertEquals("1.0.0", deserialized.get("version"));
    }

    @Test
    void testValidationUtilsEmail() {
        assertTrue(ValidationUtils.isValidEmail("user@example.com"));
        assertFalse(ValidationUtils.isValidEmail("invalid.email"));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    void testValidationUtilsRequireNonBlank() {
        assertDoesNotThrow(() -> ValidationUtils.requireNonBlank("valid", "field"));
        assertThrows(MogulBadRequestException.class,
                () -> ValidationUtils.requireNonBlank("", "field"));
        assertThrows(MogulBadRequestException.class,
                () -> ValidationUtils.requireNonBlank(null, "field"));
    }

    @Test
    void testValidationUtilsRequireValidEmail() {
        assertDoesNotThrow(() -> ValidationUtils.requireValidEmail("user@example.com"));
        assertThrows(MogulBadRequestException.class,
                () -> ValidationUtils.requireValidEmail("invalid.email"));
    }

    @Test
    void testMogulExceptionHandling() {
        MogulNotFoundException exception = new MogulNotFoundException("User not found");
        exception.withDetail("userId", 123);
        exception.withDetail("searchedAt", "2026-03-31");

        assertEquals("MOG-NOT-FOUND", exception.getErrorCode());
        assertEquals(404, exception.getHttpStatus().value());
        assertEquals(2, exception.getDetails().size());
        assertTrue(exception.getDetails().containsKey("userId"));
    }

    @Test
    void testMogulBadRequestException() {
        MogulBadRequestException exception = new MogulBadRequestException("Invalid input");
        exception.withDetail("field", "email");
        exception.withDetail("reason", "format");

        assertEquals("MOG-BAD-REQUEST", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus().value());
    }

    @Test
    void testValidationUtilsUUID() {
        String validUUID = "550e8400-e29b-41d4-a716-446655440000";
        assertTrue(ValidationUtils.isValidUUID(validUUID));

        assertFalse(ValidationUtils.isValidUUID("not-a-uuid"));
        assertFalse(ValidationUtils.isValidUUID(""));
        assertFalse(ValidationUtils.isValidUUID(null));
    }

    @Test
    void testStringUtilsTruncate() {
        String result = StringUtils.truncate("Hello World", 8);
        assertEquals("Hello...", result);

        String shortString = StringUtils.truncate("Hi", 5);
        assertEquals("Hi", shortString);
    }

    @Test
    void testStringUtilsCapitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("A", StringUtils.capitalize("a"));
    }
}
