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
import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLHttpTest extends JavaDSLActionBaseTest {
    private final static String muleXmlHttp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
            "  xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "  http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
            "  <http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"8081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "  <flow name=\"http-routeFlow\" doc:id=\"ff11723e-78e9-4cc2-b760-2bec156ef0f2\" >\n" +
            "    <http:listener doc:name=\"Listener\" doc:id=\"9f602d5c-5386-4fc9-ac8f-024d754c17e5\" config-ref=\"HTTP_Listener_Configuration\" path=\"/test\"/>\n" +
            "    <logger level=\"INFO\" doc:name=\"Logger\" doc:id=\"4585ec7f-2d4a-4d86-af24-b678d4a99227\" />\n" +
            "  </flow>\n" +
            "</mule>";

    @Test
    @Disabled("FIXME #7")
    public void shouldGenerateJavaDSLForFlowHttpMuleTag() {
        addXMLFileToResource(muleXmlHttp);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
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
                        "    IntegrationFlow http_routeFlow() {\n" +
                        "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                        "                .log(LoggingHandler.Level.INFO)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}");
        List<RewriteSourceFileHolder<? extends SourceFile>> applicationProperty = projectContext
                .getProjectResources()
                .list()
                .stream()
                .filter(r -> r.getSourcePath().toString().contains("application.properties"))
                .collect(Collectors.toList());

        assertThat(applicationProperty).hasSize(1);
        assertThat(applicationProperty.get(0).print()).isEqualTo("server.port=8081");
    }
}
