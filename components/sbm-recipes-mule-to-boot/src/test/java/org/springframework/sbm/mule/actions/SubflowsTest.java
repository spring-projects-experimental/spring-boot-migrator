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
package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubflowsTest extends JavaDSLActionBaseTest {

    private static final String subflowWithRabbit = """
            <?xml version="1.0" encoding="UTF-8"?>
                        
            <mule xmlns:amqp="http://www.mulesoft.org/schema/mule/amqp" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                  xmlns:spring="http://www.springframework.org/schema/beans"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
            http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd">
                <amqp:connector name="amqpConnector"
                                host="localhost"
                                port="5672"
                                username="guest"
                                password="guest"
                                doc:name="AMQP-0-9 Connector"
                />
                <flow name="amqp-muleFlow">
                    <amqp:inbound-endpoint
                            queueName="sbm-integration-queue-one"
                            connector-ref="amqpConnector"
                    />
                    <!-- <http:listener config-ref="HTTP_Listener_Configuration" path="/test" allowedMethods="POST" doc:name="Recieve HTTP request"/> -->
                    <logger message="payload to be sent: #[new String(payload)]" level="INFO" doc:name="Log the message content to be sent"/>
                    <flow-ref name="outToAMQP" />
                </flow>
                <sub-flow name="outToAMQP">
                    <amqp:outbound-endpoint
                            exchangeName="sbm-integration-exchange"
                            routingKey="sbm-integration-queue-two"
                            responseTimeout="10000"
                            doc:name="Send to AMQP queue"
                    />
                </sub-flow>
            </mule>
            """;

    private static final String subflowUnknown = """
            <?xml version="1.0" encoding="UTF-8"?>
                        
            <mule xmlns:amqp="http://www.mulesoft.org/schema/mule/amqp" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                  xmlns:spring="http://www.springframework.org/schema/beans"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
            http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd">
                <amqp:connector name="amqpConnector"
                                host="localhost"
                                port="5672"
                                username="guest"
                                password="guest"
                                doc:name="AMQP-0-9 Connector"
                />
                <flow name="amqp-muleFlow">
                    <amqp:inbound-endpoint
                            queueName="sbm-integration-queue-one"
                            connector-ref="amqpConnector"
                    />
                    <!-- <http:listener config-ref="HTTP_Listener_Configuration" path="/test" allowedMethods="POST" doc:name="Recieve HTTP request"/> -->
                    <logger message="payload to be sent: #[new String(payload)]" level="INFO" doc:name="Log the message content to be sent"/>
                    <flow-ref name="outToUnknown" />
                </flow>
                <sub-flow name="outToUnknown">
                     <set-variable mimeType="application/java" doc:name="setVariable_actionCode"
                                  variableName="actionCode" value="#[dw('payload.action_Code[0]')]"  />
                </sub-flow>
            </mule>
            """;

    @Test
    public void generatedFlowShouldHaveMethodParams() {

        addXMLFileToResource(subflowWithRabbit);
        runAction(projectContext1 -> {
            assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo(
                            """
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
                                IntegrationFlow amqp_muleFlow(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, org.springframework.integration.dsl.IntegrationFlow outToAMQP) {
                                    return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "sbm-integration-queue-one"))
                                            .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                            .gateway(outToAMQP)
                                            .get();
                                }
                                                   
                                @Bean
                                IntegrationFlow outToAMQP(org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate) {
                                    return flow -> flow
                                            .handle(Amqp.outboundAdapter(rabbitTemplate).exchangeName("sbm-integration-exchange").routingKey("sbm-integration-queue-two"));
                                }
                            }"""
                    );
        });
    }

    @Test
    public void shouldTranslateSubflowWithUnknownElements() {
        addXMLFileToResource(subflowUnknown);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                               package com.example.javadsl;
                               import org.springframework.context.annotation.Bean;
                               import org.springframework.context.annotation.Configuration;
                               import org.springframework.integration.amqp.dsl.Amqp;
                               import org.springframework.integration.dsl.IntegrationFlow;
                               import org.springframework.integration.dsl.IntegrationFlows;
                               import org.springframework.integration.handler.LoggingHandler;
                                                              
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow amqp_muleFlow(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, org.springframework.integration.dsl.IntegrationFlow outToUnknown) {
                                       return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, "sbm-integration-queue-one"))
                                               .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                               .gateway(outToUnknown)
                                               .get();
                                   }
                                                              
                                   @Bean
                                   IntegrationFlow outToUnknown() {
                                       return flow -> {
                                           //FIXME: element is not supported for conversion: <set-variable/>
                                       };
                                   }
                               }"""
                    );
        });
    }
}
