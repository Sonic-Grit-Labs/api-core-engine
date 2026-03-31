package io.theawesomemogul.core.health;

import java.time.LocalDateTime;

/**
 * Record representing basic information about a running Mogul Audio service.
 *
 * Provides essential service metadata for monitoring, logging, and debugging.
 * Typically returned by health check endpoints or service info endpoints.
 *
 * @param name the name of the service (e.g., "mogul-access-engine")
 * @param version the version of the service (e.g., "1.0.0")
 * @param environment the deployment environment (e.g., "dev", "staging", "production")
 * @param startedAt the timestamp when the service started
 * @param uptime the service uptime in seconds
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public record ServiceInfo(
        String name,
        String version,
        String environment,
        LocalDateTime startedAt,
        long uptime
) {
    /**
     * Creates a ServiceInfo record.
     *
     * @param name the service name
     * @param version the service version
     * @param environment the deployment environment
     * @param startedAt the service start time
     * @param uptime the uptime in seconds
     * @since 1.0.0
     */
    public ServiceInfo {
        // Compact constructor for validation if needed
    }
}
