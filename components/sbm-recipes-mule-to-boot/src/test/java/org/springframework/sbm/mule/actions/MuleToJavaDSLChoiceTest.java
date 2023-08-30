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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLChoiceTest extends JavaDSLActionBaseTest {
    private static final String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
                        
            <mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                xmlns:spring="http://www.springframework.org/schema/beans"\s
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
            http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9081" doc:name="HTTP Listener Configuration"/>
                <flow name="choiceFlow">
                    <http:listener config-ref="HTTP_Listener_Configuration" path="/choice" doc:name="HTTP"/>
                    <expression-filter expression="#[message.inboundProperties.'http.request.uri' != '/favicon.ico']" doc:name="Expression"/>
                    <set-variable variableName="language" value="#[message.inboundProperties.'http.query.params'.language]" doc:name="Set Language Variable"/>
                    <choice doc:name="Choice">
                        <when expression="#[flowVars.language == 'Spanish']">
                            <set-payload doc:name="Reply in Spanish" value="Hola!"/>
                        </when>
                        <when expression="#[flowVars.language == 'French']">
                            <set-payload doc:name="Reply in French" value="Bonjour!"/>
                        </when>
                        <otherwise>
                            <set-payload doc:name="Reply in English" value="Hello"/>
                        </otherwise>
                    </choice>
                    <logger message="#[payload]" level="INFO" doc:name="Logger"/>
                </flow>
            </mule>
            """;

    @Test
    public void supportsBasicChoiceElement() {
        addXMLFileToResource(xml);
        runAction(projectContext -> {
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
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
                                   IntegrationFlow choiceFlow() {
                                       return IntegrationFlows.from(Http.inboundGateway("/choice")).handle((p, h) -> p)
                                               //FIXME: element is not supported for conversion: <expression-filter/>
                                               //FIXME: element is not supported for conversion: <set-variable/>
                                               /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                               .<LinkedMultiValueMap<String, String>, String>route(
                                                       p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                                       m -> m
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'Spanish']*/,
                                                                       sf -> sf.handle((p, h) -> "Hola!")
                                                               )
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'French']*/,
                                                                       sf -> sf.handle((p, h) -> "Bonjour!")
                                                               )
                                                               .resolutionRequired(false)
                                                               .defaultSubFlowMapping(sf -> sf.handle((p, h) -> "Hello"))
                                               )
                                               .log(LoggingHandler.Level.INFO, "${payload}")
                                               .get();
                                   }
                               }""");
        });
    }

    @Test
    public void whenExpressionCallsSubFlow() {
        final String xml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                        <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9081" doc:name="HTTP Listener Configuration"/>
                    <flow name="choiceFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/choice" doc:name="HTTP"/>
                        <expression-filter expression="#[message.inboundProperties.'http.request.uri' != '/favicon.ico']" doc:name="Expression"/>
                        <set-variable variableName="language" value="#[message.inboundProperties.'http.query.params'.language]" doc:name="Set Language Variable"/>
                        <choice doc:name="Choice">
                            <when expression="#[flowVars.language == 'Spanish']">
                                <flow-ref name="spanishHello"></flow-ref>
                            </when>
                            <when expression="#[flowVars.language == 'French']">
                                <set-payload doc:name="Reply in French" value="Bonjour!"/>
                            </when>
                            <otherwise>
                                <set-payload doc:name="Reply in English" value="Hello"/>
                            </otherwise>
                        </choice>
                        <logger message="#[payload]" level="INFO" doc:name="Logger"/>
                    </flow>
                    <sub-flow name="spanishHello">
                       <logger message="A spanish Hello"
                             level="INFO"
                             doc:name="RequestPayloadReceived" />
                       <set-payload doc:name="Reply in Spanish" value="Hola!!!"/>
                   </sub-flow>
                </mule>
                """;
        addXMLFileToResource(xml);
        runAction(projectContext -> {

            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
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
                                   IntegrationFlow choiceFlow(org.springframework.integration.dsl.IntegrationFlow spanishHello) {
                                       return IntegrationFlows.from(Http.inboundGateway("/choice")).handle((p, h) -> p)
                                               //FIXME: element is not supported for conversion: <expression-filter/>
                                               //FIXME: element is not supported for conversion: <set-variable/>
                                               /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                               .<LinkedMultiValueMap<String, String>, String>route(
                                                       p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                                       m -> m
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'Spanish']*/,
                                                                       sf -> sf.gateway(spanishHello)
                                                               )
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'French']*/,
                                                                       sf -> sf.handle((p, h) -> "Bonjour!")
                                                               )
                                                               .resolutionRequired(false)
                                                               .defaultSubFlowMapping(sf -> sf.handle((p, h) -> "Hello"))
                                               )
                                               .log(LoggingHandler.Level.INFO, "${payload}")
                                               .get();
                                   }
                                                              
                                   @Bean
                                   IntegrationFlow spanishHello() {
                                       return flow -> flow
                                               .log(LoggingHandler.Level.INFO, "A spanish Hello")
                                               .handle((p, h) -> "Hola!!!");
                                   }
                               }""");
        });
    }

    @Test
    public void choiceDoesNotHaveOtherwise() {

        String noOtherwise = """
                <?xml version="1.0" encoding="UTF-8"?>
                                
                <mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                        <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9081" doc:name="HTTP Listener Configuration"/>
                    <flow name="choiceFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/choice" doc:name="HTTP"/>
                        <expression-filter expression="#[message.inboundProperties.'http.request.uri' != '/favicon.ico']" doc:name="Expression"/>
                        <set-variable variableName="language" value="#[message.inboundProperties.'http.query.params'.language]" doc:name="Set Language Variable"/>
                        <choice doc:name="Choice">
                            <when expression="#[flowVars.language == 'Spanish']">
                                <flow-ref name="spanishHello" doc:name="Flow Reference"></flow-ref>
                            </when>
                            <when expression="#[flowVars.language == 'French']">
                                <set-payload doc:name="Reply in French" value="Bonjour!"/>
                            </when>
                        </choice>
                        <logger message="#[payload]" level="INFO" doc:name="Logger"/>
                    </flow>
                    <sub-flow name="spanishHello">
                       <logger message="A spanish Hello"
                             level="INFO"
                             doc:name="RequestPayloadReceived" />
                       <set-payload doc:name="Reply in Spanish" value="Hola!!!"/>
                   </sub-flow>
                </mule>
                """;

        addXMLFileToResource(noOtherwise);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                    .isEqualTo("""
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
                                   IntegrationFlow choiceFlow(org.springframework.integration.dsl.IntegrationFlow spanishHello) {
                                       return IntegrationFlows.from(Http.inboundGateway("/choice")).handle((p, h) -> p)
                                               //FIXME: element is not supported for conversion: <expression-filter/>
                                               //FIXME: element is not supported for conversion: <set-variable/>
                                               /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                               .<LinkedMultiValueMap<String, String>, String>route(
                                                       p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                                       m -> m
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'Spanish']*/,
                                                                       sf -> sf.gateway(spanishHello)
                                                               )
                                                               .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[flowVars.language == 'French']*/,
                                                                       sf -> sf.handle((p, h) -> "Bonjour!")
                                                               )
                                               )
                                               .log(LoggingHandler.Level.INFO, "${payload}")
                                               .get();
                                   }
                                                          
                                   @Bean
                                   IntegrationFlow spanishHello() {
                                       return flow -> flow
                                               .log(LoggingHandler.Level.INFO, "A spanish Hello")
                                               .handle((p, h) -> "Hola!!!");
                                   }
                               }""");
        });
    }

    @Test
    @Disabled("Placeholder test, enable this test and add assertion when the feature is ready")
    public void nestedChoiceDoesNotError() {
        addXMLFileToResource(
                """
                <?xml version="1.0" encoding="UTF-8"?>
                                             
                <mule xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                    xmlns:spring="http://www.springframework.org/schema/beans"\s
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd">
                    <flow name="choicechoiceFlow">
                        <http:listener config-ref="HTTP_Listener_Configuration" path="/chchoiceoice" doc:name="HTTP"/>
                        <expression-filter expression="#[message.inboundProperties.'http.request.uri' != '/favicon.ico']" doc:name="Expression"/>
                        <set-variable variableName="language" value="#[message.inboundProperties.'http.query.params'.language]" doc:name="Set Language Variable"/>
                        <set-variable variableName="sayHello" value="#[message.inboundProperties.'http.query.params'.sayHello]" doc:name="Set  Variable"/>
                       \s
                        <choice doc:name="Choice">
                            <when expression="#[flowVars.language == 'Spanish']">
                                <logger message="#[payload] jkhjkhx" level="INFO" doc:name="Logger"/>
                               \s
                                <choice>
                                    <when expression="#[flowVars.sayHello == 'true']">
                                        <set-payload value="Hola!"/>
                                    </when>
                                    <otherwise>
                                        <set-payload value="Adiós"/>
                                    </otherwise>
                                </choice>
                            </when>
                            <when expression="#[flowVars.language == 'French']">
                                <choice>
                                    <when expression="#[flowVars.sayHello == 'true']">
                                        <set-payload doc:name="Reply in French" value="Bonjour!"/>
                                    </when>
                                    <otherwise>
                                        <set-payload doc:name="Reply in French" value="Au revoir"/>
                                    </otherwise>
                                </choice>
                            </when>
                            <otherwise>
                                <set-variable variableName="langugae" value="English" doc:name="Set Language to English"/>
                                <set-payload doc:name="Reply in English" value="Hello"/>
                            </otherwise>
                        </choice>
                    </flow>
                </mule>
                """
        );
        runAction(projectContext1 -> {});
    }

    @Test
    public void otherwiseStatementShouldDoImportsBeansAndDependencies() {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <mule xmlns:json="http://www.mulesoft.org/schema/mule/json" xmlns:db="http://www.mulesoft.org/schema/mule/db"
                   xmlns:dw="http://www.mulesoft.org/schema/mule/ee/dw"
                   xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
                   xmlns:spring="http://www.springframework.org/schema/beans"\s
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd
                http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd
                http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
                http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd
                http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd">
                    <flow name="post:/insert:application/json:cmb-hsbcnet-ss-sa-entitlement-change-request-config">
                        <choice doc:name="Choice">
                            <when expression="#[payload == null || payload.size() == 0]">
                                <logger message="empty details list: change request id #[flowVars.changeRequestId]" level="DEBUG" doc:name="empty details list"/>
                            </when>
                            <otherwise>
                                <logger message="insert details: change request id #[flowVars.changeRequestId]" level="DEBUG" doc:name="insert details"/>
                                <db:insert config-ref="Oracle_Configuration" bulkMode="true" doc:name="Database">
                                    <db:parameterized-query><![CDATA[INSERT INTO ${ORA_SCHEMA}.CHANGE_REQUEST_DETAILS (CHANGE_REQUEST_ID, CR_ATTRIBUTE_ID, SECONDARY_ATTRIBUTE, OLD_VALUE, NEW_VALUE) VALUES (#[flowVars.changeRequestId], #[payload.crAttributeId], #[payload.secondaryAttribute], #[payload.oldValue], #[payload.newValue])]]></db:parameterized-query>
                                </db:insert>
                            </otherwise>
                        </choice>
                    </flow>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction(projectContext1 ->
              assertThat(getGeneratedJavaFile()).isEqualTo(
                           """
                           package com.example.javadsl;
                           import org.springframework.context.annotation.Bean;
                           import org.springframework.context.annotation.Configuration;
                           import org.springframework.http.HttpMethod;
                           import org.springframework.integration.dsl.IntegrationFlow;
                           import org.springframework.integration.dsl.IntegrationFlows;
                           import org.springframework.integration.handler.LoggingHandler;
                           import org.springframework.integration.http.dsl.Http;
                           import org.springframework.jdbc.core.JdbcTemplate;
                           import org.springframework.util.LinkedMultiValueMap;
                                                                                
                           @Configuration
                           public class FlowConfigurations {
                               @Bean
                               IntegrationFlow post__insert_application_json_cmb_hsbcnet_ss_sa_entitlement_change_request_config(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
                                   // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config"
                                   // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config" into this flow
                                   // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref="cmb-hsbcnet-ss-sa-entitlement-change-request-config"
                                   return IntegrationFlows.from(
                                           Http.inboundGateway("/insert").requestMapping(r -> r.methods(HttpMethod.POST)))
                                           /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/
                                           .<LinkedMultiValueMap<String, String>, String>route(
                                                   p -> p.getFirst("dataKey") /*TODO: use apt condition*/,
                                                   m -> m
                                                           .subFlowMapping("dataValue" /*TODO: Translate dataValue to #[payload == null || payload.size() == 0]*/,
                                                                   sf -> sf.log(LoggingHandler.Level.DEBUG, "empty details list: change request id ${flowVars.changeRequestId}")
                                                           )
                                                           .resolutionRequired(false)
                                                           .defaultSubFlowMapping(sf -> sf.log(LoggingHandler.Level.DEBUG, "insert details: change request id ${flowVars.changeRequestId}")
                                                                   // TODO: payload type might not be always LinkedMultiValueMap please change it to appropriate type\s
                                                                   // TODO: mule expression language is not converted to java, do it manually. example: #[payload] etc\s
                                                                   .<LinkedMultiValueMap<String, String>>handle((p, h) -> {
                                                                       jdbcTemplate.update("INSERT INTO ${ORA_SCHEMA}.CHANGE_REQUEST_DETAILS (CHANGE_REQUEST_ID, CR_ATTRIBUTE_ID, SECONDARY_ATTRIBUTE, OLD_VALUE, NEW_VALUE) VALUES (?, ?, ?, ?, ?)",
                                                                               p.getFirst("flowVars.changeRequestId") /* TODO: Translate #[flowVars.changeRequestId] to java expression*/,
                                                                               p.getFirst("payload.crAttributeId") /* TODO: Translate #[payload.crAttributeId] to java expression*/,
                                                                               p.getFirst("payload.secondaryAttribute") /* TODO: Translate #[payload.secondaryAttribute] to java expression*/,
                                                                               p.getFirst("payload.oldValue") /* TODO: Translate #[payload.oldValue] to java expression*/,
                                                                               p.getFirst("payload.newValue") /* TODO: Translate #[payload.newValue] to java expression*/
                                                                       );
                                                                       return p;
                                                                   }))
                                           )
                                           .get();
                               }
                           }"""));
    }
}
