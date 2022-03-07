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
package org.springframework.sbm.mule.actions.javadsl.translators.amqp;

import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.mulesoft.schema.mule.amqp.InboundEndpointType;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Set;

/***/
@Component
public class AmqpInboundEndpointTranslator implements MuleComponentToSpringIntegrationDslTranslator<InboundEndpointType> {

    private static final String snippetTemplate = "return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"${queueName}\"))";

    @Override
    public Class getSupportedMuleType() {
        return InboundEndpointType.class;
    }

    @Override
    public DslSnippet translate(InboundEndpointType inboundEndpointType, QName name, MuleConfigurations muleConfigurations) {
        String queueName = inboundEndpointType.getQueueName();
        String renderedSnippet = snippetTemplate.replace("${queueName}", queueName);
        Bean amqpConnectionFactoryBean = new Bean("connectionFactory", "org.springframework.amqp.rabbit.connection.ConnectionFactory");
        return new DslSnippet(renderedSnippet, Set.of("org.springframework.integration.amqp.dsl.Amqp"), Set.of("org.springframework.integration:spring-integration-amqp:5.4.4"), Set.of(amqpConnectionFactoryBean));
    }
}
