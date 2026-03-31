package io.theawesomemogul.core.database;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity superclass providing soft delete functionality.
 *
 * This class extends {@link BaseEntity} and adds soft delete support, allowing
 * records to be marked as deleted without being physically removed from the database.
 * This approach maintains referential integrity and provides audit trails for deletions.
 *
 * Soft-deleted records should be filtered from query results in the application layer
 * or using JPA filters.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class SoftDeletableEntity extends BaseEntity {

    /**
     * Flag indicating whether this entity has been soft deleted.
     * Default is false (not deleted).
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * Timestamp when this entity was soft deleted.
     * Null if the entity has not been deleted.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Soft delete this entity by setting the deleted flag and deletion timestamp.
     *
     * @since 1.0.0
     */
    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Restore this entity by unsetting the deleted flag and deletion timestamp.
     *
     * @since 1.0.0
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }

    /**
     * Check if this entity is soft deleted.
     *
     * @return true if the entity is deleted, false otherwise
     * @since 1.0.0
     */
    public boolean isDeleted() {
        return deleted != null && deleted;
    }
}
