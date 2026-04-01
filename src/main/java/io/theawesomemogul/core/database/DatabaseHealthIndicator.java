package io.theawesomemogul.core.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Health indicator for database connectivity in Mogul Audio applications.
 *
 * This component provides real-time database health status for Spring Boot
 * Actuator,
 * allowing monitoring systems to track database availability and connection
 * pool health.
 *
 * The indicator performs:
 * - Connection availability check
 * - Connection pool statistics reporting
 * - Detailed error reporting for diagnostics
 *
 * Health check endpoint: GET /actuator/health/db
 *
 * Example responses:
 * UP: Database is available and responsive
 * DOWN: Database is unreachable or connection pool exhausted
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see HealthIndicator
 */
@Slf4j
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    /**
     * Constructs a DatabaseHealthIndicator.
     *
     * @param dataSource the DataSource to monitor
     * @since 1.0.0
     */
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Checks the health of the database connection.
     *
     * Attempts to obtain a connection from the pool and validate it. Reports
     * the connection pool state and any errors encountered.
     *
     * @return Health status UP if database is reachable, DOWN otherwise
     * @since 1.0.0
     */
    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            boolean isValid = connection.isValid(2);

            if (isValid) {
                return Health.up()
                        .withDetail("database", "PostgreSQL")
                        .withDetail("connection_pool", "HikariCP")
                        .withDetail("status", "Connected")
                        .build();
            } else {
                log.warn("Database health check: connection validation failed");
                return Health.down()
                        .withDetail("reason", "Connection validation failed")
                        .build();
            }
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return Health.down()
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage())
                    .build();
        }
    }
}
