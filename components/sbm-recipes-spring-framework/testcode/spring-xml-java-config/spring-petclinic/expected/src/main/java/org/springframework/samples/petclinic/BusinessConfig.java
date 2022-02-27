package org.springframework.samples.petclinic;

import java.lang.String;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BusinessConfig {
  @Value("#{jdbc.password}")
  private String jdbcPassword;

  @Value("#{jpa.showSql}")
  private String jpaShowSql;

  @Value("#{jdbc.initLocation}")
  private String jdbcInitLocation;

  @Value("#{jdbc.username}")
  private String jdbcUsername;

  @Value("#{jdbc.dataLocation}")
  private String jdbcDataLocation;

  @Value("#{jdbc.driverClassName}")
  private String jdbcDriverClassName;

  @Value("#{jpa.database}")
  private String jpaDatabase;

  @Value("#{jdbc.url}")
  private String jdbcUrl;

  @Value("#{jdbc.ac}")
  private String jdbcAc;

  @Bean
  public JdbcTemplate jdbcTemplate() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
    return jdbcTemplate;
  }

  @Bean
  public DataSourceTransactionManager transactionManager() {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    transactionManager.setDataSource(dataSource());
    return transactionManager;
  }

  @Bean
  public DataSource dataSource() {
    DataSource dataSource = new DataSource();
    dataSource.setDefaultAutoCommit(jdbcAc);
    dataSource.setDriverClassName(jdbcDriverClassName);
    dataSource.setPassword(jdbcPassword);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(jdbcUsername);
    return dataSource;
  }

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource());
    return namedParameterJdbcTemplate;
  }
}
