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

public class MultipleFlowsTest extends JavaDSLActionBaseTest {

    private final static String muleMultiFlow = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:metadata=\"http://www.mulesoft.org/schema/mule/metadata\" xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "<http:listener-config name=\"HTTP_Listener_Configuration_main\" host=\"0.0.0.0\" port=\"9081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "<flow name=\"main-flow\">\n" +
            "<http:listener doc:name=\"Listener\"  config-ref=\"HTTP_Listener_Configuration_main\" path=\"/subflows\"/>\n" +
            "<flow-ref name=\"logging\" doc:name=\"subflow\" />\n" +
            "</flow>\n" +
            "<sub-flow name=\"logging\">\n" +
            "<logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
            "</sub-flow>\n" +
            "</mule>";

    @Test
    public void shouldTranslateSubflow() {
        addXMLFileToResource(muleMultiFlow);
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
                           import org.springframework.integration.http.dsl.Http;
                                                      
                           @Configuration
                           public class FlowConfigurations {
                               @Bean
                               IntegrationFlow main_flow(org.springframework.integration.dsl.IntegrationFlow logging) {
                                   return IntegrationFlows.from(Http.inboundGateway("/subflows")).handle((p, h) -> p)
                                           .gateway(logging)
                                           .get();
                               }
                                                      
                               @Bean
                               IntegrationFlow logging() {
                                   return flow -> flow
                                           .log(LoggingHandler.Level.INFO);
                               }
                           }""");
        });
    }
}
