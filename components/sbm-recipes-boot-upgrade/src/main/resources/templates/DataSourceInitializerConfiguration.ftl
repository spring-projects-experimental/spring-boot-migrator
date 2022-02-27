package ${package};

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceInitializerConfiguration {

    @Value("${springDatasourceSchemaProperty}")
    private String ddlSqlFilename;

    @Value("${springDatasourceSchemaUsernameProperty}")
    private String ddlDbUsername;

    @Value("${springDatasourceSchemaPasswordProperty}")
    private String ddlDbPassword;

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        DataSource ddlDataSource = dataSourceBuilder
        .username(ddlDbUsername)
        .password(ddlDbPassword)
        .build();

        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new FileSystemResource(ddlSqlFilename));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(ddlDataSource);
        initializer.setDatabasePopulator(databasePopulator);
        initializer.afterPropertiesSet();
        return initializer;
    }

}