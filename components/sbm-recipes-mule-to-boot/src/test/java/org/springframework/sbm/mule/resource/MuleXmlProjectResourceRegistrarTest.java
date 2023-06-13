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
package org.springframework.sbm.mule.resource;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MuleXmlProjectResourceRegistrarTest {

    @Test
    void onProjectContextBuiltEvent() {

        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns:ee=\"http://www.mulesoft.org/schema/mule/ee/core\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/core http://www.mulesoft.org/schema/mule/ee/core/current/mule-ee.xsd\">\n" +
                "    <configuration-properties doc:name=\"Configuration properties\" file=\"mule-artifact.properties\" />\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"d6132ed6-b549-4a5a-ab30-b0006362476b\">\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"${http.port}\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"hello-worldFlow\" doc:id=\"fb709ee4-0263-492c-92e3-ba5bb6287cd7\">\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"b4f5d981-3869-46e4-879b-2ba9e8e1c2b7\" config-ref=\"HTTP_Listener_config\" path=\"/helloWorld\" />\n" +
                "        <logger level=\"INFO\" doc:name=\"Logger\" doc:id=\"d254b372-8ea9-4b59-bb07-04cc2bf715a5\" message=\"#[attributes.requestPath]\" />\n" +
                "        <set-payload value=\"Hello World!\" doc:name=\"Set Payload\" doc:id=\"c04ac342-40dd-48ec-bf80-206d3aaa77e3\" mimeType=\"text/plain\"/>\n" +
                "    </flow>\n" +
                "</mule>\n";


        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/mule/mule-def.xml", xml)
                .addRegistrar(new MuleXmlProjectResourceRegistrar(new RewriteExecutionContext()))
                .build();

        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());
        assertThat(muleXmls.get(0)).isInstanceOf(MuleXml.class);
//        assertThat(muleXmls.get(0))
    }
}