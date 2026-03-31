package io.theawesomemogul.core.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Service for retrieving internationalized messages in Mogul Audio applications.
 *
 * This service wraps Spring's MessageSource with convenience methods for accessing
 * translated messages in the current request's locale. It provides both explicit
 * locale selection and automatic locale detection from the current request context.
 *
 * Usage in services:
 * <pre>
 * @Autowired
 * private I18nService i18nService;
 *
 * public void example() {
 *     String message = i18nService.getMessage("error.not_found");
 *     String parameterized = i18nService.getMessage("subscription.quota_exceeded", "API Calls");
 *     String withLocale = i18nService.getMessage("error.unauthorized", Locale.of("pt", "BR"));
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see MessageSource
 */
@Slf4j
@Service
public class I18nService {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    /**
     * Constructs the I18nService.
     *
     * @param messageSource the Spring MessageSource for loading messages
     * @param localeResolver the LocaleResolver for determining request locale
     * @since 1.0.0
     */
    public I18nService(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    /**
     * Retrieves a message for the current request's locale without parameters.
     *
     * This method automatically detects the locale from the current HTTP request
     * using the configured LocaleResolver (typically from Accept-Language header).
     *
     * If the message code is not found, returns the code itself as a fallback.
     *
     * @param code the message code (e.g., "error.not_found")
     * @return the translated message, or the code if message not found
     * @since 1.0.0
     */
    public String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }

    /**
     * Retrieves a parameterized message for the current request's locale.
     *
     * This method automatically detects the locale from the current HTTP request
     * and supports message parameters for dynamic content (e.g., quota limits).
     *
     * Parameters are inserted into the message using {0}, {1}, etc. placeholders.
     *
     * Example message: "quota_exceeded=You've reached the limit for {0}"
     * Call: getMessage("quota_exceeded", "API Calls")
     * Result: "You've reached the limit for API Calls"
     *
     * @param code the message code
     * @param args the message parameters
     * @return the translated and parameterized message, or the code if not found
     * @since 1.0.0
     */
    public String getMessage(String code, Object... args) {
        Locale locale = resolveCurrentLocale();
        return messageSource.getMessage(code, args, code, locale);
    }

    /**
     * Retrieves a message for a specific locale without parameters.
     *
     * @param code the message code
     * @param locale the locale for translation
     * @return the translated message, or the code if message not found
     * @since 1.0.0
     */
    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }

    /**
     * Retrieves a parameterized message for a specific locale.
     *
     * @param code the message code
     * @param locale the locale for translation
     * @param args the message parameters
     * @return the translated and parameterized message, or the code if not found
     * @since 1.0.0
     */
    public String getMessage(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, code, locale);
    }

    /**
     * Resolves the current request's locale using the configured LocaleResolver.
     *
     * If no request context is available (e.g., in background jobs), returns the
     * default locale (English).
     *
     * @return the resolved locale, or Locale.ENGLISH if no request context exists
     * @since 1.0.0
     */
    private Locale resolveCurrentLocale() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Locale locale = localeResolver.resolveLocale(request);
                if (locale != null) {
                    return locale;
                }
            }
        } catch (Exception e) {
            log.debug("Could not resolve locale from request context", e);
        }
        return Locale.ENGLISH;
    }
}
