package io.theawesomemogul.core.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base entity providing common fields for all Mogul Audio entities.
 *
 * This mapped superclass provides the foundation for all persistent entities in the
 * Mogul Audio ecosystem, including:
 * - Auto-generated UUID primary key
 * - Automatic creation timestamp tracking
 * - Automatic last modification timestamp tracking
 *
 * All entities that require these common fields should extend this class.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    /**
     * Unique identifier for this entity.
     * Auto-generated UUID assigned at creation time.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    /**
     * Timestamp when this entity was created.
     * Set automatically by JPA auditing and never modified.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when this entity was last modified.
     * Updated automatically by JPA auditing on each modification.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
