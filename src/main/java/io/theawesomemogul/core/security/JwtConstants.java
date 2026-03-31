package io.theawesomemogul.core.security;

/**
 * Shared JWT (JSON Web Token) constants for Mogul Audio applications.
 *
 * Defines standard claim names and header values used across all Mogul Audio services
 * for JWT-based authentication and authorization. This ensures consistency in token
 * handling across mogul-access-engine and mogul-LLM-engine.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
public class JwtConstants {

    // HTTP Headers
    /**
     * HTTP Authorization header name.
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";

    /**
     * Authorization header value prefix for Bearer tokens.
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    // JWT Claims
    /**
     * JWT claim for subject (user ID).
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_USER_ID = "sub";

    /**
     * JWT claim for user email address.
     */
    public static final String CLAIM_EMAIL = "email";

    /**
     * JWT claim for user role/permission level.
     */
    public static final String CLAIM_ROLE = "role";

    /**
     * JWT claim for token type (typically "Bearer").
     */
    public static final String CLAIM_TYPE = "type";

    /**
     * JWT claim for token issued at timestamp.
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_ISSUED_AT = "iat";

    /**
     * JWT claim for token expiration timestamp.
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_EXPIRATION = "exp";

    /**
     * JWT claim for token not valid before timestamp.
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_NOT_BEFORE = "nbf";

    /**
     * JWT claim for issuer.
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_ISSUER = "iss";

    /**
     * JWT claim for audience.
     * Standard claim defined in RFC 7519.
     */
    public static final String CLAIM_AUDIENCE = "aud";

    // Common Role Values
    /**
     * Admin role with full system access.
     */
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * User role with standard permissions.
     */
    public static final String ROLE_USER = "USER";

    /**
     * Service role for inter-service communication.
     */
    public static final String ROLE_SERVICE = "SERVICE";

    // Default Values
    /**
     * Default token type value.
     */
    public static final String DEFAULT_TOKEN_TYPE = "Bearer";

    /**
     * Standard JWT issuer for Mogul Audio tokens.
     */
    public static final String DEFAULT_ISSUER = "mogul-audio";

    /**
     * Standard JWT audience for Mogul Audio tokens.
     */
    public static final String DEFAULT_AUDIENCE = "mogul-audio-api";
}
