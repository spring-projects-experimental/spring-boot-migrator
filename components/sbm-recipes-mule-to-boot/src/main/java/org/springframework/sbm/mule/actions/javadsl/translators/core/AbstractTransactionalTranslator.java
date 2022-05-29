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
package org.springframework.sbm.mule.actions.javadsl.translators.core;

import org.mulesoft.schema.mule.core.AbstractTransactional;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class AbstractTransactionalTranslator implements MuleComponentToSpringIntegrationDslTranslator<AbstractTransactional> {
    @Override
    public Class<AbstractTransactional> getSupportedMuleType() {
        return AbstractTransactional.class;
    }

    @Override
    public DslSnippet translate(int id,
                                AbstractTransactional component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        SubflowTopLevelElement transactionalTopLevelElement = new SubflowTopLevelElement(
                flowName + "Transactional_" + id,
                component.getMessageProcessorOrOutboundEndpoint(),
                muleConfigurations,
                translatorsMap);

        String beanName = transactionalTopLevelElement.getGeneratedIdentity();

        Set<Bean> beans = transactionalTopLevelElement
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getBeans)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        beans.add(new Bean(beanName, "org.springframework.integration.dsl.IntegrationFlow"));

        Set<String> requiredImports = transactionalTopLevelElement
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getRequiredImports)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<String> dependencies = transactionalTopLevelElement
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getRequiredDependencies)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return new DslSnippet(
                ".gateway(" + beanName + ", e -> e.transactional(true))",
                requiredImports,
                dependencies,
                beans,
                "",
                transactionalTopLevelElement.renderDslSnippet()
        );
    }
}
