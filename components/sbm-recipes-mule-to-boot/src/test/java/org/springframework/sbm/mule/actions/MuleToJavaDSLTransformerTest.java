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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLTransformerTest extends JavaDSLActionBaseTest {
    @Language("xml")
    private final static String muleXmlHttp = """
            <?xml version="1.0" encoding="UTF-8"?>
            <mule xmlns:amqp="http://www.mulesoft.org/schema/mule/amqp" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
            xmlns:spring="http://www.springframework.org/schema/beans"\s
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
            http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd">
            <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9082" doc:name="HTTP Listener Configuration"/>
            <flow name="http-flow">
            <http:listener doc:name="Listener"  config-ref="HTTP_Listener_Configuration" path="/test"/>
            <byte-array-to-string-transformer/>
            <logger message="payload to be sent: #[new String(payload)]" level="INFO" doc:name="Log the message content to be sent"/>
            <string-to-byte-array-transformer/>
            <logger message="payload to be sent: #[new String(payload)]" level="INFO" doc:name="Log the message content to be sent"/>
            <byte-array-to-string-transformer/>
            <logger message="payload to be sent: #[new String(payload)]" level="INFO" doc:name="Log the message content to be sent"/>
            </flow>
            </mule>
            """;

    @Test
    public void shouldGenerateJavaDSLForFlowHttpMuleTag() {
        addXMLFileToResource(muleXmlHttp);
        runAction(projectContext1 -> {
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
                               import org.springframework.integration.transformer.ObjectToStringTransformer;
                                                              
                               import java.nio.charset.StandardCharsets;
                                                              
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow http_flow() {
                                       return IntegrationFlows.from(Http.inboundGateway("/test")).handle((p, h) -> p)
                                               .transform(new ObjectToStringTransformer())
                                               .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                               .transform(s -> ((String) s).getBytes(StandardCharsets.UTF_8))
                                               .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                               .transform(new ObjectToStringTransformer())
                                               .log(LoggingHandler.Level.INFO, "payload to be sent: #[new String(payload)]")
                                               .get();
                                   }
                               }""");
        });
    }
}
