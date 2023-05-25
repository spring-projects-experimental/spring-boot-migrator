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
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexSubflowsTest extends JavaDSLActionBaseTest {

    private static final String subflowWithRabbit = """
            <?xml version="1.0" encoding="UTF-8"?>
            <mule
                xmlns            = "http://www.mulesoft.org/schema/mule/core"
                xmlns:apikit     = "http://www.mulesoft.org/schema/mule/apikit"
                xmlns:http       = "http://www.mulesoft.org/schema/mule/http"
                xmlns:doc        = "http://www.mulesoft.org/schema/mule/documentation"
                xmlns:validation = "http://www.mulesoft.org/schema/mule/validation"
                xmlns:xsi        = "http://www.w3.org/2001/XMLSchema-instance"
                version="EE-3.8.5"
                xsi:schemaLocation="http://www.mulesoft.org/schema/mule/apikit     http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd
                                    http://www.mulesoft.org/schema/mule/http       http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
                                    http://www.mulesoft.org/schema/mule/core       http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                                    http://www.mulesoft.org/schema/mule/validation http://www.mulesoft.org/schema/mule/validation/current/mule-validation.xsd">
                        
                <flow name="hbfr-bil-risk-client-rating-mb05-hub-sys-main">
                        
                   <http:listener path="${http.listener.path}/*"                  doc:name="customerRiskRating_HTTP"
                                  config-ref="hsbcDomainHTTPSSharedListenerConfiguration"
                                  allowedMethods="POST"/>
                        
                   <flow-ref name="set-hbfr-headers-out"                         doc:name="set-hbfr-headers-init" />
                        
                   <message-properties-transformer scope="invocation"             doc:name="setCbeFlowVars">
                       <add-message-property key="interfaceType" value="REST"/>
                   </message-properties-transformer>
                   <logger message="transactionId=&#34;#[flowVars.transactionId]&#34;, extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;, step=&#34;RequestParametersReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;, requesterAppId=&#34;#[flowVars.requesterAppId]&#34;, requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;, requesterUserId=&#34;#[flowVars.requesterUserId]&#34;, httpMethod=&#34;#[message.inboundProperties.'http.method']&#34;, httpScheme=&#34;#[message.inboundProperties.'http.scheme']&#34;, httpHost=&#34;#[message.inboundProperties.'host']&#34;, httpRequestUri=&#34;#[message.inboundProperties.'http.request.uri']&#34;, httpQueryString=&#34;#[message.inboundProperties.'http.query.string']&#34; httpVersion=&#34;#[message.inboundProperties.'http.version']&#34;, contentType=&#34;#[message.inboundProperties.'content-type']&#34;, proxyClientId=&#34;#[message.inboundProperties.'client_id']&#34;"
                         level="INFO"
                         doc:name="RequestParametersReceived"
                         category="${api.name}"/>
                   <apikit:router                                                 doc:name="APIkit Router"
                           config-ref="hbfr-bil-risk-client-rating-mb05-hub-sys-config" />
                        
                   <exception-strategy ref="hbfr-common-http-exception-handler"   doc:name="hbfr-common-http-exception-handler"/>
               </flow>
              
               <flow name="post:/clients/{client_identifier}/risk/rating:application/json:hbfr-bil-risk-client-rating-mb05-hub-sys-config">
                   <flow-ref name="commonLogStartSubFlow"                         doc:name="commonLogStartSubFlow"/>
                        
                   <flow-ref name="transformRequestSysSubFlow"                    doc:name="transformRequestSysSubFlow"/>
                  
                   <flow-ref name="callHubSysSubFlow"                             doc:name="callHubSysSubFlow"/>
              
                   <flow-ref name="transformResponseSysSubFlow"                   doc:name="transformResponseSysSubFlow"/>
                        
                   <!-- Need to handle this as an exception when HUB returns a business error -->
                   <!-- The :400 at the end of the message is the status code set by the exception handler -->
                   <validation:is-true expression="#[hubReturnCode == '00']"
                                       message='{ "hubMessage" : "#[hubMsg]", "hubReturnCode" : "#[hubReturnCode]" }:400'
                                       exceptionClass="com.hsbc.hbfr.exception.BusinessException"/>
                        
                   <flow-ref name="transformSuccessResponseSubFlow"               doc:name="transformSuccessResponseSubFlow"/>
              
                   <flow-ref name="commonLogEndSubFlow"                           doc:name="commonLogEndSubFlow"/>
                        
                   <flow-ref name="set-hbfr-headers-out"                          doc:name="set-hbfr-headers-out"/>
                  
                   <exception-strategy ref="hbfr-custom-exception-handler" doc:name="hbfr-custom-exception-handler" />
               </flow>
                        
               <sub-flow name="callHubSysSubFlow">
                   <flow-ref name="callMQ${maybeTest}"                            doc:name="flowRef_callMQ_maybeTest"/>
               </sub-flow>
                        
               <sub-flow name="commonLogStartSubFlow">
                   <logger message="transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;RequestMessageReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[message] #[message]"
                         level="DEBUG"
                         doc:name="RequestMessageReceived" category="${api.name}" />
                  
                   <logger message="transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;RequestPayloadReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[payload] #[message.payloadAs(java.lang.String)]"
                         level="INFO"
                         doc:name="RequestPayloadReceived" category="${api.name}" />
               </sub-flow>
              
               <sub-flow name="commonLogEndSubFlow">
                   <logger message="transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;ResponsePayloadSent&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34; [payload] #[message.payloadAs(java.lang.String)]"
                         level="INFO"
                         doc:name="ResponsePayloadSent" category="${api.name}" />
                  
                   <logger message="transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;ResponseMessageSent&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[message] #[message]"
                         level="DEBUG"
                         doc:name="ResponseMessageSent" category="${api.name}" />
               </sub-flow>
              
            </mule>
            """;


    @Test
    public void shouldHaveMethodsForSubflows() {
        addXMLFileToResource(subflowWithRabbit);
        runAction(projectContext ->
        assertThat(getGeneratedJavaFile())
                .isEqualTo("""
                           package com.example.javadsl;
                           import org.springframework.context.annotation.Bean;
                           import org.springframework.context.annotation.Configuration;
                           import org.springframework.http.HttpMethod;
                           import org.springframework.integration.dsl.IntegrationFlow;
                           import org.springframework.integration.dsl.IntegrationFlows;
                           import org.springframework.integration.handler.LoggingHandler;
                           import org.springframework.integration.http.dsl.Http;
                                                      
                           @Configuration
                           public class FlowConfigurations {
                               @Bean
                               IntegrationFlow hbfr_bil_risk_client_rating_mb05_hub_sys_main(org.springframework.integration.dsl.IntegrationFlow set_hbfr_headers_out) {
                                   return IntegrationFlows.from(Http.inboundGateway("${http.listener.path}/*")).handle((p, h) -> p)
                                           .gateway(set_hbfr_headers_out)
                                           //FIXME: element is not supported for conversion: <message-properties-transformer/>
                                           .log(LoggingHandler.Level.INFO, "${api.name}", "transactionId=\\"${flowVars.transactionId}\\", extCorrelationId=\\"${flowVars.extCorrelationId}\\", step=\\"RequestParametersReceived\\",functionalId=\\"${flowVars.functionalId}\\", requesterAppId=\\"${flowVars.requesterAppId}\\", requesterAppName=\\"${flowVars.requesterAppName}\\",interfaceType=\\"${flowVars.interfaceType}\\", requesterUserId=\\"${flowVars.requesterUserId}\\", httpMethod=\\"#[message.inboundProperties.'http.method']\\", httpScheme=\\"#[message.inboundProperties.'http.scheme']\\", httpHost=\\"#[message.inboundProperties.'host']\\", httpRequestUri=\\"#[message.inboundProperties.'http.request.uri']\\", httpQueryString=\\"#[message.inboundProperties.'http.query.string']\\" httpVersion=\\"#[message.inboundProperties.'http.version']\\", contentType=\\"#[message.inboundProperties.'content-type']\\", proxyClientId=\\"#[message.inboundProperties.'client_id']\\"")
                                           .get();
                               }
                                                      
                               @Bean
                               IntegrationFlow post__clients__client_identifier__risk_rating_application_json_hbfr_bil_risk_client_rating_mb05_hub_sys_config(org.springframework.integration.dsl.IntegrationFlow commonLogStartSubFlow, org.springframework.integration.dsl.IntegrationFlow transformRequestSysSubFlow, org.springframework.integration.dsl.IntegrationFlow callHubSysSubFlow, org.springframework.integration.dsl.IntegrationFlow transformResponseSysSubFlow, org.springframework.integration.dsl.IntegrationFlow transformSuccessResponseSubFlow, org.springframework.integration.dsl.IntegrationFlow commonLogEndSubFlow, org.springframework.integration.dsl.IntegrationFlow set_hbfr_headers_out) {
                                   // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref="hbfr-bil-risk-client-rating-mb05-hub-sys-config"
                                   // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref="hbfr-bil-risk-client-rating-mb05-hub-sys-config" into this flow
                                   // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref="hbfr-bil-risk-client-rating-mb05-hub-sys-config"
                                   return IntegrationFlows.from(
                                           Http.inboundGateway("/clients/{client_identifier}/risk/rating").requestMapping(r -> r.methods(HttpMethod.POST)))
                                           .gateway(commonLogStartSubFlow)
                                           .gateway(transformRequestSysSubFlow)
                                           .gateway(callHubSysSubFlow)
                                           .gateway(transformResponseSysSubFlow)
                                           .gateway(transformSuccessResponseSubFlow)
                                           .gateway(commonLogEndSubFlow)
                                           .gateway(set_hbfr_headers_out)
                                           .get();
                               }
                                                      
                               @Bean
                               IntegrationFlow callHubSysSubFlow(org.springframework.integration.dsl.IntegrationFlow callMQ) {
                                   return flow -> flow
                                           .gateway(callMQ);
                               }
                                                      
                               @Bean
                               IntegrationFlow commonLogStartSubFlow() {
                                   return flow -> flow
                                           .log(LoggingHandler.Level.DEBUG, "${api.name}", "transactionId=\\"${flowVars.transactionId}\\",extCorrelationId=\\"${flowVars.extCorrelationId}\\",step=\\"RequestMessageReceived\\",functionalId=\\"${flowVars.functionalId}\\",requesterAppId=\\"${flowVars.requesterAppId}\\",requesterAppName=\\"${flowVars.requesterAppName}\\",interfaceType=\\"${flowVars.interfaceType}\\",requesterUserId=\\"${flowVars.requesterUserId}\\",[message] ${message}")
                                           .log(LoggingHandler.Level.INFO, "${api.name}", "transactionId=\\"${flowVars.transactionId}\\",extCorrelationId=\\"${flowVars.extCorrelationId}\\",step=\\"RequestPayloadReceived\\",functionalId=\\"${flowVars.functionalId}\\",requesterAppId=\\"${flowVars.requesterAppId}\\",requesterAppName=\\"${flowVars.requesterAppName}\\",interfaceType=\\"${flowVars.interfaceType}\\",requesterUserId=\\"${flowVars.requesterUserId}\\",[payload] ${message.payloadAs(java.lang.String)}");
                               }
                                                      
                               @Bean
                               IntegrationFlow commonLogEndSubFlow() {
                                   return flow -> flow
                                           .log(LoggingHandler.Level.INFO, "${api.name}", "transactionId=\\"${flowVars.transactionId}\\",extCorrelationId=\\"${flowVars.extCorrelationId}\\",step=\\"ResponsePayloadSent\\",functionalId=\\"${flowVars.functionalId}\\",requesterAppId=\\"${flowVars.requesterAppId}\\",requesterAppName=\\"${flowVars.requesterAppName}\\",interfaceType=\\"${flowVars.interfaceType}\\",requesterUserId=\\"${flowVars.requesterUserId}\\" [payload] ${message.payloadAs(java.lang.String)}")
                                           .log(LoggingHandler.Level.DEBUG, "${api.name}", "transactionId=\\"${flowVars.transactionId}\\",extCorrelationId=\\"${flowVars.extCorrelationId}\\",step=\\"ResponseMessageSent\\",functionalId=\\"${flowVars.functionalId}\\",requesterAppId=\\"${flowVars.requesterAppId}\\",requesterAppName=\\"${flowVars.requesterAppName}\\",interfaceType=\\"${flowVars.interfaceType}\\",requesterUserId=\\"${flowVars.requesterUserId}\\",[message] ${message}");
                               }
                           }"""
                )
        );

    }

    @Test
    public void shouldHandleNonFlowElements() {

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <mule xmlns                             = "http://www.mulesoft.org/schema/mule/core"
                      xmlns:apikit                      = "http://www.mulesoft.org/schema/mule/apikit"
                      xmlns:doc                         = "http://www.mulesoft.org/schema/mule/documentation"
                      xmlns:spring                      = "http://www.springframework.org/schema/beans"
                                
                      xmlns:secure-property-placeholder = "http://www.mulesoft.org/schema/mule/secure-property-placeholder"
                      xmlns:tls                         = "http://www.mulesoft.org/schema/mule/tls"
                                
                      xmlns:xsi                         = "http://www.w3.org/2001/XMLSchema-instance"
                                
                      version="EE-3.8.5"
                      xsi:schemaLocation="http://www.mulesoft.org/schema/mule/apikit                      http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd
                                          http://www.mulesoft.org/schema/mule/secure-property-placeholder http://www.mulesoft.org/schema/mule/secure-property-placeholder/current/mule-secure-property-placeholder.xsd\s
                                          http://www.springframework.org/schema/beans                     http://www.springframework.org/schema/beans/spring-beans-current.xsd
                                          http://www.mulesoft.org/schema/mule/core                        http://www.mulesoft.org/schema/mule/core/current/mule.xsd
                                          http://www.mulesoft.org/schema/mule/tls                         http://www.mulesoft.org/schema/mule/tls/current/mule-tls.xsd">
                   <apikit:config name="hbfr-bil-risk-client-rating-mb05-hub-sys-config"
                                  doc:name="Router"
                                  raml="hbfr-bil-risk-client-rating-mb05-hub-sys.raml"
                                  consoleEnabled="false"/>
                   <tls:context name="tlsContext" doc:name="tlsContext">
                      <tls:trust-store path="${tls.truststore.path}"
                                       password="${tls.truststore.password}"
                                       type="jks"/>
                   </tls:context>
                   <spring:beans>
                      <spring:import resource="classpath:com/hsbc/hbfr/bil/mulestack/_security.xml"/>
                      <spring:import resource="classpath:lib-hbfr-headers.xml"/>
                      <spring:import resource="classpath:lib-hbfr-exceptions.xml"/>
                      <spring:import resource="classpath:nginx.xml"/>
                   </spring:beans>
                   <secure-property-placeholder:config name="Secure_Property_Placeholder_client_risk_rating"
                                                       encryptionAlgorithm="AES"
                                                       key="${mule.unlock}"
                                                       location="classpath:hbfr_bil_risk_client_rating_mb05_hub_sys.properties"
                                                       ignoreUnresolvablePlaceholders="true"/>
                </mule>
                """;

        addXMLFileToResource(xml);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
            assertThat(getGeneratedJavaFile()).isEqualTo("""
                                                                 package com.example.javadsl;
                                                                 import org.springframework.context.annotation.Configuration;
                                                                 @Configuration
                                                                 public class FlowConfigurations {
                                                                     void tlsContext() {
                                                                         //FIXME: element is not supported for conversion: <tls:context/>
                                                                     }
                                                                 }""");
        });
    }
}
