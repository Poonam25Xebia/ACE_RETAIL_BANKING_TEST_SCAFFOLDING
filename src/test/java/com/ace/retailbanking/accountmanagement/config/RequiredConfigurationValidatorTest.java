package com.ace.retailbanking.accountmanagement.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class RequiredConfigurationValidatorTest {

  @Test
  void failsFastWhenCredentialsAreMissing() {
    MockEnvironment environment = new MockEnvironment().withProperty("POSTGRES_USER", "app");

    assertThatThrownBy(() -> RequiredConfigurationValidator.validate(environment))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("POSTGRES_PASSWORD")
        .hasMessageNotContaining("POSTGRES_USER,");
  }

  @Test
  void passesWhenCredentialsArePresent() {
    MockEnvironment environment =
        new MockEnvironment()
            .withProperty("POSTGRES_USER", "app")
            .withProperty("POSTGRES_PASSWORD", "secret");

    assertThatCode(() -> RequiredConfigurationValidator.validate(environment))
        .doesNotThrowAnyException();
  }
}
