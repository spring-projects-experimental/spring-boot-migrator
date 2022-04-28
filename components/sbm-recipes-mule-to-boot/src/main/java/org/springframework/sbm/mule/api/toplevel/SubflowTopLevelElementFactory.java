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

import org.mulesoft.schema.mule.core.SubFlowType;
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
public class SubflowTopLevelElementFactory implements TopLevelElementFactory {
    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;

    public SubflowTopLevelElementFactory(List<? extends MuleComponentToSpringIntegrationDslTranslator> translators) {
        translatorsMap = translators.stream()
                .collect(Collectors.toMap(MuleComponentToSpringIntegrationDslTranslator::getSupportedMuleType, Function.identity()));
    }


    @Override
    public Class<?> getSupportedTopLevelType() {
        return SubFlowType.class;
    }

    @Override
    public TopLevelElement buildDefinition(JAXBElement topLevelElement, MuleConfigurations muleConfigurations) {
        SubFlowType sft = ((SubFlowType) topLevelElement.getValue());
        String flowName = translateToJavaName(sft.getName());

        List<JAXBElement<?>> l = new ArrayList<>(sft.getMessageProcessorOrOutboundEndpoint());

        return new SubflowTopLevelElement(flowName, l, muleConfigurations, translatorsMap);
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
