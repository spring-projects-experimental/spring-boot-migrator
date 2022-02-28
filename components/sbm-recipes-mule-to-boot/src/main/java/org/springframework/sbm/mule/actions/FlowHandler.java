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
package org.springframework.sbm.mule.actions;

import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.UnknownStatementTranslator;
import org.springframework.sbm.mule.api.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.core.SubFlowType;
import org.mulesoft.schema.mule.tls.TlsContextType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * TODO:
 * - Translators must be able to add additional methods to register components
 * - Translators must be able to define method parameters
 * - Create a MuleMigrationContext which provides access to the current Context -> Properties, Bean definitions, all XML models (populated JAXB model)... all translate methods get access to the context than.
 * - Translators (?) need to add dependencies for required SI modules they must be added before adding the method
 */
@Slf4j
@Component
public class FlowHandler {

    private static final Set<String> requiredImports = new HashSet<>();
    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;

    public FlowHandler(List<? extends MuleComponentToSpringIntegrationDslTranslator> translators) {
        translatorsMap = translators.stream()
                .collect(Collectors.toMap(MuleComponentToSpringIntegrationDslTranslator::getSupportedMuleType, Function.identity()));
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlow");
    }

    public DefinitionSnippet transformFlow(MuleMigrationContext muleMigrationContext, JAXBElement element) {

        Object jaxbValue = element.getValue();
        if (jaxbValue instanceof FlowType) {
            return transformFlow(muleMigrationContext, (FlowType) jaxbValue);
        }

        return new UnknownDefinitionSnippet(element);
    }

    private DslSnippet translate(MuleMigrationContext context, Object o, QName name) {
        MuleComponentToSpringIntegrationDslTranslator translator = translatorsMap.getOrDefault(o.getClass(), new UnknownStatementTranslator());
        return translator.translate(context, o, name);
    }

    public BaseDefinitionSnippet transformSubflow(MuleMigrationContext context, SubFlowType f) {
        String flowName = translateToJavaName(f.getName());

        List<JAXBElement> l = new ArrayList<>();
        l.addAll(f.getMessageProcessorOrOutboundEndpoint());
        // Order here is very important as this also defines the order of statements in the dsl.
        return getSubflowDefinitionSnippet(context, flowName, l);
    }

    private BaseDefinitionSnippet transformFlow(MuleMigrationContext context, FlowType f) {
        String flowName = translateToJavaName(f.getName());

        List<JAXBElement> l = new ArrayList<>();
        if (f.getAbstractMessageSource() != null) {
            l.add(f.getAbstractMessageSource());
        }
        if (f.getAbstractInboundEndpoint() != null) {
            l.add(f.getAbstractInboundEndpoint());
        }
        if (f.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor() != null) {
            l.addAll(f.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor());
        }
        return getFlowDefinitionSnippet(context, flowName, l);
    }

    @NotNull
    private BaseDefinitionSnippet getFlowDefinitionSnippet(MuleMigrationContext context, String flowName, List<JAXBElement> l) {
        List<DslSnippet> dslSnippets = getDslSnippets(context, l);

        if (ApiRouterKitFlowDefinitionSnippet.isApiRouterKitName(flowName)) {
            return new ApiRouterKitFlowDefinitionSnippet(flowName, dslSnippets);
        }
        return new FlowDefinitionSnippet(flowName, dslSnippets);
    }

    @NotNull
    private BaseDefinitionSnippet getSubflowDefinitionSnippet(MuleMigrationContext context, String flowName, List<JAXBElement> l) {
        List<DslSnippet> dslSnippets = getDslSnippets(context, l);
        return new SubflowDefinitionSnippet(flowName, dslSnippets);
    }

    @NotNull
    private List<DslSnippet> getDslSnippets(MuleMigrationContext context, List<JAXBElement> l) {
        return l.stream()
                .map(o -> this.translate(context, o.getValue(), o.getName()))
                .collect(Collectors.toList());
    }

    private String translateToJavaName(String name) {
        String resultName = name;
        int varSuffixIndex = name.indexOf("$");
        if (varSuffixIndex != -1) {
            resultName = resultName.substring(0, varSuffixIndex - 1);
        }
        return Character.toLowerCase(resultName.charAt(0)) + resultName.substring(1);
    }

}
