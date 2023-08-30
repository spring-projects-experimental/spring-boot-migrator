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
package org.springframework.sbm.mule.actions.javadsl.translators.wmq;

import org.mulesoft.schema.mule.ee.wmq.InboundEndpointType;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;

@Component
public class WmqInboundEndpointTranslator implements MuleComponentToSpringIntegrationDslTranslator<InboundEndpointType> {
    @Override
    public Class<InboundEndpointType> getSupportedMuleType() {
        return InboundEndpointType.class;
    }

    @Override
    public DslSnippet translate(int id,
                                InboundEndpointType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        return DslSnippet.builder()
                .renderedSnippet("return IntegrationFlows.from(Jms.inboundAdapter(connectionFactory).destination(\"" + component.getQueue() + "\")).handle((p, h) -> p)")
                .requiredImports(Set.of("org.springframework.integration.jms.dsl.Jms"))
                .requiredDependencies(Set.of("com.ibm.mq:mq-jms-spring-boot-starter:2.6.4", "org.springframework.integration:spring-integration-jms:5.5.8"))
                .beans(Set.of(new Bean("connectionFactory", "javax.jms.ConnectionFactory")))
                .build();
    }
}
