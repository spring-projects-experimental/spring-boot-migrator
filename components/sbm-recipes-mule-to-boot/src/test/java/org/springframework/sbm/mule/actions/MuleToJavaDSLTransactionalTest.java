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

public class MuleToJavaDSLTransactionalTest extends JavaDSLActionBaseTest {

    @Test
    public void transactionalComponentTest() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "      xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"example\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/transactional\" doc:name=\"HTTP\"/>\n" +
                "        <transactional>\n" +
                "            <foreach collection=\"#[['apple', 'banana', 'orange']]\">\n" +
                "                <logger message=\"#[payload]\" level=\"INFO\" />\n" +
                "            </foreach>\n" +
                "            <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "        </transactional>\n" +
                "    </flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(getGeneratedJavaFile()).isEqualTo("package com.example.javadsl;\n" +
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
                "    IntegrationFlow example(org.springframework.integration.dsl.IntegrationFlow exampleTransactional_1) {\n" +
                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/transactional\")).handle((p, h) -> p)\n" +
                "                .gateway(exampleTransactional_1, e -> e.transactional(true))\n" +
                "                .get();\n" +
                "    }\n" +
                "\n" +
                "    @Bean\n" +
                "    IntegrationFlow exampleTransactional_1() {\n" +
                "        return flow -> flow\n" +
                "                //TODO: translate expression #[['apple', 'banana', 'orange']] which must produces an array\n" +
                "                // to iterate over\n" +
                "                .split()\n" +
                "                .log(LoggingHandler.Level.INFO, \"${payload}\")\n" +
                "                .aggregate()\n" +
                "                .log(LoggingHandler.Level.INFO, \"Done with for looping\");\n" +
                "    }}");
    }

    @Test
    public void transactionalChildNodeUsesDWLTransformation() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "      xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"example\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/transactional\" doc:name=\"HTTP\"/>\n" +
                "        <transactional>\n" +
                "            <foreach collection=\"#[['apple', 'banana', 'orange']]\">\n" +
                "                <logger message=\"#[payload]\" level=\"INFO\" />\n" +
                "                <dw:transform-message doc:name=\"action transform\">\n" +
                "                    <dw:set-payload><![CDATA[%dw 1.0\n" +
                "%output application/json\n" +
                "---\n" +
                "{\n" +
                "    action_Code: 10,\n" +
                "    returnCode:  20\n" +
                "}]]></dw:set-payload>\n" +
                "                </dw:transform-message>\n" +
                "            </foreach>\n" +
                "            <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "        </transactional>\n" +
                "    </flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "public class ExampleTransactional_1Transform_1 {\n" +
                                "    /*\n" +
                                "     * TODO:\n" +
                                "     *\n" +
                                "     * Please add necessary transformation for below snippet\n" +
                                "     * [%dw 1.0\n" +
                                "     * %output application/json\n" +
                                "     * ---\n" +
                                "     * {\n" +
                                "     *     action_Code: 10,\n" +
                                "     *     returnCode:  20\n" +
                                "     * }]\n" +
                                "     * */\n" +
                                "    public static ExampleTransactional_1Transform_1 transform(Object payload) {\n" +
                                "\n" +
                                "        return new ExampleTransactional_1Transform_1();\n" +
                                "    }\n" +
                                "}");
    }
}
