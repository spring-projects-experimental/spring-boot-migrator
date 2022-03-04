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

import org.springframework.sbm.mule.actions.javadsl.translators.RequiredBean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.mulesoft.schema.mule.amqp.OutboundEndpointType;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Set;

@Component
public class AmqpOutboundEndpointTranslator implements MuleComponentToSpringIntegrationDslTranslator<OutboundEndpointType> {

    private static final String snippet = ".handle(Amqp.outboundAdapter(rabbitTemplate)--EXCHANGE----ROUTING-KEY--)";

    @Override
    public Class<OutboundEndpointType> getSupportedMuleType() {
        return OutboundEndpointType.class;
    }

    @Override
    public DslSnippet translate(OutboundEndpointType component, QName name, MuleConfigurations muleConfigurations) {


        addExchange(snippet, component.getExchangeName());
        return new DslSnippet(
                addRoutingKey(
                        addExchange(snippet, component.getExchangeName()),
                        component.getRoutingKey(),
                        component.getQueueName()
                ),
                Set.of("org.springframework.amqp.rabbit.core.RabbitTemplate"),
                Set.of(),
                Set.of(new RequiredBean("rabbitTemplate", "org.springframework.amqp.rabbit.core.RabbitTemplate")
                )
        );
    }

    private String addRoutingKey(String template, String routingKey, String queueName) {

        String key = routingKey == null ? queueName : routingKey;
        return template.replace("--ROUTING-KEY--", ".routingKey(\""+key+"\")");
    }

    private String addExchange(String template, String exchangeName) {
        String exchangeSnippet = "";
        if (exchangeName != null) {
            exchangeSnippet = ".exchangeName(\""+emptyStringOrValue(exchangeName)+"\")";
        }
        return template.replace("--EXCHANGE--", exchangeSnippet);
    }

    private String emptyStringOrValue(String str) {
        return str == null ? "" : str;
    }
}
