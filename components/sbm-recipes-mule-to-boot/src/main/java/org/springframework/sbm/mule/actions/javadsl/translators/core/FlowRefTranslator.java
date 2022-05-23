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

import lombok.extern.slf4j.Slf4j;
import org.mulesoft.schema.mule.core.FlowRef;
import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Transforms a {@code <http:listener/>} elements to SI DSL.
 *
 * https://docs.mulesoft.com/http-connector/0.3.9/http-listener-connector
 */
@Component
@Slf4j
public class FlowRefTranslator implements MuleComponentToSpringIntegrationDslTranslator<FlowRef> {

    public static final String SUBFLOW_NAME = "--SUBFLOW_NAME--";
    private static final String javaDslFlowRefTemplate = ".gateway(--SUBFLOW_NAME--)";

    @Override
    public Class getSupportedMuleType() {
        return FlowRef.class;
    }

    @Override
    public DslSnippet translate(int id, FlowRef component, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        String subFlowName = Helper.sanitizeForBeanMethodName(translateToJavaName(component.getName()));
        String flowRefSnippet = javaDslFlowRefTemplate.replace(SUBFLOW_NAME, subFlowName);
        return new DslSnippet(flowRefSnippet,
                Collections.emptySet(),
                Collections.emptySet(),
                Set.of(new Bean(subFlowName, "org.springframework.integration.dsl.IntegrationFlow")));
    }

    private String translateToJavaName(String name) {
        String resultName = name;
        int varSuffixIndex = name.indexOf("$");
        if (varSuffixIndex != -1) {
            resultName = resultName.substring(0, varSuffixIndex);
        }
        return Character.toLowerCase(resultName.charAt(0)) + resultName.substring(1);
    }
}
