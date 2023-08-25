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
package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLAmqpTest extends JavaDSLActionBaseTest {

    private static final String muleInboundOutboundXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans c\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "    <amqp:connector name=\"amqpConnector\"\n" +
            "      host=\"localhost\"\n" +
            "      port=\"5672\"\n" +
            "      username=\"guest\"\n" +
            "      password=\"guest\"\n" +
            "      doc:name=\"AMQP-0-9 Connector\"\n" +
            "       />\n" +
            "    <flow name=\"amqp-muleFlow\">\n" +
            "        <amqp:inbound-endpoint \n" +
            "        queueName=\"sbm-integration-queue-one\"\n" +
            "        connector-ref=\"amqpConnector\"\n" +
            "        />\n" +
            "        <!-- <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/test\" allowedMethods=\"POST\" doc:name=\"Recieve HTTP request\"/> -->\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        <amqp:outbound-endpoint \n" +
            "        exchangeName=\"sbm-integration-exchange\" \n" +
            "        routingKey=\"sbm-integration-queue-two\"\n" +
            "        responseTimeout=\"10000\"  \n" +
            "        doc:name=\"Send to AMQP queue\"\n" +
            "        />\n" +
            "    </flow>\n" +
            "</mule>\n";

    @Test
    public void generatesAmqpDSLStatements() {
        addXMLFileToResource(muleInboundOutboundXml);
        runAction(projectContext ->  {
            assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                               package com.example.javadsl;
                               import org.springframework.amqp.rabbit.core.RabbitTemplate;
                               import org.springframework.context.annotation.Bean;
                               import org.springframework.context.annotation.Configuration;
                               import org.springframework.integration.amqp.dsl.Amqp;
                               import org.springframework.integration.dsl.IntegrationFlow;
                               import org.springframework.integration.dsl.IntegrationFlows;
                               import org.springframework.integration.handler.LoggingHandler;
                                                          
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow amqp_muleFlow(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate) {
                                       return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "sbm-integration-queue-one"))
                                               .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                               .handle(Amqp.outboundAdapter(rabbitTemplate).exchangeName("sbm-integration-exchange").routingKey("sbm-integration-queue-two"))
                                               .get();
                                   }
                               }"""
                            );
        });
    }

    @Test
    public void generatesAMQPConnectorBean() {
        addXMLFileToResource(muleInboundOutboundXml);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
            assertThat(getApplicationPropertyContent()).isEqualTo("spring.rabbitmq.host=localhost\n" +
                    "spring.rabbitmq.port=5672");
        });
    }
}
