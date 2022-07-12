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

public class MuleToJavaDSLApiKitTest extends JavaDSLActionBaseTest {
    private final static String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<mule xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:apikit=\"http://www.mulesoft.org/schema/mule/apikit\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/apikit http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd\n" +
            "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.1.xsd\">\n" +
            "    <flow name=\"get:/helloworld:helloword-config\">\n" +
            "        <set-payload value=\"{&#xA;&quot;message&quot;: &quot;Hello worldXXX&quot;&#xA;}\" doc:name=\"Set Payload\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    @Test
    public void generatesApiKitDSLStatements() {
        addXMLFileToResource(muleXml);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(getGeneratedJavaFile())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.http.HttpMethod;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.http.dsl.Http;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow get__helloworld_helloword_config() {\n" +
                                "        // FIXME: the base path for Http.inboundGateway must be extracted from http:listener in flow containing apikit:router with config-ref=\"helloword-config\"\n" +
                                "        // FIXME: add all JavaDSL generated components between http:listener and apikit:router with config-ref=\"helloword-config\" into this flow\n" +
                                "        // FIXME: remove the JavaDSL generated method containing apikit:router with config-ref=\"helloword-config\"\n" +
                                "        return IntegrationFlows.from(\n" +
                                "                Http.inboundGateway(\"/helloworld\").requestMapping(r -> r.methods(HttpMethod.GET)))\n" +
                                "                .handle((p, h) -> \"{\\\"message\\\": \\\"Hello worldXXX\\\"}\")\n" +
                                "                .get();\n" +
                                "    }\n" +
                                "}");
    }
}
