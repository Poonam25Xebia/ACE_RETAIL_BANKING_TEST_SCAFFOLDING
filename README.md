# ACE_RETAIL_BANKING_TEST_SCAFFOLDING

Scaffold of the **Account Management Service** (story `SCAFFOLD-SVC-001`), a Java 21 / Spring Boot 3
backend microservice. It contains only the project skeleton, environment-based configuration, a
health-check endpoint and datastore connection helpers — no authentication flows, domain model or
business endpoints yet. See [docs/SCAFFOLD-SVC-001.md](docs/SCAFFOLD-SVC-001.md) for traceability
(AC-1 / `spec_functional`) and unresolved gaps.

## Stack (wired)

| Component | Library | Status |
|-----------|---------|--------|
| REST + health check | Spring Web, Spring Boot Actuator | active |
| Security baseline | Spring Security (health public, everything else denied) | active |
| gRPC | `net.devh:grpc-server-spring-boot-starter` (no services yet) | active, empty |
| PostgreSQL (canonical datastore) | Spring Data JDBC, PostgreSQL driver, Flyway | **required** |
| MongoDB | Spring Data MongoDB | optional, unused |
| Redis | Spring Data Redis | optional, unused |
| Kafka | Spring for Apache Kafka | optional, unused |

## Configuration

All configuration comes from environment variables (see [`.env.example`](.env.example) and
`src/main/resources/application.yml`). Only non-secret localhost defaults exist; `POSTGRES_USER`
and `POSTGRES_PASSWORD` have no defaults and startup fails fast with a clear message if they are
missing. MongoDB, Redis and Kafka connect lazily and are excluded from health, so the service starts
without them.

| Variable | Default |
|----------|---------|
| `SERVER_PORT` | `8080` |
| `GRPC_SERVER_PORT` | `9090` |
| `POSTGRES_HOST` / `POSTGRES_PORT` / `POSTGRES_DB` | `localhost` / `5432` / `account_management` |
| `POSTGRES_USER` / `POSTGRES_PASSWORD` | _required_ |
| `FLYWAY_ENABLED` | `true` |
| `MONGODB_URI` | `mongodb://localhost:27017/account_management` |
| `MONGODB_HEALTH_ENABLED` | `false` |
| `REDIS_HOST` / `REDIS_PORT` / `REDIS_PASSWORD` | `localhost` / `6379` / _(empty)_ |
| `REDIS_HEALTH_ENABLED` | `false` |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` |

## Run locally

```bash
export POSTGRES_USER=app POSTGRES_PASSWORD=change-me
docker compose up -d --wait postgres          # add --profile optional for MongoDB/Redis/Kafka
mvn spring-boot:run
curl -i http://localhost:8080/actuator/health  # 200 {"status":"UP",...}
```

Liveness/readiness probes are available at `/actuator/health/liveness` and
`/actuator/health/readiness`.

## Verify (AC-1)

```bash
mvn verify                  # integration test with Testcontainers PostgreSQL (requires Docker)
./scripts/verify-ac1.sh     # packages the jar, starts it with env vars only, checks /actuator/health
```

## Formatting

`.editorconfig` plus Spotless (google-java-format): `mvn spotless:check` / `mvn spotless:apply`.
