package io.theawesomemogul.core.logging;

import org.springframework.context.annotation.Configuration;

/**
 * Logging configuration for Mogul Audio applications.
 *
 * This configuration registers and enables the logging filters for all Mogul Audio
 * services. The filters are automatically registered as Spring components when this
 * configuration is included.
 *
 * Registered filters:
 * - CorrelationIdFilter: Generates and propagates request correlation IDs
 * - RequestLoggingFilter: Logs HTTP request/response details
 *
 * The filters operate in the order they are declared in the Spring component registry.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see CorrelationIdFilter
 * @see RequestLoggingFilter
 */
@Configuration
public class LoggingConfig {
    // Filter registration is handled via @Component annotations on the filter classes
}
