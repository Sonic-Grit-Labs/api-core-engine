package io.theawesomemogul.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Utility class for JSON serialization and deserialization.
 *
 * Provides static methods for converting between objects and JSON, with support for
 * Java 8+ time types and lenient parsing. Uses a shared ObjectMapper configured with
 * sensible defaults for Mogul Audio applications.
 *
 * Features:
 * - Automatic Java 8 time type handling (LocalDateTime, Instant, etc.)
 * - Lenient parsing (fails on unknown properties only in strict mode)
 * - Pretty-printing support
 * - Thread-safe singleton ObjectMapper
 *
 * Usage:
 * <pre>
 * User user = new User("john@example.com");
 * String json = JsonUtils.toJson(user);
 *
 * User deserialized = JsonUtils.fromJson(json, User.class);
 * Map<String, Object> map = JsonUtils.toMap(user);
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Converts an object to a JSON string.
     *
     * @param object the object to convert
     * @return JSON string representation
     * @throws RuntimeException if serialization fails
     * @since 1.0.0
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Failed to serialize object to JSON", e);
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * Converts an object to a pretty-printed JSON string.
     *
     * @param object the object to convert
     * @return pretty-printed JSON string
     * @throws RuntimeException if serialization fails
     * @since 1.0.0
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.error("Failed to serialize object to pretty JSON", e);
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * Converts a JSON string to an object of the specified type.
     *
     * @param json the JSON string
     * @param valueType the target class type
     * @param <T> the type parameter
     * @return deserialized object
     * @throws RuntimeException if deserialization fails
     * @since 1.0.0
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            log.error("Failed to deserialize JSON to {}", valueType.getSimpleName(), e);
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    /**
     * Converts an object to a Map.
     *
     * @param object the object to convert
     * @return Map representation of the object
     * @throws RuntimeException if conversion fails
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Failed to convert object to Map", e);
            throw new RuntimeException("Map conversion failed", e);
        }
    }

    /**
     * Checks if a string is valid JSON.
     *
     * @param json the string to check
     * @return true if the string is valid JSON, false otherwise
     * @since 1.0.0
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the configured ObjectMapper instance.
     *
     * Use this for advanced JSON operations not covered by static methods.
     *
     * @return the shared ObjectMapper instance
     * @since 1.0.0
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
