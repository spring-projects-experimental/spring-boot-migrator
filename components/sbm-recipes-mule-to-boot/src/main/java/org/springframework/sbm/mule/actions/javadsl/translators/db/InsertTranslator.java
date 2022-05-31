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
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;

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
        return DslSnippet.builder()
                .renderedSnippet(
                        "                 // TODO: payload type might not be always LinkedMultiValueMap please change it to appropriate type \n" +
                                "                 // TODO: mule expression language is not converted to java, do it manually. example: #[payload] etc \n" +
                                "                .<LinkedMultiValueMap<String, String>>handle((p, h) -> {\n" +
                                "                      jdbcTemplate.execute(\"" + DBCommons.escapeDoubleQuotes(component.getParameterizedQuery()) + "\");\n" +
                                "                      return p;\n" +
                                "                })")
                .requiredImports(Set.of("org.springframework.util.LinkedMultiValueMap"))
                .build();
    }
}
