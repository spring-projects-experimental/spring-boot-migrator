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


import org.mulesoft.schema.mule.core.SetPropertyType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Map;

@Component
public class SetPropertyTranslator implements MuleComponentToSpringIntegrationDslTranslator<SetPropertyType> {
    @Override
    public Class<SetPropertyType> getSupportedMuleType() {
        return SetPropertyType.class;
    }

    @Override
    public DslSnippet translate(int id, SetPropertyType component, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        return DslSnippet.builder()
                .renderedSnippet(
                        ".enrichHeaders(h -> h.header(\"" + component.getPropertyName() + "\", \"" + component.getValue() + "\"))"
                )
                .build();
    }
}
