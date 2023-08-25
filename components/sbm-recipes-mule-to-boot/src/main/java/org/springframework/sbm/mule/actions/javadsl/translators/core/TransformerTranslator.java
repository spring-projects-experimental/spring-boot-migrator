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
package org.springframework.sbm.mule.actions.javadsl.translators.core;


import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;

@Component
public class TransformerTranslator implements MuleComponentToSpringIntegrationDslTranslator<AbstractTransformerType> {
    @Override
    public Class<AbstractTransformerType> getSupportedMuleType() {
        return AbstractTransformerType.class;
    }

    @Override
    public DslSnippet translate(int id, AbstractTransformerType component, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        if (name.getLocalPart().equals("byte-array-to-string-transformer")) {
            return DslSnippet.builder()
                    .renderedSnippet(".transform(new ObjectToStringTransformer())")
                    .requiredImports(Set.of("org.springframework.integration.transformer.ObjectToStringTransformer"))
                    .build();
        } else {
            return DslSnippet.builder()
                    .renderedSnippet(".transform(s -> ((String)s).getBytes(StandardCharsets.UTF_8))")
                    .requiredImports(Set.of("java.nio.charset.StandardCharsets"))
                    .build();
        }
    }
}
