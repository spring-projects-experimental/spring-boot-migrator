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
package org.springframework.sbm.mule.actions.javadsl.translators.http;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.core.MuleType;
import org.mulesoft.schema.mule.http.ListenerType;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HttpListenerTranslatorTest {

    @Test
    void httpTranslator() {
        // no attributes
        String httpMule = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "\txmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\txsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
                "\t<http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"8081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
                "\t<flow name=\"http-flow\">\n" +
                "\t\t<http:listener doc:name=\"Listener\" doc:id=\"9f602d5c-5386-4fc9-ac8f-024d754c17e5\" config-ref=\"HTTP_Listener_Configuration\" path=\"/test\"/>\n" +
                "\t\t<logger level=\"INFO\" doc:name=\"Logger\" doc:id=\"4585ec7f-2d4a-4d86-af24-b678d4a99227\" />\n" +
                "\t</flow>\n" +
                "</mule>\n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/mule/http-mule.xml", httpMule)
                .addRegistrar(new MuleXmlProjectResourceRegistrar())
                .build();

        DslSnippet snippet = apply(projectContext);
        assertThat(snippet.getRenderedSnippet()).isEqualTo("return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)");
    }


    private DslSnippet apply(ProjectContext projectContext) {
        HttpListenerTranslator sut = new HttpListenerTranslator();
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());

        MuleType muleType = muleXmls.get(0).getMuleType();
        ListenerType listenerType = ((ListenerType) ((JAXBElement) ((FlowType) ((JAXBElement) muleType.getBeansOrBeanOrPropertyPlaceholder().get(1)).getValue()).getAbstractMessageSource()).getValue());
        return sut.translate(listenerType, new QName(""), new MuleConfigurations(new HashMap<>()), "");
    }
}
