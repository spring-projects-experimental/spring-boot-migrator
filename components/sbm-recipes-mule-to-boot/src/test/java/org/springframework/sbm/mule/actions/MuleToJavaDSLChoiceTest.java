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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLChoiceTest extends JavaDSLActionBaseTest {
    private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
            "        <http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"9081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "    <flow name=\"choiceFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/choice\" doc:name=\"HTTP\"/>\n" +
            "        <expression-filter expression=\"#[message.inboundProperties.'http.request.uri' != '/favicon.ico']\" doc:name=\"Expression\"/>\n" +
            "        <set-variable variableName=\"language\" value=\"#[message.inboundProperties.'http.query.params'.language]\" doc:name=\"Set Language Variable\"/>\n" +
            "        <choice doc:name=\"Choice\">\n" +
            "            <when expression=\"#[flowVars.language == 'Spanish']\">\n" +
            "                <set-payload doc:name=\"Reply in Spanish\" value=\"Hola!\"/>\n" +
            "            </when>\n" +
            "            <when expression=\"#[flowVars.language == 'French']\">\n" +
            "                <set-payload doc:name=\"Reply in French\" value=\"Bonjour!\"/>\n" +
            "            </when>\n" +
            "            <otherwise>\n" +
            "                <set-payload doc:name=\"Reply in English\" value=\"Hello\"/>\n" +
            "            </otherwise>\n" +
            "        </choice>\n" +
            "        <logger message=\"#[payload]\" level=\"INFO\" doc:name=\"Logger\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    @Test
    public void supportsBasicChoiceElement() {
        addXMLFileToResource(xml);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow choiceFlow() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/choice\")).handle((p, h) -> p)\n" +
                        "                //FIXME: element is not supported for conversion: <expression-filter/>\n" +
                        "                //FIXME: element is not supported for conversion: <set-variable/>\n" +
                        "                /*\n" +
                        "                                * TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/\n" +
                        "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                        "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                        "                        m -> m\n" +
                        "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[flowVars.language == 'Spanish']*/,\n" +
                        "                                        sf -> sf.handle((p, h) -> \"Hola!\")\n" +
                        "                                )\n" +
                        "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[flowVars.language == 'French']*/,\n" +
                        "                                        sf -> sf.handle((p, h) -> \"Bonjour!\")\n" +
                        "                                )\n" +
                        "                                .resolutionRequired(false)\n" +
                        "                                .defaultSubFlowMapping(sf -> sf.handle((p, h) -> \"Hello\"))\n" +
                        "                )\n" +
                        "                .log(LoggingHandler.Level.INFO, \"${payload}\")\n" +
                        "                .get();\n" +
                        "    }}");
    }

    @Test
    @Disabled
    public void whenExpressionCallsSubFlow() {
    }

    @Test
    @Disabled
    public void choiceDoesNotHaveOtherwise() {
    }

    @Test
    @Disabled
    public void choiceInsideAChoiceInsideAChoiceInsideAChoiceChoice() {

    }
}
