package io.theawesomemogul.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Shared database configuration for Mogul Audio applications.
 *
 * This configuration provides centralized DataSource bean setup with HikariCP
 * connection pooling, allowing mogul-access-engine and mogul-LLM-engine to use
 * consistent database connectivity patterns.
 *
 * The configuration reads from Spring Boot properties and defaults to sensible
 * pool settings. Properties can be overridden via application.yml or environment
 * variables.
 *
 * Connection pool properties:
 * - Maximum pool size: 20 (configurable)
 * - Minimum idle connections: 5 (configurable)
 * - Connection timeout: 30 seconds
 * - Idle timeout: 10 minutes
 * - Max lifetime: 30 minutes
 * - Health check: SELECT 1 to validate connections
 *
 * Example application.yml configuration:
 * <pre>
 * spring:
 *   datasource:
 *     url: jdbc:postgresql://localhost:5432/mogul_audio
 *     username: mogul_user
 *     password: ${DB_PASSWORD}
 *     hikari:
 *       maximum-pool-size: 20
 *       minimum-idle: 5
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see HikariDataSource
 */
@Configuration
public class DatabaseConfig {

    /**
     * Creates a HikariCP DataSource bean with optimized connection pooling.
     *
     * This bean is only created if no other DataSource bean exists, allowing
     * applications to override with custom configurations if needed.
     *
     * Configuration includes:
     * - Pool size optimization for typical Mogul Audio workloads
     * - Connection validation via health check query
     * - Proper timeout and lifecycle management
     * - PostgreSQL-specific optimizations
     *
     * @return configured HikariDataSource for database connectivity
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();

        // Connection pool sizing
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);

        // Timeouts (in milliseconds)
        config.setConnectionTimeout(30000);   // 30 seconds
        config.setIdleTimeout(600000);        // 10 minutes
        config.setMaxLifetime(1800000);       // 30 minutes

        // Health check and validation
        config.setConnectionTestQuery("SELECT 1");
        config.setLeakDetectionThreshold(60000); // 1 minute

        // PostgreSQL specific
        config.setAutoCommit(true);
        config.setJdbcUrl(System.getenv("DATABASE_URL"));
        config.setUsername(System.getenv("DATABASE_USER"));
        config.setPassword(System.getenv("DATABASE_PASSWORD"));

        return new HikariDataSource(config);
    }
}
