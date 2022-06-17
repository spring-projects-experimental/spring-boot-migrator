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

import org.mulesoft.schema.mule.db.InsertMessageProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InsertTranslator implements MuleComponentToSpringIntegrationDslTranslator<InsertMessageProcessorType> {
    @Override
    public Class<InsertMessageProcessorType> getSupportedMuleType() {
        return InsertMessageProcessorType.class;
    }

    @Override
    public DslSnippet translate(int id,
                                InsertMessageProcessorType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {


        String query = component.getDynamicQuery() == null ? component.getParameterizedQuery()
                : component.getDynamicQuery();
        QueryWithParameters queryWithParameters = DBCommons.parseQueryParameter(query);
        String argumentTemplate = "                                p.getFirst(\"%s\") /* TODO: Translate #[%s] to java expression*/";
        String arguments = queryWithParameters
                .getMuleExpressions()
                .stream()
                .map(muleExpression -> String.format(argumentTemplate, muleExpression, muleExpression))
                .collect(Collectors.joining(",\n"));

        if (!arguments.isEmpty()) {
            arguments = ",\n" + arguments + "\n";
        }

        String translation =
                "                .<LinkedMultiValueMap<String, String>>handle((p, h) -> {\n" +
                "                      jdbcTemplate.update(\"" + DBCommons.escapeDoubleQuotes(queryWithParameters.getQuery()) + "\"" +
                        arguments +
                        ");\n" +
                "                      return p;\n" +
                "                })";
        return DslSnippet.builder()
                .renderedSnippet(
                        "                 // TODO: payload type might not be always LinkedMultiValueMap please change it to appropriate type \n" +
                                "                 // TODO: mule expression language is not converted to java, do it manually. example: #[payload] etc \n" +
                                translation)
                .requiredImports(Set.of(
                        "org.springframework.util.LinkedMultiValueMap",
                        "org.springframework.jdbc.core.JdbcTemplate"
                ))
                .requiredDependencies(Set.of(
                        "org.springframework.boot:spring-boot-starter-jdbc:2.5.5",
                        "org.springframework.integration:spring-integration-jdbc:5.5.4",
                        "com.h2database:h2:2.1.214"
                ))
                .beans(Set.of(new Bean("jdbcTemplate", "org.springframework.jdbc.core.JdbcTemplate")))
                .build();
    }
}
