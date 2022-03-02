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
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpInboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpOutboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.api.*;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MuleToJavaDSLMultipleTest {
    private final String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "<amqp:connector name=\"amqpConnector\"\n" +
            "  host=\"localhost\"\n" +
            "  port=\"5672\"\n" +
            "  username=\"guest\"\n" +
            "  password=\"guest\"\n" +
            "  doc:name=\"AMQP-0-9 Connector\"\n" +
            "   />\n" +
            "<flow name=\"amqp-muleFlow\">\n" +
            "<amqp:inbound-endpoint \n" +
            "queueName=\"sbm-integration-queue-one\"\n" +
            "connector-ref=\"amqpConnector\"\n" +
            "/>\n" +
            "<!-- <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/test\" allowedMethods=\"POST\" doc:name=\"Recieve HTTP request\"/> -->\n" +
            "<logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        <amqp:outbound-endpoint \n" +
            "        exchangeName=\"sbm-integration-exchange\" \n" +
            "        routingKey=\"sbm-integration-queue-two\"\n" +
            "        responseTimeout=\"10000\"  \n" +
            "        doc:name=\"Send to AMQP queue\"\n" +
            "        />\n" +
            "</flow>\n" +
            "</mule>\n";

    private static final String muleInboundOutboundXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans c\n" +
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

    private JavaDSLAction2 myAction;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new AmqpInboundEndpointTranslator(),
                new AmqpOutboundEndpointTranslator(),
                new LoggingTranslator(new ExpressionLanguageTranslator()
                )
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
    public void detectsMuleXMLFiles() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-simple-amqp-flow.xml", muleXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .addRegistrar(registrar)
                .build();

        List<MuleXml> muleSearch = projectContext.search(new MuleXmlProjectResourceFilter());

        assertThat(projectContext.getProjectResources().list()).hasSize(2);
    }

    @Test
    public void generatesAmqpDSLStatements() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-simple-amqp-flow.xml", muleInboundOutboundXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .addRegistrar(registrar)
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
                                "import org.springframework.amqp.rabbit.core.RabbitTemplate;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.amqp.dsl.Amqp;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.handler.LoggingHandler;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow amqp_muleFlow(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {\n" +
                                "        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"sbm-integration-queue-one\"))\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .handle(Amqp.outboundAdapter(rabbitTemplate).exchangeName(\"sbm-integration-exchange\").routingKey(\"sbm-integration-queue-two\"))\n" +
                                "                .get();\n" +
                                "    }}");
    }

    @Test
    public void generatesAMQPConnectorBean() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-simple-amqp-flow.xml", muleInboundOutboundXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .addRegistrar(registrar)
                .build();


        myAction.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        List<SpringBootApplicationProperties> springBootApplicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
        assertThat(springBootApplicationProperties).hasSize(1);

        SpringBootApplicationProperties properties = springBootApplicationProperties.get(0);


        String applicationPropertiesContent = properties.print();
        assertThat(applicationPropertiesContent).isEqualTo(
                "spring.rabbitmq.host=localhost\n" +
                "spring.rabbitmq.port=5672"
        );

        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "package com.example.javadsl;\n" +
                  "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
                  "import org.springframework.amqp.rabbit.core.RabbitTemplate;\n" +
                  "import org.springframework.context.annotation.Bean;\n" +
                  "import org.springframework.context.annotation.Configuration;\n" +
                  "import org.springframework.integration.amqp.dsl.Amqp;\n" +
                  "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                  "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                  "import org.springframework.integration.handler.LoggingHandler;\n" +
                  "\n" +
                  "@Configuration\n" +
                  "public class FlowConfigurations {\n" +
                  "    @Bean\n" +
                  "    IntegrationFlow amqp_muleFlow(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate) {\n" +
                  "        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"sbm-integration-queue-one\"))\n" +
                  "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" + // FIXME: replace Mule EL with Spring EL
                  "                .handle(Amqp.outboundAdapter(rabbitTemplate).exchangeName(\"sbm-integration-exchange\").routingKey(\"sbm-integration-queue-two\"))\n" +
                  "                .get();\n" +
                  "    }}"
        );
    }
}
