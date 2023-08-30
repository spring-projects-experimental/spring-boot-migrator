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
package org.springframework.sbm.mule.actions.db;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDBInsertTest extends JavaDSLActionBaseTest {

    @Test
    public void dbInsert() {
        String muleXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:db="http://www.mulesoft.org/schema/mule/db" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd">
                    <flow name="dbFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/" doc:name="HTTP"/>
                        <logger level="INFO" doc:name="Logger"/>
                        <db:insert config-ref="Oracle_Configuration" doc:name="Database">
                            <db:parameterized-query><![CDATA[INSERT INTO STUDENTS (NAME, AGE, CITY) VALUES (#[payload.name], #[payload.age], #[payload.city])]]></db:parameterized-query>
                        </db:insert>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(muleXml);
        runAction(projectContext1 -> {
            assertThat(getGeneratedJavaFile()).isEqualTo("""
                             package com.example.javadsl;
                             import org.springframework.context.annotation.Bean;
                             import org.springframework.context.annotation.Configuration;
                             import org.springframework.integration.dsl.IntegrationFlow;
                             import org.springframework.integration.dsl.IntegrationFlows;
                             import org.springframework.integration.handler.LoggingHandler;
                             import org.springframework.integration.http.dsl.Http;
                             import org.springframework.jdbc.core.JdbcTemplate;
                             import org.springframework.util.LinkedMultiValueMap;
                                                                                      
                             @Configuration
                             public class FlowConfigurations {
                                 @Bean
                                 IntegrationFlow dbFlow(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
                                     return IntegrationFlows.from(Http.inboundGateway("/")).handle((p, h) -> p)
                                             .log(LoggingHandler.Level.INFO)
                                             // TODO: payload type might not be always LinkedMultiValueMap please change it to appropriate type\s
                                             // TODO: mule expression language is not converted to java, do it manually. example: #[payload] etc\s
                                             .<LinkedMultiValueMap<String, String>>handle((p, h) -> {
                                                 jdbcTemplate.update("INSERT INTO STUDENTS (NAME, AGE, CITY) VALUES (?, ?, ?)",
                                                         p.getFirst("payload.name") /* TODO: Translate #[payload.name] to java expression*/,
                                                         p.getFirst("payload.age") /* TODO: Translate #[payload.age] to java expression*/,
                                                         p.getFirst("payload.city") /* TODO: Translate #[payload.city] to java expression*/
                                                 );
                                                 return p;
                                             })
                                             .get();
                                 }
                             }""");
        });
    }
}
