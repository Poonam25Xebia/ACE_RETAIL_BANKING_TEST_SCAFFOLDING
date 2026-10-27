package com.ace.retailbanking.accountmanagement.persistence;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

/**
 * Access point for the PostgreSQL datastore. The underlying {@link DataSource} is configured by
 * Spring Boot from the {@code POSTGRES_*} environment variables (see {@code application.yml}).
 */
@Component
public class PostgresConnectionHelper {

  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;

  public PostgresConnectionHelper(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public DataSource dataSource() {
    return dataSource;
  }

  public JdbcTemplate jdbcTemplate() {
    return jdbcTemplate;
  }

  /**
   * Obtains a connection bound to the current transaction, if any. Callers must release it with
   * {@link #releaseConnection(Connection)}.
   */
  public Connection getConnection() {
    return DataSourceUtils.getConnection(dataSource);
  }

  public void releaseConnection(Connection connection) {
    DataSourceUtils.releaseConnection(connection, dataSource);
  }

  /** Returns {@code true} if a trivial query succeeds against the configured database. */
  public boolean isReachable() {
    try {
      Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
      return result != null && result == 1;
    } catch (RuntimeException ex) {
      return false;
    }
  }
}
