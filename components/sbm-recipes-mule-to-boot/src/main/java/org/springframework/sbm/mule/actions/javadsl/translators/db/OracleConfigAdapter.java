package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.mulesoft.schema.mule.db.OracleDatabaseConfigType;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Component
public class OracleConfigAdapter extends ConfigurationTypeAdapter<OracleDatabaseConfigType>  {
    @Override
    public String getName() {
        return getMuleConfiguration().getName();
    }

    @Override
    public Class getMuleConfigurationType() {
        return OracleDatabaseConfigType.class;
    }

    @Override
    public List<AbstractMap.SimpleEntry<String, String>> configProperties() {
        List<AbstractMap.SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.url", "--INSERT--DB-URL-HERE-Example:jdbc:oracle:thin:@localhost:1521:XE"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.username", "--INSERT-USER-NAME--"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.password", "--INSERT-PASSWORD--"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.driverClassName", "oracle.jdbc.OracleDriver"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.jpa.show-sql", "true"));
        return properties;
    }
}
