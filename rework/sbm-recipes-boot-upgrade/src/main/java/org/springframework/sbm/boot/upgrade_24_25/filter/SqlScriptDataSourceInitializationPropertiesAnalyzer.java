/*
 * Copyright 2021 - 2022 the original author or authors.
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
package org.springframework.sbm.boot.upgrade_24_25.filter;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqlScriptDataSourceInitializationPropertiesAnalyzer {

    public List<DeperecatedPropertyMatch> findDeprecatedProperties(List<SpringBootApplicationProperties> springBootApplicationProperties) {
        List<DeperecatedPropertyMatch> deprecatedProperties = new ArrayList<>();
        springBootApplicationProperties.forEach(p -> {

            Map<String, String> properties = Map.of(
                    "spring.datasource.continue-on-error", "spring.sql.init.continue-on-error",
                    "spring.datasource.data", "spring.sql.init.data-locations",
                    "spring.datasource.sql-script-encoding", "spring.sql.init.encoding",
                    "spring.datasource.initialization-mode", "spring.sql.init.mode",
                    "spring.datasource.platform", "spring.sql.init.platform",
                    "spring.datasource.schema", "spring.sql.init.schema-locations",
                    "spring.datasource.separator", "spring.sql.init.separator"
            );

            findDeprecatedProperties(properties, p, deprecatedProperties);

        });

        Map<String, String> credentialsProperties = Map.of(
                "spring.datasource.data-username", "spring.sql.init.username",
                "spring.datasource.data-password", "spring.sql.init.password",
                "spring.datasource.schema-username", "spring.sql.init.username",
                "spring.datasource.schema-password", "spring.sql.init.password"
        );
        List<DeperecatedPropertyMatch> deprecatedCredentialsProperties = findCredentialsProperties(credentialsProperties, springBootApplicationProperties);
        deprecatedProperties.addAll(deprecatedCredentialsProperties);
        return deprecatedProperties;
    }

    private List<DeperecatedPropertyMatch> findCredentialsProperties(Map<String, String> credentialsProperties, List<SpringBootApplicationProperties> springBootApplicationPropertiesList) {
        final List<DeperecatedPropertyMatch> deprecatedProperties = new ArrayList<>();
        String username = null;
        final String schemaUsernamePropertyName = "spring.datasource.schema-username";
        final String schemaPasswordPropertyName = "spring.datasource.schema-password";
        final String dataUsernamePropertyName = "spring.datasource.data-username";
        final String dataPasswordPropertyName = "spring.datasource.data-password";
        for(SpringBootApplicationProperties springBootApplicationProperties : springBootApplicationPropertiesList) {

            Optional<String> dataUsernameProperty = springBootApplicationProperties.getProperty(dataUsernamePropertyName);
            Optional<String> dataPasswordProperty = springBootApplicationProperties.getProperty(dataPasswordPropertyName);

            Optional<String> schemaUsernameProperty = springBootApplicationProperties.getProperty(schemaUsernamePropertyName);
            Optional<String> schemaPasswordProperty = springBootApplicationProperties.getProperty(schemaPasswordPropertyName);

            String relativePath = springBootApplicationProperties.getSourcePath().toString();

            // no data username
            if(schemaUsernameProperty.isPresent() && dataUsernameProperty.isEmpty()) {
                if(username == null) {
                    username = schemaUsernameProperty.get();
                }
                if( ! schemaUsernameProperty.get().equals(username)) {
                    return new ArrayList<>();
                } else {
                    DeperecatedPropertyMatch deprecatedPropertyMatchUsername = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, schemaUsernamePropertyName, "spring.sql.init.username");
                    deprecatedProperties.add(deprecatedPropertyMatchUsername);
                    if(schemaPasswordProperty.isPresent()) {
                        DeperecatedPropertyMatch deprecatedPropertyMatchPassword = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, schemaPasswordPropertyName, "spring.sql.init.password");
                        deprecatedProperties.add(deprecatedPropertyMatchPassword);
                    }
                }
            }
            // no schema username
            if(dataUsernameProperty.isPresent() && schemaUsernameProperty.isEmpty()) {
                if(username == null) {
                    username = dataUsernameProperty.get();
                }
                if( ! dataUsernameProperty.get().equals(username)) {
                    return new ArrayList<>();
                } else {
                    DeperecatedPropertyMatch deprecatedDataUsername = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, dataUsernamePropertyName, "spring.sql.init.username");
                    deprecatedProperties.add(deprecatedDataUsername);
                    if(dataPasswordProperty.isPresent()) {
                        DeperecatedPropertyMatch deprecatedDataPassword = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, dataPasswordPropertyName, "spring.sql.init.password");
                        deprecatedProperties.add(deprecatedDataPassword);
                    }
                }
            }
            // both usernames
            if(dataUsernameProperty.isPresent() && schemaUsernameProperty.isPresent()) {
                if( ! dataUsernameProperty.get().equals(schemaUsernameProperty.get())) {
                    return new ArrayList<>();
                }
                if(username == null) {
                    username = dataUsernameProperty.get();
                }
                if( ! schemaUsernameProperty.get().equals(username) || ! dataUsernameProperty.get().equals(username)) {
                    return new ArrayList<>();
                }
                 else {
                    DeperecatedPropertyMatch deprecatedDataUsername = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, dataUsernamePropertyName, "spring.sql.init.username");
                    deprecatedProperties.add(deprecatedDataUsername);
                    if(dataPasswordProperty.isPresent()) {
                        DeperecatedPropertyMatch deprecatedDataPassword = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, dataPasswordPropertyName, "spring.sql.init.password");
                        deprecatedProperties.add(deprecatedDataPassword);
                    }

                    DeperecatedPropertyMatch deprecatedSchemaUsername = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, schemaUsernamePropertyName, "spring.sql.init.username");
                    deprecatedProperties.add(deprecatedSchemaUsername);
                    if(schemaPasswordProperty.isPresent()) {
                        DeperecatedPropertyMatch deprecatedPropertyMatchPassword = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, schemaPasswordPropertyName, "spring.sql.init.password");
                        deprecatedProperties.add(deprecatedPropertyMatchPassword);
                    }
                }
            }
        }
        return deprecatedProperties;
    }

    private void findDeprecatedProperties(Map<String, String> properties, SpringBootApplicationProperties springBootApplicationProperties, List<DeperecatedPropertyMatch> deprecatedProperties) {
        properties.forEach((deprecatedPropertyName, newPropertyName) -> extracted(springBootApplicationProperties, deprecatedPropertyName, newPropertyName, deprecatedProperties));
    }

    private void extracted(SpringBootApplicationProperties springBootApplicationProperties, String deprecatedPropertyName, String newProperty, List<DeperecatedPropertyMatch> deprecatedProperties) {
        Optional<String> deprecatedProperty = springBootApplicationProperties.getProperty(deprecatedPropertyName);
        if (deprecatedProperty.isPresent()) {
            String relativePath = springBootApplicationProperties.getSourcePath().toString();
            DeperecatedPropertyMatch deprecatedPropertyMatch = new DeperecatedPropertyMatch(springBootApplicationProperties, relativePath, deprecatedPropertyName, newProperty);
            deprecatedProperties.add(deprecatedPropertyMatch);
        }
    }

    @Getter
    public class DeperecatedPropertyMatch {
        private SpringBootApplicationProperties springBootApplicationProperties;
        private final String path;
        private final String deprecatedPropery;
        private final String newProperty;

        public DeperecatedPropertyMatch(SpringBootApplicationProperties springBootApplicationProperties, String path, String deprecatedPropery, String newProperty) {
            this.springBootApplicationProperties = springBootApplicationProperties;
            this.path = path;
            this.deprecatedPropery = deprecatedPropery;
            this.newProperty = newProperty;
        }
    }
}
