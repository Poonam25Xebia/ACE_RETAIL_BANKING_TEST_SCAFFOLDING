package com.ace.retailbanking.accountmanagement;

import static org.assertj.core.api.Assertions.assertThat;

import com.ace.retailbanking.accountmanagement.persistence.PostgresConnectionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * AC-1 (spec_functional): the scaffolded service starts using only environment-based configuration
 * and its health-check endpoint responds. Only the environment variable names consumed by {@code
 * application.yml} are supplied; MongoDB, Redis and Kafka are intentionally not running.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthCheckIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

  @DynamicPropertySource
  static void environment(DynamicPropertyRegistry registry) {
    registry.add("POSTGRES_HOST", POSTGRES::getHost);
    registry.add(
        "POSTGRES_PORT", () -> POSTGRES.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
    registry.add("POSTGRES_DB", POSTGRES::getDatabaseName);
    registry.add("POSTGRES_USER", POSTGRES::getUsername);
    registry.add("POSTGRES_PASSWORD", POSTGRES::getPassword);
    registry.add("SERVER_PORT", () -> 0);
    registry.add("GRPC_SERVER_PORT", () -> 0);
  }

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PostgresConnectionHelper postgresConnectionHelper;

  @Test
  void ac1HealthEndpointReturnsUp() {
    ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("\"status\":\"UP\"");
  }

  @Test
  void ac1PostgresConnectionHelperReachesDatabase() {
    assertThat(postgresConnectionHelper.isReachable()).isTrue();
  }

  @Test
  void nonHealthEndpointsAreDenied() {
    ResponseEntity<String> response = restTemplate.getForEntity("/actuator/env", String.class);

    assertThat(response.getStatusCode().is4xxClientError()).isTrue();
  }
}
