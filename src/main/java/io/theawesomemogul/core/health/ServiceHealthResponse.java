package io.theawesomemogul.core.health;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Record representing the health status of a Mogul Audio service.
 *
 * Provides comprehensive health information including overall status, service metadata,
 * and individual component health checks. Typically returned by the /actuator/health
 * endpoint or custom health check endpoints.
 *
 * Status values:
 * - UP: Service is healthy and operational
 * - DOWN: Service is not operational
 * - DEGRADED: Service is operational but with reduced capability
 * - UNKNOWN: Service status cannot be determined
 *
 * @param status the overall service status (UP, DOWN, DEGRADED, UNKNOWN)
 * @param serviceInfo metadata about the service
 * @param checks health status of individual service components (omitted if empty)
 *
 * Example JSON response:
 * <pre>
 * {
 *   "status": "UP",
 *   "serviceInfo": {
 *     "name": "mogul-access-engine",
 *     "version": "1.0.0",
 *     "environment": "production",
 *     "startedAt": "2026-03-31T10:00:00",
 *     "uptime": 3600
 *   },
 *   "checks": {
 *     "database": "UP",
 *     "cache": "UP",
 *     "externalApi": "DEGRADED"
 *   }
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ServiceHealthResponse(
        String status,
        ServiceInfo serviceInfo,
        Map<String, String> checks
) {
    /**
     * Creates a ServiceHealthResponse record.
     *
     * @param status the overall health status
     * @param serviceInfo metadata about the service
     * @param checks health status of components
     * @since 1.0.0
     */
    public ServiceHealthResponse {
        // Compact constructor for validation if needed
    }

    /**
     * Creates a healthy service response.
     *
     * @param serviceInfo service metadata
     * @param checks component health checks
     * @return a ServiceHealthResponse with status UP
     * @since 1.0.0
     */
    public static ServiceHealthResponse up(ServiceInfo serviceInfo, Map<String, String> checks) {
        return new ServiceHealthResponse("UP", serviceInfo, checks);
    }

    /**
     * Creates a degraded service response.
     *
     * @param serviceInfo service metadata
     * @param checks component health checks
     * @return a ServiceHealthResponse with status DEGRADED
     * @since 1.0.0
     */
    public static ServiceHealthResponse degraded(ServiceInfo serviceInfo, Map<String, String> checks) {
        return new ServiceHealthResponse("DEGRADED", serviceInfo, checks);
    }

    /**
     * Creates an unhealthy service response.
     *
     * @param serviceInfo service metadata
     * @param checks component health checks
     * @return a ServiceHealthResponse with status DOWN
     * @since 1.0.0
     */
    public static ServiceHealthResponse down(ServiceInfo serviceInfo, Map<String, String> checks) {
        return new ServiceHealthResponse("DOWN", serviceInfo, checks);
    }
}
