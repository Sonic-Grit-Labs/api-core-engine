package io.theawesomemogul.core.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Internationalization (i18n) configuration for Mogul Audio applications.
 *
 * This configuration enables multi-language support across the Mogul Audio ecosystem,
 * allowing error messages, system messages, and user-facing text to be translated
 * based on the request locale.
 *
 * Features:
 * - Support for multiple languages via ResourceBundleMessageSource
 * - Accept-Language header detection for automatic locale selection
 * - URL parameter override (lang parameter) for explicit locale selection
 * - Default locale set to English (en) if none provided
 * - Message resource bundles: messages.properties, messages_pt_BR.properties, etc.
 *
 * To use:
 * - Inject I18nService into your services
 * - Call getMessage(code) or getMessage(code, locale)
 * - Spring will return translated messages based on current locale
 *
 * Example:
 * <pre>
 * GET /api/users/invalid -> Returns error message in client's language
 * GET /api/users/invalid?lang=pt_BR -> Returns error message in Portuguese
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see I18nService
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * Configures the MessageSource bean for loading i18n resource bundles.
     *
     * Resource bundles are loaded from classpath:messages.properties and variants.
     * Supports multiple languages through locale-specific property files.
     *
     * @return configured ResourceBundleMessageSource
     * @since 1.0.0
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setCacheSeconds(3600); // Cache for 1 hour
        return messageSource;
    }

    /**
     * Configures the LocaleResolver bean using Accept-Language header detection.
     *
     * This resolver reads the Accept-Language header from HTTP requests to determine
     * the client's preferred locale. Defaults to English if not specified.
     *
     * @return AcceptHeaderLocaleResolver configured for English default
     * @since 1.0.0
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    /**
     * Configures the LocaleChangeInterceptor for URL-based locale override.
     *
     * Allows clients to override the Accept-Language header by providing a lang
     * URL parameter (e.g., ?lang=pt_BR).
     *
     * @return configured LocaleChangeInterceptor
     * @since 1.0.0
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registers the LocaleChangeInterceptor in the web MVC interceptor registry.
     *
     * @param registry the InterceptorRegistry
     * @since 1.0.0
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
