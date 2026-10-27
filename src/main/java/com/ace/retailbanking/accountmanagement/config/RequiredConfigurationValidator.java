package com.ace.retailbanking.accountmanagement.config;

import java.util.List;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Fails fast, before any infrastructure bean is created, when required environment variables
 * (credentials with no safe default) are missing.
 */
@Component
public class RequiredConfigurationValidator implements BeanFactoryPostProcessor, EnvironmentAware {

  static final List<String> REQUIRED_VARIABLES = List.of("POSTGRES_USER", "POSTGRES_PASSWORD");

  private Environment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    validate(environment);
  }

  static void validate(Environment environment) {
    List<String> missing =
        REQUIRED_VARIABLES.stream()
            .filter(name -> !StringUtils.hasText(environment.getProperty(name)))
            .toList();
    if (!missing.isEmpty()) {
      throw new IllegalStateException(
          "Missing required environment variable(s): "
              + String.join(", ", missing)
              + ". See .env.example for the full list of supported variables.");
    }
  }
}
