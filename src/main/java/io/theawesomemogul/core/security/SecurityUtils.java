package io.theawesomemogul.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class for accessing security information in Mogul Audio applications.
 *
 * Provides convenience methods for retrieving the current authenticated user's
 * information from Spring Security's SecurityContext. These methods help services
 * determine who is making a request and what permissions they have.
 *
 * Usage:
 * <pre>
 * Optional<UUID> userId = SecurityUtils.getCurrentUserId();
 * if (userId.isPresent()) {
 *     // User is authenticated
 * }
 *
 * Optional<String> role = SecurityUtils.getCurrentUserRole();
 * if (role.filter(r -> r.equals("ADMIN")).isPresent()) {
 *     // User is admin
 * }
 * </pre>
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
public class SecurityUtils {

    /**
     * Retrieves the current authenticated user's ID from the SecurityContext.
     *
     * Returns an empty Optional if:
     * - No authentication is present
     * - User is not authenticated
     * - Subject claim is missing from JWT
     *
     * @return Optional containing the user ID (UUID) or empty
     * @since 1.0.0
     */
    public static Optional<UUID> getCurrentUserId() {
        return getCurrentAuthentication()
                .flatMap(auth -> extractUserIdFromAuth(auth));
    }

    /**
     * Retrieves the current authenticated user's email from the SecurityContext.
     *
     * Returns an empty Optional if:
     * - No authentication is present
     * - User is not authenticated
     * - Email claim is missing from JWT
     *
     * @return Optional containing the user email or empty
     * @since 1.0.0
     */
    public static Optional<String> getCurrentUserEmail() {
        return getCurrentAuthentication()
                .flatMap(auth -> extractEmailFromAuth(auth));
    }

    /**
     * Retrieves the current authenticated user's role from the SecurityContext.
     *
     * Returns an empty Optional if:
     * - No authentication is present
     * - User is not authenticated
     * - Role claim is missing from JWT
     *
     * @return Optional containing the user role or empty
     * @since 1.0.0
     */
    public static Optional<String> getCurrentUserRole() {
        return getCurrentAuthentication()
                .flatMap(auth -> extractRoleFromAuth(auth));
    }

    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if the user is authenticated and not anonymous, false otherwise
     * @since 1.0.0
     */
    public static boolean isAuthenticated() {
        return getCurrentAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }

    /**
     * Retrieves the current Authentication from SecurityContextHolder.
     *
     * @return Optional containing the Authentication or empty
     * @since 1.0.0
     */
    private static Optional<Authentication> getCurrentAuthentication() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context != null) {
                Authentication auth = context.getAuthentication();
                if (auth != null && auth.isAuthenticated()) {
                    return Optional.of(auth);
                }
            }
        } catch (Exception e) {
            log.debug("Error retrieving authentication from context", e);
        }
        return Optional.empty();
    }

    /**
     * Extracts user ID from the current Authentication.
     *
     * Supports JWT tokens where the user ID is stored in the "sub" claim.
     *
     * @param auth the Authentication object
     * @return Optional containing the user ID or empty
     * @since 1.0.0
     */
    private static Optional<UUID> extractUserIdFromAuth(Authentication auth) {
        try {
            Object principal = auth.getPrincipal();
            if (principal instanceof Jwt jwt) {
                String subject = jwt.getSubject();
                if (subject != null && !subject.isEmpty()) {
                    return Optional.of(UUID.fromString(subject));
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting user ID from authentication", e);
        }
        return Optional.empty();
    }

    /**
     * Extracts email from the current Authentication.
     *
     * Supports JWT tokens where the email is stored in the "email" claim.
     *
     * @param auth the Authentication object
     * @return Optional containing the email or empty
     * @since 1.0.0
     */
    private static Optional<String> extractEmailFromAuth(Authentication auth) {
        try {
            Object principal = auth.getPrincipal();
            if (principal instanceof Jwt jwt) {
                String email = jwt.getClaimAsString(JwtConstants.CLAIM_EMAIL);
                if (email != null && !email.isEmpty()) {
                    return Optional.of(email);
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting email from authentication", e);
        }
        return Optional.empty();
    }

    /**
     * Extracts role from the current Authentication.
     *
     * Supports JWT tokens where the role is stored in the "role" claim.
     *
     * @param auth the Authentication object
     * @return Optional containing the role or empty
     * @since 1.0.0
     */
    private static Optional<String> extractRoleFromAuth(Authentication auth) {
        try {
            Object principal = auth.getPrincipal();
            if (principal instanceof Jwt jwt) {
                String role = jwt.getClaimAsString(JwtConstants.CLAIM_ROLE);
                if (role != null && !role.isEmpty()) {
                    return Optional.of(role);
                }
            }
        } catch (Exception e) {
            log.debug("Error extracting role from authentication", e);
        }
        return Optional.empty();
    }
}
