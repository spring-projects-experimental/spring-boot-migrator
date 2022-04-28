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
package org.springframework.sbm.mule.api.toplevel;

import org.mulesoft.schema.mule.core.FlowType;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FlowTopLevelElementFactory implements TopLevelElementFactory {
    private List<String> requiredImports = new ArrayList<>();
    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;


    public FlowTopLevelElementFactory(List<? extends MuleComponentToSpringIntegrationDslTranslator> translators) {
        translatorsMap = translators.stream()
                .collect(Collectors.toMap(MuleComponentToSpringIntegrationDslTranslator::getSupportedMuleType, Function.identity()));
    }

    @Override
    public Class<?> getSupportedTopLevelType() {
        return FlowType.class;
    }

    @Override
    public TopLevelElement buildDefinition(JAXBElement topLevelElement, MuleConfigurations muleConfigurations) {
        FlowType ft = ((FlowType) topLevelElement.getValue());
        if (ApiRouterKitFlowTopLevelElement.isApiRouterKitName(ft.getName())) {
            return new ApiRouterKitFlowTopLevelElement(ft.getName(), extractFlowElements(ft), muleConfigurations, translatorsMap);
        } else {
            return new FlowTopLevelElement(ft.getName(), extractFlowElements(ft), muleConfigurations, translatorsMap);
        }
    }

    private List<JAXBElement<?>> extractFlowElements(FlowType ft) {
        List<JAXBElement<?>> l = new ArrayList<>();
        if (ft.getAbstractMessageSource() != null) {
            l.add(ft.getAbstractMessageSource());
        }
        if (ft.getAbstractInboundEndpoint() != null) {
            l.add(ft.getAbstractInboundEndpoint());
        }
        if (ft.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor() != null) {
            l.addAll(ft.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor());
        }
        return l;
    }

}
