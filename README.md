# Mogul Engine Core

> Shared Java library of common implementations for the Mogul Audio Technologies ecosystem

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36)](https://maven.apache.org/)

## Overview

`mogul-engine-core` is a Maven JAR library that provides reusable, standardized implementations for all Mogul Audio microservices. It abstracts away common concerns like exception handling, database connectivity, logging, authentication, and utilities, enabling consistent behavior across the entire platform.

This library is consumed by services such as **Mogul Access Engine** (authentication & identity) and **Mogul LLM Engine** (AI orchestration).

**This is a shared library, not a standalone application.**

## Key Features

- **Exception Handling** - Standardized exception hierarchy with HTTP status mapping and structured error responses
- **Database Support** - Base entity classes, JPA timestamp auditing, soft delete support, HikariCP connection pooling
- **Structured Logging** - JSON-ready logging, request correlation IDs, audit trails, performance metrics
- **Internationalization** - Multi-language message support (English, Portuguese)
- **Security Utilities** - JWT claim access, authentication context helpers, role constants
- **Common Utilities** - Validation, JSON handling, string manipulation, date/time operations
- **Health Monitoring** - Service health indicators, database connectivity checks, uptime tracking

## Architecture

```
mogul-engine-core/
├── src/main/java/io/theawesomemogul/core/
│   ├── database/              # Database configuration and base entities
│   │   ├── BaseEntity.java             # Base with id, createdAt, updatedAt
│   │   ├── SoftDeletableEntity.java    # Soft delete support
│   │   ├── DatabaseConfig.java         # HikariCP pool settings
│   │   ├── DatabaseHealthIndicator.java# DB connectivity health check
│   │   └── JpaAuditingConfig.java      # Automatic timestamp auditing
│   ├── exception/             # Exception handling framework
│   │   ├── MogulException.java         # Base exception with error codes
│   │   ├── MogulNotFoundException.java  # 404
│   │   ├── MogulBadRequestException.java# 400
│   │   ├── MogulUnauthorizedException.java# 401
│   │   ├── MogulForbiddenException.java# 403
│   │   ├── MogulConflictException.java # 409
│   │   ├── MogulQuotaExceededException.java# 429
│   │   ├── MogulServiceException.java  # 500/503
│   │   ├── MogulExceptionHandler.java  # Global REST exception handler
│   │   └── ErrorResponse.java          # Structured error response
│   ├── i18n/                  # Internationalization
│   │   ├── I18nConfig.java             # Message source configuration
│   │   └── I18nService.java            # Locale-aware message service
│   ├── logging/               # Structured logging framework
│   │   ├── MogulLogger.java            # JSON-ready fluent logger
│   │   ├── CorrelationIdFilter.java    # X-Correlation-ID generation
│   │   ├── RequestLoggingFilter.java   # HTTP request/response logging
│   │   └── LoggingConfig.java          # Filter registration
│   ├── security/              # Authentication and authorization
│   │   ├── JwtConstants.java           # JWT claims, roles, prefixes
│   │   └── SecurityUtils.java          # SecurityContext utilities
│   ├── health/                # Service health monitoring
│   │   ├── ServiceInfo.java            # Service metadata
│   │   └── ServiceHealthResponse.java  # Health status record
│   └── util/                  # Utility classes
│       ├── ValidationUtils.java        # Input validation
│       ├── JsonUtils.java              # JSON serialization
│       ├── StringUtils.java            # String operations
│       └── DateUtils.java              # Date/time utilities
├── src/main/resources/
│   ├── messages.properties              # English messages
│   └── messages_pt_BR.properties        # Portuguese (Brazil)
└── src/test/java/             # Unit tests
```

## Tech Stack

- **Runtime:** Java 21 (OpenJDK / Eclipse Temurin)
- **Framework:** Spring Boot 3.2.0 (dependency management only, no embedded server)
- **Persistence:** Spring Data JPA, Hibernate
- **Database:** PostgreSQL 15+ with HikariCP connection pooling
- **Security:** Spring Security, JWT
- **JSON:** Jackson with Java 8+ date/time support
- **Logging:** SLF4J + Logback
- **Build:** Maven 3.9+
- **Testing:** JUnit 5, Spring Test

## Maven Coordinates

Add to your service's `pom.xml`:

```xml
<dependency>
    <groupId>io.theawesomemogul</groupId>
    <artifactId>mogul-engine-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

No explicit version management needed; all transitive dependencies are managed by Spring Boot 3.2.

### GitHub Packages Repository Configuration

Since this library is published to **GitHub Packages**, consuming projects (e.g. `mogul-LLM-engine`, `mogul-access-engine`) must configure the GitHub Packages Maven repository.

**1. Add the repository to your service's `pom.xml`:**

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/Sonic-Grit-Labs/mogul-engine-core</url>
    </repository>
</repositories>
```

**2. Configure authentication in `~/.m2/settings.xml`** (local development):

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

> **Note:** The token needs the `read:packages` scope.

**3. For CI/CD (GitHub Actions)**, the workflow already has access via `GITHUB_TOKEN`. Add this step before building:

```yaml
- uses: actions/setup-java@v4
  with:
    java-version: '21'
    distribution: 'temurin'
    server-id: github
    settings-path: ${{ github.workspace }}
```

## Module Overview

### Database Module (`io.theawesomemogul.core.database`)

Base classes and configuration for all JPA data access:

**BaseEntity** - Abstract MappedSuperclass providing:
- `id: UUID` - Primary key (auto-generated)
- `createdAt: LocalDateTime` - Insertion timestamp (auto-set by JPA auditing)
- `updatedAt: LocalDateTime` - Last modification timestamp (auto-updated)

**SoftDeletableEntity** - Extends BaseEntity with:
- `deletedAt: LocalDateTime` - Soft deletion timestamp (null = active)
- Queries automatically filter out soft-deleted records

**DatabaseConfig**
- Configures HikariCP connection pooling
- Maximum pool size: 10 connections
- Idle timeout: 10 minutes
- Connection timeout: 30 seconds

**DatabaseHealthIndicator**
- Spring Boot Actuator health endpoint integration
- Tests database connectivity
- Reports as `UP` or `DOWN`

**JpaAuditingConfig**
- Enables Spring Data JPA auditing
- Automatically sets `createdAt` and `updatedAt`
- Provides `@CreatedDate` and `@LastModifiedDate` annotations

**Example:**
```java
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Inherits: id, createdAt, updatedAt (auto-managed)
}
```

### Exception Handling (`io.theawesomemogul.core.exception`)

Comprehensive exception hierarchy with automatic HTTP response mapping:

**Exception Classes:**

| Class | HTTP Status | Use Case |
|-------|-------------|----------|
| MogulNotFoundException | 404 | User/resource not found |
| MogulBadRequestException | 400 | Invalid input, validation failures |
| MogulUnauthorizedException | 401 | Missing/invalid authentication |
| MogulForbiddenException | 403 | Insufficient permissions/quota |
| MogulConflictException | 409 | Duplicate email, plan conflict |
| MogulQuotaExceededException | 429 | Rate limit or quota exceeded |
| MogulServiceException | 500/503 | Database, cache, or service failures |

**MogulExceptionHandler**
- Global REST controller advice
- Automatically converts exceptions to HTTP responses
- Returns structured ErrorResponse

**ErrorResponse** - Record containing:
- `timestamp: Instant` - When error occurred
- `code: String` - Error code for client handling
- `message: String` - User-friendly message (localized)
- `details: Map<String, String>` - Additional context
- `path: String` - Request path

**Example:**
```java
public Entitlement getEntitlement(UUID userId, String featureKey) {
    return entitlementRepository
        .findByUserIdAndFeatureKey(userId, featureKey)
        .orElseThrow(() ->
            new MogulForbiddenException("Feature not available in your plan")
                .withDetail("feature", featureKey)
                .withDetail("userId", userId.toString())
        );
}
```

### Internationalization (`io.theawesomemogul.core.i18n`)

Multi-language support with automatic locale detection:

**I18nConfig**
- Configures Spring MessageSource
- Loads properties from classpath: `messages.properties`
- Configures LocaleResolver (from Accept-Language header)

**I18nService**
- Convenience methods for getMessage()
- Auto-detects locale from SecurityContext or HTTP request
- Fallback to default locale (en_US) if not found

**Supported Locales:**
- `en_US` - English (default)
- `pt_BR` - Portuguese (Brazil)

**Resource Files:**
- `messages.properties` - English messages
- `messages_pt_BR.properties` - Portuguese translations

**Example:**
```java
@Service
public class NotificationService {
    @Autowired
    private I18nService i18nService;

    public void notifyUserLimitReached(UUID userId) {
        String message = i18nService.getMessage(
            "notification.quota_exceeded",
            new Locale("pt_BR")
        );
        // Send localized notification
    }
}
```

### Logging (`io.theawesomemogul.core.logging`)

Structured logging with correlation IDs and performance metrics:

**MogulLogger** - Fluent logger with:
- `debug(message)` - Detailed tracing
- `audit(userId, action, details)` - Compliance audit trail
- `warn(message)` - Quota alerts, warnings
- `perf(operation, duration_ms)` - Performance metrics
- JSON output ready for log aggregation

**CorrelationIdFilter**
- Generates unique `X-Correlation-ID` header per request
- Propagates through request/response cycle
- Aids in distributed tracing

**RequestLoggingFilter**
- Logs HTTP method, path, status code
- Logs response time in milliseconds
- Useful for API performance monitoring

**LoggingConfig**
- Registers filters in Spring filter chain
- Orders filters appropriately

**Example:**
```java
@Service
public class AuthService {
    private static final MogulLogger logger = MogulLogger.of(AuthService.class);

    public void loginUser(UUID userId, String email, String ipAddress) {
        long startMs = System.currentTimeMillis();

        // ... authentication logic

        logger.withUserId(userId.toString())
              .audit(userId.toString(), "LOGIN_SUCCESS",
                  Map.of("email", StringUtils.maskEmail(email), "ip", ipAddress)
              );
        logger.perf("loginUser", System.currentTimeMillis() - startMs);
    }
}
```

### Utilities (`io.theawesomemogul.core.util`)

Common utility classes for validation, JSON, string operations:

**ValidationUtils**
- `requireNonNull(value, fieldName)` - Throws MogulBadRequestException if null
- `requireNonEmpty(value, fieldName)` - Throws if null or empty string
- `requireValidEmail(email)` - RFC email validation
- `requireValidUUID(uuid)` - UUID format validation

**JsonUtils**
- Serialization/deserialization with Jackson
- Automatic LocalDateTime/Instant handling
- Pretty printing for logging

**StringUtils**
- `slugify(text)` - Convert to URL-safe slug
- `truncate(text, maxLength)` - Safely shorten strings
- `maskEmail(email)` - "user@example.com" → "u***@example.com"
- `maskLicenseKey(key)` - "1234-5678-9ABC-DEF0" → "****-****-9ABC-DEF0"

**DateUtils**
- All operations in UTC timezone
- ISO 8601 formatting
- Epoch millisecond conversion
- Duration calculations

**Example:**
```java
@Service
public class UserService {
    public void createUser(String email, String fullName) {
        // Throws MogulBadRequestException if invalid
        ValidationUtils.requireValidEmail(email);
        ValidationUtils.requireNonEmpty(fullName, "fullName");

        if (userRepository.existsByEmail(email)) {
            throw new MogulConflictException("Email already registered")
                .withDetail("email", StringUtils.maskEmail(email));
        }

        String slug = StringUtils.slugify(fullName);
        // ... create user
    }
}
```

### Security (`io.theawesomemogul.core.security`)

JWT and authentication context utilities:

**JwtConstants**
- Standard JWT claim names: `sub` (user ID), `email`, `role`, `iat`, `exp`
- Role constants: `ROLE_ADMIN`, `ROLE_USER`, `ROLE_BASIC`, `ROLE_PRO`, etc.
- Token prefix: `Bearer `

**SecurityUtils**
- `getCurrentUserId(): UUID` - Get authenticated user's ID
- `getCurrentEmail(): String` - Get authenticated user's email
- `getCurrentRole(): String` - Get authenticated user's role
- Pulls from Spring SecurityContext

**Example:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMyProfile() {
        // Requires @Secured or @PreAuthorize
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
```

### Health (`io.theawesomemogul.core.health`)

Service health and status monitoring:

**ServiceInfo** - Record with:
- `name: String` - Service name
- `version: String` - Service version
- `environment: String` - dev, staging, production
- `uptime: long` - Milliseconds since startup

**ServiceHealthResponse** - Record with:
- `status: String` - "UP" or "DOWN"
- `timestamp: Instant` - Health check time
- `database: boolean` - Database connectivity
- `services: List<String>` - Available components

**Example:**
```java
@RestController
@RequestMapping("/api/health")
public class HealthController {
    @Autowired
    private ActuatorEndpoint actuatorEndpoint;

    @GetMapping
    public ResponseEntity<ServiceHealthResponse> getHealth() {
        Health health = actuatorEndpoint.health();
        return ResponseEntity.ok(
            new ServiceHealthResponse(
                health.getStatus().toString(),
                Instant.now(),
                health.getComponent("db").getStatus().equals(Status.UP),
                List.of("database", "cache", "messageQueue")
            )
        );
    }
}
```

## Getting Started

### Prerequisites

- Java 21+ (OpenJDK or Eclipse Temurin)
- Maven 3.9+
- Git

### Building the Library

```bash
# Clone the repository
git clone https://github.com/Sonic-Grit-Labs/mogul-engine-core.git
cd mogul-engine-core

# Build and install to local Maven repository
mvn clean install
```

Artifacts generated:
- `target/mogul-engine-core-1.0.0-SNAPSHOT.jar` - Main JAR
- `target/mogul-engine-core-1.0.0-SNAPSHOT-sources.jar` - Source code
- `target/mogul-engine-core-1.0.0-SNAPSHOT-javadoc.jar` - API documentation

### Running Tests

```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=MogulExceptionHandlerTest

# Run with coverage report
mvn test jacoco:report
```

### Development Setup (VS Code)

#### Prerequisites on macOS

```bash
# Install Java 21
brew install openjdk@21
# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
echo $JAVA_HOME

# Install Maven 3.9+
brew install maven
mvn --version
```

#### VS Code Setup

1. Install extensions:
   - Extension Pack for Java (Microsoft)
   - Spring Boot Extension Pack (VMware)

2. Open project in VS Code:
   ```bash
   code mogul-engine-core/
   ```

3. Trust workspace when prompted

4. Wait for Maven to download dependencies (2-3 minutes first time)

5. Verify setup:
   ```bash
   mvn clean compile
   ```

#### VS Code Debug Configuration

Press `F5` or use Run > Start Debugging:
- Debug tests in VS Code Test Explorer sidebar
- Set breakpoints and inspect variables
- View Maven output in Terminal

## Configuration

Services consuming `mogul-engine-core` should configure:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Never auto-create/modify schema in production
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        jdbc:
          batch_size: 20

  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      idle-timeout: 600000  # 10 minutes
      connection-timeout: 30000
      leak-detection-threshold: 60000

  messages:
    basename: classpath:messages
    encoding: UTF-8
    default-locale: en_US

logging:
  level:
    io.theawesomemogul: INFO
    org.springframework: WARN
```

## Dependency Tree

```
mogul-engine-core
├── Spring Boot 3.2.0
│   ├── Spring Framework 6.1+
│   ├── Spring Data JPA
│   └── Spring Security
├── PostgreSQL Driver 42.7.1
├── HikariCP (connection pooling)
├── Flyway (migrations)
├── Jackson (JSON)
├── SLF4J + Logback (logging)
├── Lombok (code generation)
└── JUnit 5 (testing)
```

All versions managed by Spring Boot parent POM.

## Publishing Updates

When changes are complete:

```bash
# Verify all tests pass
mvn clean test

# Build with full artifacts
mvn clean install

# Create git tag (when releasing)
git tag -a v1.0.1 -m "Release 1.0.1"
git push origin v1.0.1
```

Future versions: Update `pom.xml` version and commit with semantic versioning.

## Related Projects

- **Mogul Audio Core** - Alternative core library
- **Mogul Access Engine** - Authentication/identity service using this library
- **Mogul LLM Engine** - AI orchestration service using this library

## License

Copyright 2026 Sonic Grit Ventures. All rights reserved.

Proprietary - Sonic Grit Labs / Mogul Audio Technologies

## Support

- **Repository:** https://github.com/Sonic-Grit-Labs/mogul-engine-core
- **Issues:** GitHub Issues tracker
- **Documentation:** Javadoc in attached JAR, README.md
- **Community:** GitHub Discussions (when enabled)
- **Contributing:** See repository CONTRIBUTING.md

## FAQ

**Q: Can I use this library standalone?**
A: No, this is a shared library meant to be imported by Spring Boot applications. It provides no executable entry point.

**Q: Does this include database migrations?**
A: No, but Flyway is included as a dependency. Each service using this library manages its own schema.

**Q: How do I upgrade to a new version?**
A: Update the `<version>` in your service's pom.xml and run `mvn clean install` to fetch the new JAR.

**Q: Can I customize exception messages?**
A: Yes, add keys to `messages.properties` and reference them in exception throwing code.

**Q: How do I add a new language?**
A: Create `messages_xx_XX.properties` in `src/main/resources/` and register the locale in `I18nConfig.java`.
