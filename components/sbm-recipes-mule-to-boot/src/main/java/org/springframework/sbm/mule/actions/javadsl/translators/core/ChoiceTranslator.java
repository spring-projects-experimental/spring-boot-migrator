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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mulesoft.schema.mule.core.SelectiveOutboundRouterType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.ChoiceTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ChoiceTranslator implements MuleComponentToSpringIntegrationDslTranslator<SelectiveOutboundRouterType> {

    @Override
    public Class<SelectiveOutboundRouterType> getSupportedMuleType() {
        return SelectiveOutboundRouterType.class;
    }

    private final static String subflowTemplate =
            "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to $TRANSLATE_EXPRESSION*/,\n" +
                    "                                       $SUBFLOW_CONTENT\n" +
                    "                                )\n";

    private final static String defaultSubflowMapping =
            "                                .defaultSubFlowMapping($OTHERWISE_STATEMENTS)\n";

    @Override
    public DslSnippet translate(int id, SelectiveOutboundRouterType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        List<ImmutablePair<String, ChoiceTopLevelElement>> whenStatements = component
                .getWhen()
                .stream()
                .map(item -> new ImmutablePair<>(item.getExpression(), new ChoiceTopLevelElement(
                        flowName,
                        item.getMessageProcessorOrOutboundEndpoint(),
                        muleConfigurations,
                        translatorsMap)))
                .collect(Collectors.toList());

        String whenSubflowMappings = whenStatements
                .stream()
                .map(item ->
                        subflowTemplate
                                .replace("$TRANSLATE_EXPRESSION", item.getLeft())
                                .replace("$SUBFLOW_CONTENT", item.getValue().renderDslSnippet())
                )
                .collect(Collectors.joining());

        List<DslSnippet> whenStatementDslSnippets = whenStatements
                .stream()
                .map(item -> DslSnippet.createDSLSnippetFromTopLevelElement(item.getValue()))
                .collect(Collectors.toList());

        Set<String> requiredImports = whenStatementDslSnippets
                .stream()
                .map(DslSnippet::getRequiredImports)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());


        requiredImports.add("org.springframework.util.LinkedMultiValueMap");

        Set<Bean> requiredBeans = whenStatementDslSnippets.stream()
                .map(DslSnippet::getBeans)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<String> requiredDependencies = whenStatementDslSnippets.stream()
                .map(DslSnippet::getRequiredDependencies)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        String otherwiseSubflowMappings = "";

        if (component.getOtherwise() != null) {
            ChoiceTopLevelElement otherWiseStatement = new ChoiceTopLevelElement(
                    flowName,
                    component.getOtherwise().getMessageProcessorOrOutboundEndpoint(),
                    muleConfigurations,
                    translatorsMap);

            DslSnippet otherWiseDSLSnippet = DslSnippet.createDSLSnippetFromTopLevelElement(otherWiseStatement);

            requiredImports.addAll(otherWiseDSLSnippet.getRequiredImports());
            requiredDependencies.addAll(otherWiseDSLSnippet.getRequiredDependencies());
            requiredBeans.addAll(otherWiseDSLSnippet.getBeans());

            otherwiseSubflowMappings = defaultSubflowMapping.replace(
                    "$OTHERWISE_STATEMENTS",
                    otherWiseStatement.renderDslSnippet()
            );

            otherwiseSubflowMappings = "                                .resolutionRequired(false)\n" +
                    otherwiseSubflowMappings;
        }

        return DslSnippet.builder()
                .renderedSnippet(" /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/\n" +
                        "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                        "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                        "                        m -> m\n" +
                        whenSubflowMappings +
                        otherwiseSubflowMappings +
                        "                )")
                .requiredImports(requiredImports)
                .requiredDependencies(requiredDependencies)
                .beans(requiredBeans)
                .build();
    }
}
