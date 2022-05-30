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

import org.mulesoft.schema.mule.core.ForeachProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.ForeachTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class ForeachTranslator implements MuleComponentToSpringIntegrationDslTranslator<ForeachProcessorType> {
    @Override
    public Class<ForeachProcessorType> getSupportedMuleType() {
        return ForeachProcessorType.class;
    }

    @Override
    public DslSnippet translate(int id,
                                ForeachProcessorType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap
    ) {

        ForeachTopLevelElement forEachTopLevelTranslations =
                new ForeachTopLevelElement(
                        flowName,
                        component.getMessageProcessorOrOutboundEndpoint(),
                        muleConfigurations,
                        translatorsMap
                );

        Set<Bean> beans = forEachTopLevelTranslations
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getBeans)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());


        Set<String> requiredImports = forEachTopLevelTranslations
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getRequiredImports)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<String> dependencies = forEachTopLevelTranslations
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getRequiredDependencies)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Optional<String> optionalExternalClassContent = forEachTopLevelTranslations
                .getDslSnippets()
                .stream()
                .map(DslSnippet::getExternalClassContent)
                .filter(k -> k !=null && !k.isBlank())
                .findFirst();

        return new DslSnippet(
                "                //TODO: translate expression " + component.getCollection() + " which must produces an array\n" +
                        "                // to iterate over\n" +
                        "                .split()\n" +
                        "                " + forEachTopLevelTranslations.renderDslSnippet() + "\n" +
                        "                .aggregate()",
                requiredImports,
                dependencies,
                beans,
                optionalExternalClassContent.orElse(null),
                ""
        );
    }
}
