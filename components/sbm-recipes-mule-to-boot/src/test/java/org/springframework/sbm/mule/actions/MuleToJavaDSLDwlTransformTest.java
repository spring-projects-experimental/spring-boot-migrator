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

    // workaround to force-enable the TriggerMesh transform mode
    private void enableTriggerMeshTransform() {
        myAction.setMuleTriggerMeshTransformEnabled(true);
        System.setProperty("sbm.muleTriggerMeshTransformEnabled", "true");
    }

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

    @Test
    public void shouldTranslateDwlTransformationWithSetPayload() {
        addXMLFileToResource(muleXmlSetPayload);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(getGeneratedJavaFile())
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
                                "                .transform(DwlFlowTransform_2::transform)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }\n" +
                                "}");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "\n" +
                                "public class DwlFlowTransform_2 {\n" +
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
                                "    public static DwlFlowTransform_2 transform(Object payload) {\n" +
                                "\n" +
                                "        return new DwlFlowTransform_2();\n" +
                                "    }\n" +
                                "}");
    }

    @Test
    public void shouldTranslateDwlTransformationWithMuleTriggerMeshTransformAndSetPayloadEnabled() {
        enableTriggerMeshTransform();
        addXMLFileToResource(muleXmlSetPayload);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(3);
        assertThat(getGeneratedJavaFile())
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
                                "                .handle((p, h) -> {\n" +
                                "                    TmDwPayload dwPayload = new TmDwPayload();\n" +
                                "                    String contentType = \"application/json\";\n" +
                                "                    if (h.get(\"contentType\") != null) {\n" +
                                "                        contentType = h.get(\"contentType\").toString();\n" +
                                "                    }\n" +
                                "                    dwPayload.setId(h.getId().toString());\n" +
                                "                    dwPayload.setSourceType(contentType);\n" +
                                "                    dwPayload.setSource(h.get(\"http_requestUrl\").toString());\n" +
                                "                    dwPayload.setPayload(p.toString());\n" +
                                "                    return dwPayload;\n" +
                                "                })\n" +
                                "                .transform(DwlFlowTransformTM_2::transform)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }\n" +
                                "}");
        assertThat(projectContext.getProjectJavaSources().list().get(1).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "\n" +
                                "import lombok.Data;\n" +
                                "\n" +
                                "/* Included with the baseline to support bridging between the Flow configuration and the translation implementation. */\n" +
                                "\n" +
                                "@Data\n" +
                                "public class TmDwPayload {\n" +
                                "    private String id;\n" +
                                "    private String source;\n" +
                                "    private String sourceType;\n" +
                                "    private String payload;\n" +
                                "}\n"
                );
        assertThat(projectContext.getProjectJavaSources().list().get(2).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "\n" +
                        "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                        "\n" +
                        "import java.net.URI;\n" +
                        "import java.net.http.HttpClient;\n" +
                        "import java.net.http.HttpRequest;\n" +
                        "import java.net.http.HttpResponse;\n" +
                        "\n" +
                        "public class DwlFlowTransformTM_2 {\n" +
                        "    public static class DataWeavePayload {\n" +
                        "        public String input_data;\n" +
                        "        public String spell;\n" +
                        "        public String input_content_type;\n" +
                        "        public String output_content_type;\n" +
                        "    };\n" +
                        "\n" +
                        "    public static String transform(TmDwPayload payload) {\n" +
                        "        String uuid = payload.getId();\n" +
                        "        String url = System.getenv(\"K_SINK\");\n" +
                        "        HttpClient client = HttpClient.newHttpClient();\n" +
                        "        HttpRequest.Builder requestBuilder;\n" +
                        "        DataWeavePayload dwPayload = new DataWeavePayload();\n" +
                        "\n" +
                        "        if (payload.getSourceType().contains(\";\")) {\n" +
                        "            dwPayload.input_content_type = payload.getSourceType().split(\";\")[0];\n" +
                        "        } else {\n" +
                        "            dwPayload.input_content_type = payload.getSourceType();\n" +
                        "        }\n" +
                        "        dwPayload.output_content_type = \"application/json\";\n" +
                        "\n" +
                        "        //TODO: Verify the spell conforms to Dataweave 2.x: https://docs.mulesoft.com/mule-runtime/4.4/migration-dataweave\n" +
                        "        dwPayload.spell = \"%dw 1.0\\n%output application/json\\n---\\n{\\n    action_Code: 10,\\n    returnCode:  20\\n}\";\n" +
                        "        dwPayload.input_data = payload.getPayload();\n" +
                        "        String body;\n" +
                        "\n" +
                        "        try {\n" +
                        "            requestBuilder = HttpRequest.newBuilder(new URI(url));\n" +
                        "            ObjectMapper om = new ObjectMapper();\n" +
                        "            body = om.writeValueAsString(dwPayload);\n" +
                        "        } catch (Exception e) {\n" +
                        "            System.out.println(\"Error sending request: \" + e.toString());\n" +
                        "            return null;\n" +
                        "        }\n" +
                        "\n" +
                        "        requestBuilder.setHeader(\"content-type\", \"application/json\");\n" +
                        "        requestBuilder.setHeader(\"ce-specversion\", \"1.0\");\n" +
                        "        requestBuilder.setHeader(\"ce-source\", payload.getSource());\n" +
                        "        requestBuilder.setHeader(\"ce-type\", \"io.triggermesh.dataweave.transform\");\n" +
                        "        requestBuilder.setHeader(\"ce-id\", payload.getId());\n" +
                        "\n" +
                        "        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();\n" +
                        "\n" +
                        "        try {\n" +
                        "            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());\n" +
                        "            // TODO: verify the response status and body\n" +
                        "            return response.body();\n" +
                        "        } catch (Exception e) {\n" +
                        "            System.out.println(\"Error sending event: \" + e.toString());\n" +
                        "            return null;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n");
    }

    @Test
    public void shouldTransformDWLWithFileWithSetPayload() {
        final String dwlXMLWithExternalFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
        addXMLFileToResource(dwlXMLWithExternalFile);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(2);
        assertThat(getGeneratedJavaFile())
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
                                "    }\n" +
                                "}");
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
        assertThat(getGeneratedJavaFile())
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
                                "    }\n" +
                                "}");
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
        assertThat(getGeneratedJavaFile())
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
                                "    }\n" +
                                "}");
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

    @Test
    public void multipleDWLTransformInSameFlowShouldProduceMultipleClasses() {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>    " +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"    " +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"    " +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd    " +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd    " +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd    " +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">    " +
                "    <flow name=\"multipleTransforms\">    " +
                "        <dw:transform-message doc:name=\"Transform Message\">    " +
                "            <dw:set-payload><![CDATA[%dw 1.0    " +
                "%output application/json indent = true, skipNullOn = \"everywhere\"    " +
                "---    " +
                "{    " +
                "    \"hello\": {    " +
                "        \"world\": {    " +
                "            \"hello\": \"indeed!\",    " +
                "        },    " +
                "    }    " +
                "}]]></dw:set-payload>    " +
                "        </dw:transform-message>    " +
                "        <logger />    " +
                "        <dw:transform-message doc:name=\"Build Response Message\">    " +
                "            <dw:set-payload><![CDATA[%dw 1.0    " +
                "%output application/json indent = true, skipNullOn = \"everywhere\"    " +
                "---    " +
                "{    " +
                "    \"responseBody\": {    " +
                "        \"responseInfo\": {    " +
                "            \"responseStatus\": \"200\"    " +
                "        },    " +
                "    }    " +
                "}]]></dw:set-payload>    " +
                "        </dw:transform-message>    " +
                "    </flow>    " +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(3);
        assertThat(projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.FlowConfigurations");
        assertThat(projectContext.getProjectJavaSources().list().get(1).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransform_2");
        assertThat(projectContext.getProjectJavaSources().list().get(2).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransform_0");
    }

    @Test
    public void multipleDWLTransformInSameFlowShouldProduceMultipleClassesWithTriggerMeshEnabled() {
        enableTriggerMeshTransform();

        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>    " +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"    " +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"    " +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd    " +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd    " +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd    " +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">    " +
                "    <flow name=\"multipleTransforms\">    " +
                "        <dw:transform-message doc:name=\"Transform Message\">    " +
                "            <dw:set-payload><![CDATA[%dw 1.0    " +
                "%output application/json indent = true, skipNullOn = \"everywhere\"    " +
                "---    " +
                "{    " +
                "    \"hello\": {    " +
                "        \"world\": {    " +
                "            \"hello\": \"indeed!\",    " +
                "        },    " +
                "    }    " +
                "}]]></dw:set-payload>    " +
                "        </dw:transform-message>    " +
                "        <logger />    " +
                "        <dw:transform-message doc:name=\"Build Response Message\">    " +
                "            <dw:set-payload><![CDATA[%dw 1.0    " +
                "%output application/json indent = true, skipNullOn = \"everywhere\"    " +
                "---    " +
                "{    " +
                "    \"responseBody\": {    " +
                "        \"responseInfo\": {    " +
                "            \"responseStatus\": \"200\"    " +
                "        },    " +
                "    }    " +
                "}]]></dw:set-payload>    " +
                "        </dw:transform-message>    " +
                "    </flow>    " +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(4);
        assertThat(projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.FlowConfigurations");
        assertThat(projectContext.getProjectJavaSources().list().get(1).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.TmDwPayload");
        assertThat(projectContext.getProjectJavaSources().list().get(2).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransformTM_2");
        assertThat(projectContext.getProjectJavaSources().list().get(3).getTypes().get(0).toString()).isEqualTo("com.example.javadsl.MultipleTransformsTransformTM_0");
    }
}
