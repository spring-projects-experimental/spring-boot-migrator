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
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "    xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"foreach\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/foreach\" doc:name=\"HTTP\"/>    \n" +
                "        <foreach collection=\"#[['apple', 'banana', 'orange']]\">\n" +
                "            <logger message=\"#[payload]\" level=\"INFO\" />\n" +
                "        </foreach>\n" +
                "        <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "    </flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();
        assertThat(getGeneratedJavaFile()).isEqualTo(
                "package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integr" +
                        "ation.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow foreach() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/foreach\")).handle((p, h) -> p)\n" +
                        "                //TODO: translate expression #[['apple', 'banana', 'orange']] which must produces an array\n" +
                        "                // to iterate over\n" +
                        "                .split()\n" +
                        "                .log(LoggingHandler.Level.INFO, \"${payload}\")\n" +
                        "                .aggregate()\n" +
                        "                .log(LoggingHandler.Level.INFO, \"Done with for looping\")\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}");
    }

    @Test
    public void forEachWithChoice() {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "    xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"foreach\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/foreach\" doc:name=\"HTTP\"/>    \n" +
                "        <foreach collection=\"#[[1, 2, 3, 4]]\">\n" +
                "            <choice doc:name=\"Choice\">\n" +
                "            <when expression=\"#[payload == 1]\">\n" +
                "                <logger level=\"INFO\" message=\"Ondu\"></logger>\n" +
                "            </when>\n" +
                "            <when expression=\"#[payload == 2]\">\n" +
                "                <logger level=\"INFO\" message=\"Eradu\"></logger>\n" +
                "            </when>\n" +
                "            <when expression=\"#[payload == 3]\">\n" +
                "                <logger level=\"INFO\" message=\"Mooru\"></logger>\n" +
                "            </when>\n" +
                "            <otherwise>\n" +
                "                <logger level=\"INFO\" message=\"Moorina mele\"></logger>\n" +
                "            </otherwise>\n" +
                "        </choice>\n" +
                "        </foreach>\n" +
                "        <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "    </flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(getGeneratedJavaFile()).isEqualTo(
                "package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.http.dsl.Http;\n" +
                        "import org.springframework.util.LinkedMultiValueMap;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow foreach() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/foreach\")).handle((p, h) -> p)\n" +
                        "                //TODO: translate expression #[[1, 2, 3, 4]] which must produces an array\n" +
                        "                // to iterate over\n" +
                        "                .split()\n" +
                        "                /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/\n" +
                        "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                        "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                        "                        m -> m\n" +
                        "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 1]*/,\n" +
                        "                                        sf -> sf.log(LoggingHandler.Level.INFO, \"Ondu\")\n" +
                        "                                )\n" +
                        "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 2]*/,\n" +
                        "                                        sf -> sf.log(LoggingHandler.Level.INFO, \"Eradu\")\n" +
                        "                                )\n" +
                        "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 3]*/,\n" +
                        "                                        sf -> sf.log(LoggingHandler.Level.INFO, \"Mooru\")\n" +
                        "                                )\n" +
                        "                                .resolutionRequired(false)\n" +
                        "                                .defaultSubFlowMapping(sf -> sf.log(LoggingHandler.Level.INFO, \"Moorina mele\"))\n" +
                        "                )\n" +
                        "                .aggregate()\n" +
                        "                .log(LoggingHandler.Level.INFO, \"Done with for looping\")\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}");
    }

    @Test
    public void forEachWithCallToSubflow() {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "    xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"foreach\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/foreach\" doc:name=\"HTTP\"/>    \n" +
                "        <foreach collection=\"#[[1, 2, 3, 4]]\">\n" +
                "            <choice doc:name=\"Choice\">\n" +
                "            <when expression=\"#[payload == 1]\">\n" +
                "                <flow-ref name=\"logOneInKannada\"></flow-ref>\n" +
                "            </when>\n" +
                "            <when expression=\"#[payload == 2]\">\n" +
                "                <logger level=\"INFO\" message=\"Eradu\"></logger>\n" +
                "            </when>\n" +
                "            <when expression=\"#[payload == 3]\">\n" +
                "                <logger level=\"INFO\" message=\"Mooru\"></logger>\n" +
                "            </when>\n" +
                "            <otherwise>\n" +
                "                <logger level=\"INFO\" message=\"Moorina mele\"></logger>\n" +
                "            </otherwise>\n" +
                "        </choice>\n" +
                "        </foreach>\n" +
                "        <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "    </flow>\n" +
                "    \n" +
                "    <sub-flow name=\"logOneInKannada\">\n" +
                "       <logger message=\"Ondu\" level=\"INFO\" doc:name=\"loggerOrdinal\"/>\n" +
                "   </sub-flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(getGeneratedJavaFile()).isEqualTo(
                "package com.example.javadsl;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                "import org.springframework.integration.handler.LoggingHandler;\n" +
                "import org.springframework.integration.http.dsl.Http;\n" +
                "import org.springframework.util.LinkedMultiValueMap;\n" +
                "\n" +
                "@Configuration\n" +
                "public class FlowConfigurations {\n" +
                "    @Bean\n" +
                "    IntegrationFlow foreach(org.springframework.integration.dsl.IntegrationFlow logOneInKannada) {\n" +
                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/foreach\")).handle((p, h) -> p)\n" +
                "                //TODO: translate expression #[[1, 2, 3, 4]] which must produces an array\n" +
                "                // to iterate over\n" +
                "                .split()\n" +
                "                /* TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/\n" +
                "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                "                        m -> m\n" +
                "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 1]*/,\n" +
                "                                        sf -> sf.gateway(logOneInKannada)\n" +
                "                                )\n" +
                "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 2]*/,\n" +
                "                                        sf -> sf.log(LoggingHandler.Level.INFO, \"Eradu\")\n" +
                "                                )\n" +
                "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to #[payload == 3]*/,\n" +
                "                                        sf -> sf.log(LoggingHandler.Level.INFO, \"Mooru\")\n" +
                "                                )\n" +
                "                                .resolutionRequired(false)\n" +
                "                                .defaultSubFlowMapping(sf -> sf.log(LoggingHandler.Level.INFO, \"Moorina mele\"))\n" +
                "                )\n" +
                "                .aggregate()\n" +
                "                .log(LoggingHandler.Level.INFO, \"Done with for looping\")\n" +
                "                .get();\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    IntegrationFlow logOneInKannada() {\n" +
                "        return flow -> flow\n" +
                "                .log(LoggingHandler.Level.INFO, \"Ondu\");\n" +
                "    }\n" +
                "}");
    }
}
