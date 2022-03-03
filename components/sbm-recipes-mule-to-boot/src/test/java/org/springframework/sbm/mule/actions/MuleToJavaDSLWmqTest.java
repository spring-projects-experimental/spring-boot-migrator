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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.SetPropertyTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.OutboundEndpointTranslator;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MuleToJavaDSLWmqTest {
    private final static String muleXml = "<mule xmlns:wmq=\"http://www.mulesoft.org/schema/mule/ee/wmq\" xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/wmq http://www.mulesoft.org/schema/mule/ee/wmq/current/mule-wmq-ee.xsd\">\n" +
            "<http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"9081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "<wmq:connector name=\"WMQ\" hostName=\"localhost\" port=\"1414\" queueManager=\"QM1\" channel=\"Channel1\" username=\"username\" password=\"password\" transportType=\"CLIENT_MQ_TCPIP\" targetClient=\"JMS_COMPLIANT\" validateConnections=\"true\" doc:name=\"WMQ\"/>\n" +
            "<flow name=\"http-flow\">\n" +
            "<http:listener doc:name=\"Listener\"  config-ref=\"HTTP_Listener_Configuration\" path=\"/test\"/>\n" +
            "<wmq:outbound-endpoint queue=\"Q1\" targetClient=\"JMS_COMPLIANT\" connector-ref=\"WMQ\" doc:name=\"WMQ\"/>\n" +
            "</flow>\n" +
            "</mule>";

    private JavaDSLAction2 myAction;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new HttpListenerTranslator(),
                new OutboundEndpointTranslator()
        );
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(
                new FlowTopLevelElementFactory(translators),
                new SubflowTopLevelElementFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(new AmqpConfigTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction.setEventPublisher(eventPublisher);
    }

    @Test
    public void shouldGenerateSetPropertyStatements() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-set-property-flow.xml", muleXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework:spring-context:5.3.1",
                        "org.springframework:spring-beans:5.3.1",
                        "org.springframework.integration:spring-integration-core:5.5.8",
                        "org.springframework.integration:spring-integration-http:5.5.8",
                        "org.springframework.integration:spring-integration-jms:5.5.8",
                        "org.springframework.boot:spring-boot-starter-integration:2.6.3",
                        "com.ibm.mq:mq-jms-spring-boot-starter:2.6.4"
                )
                .addRegistrar(registrar)
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.http.dsl.Http;\n"+
                                "import org.springframework.integration.jms.dsl.Jms;\n" +
                                "\n" +
                                "import javax.jms.ConnectionFactory;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow http_flow(ConnectionFactory connectionFactory) {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/test\")).handle((p, h) -> p)\n" +
                                "                .handle(Jms.outboundAdapter(connectionFactory).destination(\"Q1\"))\n" +
                                "                .get();\n" +
                                "    }}");
    }
}
