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
package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.mulesoft.schema.mule.db.SelectMessageProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Component
public class SelectTranslator implements MuleComponentToSpringIntegrationDslTranslator<SelectMessageProcessorType> {

    @Override
    public Class<SelectMessageProcessorType> getSupportedMuleType() {
        return SelectMessageProcessorType.class;
    }

    @Override
    public DslSnippet translate(SelectMessageProcessorType component, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        return new DslSnippet("// TODO: substitute expression language with appropriate java code \n" +
                "                .handle((p, h) -> jdbcTemplate.queryForList(\"SELECT * FROM STUDENTS LIMIT 500\"))",
                Collections.emptySet(),
                Set.of(
                        "org.springframework.boot:spring-boot-starter-jdbc:2.5.5",
                        "org.springframework.integration:spring-integration-jdbc:5.5.4"
                ),
                Set.of(new Bean("jdbcTemplate", "org.springframework.jdbc.core.JdbcTemplate"))
        );
    }
}
