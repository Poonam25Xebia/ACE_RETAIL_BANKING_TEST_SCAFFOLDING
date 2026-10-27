# SCAFFOLD-SVC-001 — implementation notes

Spec bundle `4cc55814-1989-4c96-8f15-1a6ed645bd69` (content hash
`sha256:a10a22647d428b23d92732e12b4da7aaddcbc96cc92f631876b48c2d43f105e0`).

## Traceability

| Criterion | Functional spec ref | Evidence |
|-----------|---------------------|----------|
| AC-1 | `spec_functional` | `HealthCheckIntegrationTest` (`mvn verify`) and `scripts/verify-ac1.sh` |

AC-1 (quality attribute obligation): the scaffolded service starts successfully and its
health-check endpoint (`GET /actuator/health`) responds `200 {"status":"UP"}` using only
environment-based configuration.

## Task mapping

| Task | Delivered |
|------|-----------|
| T001 Project skeleton | `pom.xml`, `AccountManagementServiceApplication`, `.editorconfig`, Spotless |
| T002 Environment configuration | `application.yml`, `.env.example`, `RequiredConfigurationValidator` |
| T003 Health check | Spring Boot Actuator `/actuator/health` (+ liveness/readiness), `SecurityConfiguration` |
| T004 Database connection helper | `PostgresConnectionHelper`, placeholder `MongoConnectionHelper` |
| T005 Verification | `HealthCheckIntegrationTest`, `RequiredConfigurationValidatorTest`, `scripts/verify-ac1.sh` |

## Assumptions

- PostgreSQL is treated as the canonical datastore and is required at startup (Flyway runs on
  boot; no migrations exist yet). MongoDB, Redis and Kafka are optional and excluded from health.
- Root package `com.ace.retailbanking.accountmanagement`, artifact `account-management-service`.
- gRPC uses `net.devh:grpc-server-spring-boot-starter`; Spring Security is the baseline (Spring
  Authorization Server vs. Keycloak adapter is not chosen).
- Health details are hidden (`show-details: never`) so no infrastructure details leak publicly.

## Unresolved gaps

1. **Stack conflict**: `functional_behavior.scaffold_setup` / AC-1 test-plan text says
   "Node.js, OAuth 2.0, JWT, PostgreSQL, Express.js", while `tech_stack` and
   `implementation_constraints` mandate Java / Spring Boot. Implemented Java / Spring Boot per the
   constraints; the conflict needs resolution in the spec.
2. **Architecture grounding**: the C3/C4 diagrams and API spec referenced by the spec were not
   available in this repository; `specs/SCAFFOLD-SVC-001/` also does not exist. Package and module
   boundaries are minimal and must be aligned once those artifacts are provided.
3. **Auth provider choice**: Spring Authorization Server vs. Keycloak adapter is left open; OAuth 2.0 /
   JWT flows, account registration and token handling are out of scope for this story.
4. **Canonical datastore**: spec lists both PostgreSQL and MongoDB; PostgreSQL assumed canonical.
5. **Docker / Kubernetes**: listed in the stack, but no container image or manifests are specified
   by the story; only a `docker-compose.yml` for local infrastructure is provided.
6. **Security obligation** ("validate credential and personal-data handling against the story's
   acceptance criteria"): AC-1 contains no credential/PII criteria. The scaffold holds no personal
   data, has no hardcoded credentials, and denies all non-health requests.
