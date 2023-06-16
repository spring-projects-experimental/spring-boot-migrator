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
package org.springframework.sbm.mule.actions.wmq;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/195")
public class WMQFlowTest extends JavaDSLActionBaseTest {

    String wmqXML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <mule xmlns:wmq="http://www.mulesoft.org/schema/mule/ee/wmq" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
               xmlns:spring="http://www.springframework.org/schema/beans"\s
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/ee/wmq http://www.mulesoft.org/schema/mule/ee/wmq/current/mule-wmq-ee.xsd">
               <wmq:connector name="WMQ" hostName="localhost" port="1414" queueManager="TestQueueManager" channel="TestChannel" username="guest" password="guet" transportType="CLIENT_MQ_TCPIP" validateConnections="true" doc:name="WMQ"/>
               <flow name="wmqtestFlow">
                  <wmq:inbound-endpoint queue="TestQueue" connector-ref="WMQ" doc:name="WMQ" targetClient="JMS_COMPLIANT"/>
                  <logger level="INFO" doc:name="Logger"/>
               </flow>
            </mule>
            """;


    @Test
    public void wmq() {
        addXMLFileToResource(wmqXML);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                               package com.example.javadsl;
                               import org.springframework.context.annotation.Bean;
                               import org.springframework.context.annotation.Configuration;
                               import org.springframework.integration.dsl.IntegrationFlow;
                               import org.springframework.integration.dsl.IntegrationFlows;
                               import org.springframework.integration.handler.LoggingHandler;
                               import org.springframework.integration.jms.dsl.Jms;
                                                              
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow wmqtestFlow(javax.jms.ConnectionFactory connectionFactory) {
                                       return IntegrationFlows.from(Jms.inboundAdapter(connectionFactory).destination("TestQueue")).handle((p, h) -> p)
                                               .log(LoggingHandler.Level.INFO)
                                               .get();
                                   }
                               }
                               """
                    );
        });
    }
}
