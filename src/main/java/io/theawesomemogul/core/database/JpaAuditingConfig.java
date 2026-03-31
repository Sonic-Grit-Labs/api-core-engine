package io.theawesomemogul.core.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Shared JPA auditing configuration for Mogul Audio applications.
 *
 * This configuration enables automatic tracking of entity creation and modification
 * timestamps through Spring Data JPA's auditing features. It is applied to all
 * entities that use @CreatedDate and @LastModifiedDate annotations.
 *
 * The auditing configuration allows persistent tracking of:
 * - Entity creation timestamps (via @CreatedDate)
 * - Last modification timestamps (via @LastModifiedDate)
 * - Entity creators and modifiers (via @CreatedBy and @LastModifiedBy)
 *
 * All Mogul Audio entities should extend BaseEntity to leverage this auditing.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 * @see BaseEntity
 * @see EnableJpaAuditing
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    /**
     * Provides auditor information for JPA auditing.
     *
     * This bean supplies the current user/system identifier for @CreatedBy and
     * @LastModifiedBy annotations. In the Mogul Audio ecosystem, this defaults
     * to the authenticated user ID from SecurityContext, or "SYSTEM" for
     * background processes.
     *
     * @return an AuditorAware implementation providing the current auditor
     * @since 1.0.0
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("SYSTEM");
    }
}
