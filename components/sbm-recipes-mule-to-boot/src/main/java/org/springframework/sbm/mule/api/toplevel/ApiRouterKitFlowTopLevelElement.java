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
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiRouterKitFlowTopLevelElement extends AbstractTopLevelElement {
    private static final String apiRoutingKitNamingPattern = "(.*):(.*):.*";
    @Getter
    private final String route;
    @Getter
    private final String method;

    @Getter
    private final String configRef;

    public ApiRouterKitFlowTopLevelElement(String flowName,
                                           List<JAXBElement<?>> elements,
                                           MuleConfigurations muleConfigurations,
                                           Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        super(flowName, elements, muleConfigurations, translatorsMap);

        String[] components = flowName.split(":");
        method = components[0];
        route = components[1];

        configRef = components[components.length - 1];
    }

    @Override
    protected String composePrefixDslCode() {
        return  " // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref=\"" + configRef + "\"\n" +
                " // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref=\"" + configRef + "\" into this flow\n" +
                " // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref=\"" + configRef + "\"\n" +
                "return IntegrationFlows.from(\n" +
                "                Http.inboundGateway(\"" + route + "\").requestMapping(r -> r.methods(HttpMethod." + method.toUpperCase() + ")))\n";
    }

    @Override
    protected String composeSuffixDslCode() {
        return ".get();\n";
    }

    @Override
    public Set<String> getRequiredImports() {
        Set<String> requiredImports = super.getRequiredImports();
        requiredImports.add("org.springframework.http.HttpMethod");
        requiredImports.add("org.springframework.integration.http.dsl.Http");
        return requiredImports;
    }

    @Override
    public Set<String> getRequiredDependencies() {
        Set<String> requiredDependencies = super.getRequiredDependencies();
        requiredDependencies.add("org.springframework.integration:spring-integration-http:5.4.4");
        return requiredDependencies;
    }

    public static boolean isApiRouterKitName(String name) {
        return name.matches(apiRoutingKitNamingPattern);
    }
}
