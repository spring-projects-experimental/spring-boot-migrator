package it.dontesta.spring.example;

import java.lang.String;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ApplicationContext {
  @Value("#{jdbc.password}")
  private String jdbcPassword;

  @Value("#{jdbc.username}")
  private String jdbcUsername;

  @Value("#{jdbc.driverClassName}")
  private String jdbcDriverClassName;

  @Value("#{jdbc.url}")
  private String jdbcUrl;

  @Bean
  public JdbcTemplate jdbcTemplate() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
    return jdbcTemplate;
  }

  @Bean
  public DataSource dataSource() {
    DataSource dataSource = new DataSource();
    dataSource.setDriverClassName("${jdbc.driverClassName}");
    dataSource.setUrl("${jdbc.url}");
    dataSource.setUsername("${jdbc.username}");
    dataSource.setPassword("${jdbc.password}");
    dataSource.setInitialSize(5);
    dataSource.setMaxActive(10);
    dataSource.setMaxIdle(5);
    dataSource.setMinIdle(2);
    return dataSource;
  }
}
