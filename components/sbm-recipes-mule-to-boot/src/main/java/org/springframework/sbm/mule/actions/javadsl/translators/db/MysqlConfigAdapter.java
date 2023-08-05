/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.mulesoft.schema.mule.db.MySqlDatabaseConfigType;
import org.mulesoft.schema.mule.db.OracleDatabaseConfigType;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Component
public class MysqlConfigAdapter extends ConfigurationTypeAdapter<MySqlDatabaseConfigType>  {
    @Override
    public String getName() {
        return getMuleConfiguration().getName();
    }

    @Override
    public Class getMuleConfigurationType() {
        return MySqlDatabaseConfigType.class;
    }

    @Override
    public List<AbstractMap.SimpleEntry<String, String>> configProperties() {
        List<AbstractMap.SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.url", "--INSERT--DB-URL-HERE-Example:--INSERT--DB-URL-HERE-Example:jdbc:mysql://localhost:3306/sonoo"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.username", "--INSERT-USER-NAME--"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.password", "--INSERT-PASSWORD--"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.datasource.driverClassName", "com.mysql.cj.jdbc.Driver"));
        properties.add(new AbstractMap.SimpleEntry<>("spring.jpa.show-sql", "true"));
        return properties;
    }

    @Override
    public List<Dependency> getDependencies() {

        return List.of(Dependency.builder()
                .groupId("mysql")
                .artifactId("mysql-connector-java")
                .version("8.0.29")
                .build());
    }
}
