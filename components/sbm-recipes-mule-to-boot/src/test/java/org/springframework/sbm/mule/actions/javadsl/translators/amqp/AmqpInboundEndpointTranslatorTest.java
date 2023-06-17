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
package org.springframework.sbm.mule.actions.javadsl.translators.amqp;

import org.junit.jupiter.api.Test;
import org.mulesoft.schema.mule.amqp.InboundEndpointType;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.core.MuleType;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.Bean;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AmqpInboundEndpointTranslatorTest {

    private static String amqpXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "    <amqp:connector name=\"amqpConnector\"\n" +
            "      host=\"localhost\"\n" +
            "      port=\"5672\"\n" +
            "      username=\"guest\"\n" +
            "      password=\"guest\"\n" +
            "      doc:name=\"AMQP-0-9 Connector\"\n" +
            "       />\n" +
            "    <flow name=\"amqp-muleFlow\">\n" +
            "        <amqp:inbound-endpoint \n" +
            "        queueName=\"sbm-integration-queue-one\"\n" +
            "        connector-ref=\"amqpConnector\"\n" +
            "        />\n" +
            "        <!-- <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/test\" allowedMethods=\"POST\" doc:name=\"Recieve HTTP request\"/> -->\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        <amqp:outbound-endpoint \n" +
            "        exchangeName=\"sbm-integration-exchange\" \n" +
            "        routingKey=\"sbm-integration-queue-two\"\n" +
            "        responseTimeout=\"10000\"  \n" +
            "        doc:name=\"Send to AMQP queue\"\n" +
            "        />\n" +
            "    </flow>\n" +
            "</mule>\n";


    @Test
    void amqpTranslatorTest() {

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("src/main/mule/amqp-mule.xml", amqpXML)
                .addRegistrar(new MuleXmlProjectResourceRegistrar(new RewriteExecutionContext()))
                .build();

        DslSnippet snippet = apply(projectContext);
        assertThat(snippet.getBeans()).isNotNull();
        assertThat(snippet.getBeans()).contains(new Bean("connectionFactory", "org.springframework.amqp.rabbit.connection.ConnectionFactory"));
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.amqp.dsl.Amqp");
        assertThat(snippet.getRenderedSnippet()).isEqualTo("return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"sbm-integration-queue-one\"))");
    }


    private DslSnippet apply(ProjectContext projectContext) {
        AmqpInboundEndpointTranslator sut = new AmqpInboundEndpointTranslator();
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());

        MuleType muleType = muleXmls.get(0).getMuleType();
        InboundEndpointType inboundEndpointType = (InboundEndpointType) ((FlowType) ((JAXBElement) muleType.getBeansOrBeanOrPropertyPlaceholder().get(1)).getValue()).getAbstractInboundEndpoint().getValue();

        return sut.translate(1, inboundEndpointType, new QName(""), new MuleConfigurations(new HashMap<>()), "", Map.of());
    }
}
