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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpInboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.amqp.AmqpOutboundEndpointTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.core.FlowRefTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.api.*;
import org.springframework.sbm.mule.api.toplevel.FlowTypeFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTypeFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelTypeFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SubflowsTest {

    private static final String subflowWithRabbit = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
            "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "      xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "    <amqp:connector name=\"amqpConnector\"\n" +
            "                    host=\"localhost\"\n" +
            "                    port=\"5672\"\n" +
            "                    username=\"guest\"\n" +
            "                    password=\"guest\"\n" +
            "                    doc:name=\"AMQP-0-9 Connector\"\n" +
            "    />\n" +
            "    <flow name=\"amqp-muleFlow\">\n" +
            "        <amqp:inbound-endpoint\n" +
            "                queueName=\"sbm-integration-queue-one\"\n" +
            "                connector-ref=\"amqpConnector\"\n" +
            "        />\n" +
            "        <!-- <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/test\" allowedMethods=\"POST\" doc:name=\"Recieve HTTP request\"/> -->\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        <flow-ref name=\"outToAMQP\" />\n" +
            "    </flow>\n" +
            "    <sub-flow name=\"outToAMQP\">\n" +
            "        <amqp:outbound-endpoint\n" +
            "                exchangeName=\"sbm-integration-exchange\"\n" +
            "                routingKey=\"sbm-integration-queue-two\"\n" +
            "                responseTimeout=\"10000\"\n" +
            "                doc:name=\"Send to AMQP queue\"\n" +
            "        />\n" +
            "    </sub-flow>\n" +
            "</mule>\n";

    private static final String subflowUnknown = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
            "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "      xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\">\n" +
            "    <amqp:connector name=\"amqpConnector\"\n" +
            "                    host=\"localhost\"\n" +
            "                    port=\"5672\"\n" +
            "                    username=\"guest\"\n" +
            "                    password=\"guest\"\n" +
            "                    doc:name=\"AMQP-0-9 Connector\"\n" +
            "    />\n" +
            "    <flow name=\"amqp-muleFlow\">\n" +
            "        <amqp:inbound-endpoint\n" +
            "                queueName=\"sbm-integration-queue-one\"\n" +
            "                connector-ref=\"amqpConnector\"\n" +
            "        />\n" +
            "        <!-- <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/test\" allowedMethods=\"POST\" doc:name=\"Recieve HTTP request\"/> -->\n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        <flow-ref name=\"outToUnknown\" />\n" +
            "    </flow>\n" +
            "    <sub-flow name=\"outToUnknown\">\n" +
            "         <set-variable mimeType=\"application/java\" doc:name=\"setVariable_actionCode\"\n" +
            "                      variableName=\"actionCode\" value=\"#[dw('payload.action_Code[0]')]\"  />" +
            "    </sub-flow>\n" +
            "</mule>\n";

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
        List<TopLevelTypeFactory> topLevelTypeFactories = List.of(
                new FlowTypeFactory(translators),
                new SubflowTypeFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(new AmqpConfigTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction2 = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction2.setEventPublisher(eventPublisher);
    }

    @Test
    public void generatedFlowShouldHaveMethodParams() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-rabbit.xml", subflowWithRabbit)
                .addRegistrar(registrar)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .build();
        myAction2.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
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
                        "    IntegrationFlow amqp_muleFlow(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, org.springframework.integration.dsl.IntegrationFlow outToAMQP) {\n" +
                        "        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"sbm-integration-queue-one\"))\n" +
                        "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                        "                .gateway(outToAMQP)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow outToAMQP(org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate) {\n" +
                        "        return flow -> flow\n" +
                        "                .handle(Amqp.outboundAdapter(rabbitTemplate).exchangeName(\"sbm-integration-exchange\").routingKey(\"sbm-integration-queue-two\"));\n" +
                        "    }}"
                );

    }

    @Test
    public void shouldTranslateSubflowWithUnknownElements() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-rabbit.xml", subflowUnknown)
                .addRegistrar(registrar)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .build();
        myAction2.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.amqp.rabbit.connection.ConnectionFactory;\n" +
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
                        "    IntegrationFlow amqp_muleFlow(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, org.springframework.integration.dsl.IntegrationFlow outToUnknown) {\n" +
                        "        return IntegrationFlows.from(Amqp.inboundAdapter(connectionFactory, \"sbm-integration-queue-one\"))\n" +
                        "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                        "                .gateway(outToUnknown)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow outToUnknown() {\n" +
                        "        return flow -> {\n" +
                        "            // FIXME: Conversion is not supported for Mule type: org.mulesoft.schema.mule.core.SetVariableType\n" +
                        "        };\n" +
                        "    }}"
                );

    }
}
