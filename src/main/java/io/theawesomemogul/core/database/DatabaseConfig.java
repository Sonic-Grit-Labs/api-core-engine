package io.theawesomemogul.core.database;

import org.springframework.context.annotation.Configuration;

/**
 * Shared database configuration for Mogul Audio applications.
 *
 * <p>DataSource creation is delegated entirely to Spring Boot auto-configuration,
 * which reads {@code spring.datasource.*} properties from each application's
 * {@code application.yml} and environment variables.</p>
 *
 * <p>Each engine defines its own datasource properties:</p>
 * <ul>
 *   <li><b>mogul-access-engine</b> — {@code POSTGRES_HOST}, {@code POSTGRES_PORT},
 *       {@code POSTGRES_DB}, {@code POSTGRES_USER}, {@code POSTGRES_PASSWORD}</li>
 *   <li><b>mogul-AI-engine</b> — {@code DATASOURCE_URL}, {@code DATABASE_USERNAME},
 *       {@code DATABASE_PASSWORD}</li>
 * </ul>
 *
 * <p>HikariCP connection pool tuning is done via {@code spring.datasource.hikari.*}
 * properties in each application's YAML profile (production, staging, dev).</p>
 *
 * <p><b>Important:</b> This class intentionally does NOT create a {@code DataSource}
 * bean. A previous version used {@code System.getenv("DATABASE_URL")} which
 * conflicts with Railway's auto-injected {@code DATABASE_URL} (non-JDBC URI format)
 * and uses different variable names than the engines expect.</p>
 *
 * @author Mogul Audio Core Team
 * @since 2.0.0
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 */
@Configuration
public class DatabaseConfig {
    // DataSource bean is auto-configured by Spring Boot.
    // HikariCP pool settings come from spring.datasource.hikari.* properties.
}
