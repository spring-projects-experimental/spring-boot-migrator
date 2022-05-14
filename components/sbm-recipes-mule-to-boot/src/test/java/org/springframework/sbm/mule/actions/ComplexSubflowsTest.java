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

    private static final String subflowWithRabbit = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<mule \n" +
            "    xmlns            = \"http://www.mulesoft.org/schema/mule/core\" \n" +
            "    xmlns:apikit     = \"http://www.mulesoft.org/schema/mule/apikit\"\n" +
            "    xmlns:http       = \"http://www.mulesoft.org/schema/mule/http\"\n" +
            "    xmlns:doc        = \"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:validation = \"http://www.mulesoft.org/schema/mule/validation\"\n" +
            "    xmlns:xsi        = \"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\n" +
            "    version=\"EE-3.8.5\"\n" +
            "\n" +
            "    xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/apikit     http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd \n" +
            "                        http://www.mulesoft.org/schema/mule/http       http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd \n" +
            "                        http://www.mulesoft.org/schema/mule/core       http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "                        http://www.mulesoft.org/schema/mule/validation http://www.mulesoft.org/schema/mule/validation/current/mule-validation.xsd\">\n" +
            "\n" +
            "    <flow name=\"hbfr-bil-risk-client-rating-mb05-hub-sys-main\">\n" +
            "\n" +
            "       <http:listener path=\"${http.listener.path}/*\"                  doc:name=\"customerRiskRating_HTTP\"\n" +
            "                      config-ref=\"hsbcDomainHTTPSSharedListenerConfiguration\"\n" +
            "                      allowedMethods=\"POST\"/>\n" +
            "\n" +
            "       <flow-ref name=\"set-hbfr-headers-out\"                         doc:name=\"set-hbfr-headers-init\" />\n" +
            "\n" +
            "       <message-properties-transformer scope=\"invocation\"             doc:name=\"setCbeFlowVars\">\n" +
            "           <add-message-property key=\"interfaceType\" value=\"REST\"/>\n" +
            "       </message-properties-transformer>\n" +
            "       <logger message=\"transactionId=&#34;#[flowVars.transactionId]&#34;, extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;, step=&#34;RequestParametersReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;, requesterAppId=&#34;#[flowVars.requesterAppId]&#34;, requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;, requesterUserId=&#34;#[flowVars.requesterUserId]&#34;, httpMethod=&#34;#[message.inboundProperties.'http.method']&#34;, httpScheme=&#34;#[message.inboundProperties.'http.scheme']&#34;, httpHost=&#34;#[message.inboundProperties.'host']&#34;, httpRequestUri=&#34;#[message.inboundProperties.'http.request.uri']&#34;, httpQueryString=&#34;#[message.inboundProperties.'http.query.string']&#34; httpVersion=&#34;#[message.inboundProperties.'http.version']&#34;, contentType=&#34;#[message.inboundProperties.'content-type']&#34;, proxyClientId=&#34;#[message.inboundProperties.'client_id']&#34;\"\n" +
            "             level=\"INFO\"\n" +
            "             doc:name=\"RequestParametersReceived\"\n" +
            "             category=\"${api.name}\"/>\n" +
            "       <apikit:router                                                 doc:name=\"APIkit Router\"\n" +
            "               config-ref=\"hbfr-bil-risk-client-rating-mb05-hub-sys-config\" />\n" +
            "\n" +
            "       <exception-strategy ref=\"hbfr-common-http-exception-handler\"   doc:name=\"hbfr-common-http-exception-handler\"/>\n" +
            "   </flow>\n" +
            "   \n" +
            "   <flow name=\"post:/clients/{client_identifier}/risk/rating:application/json:hbfr-bil-risk-client-rating-mb05-hub-sys-config\">\n" +
            "       <flow-ref name=\"commonLogStartSubFlow\"                         doc:name=\"commonLogStartSubFlow\"/>\n" +
            "\n" +
            "       <flow-ref name=\"transformRequestSysSubFlow\"                    doc:name=\"transformRequestSysSubFlow\"/>\n" +
            "       \n" +
            "       <flow-ref name=\"callHubSysSubFlow\"                             doc:name=\"callHubSysSubFlow\"/>\n" +
            "   \n" +
            "       <flow-ref name=\"transformResponseSysSubFlow\"                   doc:name=\"transformResponseSysSubFlow\"/>\n" +
            "\n" +
            "       <!-- Need to handle this as an exception when HUB returns a business error -->\n" +
            "       <!-- The :400 at the end of the message is the status code set by the exception handler -->\n" +
            "       <validation:is-true expression=\"#[hubReturnCode == '00']\"\n" +
            "                           message='{ \"hubMessage\" : \"#[hubMsg]\", \"hubReturnCode\" : \"#[hubReturnCode]\" }:400'\n" +
            "                           exceptionClass=\"com.hsbc.hbfr.exception.BusinessException\"/>\n" +
            "\n" +
            "       <flow-ref name=\"transformSuccessResponseSubFlow\"               doc:name=\"transformSuccessResponseSubFlow\"/>\n" +
            "   \n" +
            "       <flow-ref name=\"commonLogEndSubFlow\"                           doc:name=\"commonLogEndSubFlow\"/>\n" +
            "\n" +
            "       <flow-ref name=\"set-hbfr-headers-out\"                          doc:name=\"set-hbfr-headers-out\"/>\n" +
            "       \n" +
            "       <exception-strategy ref=\"hbfr-custom-exception-handler\" doc:name=\"hbfr-custom-exception-handler\" />\n" +
            "   </flow>\n" +
            "\n" +
            "   <sub-flow name=\"callHubSysSubFlow\">\n" +
            "       <flow-ref name=\"callMQ${maybeTest}\"                            doc:name=\"flowRef_callMQ_maybeTest\"/>\n" +
            "   </sub-flow>\n" +
            "\n" +
            "   <sub-flow name=\"commonLogStartSubFlow\">\n" +
            "       <logger message=\"transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;RequestMessageReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[message] #[message]\"\n" +
            "             level=\"DEBUG\"\n" +
            "             doc:name=\"RequestMessageReceived\" category=\"${api.name}\" />\n" +
            "       \n" +
            "       <logger message=\"transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;RequestPayloadReceived&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[payload] #[message.payloadAs(java.lang.String)]\"\n" +
            "             level=\"INFO\"\n" +
            "             doc:name=\"RequestPayloadReceived\" category=\"${api.name}\" />\n" +
            "   </sub-flow>\n" +
            "   \n" +
            "   <sub-flow name=\"commonLogEndSubFlow\">\n" +
            "       <logger message=\"transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;ResponsePayloadSent&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34; [payload] #[message.payloadAs(java.lang.String)]\"\n" +
            "             level=\"INFO\"\n" +
            "             doc:name=\"ResponsePayloadSent\" category=\"${api.name}\" />\n" +
            "       \n" +
            "       <logger message=\"transactionId=&#34;#[flowVars.transactionId]&#34;,extCorrelationId=&#34;#[flowVars.extCorrelationId]&#34;,step=&#34;ResponseMessageSent&#34;,functionalId=&#34;#[flowVars.functionalId]&#34;,requesterAppId=&#34;#[flowVars.requesterAppId]&#34;,requesterAppName=&#34;#[flowVars.requesterAppName]&#34;,interfaceType=&#34;#[flowVars.interfaceType]&#34;,requesterUserId=&#34;#[flowVars.requesterUserId]&#34;,[message] #[message]\"\n" +
            "             level=\"DEBUG\"\n" +
            "             doc:name=\"ResponseMessageSent\" category=\"${api.name}\" />\n" +
            "   </sub-flow>\n" +
            "   \n" +
            "</mule>\n";



    @Test
    public void shouldHaveMethodsForSubflows() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-rabbit.xml", subflowWithRabbit)
                .addRegistrar(registrar)
                .withSbmApplicationProperties(sbmApplicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.http.HttpMethod;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow hbfr_bil_risk_client_rating_mb05_hub_sys_main(org.springframework.integration.dsl.IntegrationFlow set_hbfr_headers_out) {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"${http.listener.path}/*\")).handle((p, h) -> p)\n" +
                        "                .gateway(set_hbfr_headers_out)\n" +
                        "                //FIXME: element is not supported for conversion: <message-properties-transformer/>\n" +
                        "                .log(LoggingHandler.Level.INFO, \"${api.name}\", \"transactionId=\\\"${flowVars.transactionId}\\\", extCorrelationId=\\\"${flowVars.extCorrelationId}\\\", step=\\\"RequestParametersReceived\\\",functionalId=\\\"${flowVars.functionalId}\\\", requesterAppId=\\\"${flowVars.requesterAppId}\\\", requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\", requesterUserId=\\\"${flowVars.requesterUserId}\\\", httpMethod=\\\"#[message.inboundProperties.'http.method']\\\", httpScheme=\\\"#[message.inboundProperties.'http.scheme']\\\", httpHost=\\\"#[message.inboundProperties.'host']\\\", httpRequestUri=\\\"#[message.inboundProperties.'http.request.uri']\\\", httpQueryString=\\\"#[message.inboundProperties.'http.query.string']\\\" httpVersion=\\\"#[message.inboundProperties.'http.version']\\\", contentType=\\\"#[message.inboundProperties.'content-type']\\\", proxyClientId=\\\"#[message.inboundProperties.'client_id']\\\"\")\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow post__clients__client_identifier__risk_rating_application_json_hbfr_bil_risk_client_rating_mb05_hub_sys_config(org.springframework.integration.dsl.IntegrationFlow commonLogStartSubFlow, org.springframework.integration.dsl.IntegrationFlow transformRequestSysSubFlow, org.springframework.integration.dsl.IntegrationFlow callHubSysSubFlow, org.springframework.integration.dsl.IntegrationFlow transformResponseSysSubFlow, org.springframework.integration.dsl.IntegrationFlow transformSuccessResponseSubFlow, org.springframework.integration.dsl.IntegrationFlow commonLogEndSubFlow, org.springframework.integration.dsl.IntegrationFlow set_hbfr_headers_out) {\n" +
                        "        // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref=\"hbfr-bil-risk-client-rating-mb05-hub-sys-config\"\n" +
                        "        // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref=\"hbfr-bil-risk-client-rating-mb05-hub-sys-config\" into this flow\n" +
                        "        // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref=\"hbfr-bil-risk-client-rating-mb05-hub-sys-config\"\n" +
                        "        return IntegrationFlows.from(\n" +
                        "                Http.inboundGateway(\"/clients/{client_identifier}/risk/rating\").requestMapping(r -> r.methods(HttpMethod.POST)))\n" +
                        "                .gateway(commonLogStartSubFlow)\n" +
                        "                .gateway(transformRequestSysSubFlow)\n" +
                        "                .gateway(callHubSysSubFlow)\n" +
                        "                .gateway(transformResponseSysSubFlow)\n" +
                        "                .gateway(transformSuccessResponseSubFlow)\n" +
                        "                .gateway(commonLogEndSubFlow)\n" +
                        "                .gateway(set_hbfr_headers_out)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow callHubSysSubFlow(org.springframework.integration.dsl.IntegrationFlow callMQ) {\n" +
                        "        return flow -> flow\n" +
                        "                .gateway(callMQ);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow commonLogStartSubFlow() {\n" +
                        "        return flow -> flow\n" +
                        "                .log(LoggingHandler.Level.DEBUG, \"${api.name}\", \"transactionId=\\\"${flowVars.transactionId}\\\",extCorrelationId=\\\"${flowVars.extCorrelationId}\\\",step=\\\"RequestMessageReceived\\\",functionalId=\\\"${flowVars.functionalId}\\\",requesterAppId=\\\"${flowVars.requesterAppId}\\\",requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\",requesterUserId=\\\"${flowVars.requesterUserId}\\\",[message] ${message}\")\n" +
                        "                .log(LoggingHandler.Level.INFO, \"${api.name}\", \"transactionId=\\\"${flowVars.transactionId}\\\",extCorrelationId=\\\"${flowVars.extCorrelationId}\\\",step=\\\"RequestPayloadReceived\\\",functionalId=\\\"${flowVars.functionalId}\\\",requesterAppId=\\\"${flowVars.requesterAppId}\\\",requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\",requesterUserId=\\\"${flowVars.requesterUserId}\\\",[payload] ${message.payloadAs(java.lang.String)}\");\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow commonLogEndSubFlow() {\n" +
                        "        return flow -> flow\n" +
                        "                .log(LoggingHandler.Level.INFO, \"${api.name}\", \"transactionId=\\\"${flowVars.transactionId}\\\",extCorrelationId=\\\"${flowVars.extCorrelationId}\\\",step=\\\"ResponsePayloadSent\\\",functionalId=\\\"${flowVars.functionalId}\\\",requesterAppId=\\\"${flowVars.requesterAppId}\\\",requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\",requesterUserId=\\\"${flowVars.requesterUserId}\\\" [payload] ${message.payloadAs(java.lang.String)}\")\n" +
                        "                .log(LoggingHandler.Level.DEBUG, \"${api.name}\", \"transactionId=\\\"${flowVars.transactionId}\\\",extCorrelationId=\\\"${flowVars.extCorrelationId}\\\",step=\\\"ResponseMessageSent\\\",functionalId=\\\"${flowVars.functionalId}\\\",requesterAppId=\\\"${flowVars.requesterAppId}\\\",requesterAppName=\\\"${flowVars.requesterAppName}\\\",interfaceType=\\\"${flowVars.interfaceType}\\\",requesterUserId=\\\"${flowVars.requesterUserId}\\\",[message] ${message}\");\n" +
                        "    }}"
                );

    }

    @Test
    public void shouldHandleNonFlowElements() {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns                             = \"http://www.mulesoft.org/schema/mule/core\"\n" +
                "      xmlns:apikit                      = \"http://www.mulesoft.org/schema/mule/apikit\"\n" +
                "      xmlns:doc                         = \"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "      xmlns:spring                      = \"http://www.springframework.org/schema/beans\"\n" +
                "\n" +
                "      xmlns:secure-property-placeholder = \"http://www.mulesoft.org/schema/mule/secure-property-placeholder\"\n" +
                "      xmlns:tls                         = \"http://www.mulesoft.org/schema/mule/tls\"\n" +
                "\n" +
                "      xmlns:xsi                         = \"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\n" +
                "      version=\"EE-3.8.5\"\n" +
                "      xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/apikit                      http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd\n" +
                "                          http://www.mulesoft.org/schema/mule/secure-property-placeholder http://www.mulesoft.org/schema/mule/secure-property-placeholder/current/mule-secure-property-placeholder.xsd \n" +
                "                          http://www.springframework.org/schema/beans                     http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "                          http://www.mulesoft.org/schema/mule/core                        http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "                          http://www.mulesoft.org/schema/mule/tls                         http://www.mulesoft.org/schema/mule/tls/current/mule-tls.xsd\">\n" +
                "   <apikit:config name=\"hbfr-bil-risk-client-rating-mb05-hub-sys-config\"\n" +
                "                  doc:name=\"Router\"\n" +
                "                  raml=\"hbfr-bil-risk-client-rating-mb05-hub-sys.raml\"\n" +
                "                  consoleEnabled=\"false\"/>\n" +
                "   <tls:context name=\"tlsContext\" doc:name=\"tlsContext\">\n" +
                "      <tls:trust-store path=\"${tls.truststore.path}\"\n" +
                "                       password=\"${tls.truststore.password}\"\n" +
                "                       type=\"jks\"/>\n" +
                "   </tls:context>\n" +
                "   <spring:beans>\n" +
                "      <spring:import resource=\"classpath:com/hsbc/hbfr/bil/mulestack/_security.xml\"/>\n" +
                "      <spring:import resource=\"classpath:lib-hbfr-headers.xml\"/>\n" +
                "      <spring:import resource=\"classpath:lib-hbfr-exceptions.xml\"/>\n" +
                "      <spring:import resource=\"classpath:nginx.xml\"/>\n" +
                "   </spring:beans>\n" +
                "   <secure-property-placeholder:config name=\"Secure_Property_Placeholder_client_risk_rating\"\n" +
                "                                       encryptionAlgorithm=\"AES\"\n" +
                "                                       key=\"${mule.unlock}\"\n" +
                "                                       location=\"classpath:hbfr_bil_risk_client_rating_mb05_hub_sys.properties\"\n" +
                "                                       ignoreUnresolvablePlaceholders=\"true\"/>\n" +
                "</mule>\n";

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-rabbit.xml", xml)
                .addRegistrar(registrar)
                .withSbmApplicationProperties(sbmApplicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    void tlsContext() {\n" +
                        "        //FIXME: element is not supported for conversion: <tls:context/>\n" +
                        "    }}"
                );

    }
}
