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
package org.springframework.sbm.mule.api.toplevel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.UnknownStatementTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the snippet for a Spring Integration Flow bean definition
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractTopLevelElement implements TopLevelElement {
    private final Set<String> requiredImports = new HashSet<>();
    private String flowName;
    private List<JAXBElement<?>> elements;
    private final Set<String> requiredDependencies = new HashSet<>();
    private final MuleConfigurations muleConfigurations;
    private List<DslSnippet> dslSnippets;

    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;


    public AbstractTopLevelElement(String flowName,
                                   List<JAXBElement<?>> elements,
                                   MuleConfigurations muleConfigurations,
                                   Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap)  {
        this.flowName = flowName;
        this.elements = elements;
        this.muleConfigurations = muleConfigurations;
        this.translatorsMap = translatorsMap;
        requiredImports.add("org.springframework.context.annotation.Bean");
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlow");
        dslSnippets = buildDslSnippets();
    }

    public List<DslSnippet> buildDslSnippets() {

        List<DslSnippet> dslSnippets = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            JAXBElement<?> o = elements.get(i);
            dslSnippets.add(
                    translate(i, o.getValue(), o.getName(), muleConfigurations, flowName)
            );
        }
        return dslSnippets;
    }

    private DslSnippet translate(int id, Object o, QName name, MuleConfigurations muleConfigurations, String flowName) {
        MuleComponentToSpringIntegrationDslTranslator translator = translatorsMap.getOrDefault(o.getClass(), new UnknownStatementTranslator());
        return translator.translate(id, o, name, muleConfigurations, flowName, this.translatorsMap);
    }


    /**
     * @return the list of required types to be imported
     */
    public Set<String> getRequiredImports() {
        Set<String> collect = dslSnippets.stream().flatMap(s -> s.getRequiredImports().stream()).collect(Collectors.toSet());
        requiredImports.addAll(collect);
        return requiredImports;
    }

    /**
     * @return a Set of Maven coordinates that must exist in classpath
     */
    public Set<String> getRequiredDependencies() {
        Set<String> collect = dslSnippets.stream().flatMap(s -> s.getRequiredDependencies().stream()).collect(Collectors.toSet());
        requiredDependencies.addAll(collect);
        return requiredDependencies;
    }

    /**
     * @return the SI DSL bean definition code
     */
    public String renderDslSnippet() {
        StringBuilder sb = new StringBuilder();
        sb.append("@Bean\n");
        String methodParams = DslSnippet.renderMethodParameters(dslSnippets);
        sb.append("IntegrationFlow ").append(getGeneratedIdentity()).append("(").append(methodParams).append(") {\n");
        sb.append(composePrefixDslCode());
        String dsl = getDslSnippets().stream().map(DslSnippet::getRenderedSnippet).collect(Collectors.joining("\n"));
        sb.append(dsl).append("\n");
        sb.append(composeSuffixDslCode());
        sb.append("}\n");
        Set<String> requiredImports = getRequiredImports();
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlow");
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlows");
        getDslSnippets().forEach(ds -> requiredImports.addAll(ds.getRequiredImports()));
        return sb.toString();
    }

    public String getGeneratedIdentity() {
        return Helper.sanitizeForBeanMethodName(getFlowName());
    }

    @Override
    public Set<String> getExternalClassContents() {
        return dslSnippets.stream()
                .map(DslSnippet::getExternalClassContent)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasGeneratedDependentFlows() {
        return dslSnippets
                .stream()
                .anyMatch(f -> f.getRenderedDependentFlows() != null);
    }

    @Override
    public List<String> generatedDependentFlows() {
        return dslSnippets
                .stream()
                .map(DslSnippet::getRenderedDependentFlows)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected String composePrefixDslCode() {
        return "";
    }

    protected String composeSuffixDslCode() {
        return "";
    }
}
