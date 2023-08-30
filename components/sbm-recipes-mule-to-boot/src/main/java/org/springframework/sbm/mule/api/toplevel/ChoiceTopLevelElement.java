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

import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChoiceTopLevelElement extends AbstractTopLevelElement {

    public String renderDslSnippet() {
        StringBuilder sb = new StringBuilder();
        String dsl = getDslSnippets().stream().map(DslSnippet::getRenderedSnippet).collect(Collectors.joining("\n"));
        sb.append(composePrefixDslCode(dsl));
        sb.append(dsl);
        Set<String> requiredImports = getRequiredImports();
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlow");
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlows");
        getDslSnippets().forEach(ds -> requiredImports.addAll(ds.getRequiredImports()));
        return sb.toString();
    }

    protected String composePrefixDslCode(String dsl) {
        if (dsl != null && dsl.isBlank()) {
            return "sf -> sf.handle((p,h) -> p)";
        }
        return "sf -> sf";
    }

    public ChoiceTopLevelElement(String flowName,
                                 List<JAXBElement<?>> elements,
                                 MuleConfigurations muleConfigurations,
                                 Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        super(flowName, elements, muleConfigurations, translatorsMap);
    }
}
