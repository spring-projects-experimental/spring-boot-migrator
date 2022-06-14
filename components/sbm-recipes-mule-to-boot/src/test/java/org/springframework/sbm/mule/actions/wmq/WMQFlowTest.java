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
package org.springframework.sbm.mule.actions.wmq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.JavaDSLAction2;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpInboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpOutboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.FlowRefTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class WMQFlowTest {

    String wmqXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<mule xmlns:wmq=\"http://www.mulesoft.org/schema/mule/ee/wmq\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "   xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "   xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/wmq http://www.mulesoft.org/schema/mule/ee/wmq/current/mule-wmq-ee.xsd\">\n" +
            "   <wmq:connector name=\"WMQ\" hostName=\"localhost\" port=\"1414\" queueManager=\"TestQueueManager\" channel=\"TestChannel\" username=\"guest\" password=\"guet\" transportType=\"CLIENT_MQ_TCPIP\" validateConnections=\"true\" doc:name=\"WMQ\"/>\n" +
            "   <flow name=\"wmqtestFlow\">\n" +
            "      <wmq:inbound-endpoint queue=\"TestQueue\" connector-ref=\"WMQ\" doc:name=\"WMQ\" targetClient=\"JMS_COMPLIANT\"/>\n" +
            "      <logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
            "   </flow>\n" +
            "</mule>";

    private JavaDSLAction2 myAction2;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);


    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators =
                List.of(
                        new HttpListenerTranslator(),
                        new LoggingTranslator(new ExpressionLanguageTranslator()),
                        new FlowRefTranslator(),
                        new AmqpOutboundEndpointTranslator(),
                        new AmqpInboundEndpointTranslator()
                );
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(new FlowTopLevelElementFactory(translators));

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(new AmqpConfigTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction2 = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction2.setEventPublisher(eventPublisher);
    }

    @Test
    public void wmq() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-rabbit.xml", wmqXML)
                .addRegistrar(registrar)
                .withApplicationProperties(sbmApplicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                ).build();
        myAction2.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow wmqtestFlow() {\n" +
                        "        //FIXME: element is not supported for conversion: <wmq:inbound-endpoint/>\n" +
                        "        IntegrationFlows.from(\"\")\n" +
                        "                .log(LoggingHandler.Level.INFO)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}"
                );
    }
}
