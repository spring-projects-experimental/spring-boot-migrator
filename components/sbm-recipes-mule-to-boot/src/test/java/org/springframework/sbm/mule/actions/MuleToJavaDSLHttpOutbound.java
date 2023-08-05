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

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLHttpOutbound extends JavaDSLActionBaseTest {
    private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:json=\"http://www.mulesoft.org/schema/mule/json\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd\">\n" +
            "    <http:listener-config name=\"HTTP_Listener_Configuration1\" host=\"0.0.0.0\" port=\"9082\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "    <http:request-config name=\"HTTP_Request_Configuration\" host=\"catfact.ninja\" port=\"443\" doc:name=\"HTTP Request Configuration\" protocol=\"HTTPS\"/>\n" +
            "    <flow name=\"httpFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration1\" path=\"/gimme-a-cat-fact\" doc:name=\"HTTP\"/>\n" +
            "        <http:request config-ref=\"HTTP_Request_Configuration\" path=\"/fact\" method=\"GET\" doc:name=\"HTTP\"/>\n" +
            "        <set-payload doc:name=\"Set Payload\" value=\"#[payload]\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    @Test
    public void supportForHttpOutboundRequest() {
        addXMLFileToResource(xml);
        runAction(projectContext1 -> {
            assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                               package com.example.javadsl;
                               import org.springframework.context.annotation.Bean;
                               import org.springframework.context.annotation.Configuration;
                               import org.springframework.http.HttpMethod;
                               import org.springframework.integration.dsl.IntegrationFlow;
                               import org.springframework.integration.dsl.IntegrationFlows;
                               import org.springframework.integration.http.dsl.Http;
                                                              
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow httpFlow() {
                                       return IntegrationFlows.from(Http.inboundGateway("/gimme-a-cat-fact")).handle((p, h) -> p)
                                               .headerFilter("accept-encoding", false)
                                               .handle(
                                                       Http.outboundGateway("https://catfact.ninja:443/fact")
                                                               .httpMethod(HttpMethod.GET)
                                                               //FIXME: Use appropriate response class type here instead of String.class
                                                               .expectedResponseType(String.class)
                                               )
                                               .handle((p, h) -> "#[payload]")
                                               .get();
                                   }
                               }""");
        });
    }
}
