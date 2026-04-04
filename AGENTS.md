# Mogul Audio Technologies â Engineering Standards for AI Agents

> **Canonical reference for ALL code generation, review, and build configuration.**
> Every AI agent (GitHub Copilot, Claude, Cursor, etc.) MUST consult this file before writing, modifying, or reviewing any code in this repository.

**Last updated:** 2026-04-03
**Maintainer:** Sonic Grit Ventures / @usegall
**Applies to:** mogul-engine-core, mogul-access-engine, mogul-AI-engine, mogul-api-gateway

---

## 1. Platform Version Matrix

All repositories MUST use these exact versions. Never upgrade or downgrade without updating this file first.

### Core Stack

| Component               | Version   | Management                          |
|------------------------|-----------|-------------------------------------|
| Java (JDK)             | 21        | `java.version` property             |
| Spring Boot            | 4.0.5     | Parent POM `spring-boot-starter-parent` |
| Spring Framework       | 7.0.x     | Managed by Spring Boot BOM          |
| Hibernate / JPA        | 7.x       | Managed by Spring Boot BOM          |
| Jakarta EE             | 11        | Managed by Spring Boot BOM          |
| PostgreSQL Driver      | BOM       | Managed by Spring Boot BOM          |
| Flyway                 | 12.3.0    | `${flyway.version}` property        |
| Lombok                 | BOM       | Managed by Spring Boot BOM          |
| Jackson                | 3.x (BOM) | Managed by Spring Boot BOM         |

### Spring Cloud Stack (mogul-api-gateway only)

| Component                          | Version   | Notes                                    |
|-----------------------------------|-----------|------------------------------------------|
| Spring Cloud BOM                  | 2025.1.1  | Oakwood â REQUIRED for Spring Boot 4.0.1+ |
| Spring Cloud Gateway Server       | 5.x (BOM) | Managed by Spring Cloud BOM              |
| Gateway Starter Artifact          | `spring-cloud-starter-gateway-server-webflux` | REPLACES deprecated `spring-cloud-starter-gateway` |

> **CRITICAL:** Spring Cloud `2025.0.x` is INCOMPATIBLE with Spring Boot `4.0.1+`.
> Only `2025.1.x` (Oakwood) works with Spring Boot `4.0.5`.
> The old artifact `spring-cloud-starter-gateway` was deprecated in 2025.1.x â
> use `spring-cloud-starter-gateway-server-webflux` instead.

### Build Toolchain

| Tool                        | Version  | Property Key                          |
|-----------------------------|----------|---------------------------------------|
| Maven Compiler Plugin       | 3.14.1   | `${maven-compiler-plugin.version}`    |
| Maven Surefire Plugin       | 3.5.2    | `${maven-surefire-plugin.version}`    |
| Maven JAR Plugin            | 3.4.2    | `${maven-jar-plugin.version}`         |
| Maven Source Plugin         | 3.3.1    | `${maven-source-plugin.version}`      |
| Maven Javadoc Plugin        | 3.6.3    | `${maven-javadoc-plugin.version}`     |
| SpotBugs Maven Plugin       | 4.8.3.1  | `${spotbugs.version}`                 |
| FindSecBugs Plugin          | 1.13.0   | `${findsecbugs.version}`              |
| OWASP Dependency-Check      | 12.2.0   | `${owasp.dependency-check.version}`   |
| JaCoCo                      | 0.8.14   | Hardcoded in staging profile          |

### Service-Specific Dependencies

| Dependency                  | Version  | Used In        | Property Key                    |
|-----------------------------|----------|----------------|---------------------------------|
| mogul-engine-core           | 2.0.0    | AI, Access     | Direct version in `<dependency>` |
| JJWT (io.jsonwebtoken)      | 0.13.0   | Access only    | `${jjwt.version}`               |
| SpringDoc OpenAPI           | 3.0.1    | AI, Access     | `${springdoc-openapi.version}`   |
| Spring Boot DevTools        | BOM      | Dev profile    | Managed by Spring Boot BOM       |

### CI/CD Tooling

| Tool              | Version  | Pinned In                    |
|-------------------|----------|------------------------------|
| Railway CLI       | 3.22.0   | `.github/workflows/maven.yml` |
| GitHub Actions    | v6/v5    | Checkout v6, setup-java v5   |
| JDK distribution  | Temurin  | setup-java action            |

---

## 2. Version Pinning Rules

### MANDATORY: All versions in `<properties>` block

Every dependency and plugin version that is NOT managed by the Spring Boot BOM **MUST** be declared as a property in the POM `<properties>` block and referenced as `${property.name}`.

```xml
<!-- CORRECT -->
<properties>
    <flyway.version>12.3.0</flyway.version>
</properties>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>${flyway.version}</version>
</dependency>

<!-- WRONG â hardcoded version -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>12.3.0</version>
</dependency>
```

### BOM-Managed Dependencies â NO explicit version

Dependencies managed by `spring-boot-starter-parent` BOM must NOT declare an explicit version. Spring Boot manages these transitively.

```xml
<!-- CORRECT â version managed by BOM -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- WRONG â overriding BOM version without reason -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
```

### CI Tool Pinning

All CI tools MUST be pinned to exact versions. Never use `@latest` or unpinned installs.

```yaml
# CORRECT
- run: npm install -g @railway/cli@3.22.0

# WRONG
- run: npm install -g @railway/cli
```

### Railway CLI 3.22.0 â Deploy Pattern

Railway CLI 3.22.0 removed `--project` as an inline flag for `railway up`. The correct CI/CD approach is a two-step process: `railway link` then `railway up`.

**Token type:** Use `RAILWAY_TOKEN` (project-scoped), NOT `RAILWAY_API_TOKEN` (account-level). Falls back to `RAILWAY_API_TOKEN` if project token not configured.

```yaml
# CORRECT â link first, then deploy
- name: Install Railway CLI
  run: npm install -g @railway/cli@3.22.0
- name: Link Railway project
  run: railway link <PROJECT_ID> --service my-service --environment production
  env:
    RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN || secrets.RAILWAY_API_TOKEN }}
- name: Deploy to Railway
  run: railway up --detach
  env:
    RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN || secrets.RAILWAY_API_TOKEN }}

# WRONG â --project flag no longer accepted in railway up
- run: railway up --detach --service my-service --project <ID> --environment production
```

---

## 3. Spring Boot 4.x Compatibility Patterns

Spring Boot 4.0 (Spring Framework 7.0) introduced **breaking changes** from 3.x. All agents MUST apply these patterns.

### 3.1 ObjectMapper â Explicit Bean Required

Spring Boot 4.x does NOT auto-configure `ObjectMapper` in Servlet-based applications. Every service module MUST provide an explicit `@Bean`.

```java
@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
```

**Rule:** If you add a new service module, always include `JacksonConfig.java` with `@Primary ObjectMapper`.

### 3.2 WebClient.Builder â Fresh Instances Per Bean

Spring Boot 4.x may not auto-configure `WebClient.Builder`. When creating multiple `WebClient` beans, use fresh `WebClient.builder()` instances to prevent header bleed.

```java
// CORRECT â fresh builder per bean
@Bean
public WebClient openAiWebClient() {
    return WebClient.builder()
            .baseUrl(openAiBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
            .build();
}

@Bean
public WebClient claudeWebClient() {
    return WebClient.builder()
            .baseUrl(claudeBaseUrl)
            .defaultHeader("x-api-key", claudeApiKey)
            .build();
}

// WRONG â shared injected builder causes header bleed
@Bean
public WebClient openAiWebClient(WebClient.Builder builder) {
    return builder.baseUrl(openAiBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
            .build();
}
```

### 3.3 ExceptionHandler â No Ambiguous Handlers

Spring Boot 4.x strictly rejects ambiguous `@ExceptionHandler` methods for the same exception type within a single bean hierarchy. If a child class extends a parent `@RestControllerAdvice`, it MUST `@Override` the parent's catch-all rather than declaring a separate method.

```java
// CORRECT â override parent method
@Override
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex, WebRequest request) {
    // child-specific logic
}

// WRONG â new method with same exception type as parent
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleUnexpected(
        Exception ex, HttpServletRequest request) {
    // Spring Boot 4.x rejects this as ambiguous
}
```

### 3.4 Hibernate Dialect â Do NOT Specify Explicitly

Hibernate 7.x auto-detects the dialect from the JDBC URL. Specifying `hibernate.dialect` explicitly triggers warning `HHH90000025` and may cause unexpected behavior.

```yaml
# CORRECT â let Hibernate auto-detect
spring:
  jpa:
    properties:
      hibernate:
        format_sql: true

# WRONG â explicit dialect causes HHH90000025
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 3.5 Jakarta EE 11 Namespace

Spring Boot 4.x requires Jakarta EE 11. All imports MUST use `jakarta.*` namespace. Never use `javax.*`.

```java
// CORRECT
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;

// WRONG â will not compile
import javax.servlet.http.HttpServletRequest;
```

---

## 4. Build & Compilation Requirements

### Pre-Compilation Checklist

Before writing or modifying any code, agents MUST verify:

1. **Parent POM version** matches `4.0.5` â do not upgrade without platform-wide coordination
2. **mogul-engine-core version** matches `2.0.0` across all consumer POMs
3. **Flyway version** matches `12.3.0` across all modules (core + flyway-database-postgresql)
4. **Java source/target** is `21` â no Java 17 or 22 code patterns
5. **No `javax.*` imports** â only `jakarta.*` for EE APIs
6. **No explicit Hibernate dialect** in YAML configurations

### Compilation Command

```bash
# Standard build (all repos)
mvn -B clean compile

# Full install (skip tests for speed)
mvn -B install -DskipTests

# With specific profile
mvn -B install -DskipTests -Pstaging
```

### CI Pipeline Expectation

The GitHub Actions CI pipeline runs: `clean` â `compile` â `install -DskipTests` â deploy via Railway CLI. Code that breaks `compile` blocks ALL deployments.

---

## 5. Database & Migration Standards

### Flyway Conventions

| Rule | Standard |
|------|----------|
| Migration prefix | `V{N}__description.sql` (double underscore) |
| Naming | Snake_case, descriptive (e.g., `V15__seed_admin_user.sql`) |
| Idempotency | All DML migrations MUST be idempotent (use `IF EXISTS` / `IF NOT EXISTS`) |
| DDL auto | `update` for dev/staging, `validate` for production |
| Init mode | `never` for staging/production |

### Schema Rules

- UUIDs as primary keys (`gen_random_uuid()`)
- `created_at` and `updated_at` on all tables
- JSONB for flexible metadata columns
- BCrypt for password hashing (strength 10)

---

## 6. Project Structure Conventions

### Package Hierarchy

```
io.theawesomemogul.{module}
    âââ config/          # @Configuration classes (JacksonConfig, WebClientConfig, SecurityConfig)
    âââ controller/      # @RestController endpoints
    âââ dto/             # Request/Response DTOs
    âââ entity/          # JPA @Entity classes
    âââ exception/       # Custom exceptions + GlobalExceptionHandler
    âââ repository/      # Spring Data JPA repositories
    âââ service/         # Business logic @Service classes
    âââ util/            # Static utilities
```

### Error Response Format

All error responses use the core `ErrorResponse` pattern:

```java
ErrorResponse.now(
    HttpStatus.BAD_REQUEST.value(),    // HTTP status code
    "MOG-BAD-REQUEST",                 // Mogul error code (MOG-prefix)
    "Descriptive message",             // Human-readable detail
    request.getRequestURI()            // Request path
);
```

Error codes: `MOG-BAD-REQUEST`, `MOG-NOT-FOUND`, `MOG-UNAUTHORIZED`, `MOG-AI-SERVICE-ERROR`, `MOG-AUDIO-PROCESSING-ERROR`, `MOG-INTERNAL-ERROR`.

---

## 7. Deployment & Environment Matrix

| Environment  | Git Branch    | Railway Env  | Spring Profile | URL Pattern                     |
|-------------|---------------|--------------|----------------|---------------------------------|
| Production  | `main`        | `api` / `web`| `api`          | `*.mogul.audio`                 |
| Staging     | `testing`     | `staging`    | `staging`      | `*.staging.mogul.audio`         |
| Development | `development` | `dev`        | `dev`          | `*.dev.mogul.audio`             |

### Railway Services

| Service             | Subdomain                  | Internal Port |
|---------------------|----------------------------|---------------|
| mogul-api-gateway   | `api.theawesomemogul.io`   | 8090          |
| mogul-access-engine | `keystone.theawesomemogul.io` | 8080       |
| mogul-ai-engine     | `mogul.theawesomemogul.io` | 8081          |
| mogul-web-portal    | `portal.theawesomemogul.io`| 80 (nginx)    |

> **Architecture:** All browser requests â mogul-web-portal â mogul-api-gateway â backends.
> The gateway routes by path prefix: `/api/design/**`, `/api/presets/**`, `/api/prompt/**`,
> `/api/sessions/**`, `/api/user/**` â AI Engine. Everything else â Keystone.

---

## 8. Security Standards

- OWASP Dependency-Check enabled â fails build on CVSS >= 7.0
- SpotBugs + FindSecBugs enabled (SAST) â active in CI pipeline
- No secrets in code or YAML files â all via environment variables
- JWT-based authentication (JJWT 0.13.0 in access-engine)
- Spring Security 7.x with OAuth2 JOSE for inter-service auth
- BCrypt password hashing (strength 10, `$2b$10$` prefix)
- CORS centralized in mogul-api-gateway â backends accept only internal/localhost origins

---

## 9. Version Lock â Anti-Bot Drift Protection

> **INCIDENT 2026-04-02:** `railway-app[bot]` auto-upgraded `java.version` from 21â25 via PR #98.
> Dockerfile still used `eclipse-temurin:21` â JDK 21 cannot compile `--release 25` â **build failure**.
> No dependency in our stack requires Java 25. Spring Cloud 2025.1.1 floor is Java 21.

### Version Compatibility Contract

| Component              | Locked Version | Lock Reason                                    |
|------------------------|---------------|------------------------------------------------|
| Java (source/target)   | **21**        | Dockerfile base = `eclipse-temurin:21`          |
| Spring Boot            | **4.0.x**     | Parent POM                                     |
| Spring Cloud           | **2025.1.x**  | Oakwood â required for Boot 4.0.x              |
| SpringDoc OpenAPI      | **3.0.1**     | Jackson 3.x HATEOAS conflict at 3.0.2         |

### Lock Rules

1. **`<java.version>` in pom.xml MUST match Dockerfile JDK major version** â no exceptions
2. **NO automated PR may change `<java.version>`** without corresponding Dockerfile update
3. **Spring Cloud version MUST be compatible** with the Spring Boot parent version
4. **Any version upgrade requires:**
   - Compatibility matrix check against this table
   - Dockerfile base image alignment
   - Local `mvn clean compile` verification
   - AGENTS.md update in ALL 4 repos
   - Linear issue documenting the change

### Bot / Automated PR Policy

- **REJECT** any bot-created PR that modifies `<java.version>`, Spring Boot parent, or Spring Cloud BOM
- **REVIEW** dependency update PRs against the compatibility contract above
- CI build naturally guards: mismatched java.version vs Dockerfile will fail compilation

---

## 10. Agent Behavior Rules

### Before Writing Code

1. Read this `AGENTS.md` file completely
2. Verify version compatibility against Section 1
3. Check Spring Boot 4.x patterns in Section 3
4. Confirm package structure matches Section 6

### Before Creating a PR

1. Ensure `mvn -B clean compile` passes
2. No hardcoded versions outside `<properties>`
3. No `javax.*` imports
4. No explicit Hibernate dialect in YAML
5. All new `@Configuration` beans follow established patterns
6. Error responses use `ErrorResponse.now()` with `MOG-*` codes

### Before Upgrading Any Dependency

1. Check Spring Boot BOM compatibility first
2. Update the version in mogul-engine-core `<properties>` block
3. Propagate to ALL consumer repos (access-engine, AI-engine)
4. Update THIS FILE with the new version
5. Create a Linear issue documenting the change
6. Test compilation across all 3 repos before merging

---

## 11. Reference Links

- **Engineering Standards Site:** `https://sonic-grit-labs.github.io/engineering-standards/`
- **Notion Environment Matrix:** Search "Environment & Version Matrix" in Notion workspace
- **Linear Project:** EPIC 0 â Platform Infrastructure
- **GitHub Org:** `https://github.com/Sonic-Grit-Labs`

---

*This document is the single source of truth for build and version compatibility. If any conflict exists between this file and other documentation, THIS FILE takes precedence for code generation and build decisions.*
