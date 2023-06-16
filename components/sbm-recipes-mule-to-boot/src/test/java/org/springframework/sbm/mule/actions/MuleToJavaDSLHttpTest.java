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
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLHttpTest extends JavaDSLActionBaseTest {
    private final static String muleXmlHttp =
            """
            <?xml version="1.0" encoding="UTF-8"?>
                        
            <mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core"
              xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
              http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd">
              <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="8081" doc:name="HTTP Listener Configuration"/>
              <flow name="http-routeFlow" doc:id="ff11723e-78e9-4cc2-b760-2bec156ef0f2" >
                <http:listener doc:name="Listener" doc:id="9f602d5c-5386-4fc9-ac8f-024d754c17e5" config-ref="HTTP_Listener_Configuration" path="/test"/>
                <logger level="INFO" doc:name="Logger" doc:id="4585ec7f-2d4a-4d86-af24-b678d4a99227" />
              </flow>
            </mule>
            """;

    @Test
    public void shouldGenerateJavaDSLForFlowHttpMuleTag() {
        addXMLFileToResource(muleXmlHttp);
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
                                   IntegrationFlow http_routeFlow() {
                                       return IntegrationFlows.from(Http.inboundGateway("/test")).handle((p, h) -> p)
                                               .log(LoggingHandler.Level.INFO)
                                               .get();
                                   }
                               }""");

            assertThat(getApplicationPropertyContent()).isEqualTo("server.port=8081");
        });
    }
}
