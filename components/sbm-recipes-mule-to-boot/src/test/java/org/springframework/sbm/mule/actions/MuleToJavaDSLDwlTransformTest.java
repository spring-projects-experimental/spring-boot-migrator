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

public class MuleToJavaDSLDwlTransformTest extends JavaDSLActionBaseTest {

    private static final String muleXmlSetPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
            "    <flow name=\"dwlFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
            "    \n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        \n" +
            "        <dw:transform-message doc:name=\"action transform\">\n" +
            "            <dw:set-payload><![CDATA[%dw 1.0\n" +
            "%output application/json\n" +
            "---\n" +
            "{\n" +
            "    action_Code: 10,\n" +
            "    returnCode:  20\n" +
            "}]]></dw:set-payload>\n" +
            "        </dw:transform-message>\n" +
            "        \n" +
            "         <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "    </flow>\n" +
            "</mule>\n";

    private static final String dwlXMLWithExternalFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
            "      xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
            "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "      xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
            "    <flow name=\"dwlFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
            "\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\"\n" +
            "                doc:name=\"Log the message content to be sent\"/>\n" +
            "\n" +
            "        <dw:transform-message doc:name=\"action transform via file\">\n" +
            "            <dw:input-payload mimeType=\"text/plain\">\n" +
            "                <dw:reader-property name=\"schemaPath\" value=\"schemas/MQOutput.ffd\"/>\n" +
            "            </dw:input-payload>\n" +
            "            <dw:set-payload resource=\"classpath:dwl/mapClientRiskRatingResponse.dwl\"/>\n" +
            "        </dw:transform-message>\n" +
            "\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\"\n" +
            "                doc:name=\"Log the message content to be sent\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    @Test
    public void shouldTranslateDwlTransformationWithSetPayload() {
        addXMLFileToResource(muleXmlSetPayload);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
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
                                "    IntegrationFlow dwlFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/dwl\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .transform(DwlFlowTransform::transform)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }}");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "public class DwlFlowTransform {\n" +
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
                                "    public static DwlFlowTransform transform(Object payload) {\n" +
                                "\n" +
                                "        return new DwlFlowTransform();\n" +
                                "    }\n" +
                                "}");
    }

    @Test
    public void shouldTransformDWLWithFileWithSetPayload() {
        addXMLFileToResource(dwlXMLWithExternalFile);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
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
                                "    IntegrationFlow dwlFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/dwl\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .transform(MapClientRiskRatingResponseTransform::transform)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }}");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "public class MapClientRiskRatingResponseTransform {\n" +
                                "    /*\n" +
                                "     * TODO:\n" +
                                "     *\n" +
                                "     * Please add necessary transformation for below snippet\n" +
                                "     * from file dwl/mapClientRiskRatingResponse.dwl" +
                                "     * */\n" +
                                "    public static MapClientRiskRatingResponseTransform transform(Object payload) {\n" +
                                "\n" +
                                "        return new MapClientRiskRatingResponseTransform();\n" +
                                "    }\n" +
                                "}");
    }

    @Test
    public void shouldTranslateDWLTransformationWithOnlyOneSetVariable() {
        String muleXMLSetVariable = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
                "    <flow name=\"dwlFlow\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
                "    \n" +
                "        <dw:transform-message doc:name=\"action transform\">\n" +
                "        <dw:set-variable variableName=\"temp\"><![CDATA[%dw 1.0\n" +
                "%output application/json\n" +
                "---\n" +
                "{\n" +
                "    action_Code: 10,\n" +
                "    returnCode:  20\n" +
                "}]]>\n" +
                "        \n" +
                "        </dw:set-variable>\n" +
                "        </dw:transform-message>\n" +
                "        \n" +
                "         <logger message=\"Hello World:  #[flowVars.temp]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
                "    </flow>\n" +
                "</mule>";
        addXMLFileToResource(muleXMLSetVariable);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
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
                                "    IntegrationFlow dwlFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/dwl\")).handle((p, h) -> p)\n" +
                                "                // FIXME: No support for following DW transformation: <dw:set-property/> <dw:set-session-variable /> <dw:set-variable />\n" +
                                "                .log(LoggingHandler.Level.INFO, \"Hello World:  ${flowVars.temp}\")\n" +
                                "                .get();\n" +
                                "    }}");
    }

    @Test
    public void shouldNotErrorWhenDWLFileHasDash() {
        final String dwlExternalFileSpecialChars = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
                "      xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
                "    <flow name=\"dwlFlow\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
                "\n" +
                "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\"\n" +
                "                doc:name=\"Log the message content to be sent\"/>\n" +
                "\n" +
                "        <dw:transform-message doc:name=\"action transform via file\">\n" +
                "            <dw:input-payload mimeType=\"text/plain\">\n" +
                "                <dw:reader-property name=\"schemaPath\" value=\"schemas/MQOutput.ffd\"/>\n" +
                "            </dw:input-payload>\n" +
                "            <dw:set-payload resource=\"classpath:dwl/map-client-risk-rating-response.dwl\"/>\n" +
                "        </dw:transform-message>\n" +
                "\n" +
                "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\"\n" +
                "                doc:name=\"Log the message content to be sent\"/>\n" +
                "    </flow>\n" +
                "</mule>";
        addXMLFileToResource(dwlExternalFileSpecialChars);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
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
                                "    IntegrationFlow dwlFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/dwl\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .transform(MapclientriskratingresponseTransform::transform)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }}");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "public class MapclientriskratingresponseTransform {\n" +
                                "    /*\n" +
                                "     * TODO:\n" +
                                "     *\n" +
                                "     * Please add necessary transformation for below snippet\n" +
                                "     * from file dwl/map-client-risk-rating-response.dwl     * */\n" +
                                "    public static MapclientriskratingresponseTransform transform(Object payload) {\n" +
                                "\n" +
                                "        return new MapclientriskratingresponseTransform();\n" +
                                "    }\n" +
                                "}");
    }
}
