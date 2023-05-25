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

public class MuleToJavaDSLForeachTest extends JavaDSLActionBaseTest {

    @Test
    public void simpleForEachTest() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                    xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="foreach">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/foreach" doc:name="HTTP"/>   \s
                        <foreach collection="#[['apple', 'banana', 'orange']]">
                            <logger message="#[payload]" level="INFO" />
                        </foreach>
                        <logger message="Done with for looping" level="INFO" />
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
                    IntegrationFlow foreach() {
                        return IntegrationFlows.from(Http.inboundGateway("/foreach")).handle((p, h) -> p)
                                //TODO: translate expression #[['apple', 'banana', 'orange']] which must produces an array
                                // to iterate over
                                .split()
                                .log(LoggingHandler.Level.INFO, "${payload}")
                                .aggregate()
                                .log(LoggingHandler.Level.INFO, "Done with for looping")
                                .get();
                    }
                }""");
        });
    }

    @Test
    public void forEachWithChoice() {

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                    xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="foreach">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/foreach" doc:name="HTTP"/>   \s
                        <foreach collection="#[[1, 2, 3, 4]]">
                            <choice doc:name="Choice">
                            <when expression="#[payload == 1]">
                                <logger level="INFO" message="Ondu"></logger>
                            </when>
                            <when expression="#[payload == 2]">
                                <logger level="INFO" message="Eradu"></logger>
                            </when>
                            <when expression="#[payload == 3]">
                                <logger level="INFO" message="Mooru"></logger>
                            </when>
                            <otherwise>
                                <logger level="INFO" message="Moorina mele"></logger>
                            </otherwise>
                        </choice>
                        </foreach>
                        <logger message="Done with for looping" level="INFO" />
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
                    import org.springframework.util.LinkedMultiValueMap;
                                     
                    @Configuration
                    public class FlowConfigurations {
                        @Bean
                        IntegrationFlow foreach() {
                            return IntegrationFlows.from(Http.inboundGateway("/foreach")).handle((p, h) -> p)
                                    //TODO: translate expression #[[1, 2, 3, 4]] which must produces an array
                                    // to iterate over
                                    .split()
                                    /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                    .<LinkedMultiValueMap<String, String>, String>route(
                                            p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                            m -> m
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 1]*/,
                                                            sf -> sf.log(LoggingHandler.Level.INFO, "Ondu")
                                                    )
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 2]*/,
                                                            sf -> sf.log(LoggingHandler.Level.INFO, "Eradu")
                                                    )
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 3]*/,
                                                            sf -> sf.log(LoggingHandler.Level.INFO, "Mooru")
                                                    )
                                                    .resolutionRequired(false)
                                                    .defaultSubFlowMapping(sf -> sf.log(LoggingHandler.Level.INFO, "Moorina mele"))
                                    )
                                    .aggregate()
                                    .log(LoggingHandler.Level.INFO, "Done with for looping")
                                    .get();
                        }
                    }""");
        });
    }

    @Test
    public void forEachWithCallToSubflow() {

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                    xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="foreach">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/foreach" doc:name="HTTP"/>   \s
                        <foreach collection="#[[1, 2, 3, 4]]">
                            <choice doc:name="Choice">
                            <when expression="#[payload == 1]">
                                <flow-ref name="logOneInKannada"></flow-ref>
                            </when>
                            <when expression="#[payload == 2]">
                                <logger level="INFO" message="Eradu"></logger>
                            </when>
                            <when expression="#[payload == 3]">
                                <logger level="INFO" message="Mooru"></logger>
                            </when>
                            <otherwise>
                                <logger level="INFO" message="Moorina mele"></logger>
                            </otherwise>
                        </choice>
                        </foreach>
                        <logger message="Done with for looping" level="INFO" />
                    </flow>
                   \s
                    <sub-flow name="logOneInKannada">
                       <logger message="Ondu" level="INFO" doc:name="loggerOrdinal"/>
                   </sub-flow>
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
                    import org.springframework.util.LinkedMultiValueMap;
                                             
                    @Configuration
                    public class FlowConfigurations {
                        @Bean
                        IntegrationFlow foreach(org.springframework.integration.dsl.IntegrationFlow logOneInKannada) {
                            return IntegrationFlows.from(Http.inboundGateway("/foreach")).handle((p, h) -> p)
                                    //TODO: translate expression #[[1, 2, 3, 4]] which must produces an array
                                    // to iterate over
                                    .split()
                                    /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                    .<LinkedMultiValueMap<String, String>, String>route(
                                            p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                            m -> m
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 1]*/,
                                                            sf -> sf.gateway(logOneInKannada)
                                                    )
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 2]*/,
                                                            sf -> sf.log(LoggingHandler.Level.INFO, "Eradu")
                                                    )
                                                    .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == 3]*/,
                                                            sf -> sf.log(LoggingHandler.Level.INFO, "Mooru")
                                                    )
                                                    .resolutionRequired(false)
                                                    .defaultSubFlowMapping(sf -> sf.log(LoggingHandler.Level.INFO, "Moorina mele"))
                                    )
                                    .aggregate()
                                    .log(LoggingHandler.Level.INFO, "Done with for looping")
                                    .get();
                        }
                                             
                        @Bean
                        IntegrationFlow logOneInKannada() {
                            return flow -> flow
                                    .log(LoggingHandler.Level.INFO, "Ondu");
                        }
                    }"""
                    );
        });


    }
}
