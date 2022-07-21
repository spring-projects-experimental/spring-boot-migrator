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
package org.springframework.sbm.jee.jpa.actions;


import org.springframework.sbm.jee.jpa.api.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class JpaHibernatePropertiesToSpringBootPropertiesMapper {

    public static final String HIBERNATE_PROPERTY_PREFIX = "hibernate.";
    public static final String JPA_PROPERTY_PREFIX = "javax.persistence.";
    private final Map<String, String> jpaPropertiesMap = new HashMap<>();

    public JpaHibernatePropertiesToSpringBootPropertiesMapper() {
        jpaPropertiesMap.put("hibernate.hbm2ddl.auto", "spring.jpa.hibernate.ddl-auto");
        // FIXME: key AND value change: javax.persistence.schema-generation.database.action=drop-and-create becomes spring.jpa.hibernate.ddl-auto=create-drop

        jpaPropertiesMap.put("hibernate.dialect", "spring.jpa.database-platform");
        jpaPropertiesMap.put("hibernate.connection.driver_class", "spring.datasource.driver-class-name");
        jpaPropertiesMap.put("hibernate.connection.url", "spring.datasource.url");
        jpaPropertiesMap.put("hibernate.connection.username", "spring.datasource.username");
        jpaPropertiesMap.put("hibernate.connection.password", "spring.datasource.password");
        jpaPropertiesMap.put("hibernate.transaction.jta.platform", "spring.jpa.database-platform"); // FIXME: probably wrong

        jpaPropertiesMap.put("hibernate.cache.provider_class", "");
        jpaPropertiesMap.put("hibernate.jdbc.batch_size", "");

        // NON JNDI (JSE)
        jpaPropertiesMap.put("javax.persistence.jdbc.url", "spring.datasource.url");
        jpaPropertiesMap.put("javax.persistence.jdbc.driver", "spring.datasource.driver-class-name");
        jpaPropertiesMap.put("javax.persistence.jdbc.user", "spring.datasource.username");
        jpaPropertiesMap.put("javax.persistence.jdbc.password", "spring.datasource.password");
        // General
        jpaPropertiesMap.put("javax.persistence.transactionType", "");
        jpaPropertiesMap.put("javax.persistence.lock.timeout", "");
        jpaPropertiesMap.put("javax.persistence.query.timeout", "");
        jpaPropertiesMap.put("javax.persistence.validation.mode", "");
        jpaPropertiesMap.put("javax.persistence.validation.group.pre-persist", "");
        jpaPropertiesMap.put("javax.persistence.validation.group.pre-update", "");
        jpaPropertiesMap.put("javax.persistence.validation.group.pre-remove", "");
        jpaPropertiesMap.put("javax.persistence.provider", "");
        jpaPropertiesMap.put("javax.persistence.transactionType ", "");
        jpaPropertiesMap.put("javax.persistence.jtaDataSource", "");
        jpaPropertiesMap.put("javax.persistence.nonJtaDataSource", ""); // JNDI -> remove ?!
        jpaPropertiesMap.put("javax.persistence.sharedCache.mode", "");
    }

    public Optional<SpringBootJpaProperty> map(Persistence.PersistenceUnit.Properties.Property property) {
        return Optional.ofNullable(jpaPropertiesMap.get(property.getName()))
            .map(springJpaPropertyKey -> new SpringBootJpaProperty(springJpaPropertyKey, property.getValue()));
    }
}
