package io.theawesomemogul.core;

import io.theawesomemogul.core.exception.MogulBadRequestException;
import io.theawesomemogul.core.exception.MogulNotFoundException;
import io.theawesomemogul.core.util.JsonUtils;
import io.theawesomemogul.core.util.StringUtils;
import io.theawesomemogul.core.util.ValidationUtils;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

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
class ApiCoreEngineTests {

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
                "enabled", true);

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
        assertEquals(404, exception.getStatusCode());
        assertEquals(2, exception.getDetails().size());
        assertTrue(exception.getDetails().containsKey("userId"));
    }

    @Test
    void testMogulBadRequestException() {
        MogulBadRequestException exception = new MogulBadRequestException("Invalid input");
        exception.withDetail("field", "email");
        exception.withDetail("reason", "format");

        assertEquals("MOG-BAD-REQUEST", exception.getErrorCode());
        assertEquals(400, exception.getStatusCode());
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

    // -------------------------------------------------------------------------
    // JsonUtils — testes de inicialização e uso das dependências de runtime
    // Estes testes expõem falhas de ClassNotFoundException /
    // ExceptionInInitializerError
    // causadas pelo JavaTimeModule (jackson-datatype-jsr310) e Jdk8Module
    // (jackson-datatype-jdk8) não estarem disponíveis em runtime.
    // -------------------------------------------------------------------------

    @Test
    void testJsonUtilsObjectMapperInitializes() {
        // Força a inicialização do campo estático ObjectMapper do JsonUtils.
        // Falha com ExceptionInInitializerError se JavaTimeModule ou Jdk8Module
        // não forem encontrados no classpath em runtime.
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        assertNotNull(mapper,
                "ObjectMapper não deve ser nulo — verifique se jackson-datatype-jsr310 e jackson-datatype-jdk8 estão no classpath");
    }

    @Test
    void testJsonUtilsSerializaLocalDateTime() {
        // Exercita o JavaTimeModule (jackson-datatype-jsr310).
        // Sem o módulo registrado, LocalDateTime seria serializado como array de
        // inteiros
        // ou lançaria exception dependendo da versão do Jackson.
        LocalDateTime agora = LocalDateTime.of(2026, 4, 2, 12, 0, 0);
        String json = JsonUtils.toJson(agora);
        assertNotNull(json);
        // Com WRITE_DATES_AS_TIMESTAMPS desabilitado, deve serializar como string
        // ISO-8601
        assertFalse(json.startsWith("["),
                "LocalDateTime deve ser serializado como string ISO-8601, não como array — JavaTimeModule pode não estar registrado");
        assertTrue(JsonUtils.isValidJson(json));
    }

    @Test
    void testJsonUtilsDeserializaLocalDateTime() {
        // Testa round-trip de LocalDateTime para garantir que o JavaTimeModule
        // está funcional tanto na serialização quanto na desserialização.
        LocalDateTime original = LocalDateTime.of(2026, 4, 2, 15, 30, 0);
        String json = JsonUtils.toJson(original);
        LocalDateTime desserializado = JsonUtils.fromJson(json, LocalDateTime.class);
        assertEquals(original, desserializado, "Round-trip de LocalDateTime falhou — verifique o JavaTimeModule");
    }

    @Test
    void testJsonUtilsSerializaInstant() {
        // Exercita o JavaTimeModule com Instant.
        // Instant requer registro explícito do JavaTimeModule para ser
        // serializado/desserializado corretamente.
        Instant agora = Instant.parse("2026-04-02T12:00:00Z");
        String json = JsonUtils.toJson(agora);
        assertNotNull(json);
        assertTrue(JsonUtils.isValidJson(json));
        Instant desserializado = JsonUtils.fromJson(json, Instant.class);
        assertEquals(agora, desserializado, "Round-trip de Instant falhou — verifique o JavaTimeModule");
    }

    @Test
    void testJsonUtilsSerializaZonedDateTime() {
        // ZonedDateTime é outro tipo que depende do JavaTimeModule.
        ZonedDateTime zdt = ZonedDateTime.parse("2026-04-02T12:00:00Z");
        String json = JsonUtils.toJson(zdt);
        assertNotNull(json);
        assertTrue(JsonUtils.isValidJson(json));
    }

    @Test
    void testJsonUtilsSerializaOptional() {
        // Exercita o Jdk8Module (jackson-datatype-jdk8).
        // Sem o Jdk8Module, Optional<String> seria serializado como objeto
        // com campo interno em vez de um valor direto, ou lançaria exception.
        Optional<String> presente = Optional.of("mogul-audio");
        String json = JsonUtils.toJson(presente);
        assertNotNull(json);
        assertTrue(JsonUtils.isValidJson(json),
                "Serialização de Optional falhou — verifique se o Jdk8Module está no classpath");

        Optional<String> vazio = Optional.empty();
        String jsonVazio = JsonUtils.toJson(vazio);
        assertNotNull(jsonVazio);
        assertTrue(JsonUtils.isValidJson(jsonVazio));
    }

    @Test
    void testJsonUtilsToPrettyJson() {
        Map<String, Object> dados = Map.of("projeto", "mogul-audio", "versao", "2.0.0");
        String prettyJson = JsonUtils.toPrettyJson(dados);
        assertNotNull(prettyJson);
        assertTrue(prettyJson.contains("\n"), "toPrettyJson deve conter quebras de linha");
        assertTrue(JsonUtils.isValidJson(prettyJson));
    }

    @Test
    void testJsonUtilsToMap() {
        Map<String, Object> original = Map.of("chave", "valor", "numero", 42);
        Map<String, Object> resultado = JsonUtils.toMap(original);
        assertNotNull(resultado);
        assertEquals("valor", resultado.get("chave"));
        // Jackson desserializa números inteiros como Integer
        assertEquals(42, ((Number) resultado.get("numero")).intValue());
    }

    @Test
    void testJsonUtilsIsValidJsonComJsonInvalido() {
        assertFalse(JsonUtils.isValidJson("{invalid"));
        assertFalse(JsonUtils.isValidJson("not json at all"));
        // Nota: string vazia não lança exception no Jackson (readTree retorna null),
        // portanto não é incluída na assertiva de "inválido".
        assertTrue(JsonUtils.isValidJson("{}"));
        assertTrue(JsonUtils.isValidJson("[]"));
        assertTrue(JsonUtils.isValidJson("\"string simples\""));
        assertTrue(JsonUtils.isValidJson("null"));
        assertTrue(JsonUtils.isValidJson("42"));
    }

    @Test
    void testJsonUtilsFromJsonComJsonInvalidoLancaException() {
        // Garante que fromJson propaga RuntimeException com mensagem legível
        // em vez de expor detalhes internos do Jackson.
        assertThrows(RuntimeException.class,
                () -> JsonUtils.fromJson("{invalid-json}", Map.class),
                "fromJson deve lançar RuntimeException para JSON inválido");
    }

    @Test
    void testJsonUtilsToJsonNullProduceJsonNullLiteral() {
        // null deve ser serializado como o literal JSON "null".
        String json = JsonUtils.toJson(null);
        assertEquals("null", json);
        assertTrue(JsonUtils.isValidJson(json));
    }
}
