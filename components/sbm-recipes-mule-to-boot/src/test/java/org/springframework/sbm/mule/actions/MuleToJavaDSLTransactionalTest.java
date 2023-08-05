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

public class MuleToJavaDSLTransactionalTest extends JavaDSLActionBaseTest {

    @Test
    public void transactionalComponentTest() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                      xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                      xmlns:spring="http://www.springframework.org/schema/beans"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="example">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/transactional" doc:name="HTTP"/>
                        <transactional>
                            <foreach collection="#[['apple', 'banana', 'orange']]">
                                <logger message="#[payload]" level="INFO" />
                            </foreach>
                            <logger message="Done with for looping" level="INFO" />
                        </transactional>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction(projectContext -> {
            assertThat(getGeneratedJavaFile()).isEqualTo(
                    """
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
                        IntegrationFlow example(org.springframework.integration.dsl.IntegrationFlow exampleTransactional_1) {
                            return IntegrationFlows.from(Http.inboundGateway("/transactional")).handle((p, h) -> p)
                                    .gateway(exampleTransactional_1, e -> e.transactional(true))
                                    .get();
                        }
                                     
                        @Bean
                        IntegrationFlow exampleTransactional_1() {
                            return flow -> flow
                                    //TODO: translate expression #[['apple', 'banana', 'orange']] which must produces an array
                                    // to iterate over
                                    .split()
                                    .log(LoggingHandler.Level.INFO, "${payload}")
                                    .aggregate()
                                    .log(LoggingHandler.Level.INFO, "Done with for looping");
                        }
                    }""");
        });
    }

    @Test
    public void transactionalChildNodeUsesDWLTransformation() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                      xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                      xmlns:spring="http://www.springframework.org/schema/beans"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="example">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/transactional" doc:name="HTTP"/>
                        <transactional>
                            <foreach collection="#[['apple', 'banana', 'orange']]">
                                <logger message="#[payload]" level="INFO" />
                                <dw:transform-message doc:name="action transform">
                                    <dw:set-payload><![CDATA[%dw 1.0
                %output application/json
                ---
                {
                    action_Code: 10,
                    returnCode:  20
                }]]></dw:set-payload>
                                </dw:transform-message>
                            </foreach>
                            <logger message="Done with for looping" level="INFO" />
                        </transactional>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
            assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                    .isEqualTo(
                        """
                        package com.example.javadsl;
                                                   
                        public class ExampleTransactional_1Transform_1 {
                            /*
                             * TODO:
                             *
                             * Please add necessary transformation for below snippet
                             * [%dw 1.0
                             * %output application/json
                             * ---
                             * {
                             *     action_Code: 10,
                             *     returnCode:  20
                             * }]
                             * */
                            public static ExampleTransactional_1Transform_1 transform(Object payload) {
                                                   
                                return new ExampleTransactional_1Transform_1();
                            }
                        }"""
                    );
        });
    }
}
