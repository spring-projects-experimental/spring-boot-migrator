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
import org.openrewrite.SourceFile;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.WmqConnectorTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.wmq.WmqOutboundEndpointTranslator;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MuleToJavaDSLWmqInboundTest {
    private final static String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<!--\n" +
            "  ~ Copyright 2021 - 2022 the original author or authors.\n" +
            "  ~\n" +
            "  ~ Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "  ~ you may not use this file except in compliance with the License.\n" +
            "  ~ You may obtain a copy of the License at\n" +
            "  ~\n" +
            "  ~      https://www.apache.org/licenses/LICENSE-2.0\n" +
            "  ~\n" +
            "  ~ Unless required by applicable law or agreed to in writing, software\n" +
            "  ~ distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "  ~ See the License for the specific language governing permissions and\n" +
            "  ~ limitations under the License.\n" +
            "  -->\n" +
            "\n" +
            "<mule xmlns:wmq=\"http://www.mulesoft.org/schema/mule/ee/wmq\" xmlns:amqp=\"http://www.mulesoft.org/schema/mule/amqp\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/wmq http://www.mulesoft.org/schema/mule/ee/wmq/current/mule-wmq-ee.xsd\">\n" +
            "<http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"9081\" doc:name=\"HTTP Listener Configuration\"/>\n" +
            "<wmq:connector name=\"WMQ\" hostName=\"localhost\" port=\"1414\" queueManager=\"QM1\" channel=\"Channel1\" username=\"username\" password=\"password\" transportType=\"CLIENT_MQ_TCPIP\" targetClient=\"JMS_COMPLIANT\" validateConnections=\"true\" doc:name=\"WMQ\"/>\n" +
            "<flow name=\"http-muleFlow\">\n" +
            "<wmq:inbound-endpoint queue=\"Q2\" doc:name=\"WMQ\" connector-ref=\"WMQ\"/>\n" +
            "<logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
            "</flow>\n" +
            "</mule>";

    private JavaDSLAction2 myAction;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new LoggingTranslator(new ExpressionLanguageTranslator()),
                new WmqOutboundEndpointTranslator()
        );
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(
                new FlowTopLevelElementFactory(translators),
                new SubflowTopLevelElementFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(
                new HttpListenerConfigTypeAdapter(),
                new WmqConnectorTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction.setEventPublisher(eventPublisher);
    }

    @Test
    public void shouldGenerateWmqInboundStatements() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-wmq-flow.xml", muleXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework:spring-context:5.3.1",
                        "org.springframework:spring-beans:5.3.1",
                        "org.springframework.integration:spring-integration-core:5.5.8",
                        "org.springframework.integration:spring-integration-http:5.5.8",
                        "org.springframework.boot:spring-boot-starter-integration:2.6.3"
                )
                .addRegistrar(registrar)
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.demorabitmqspringintegration;\n" +
                                "\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.jms.dsl.Jms;\n" +
                                "import javax.jms.ConnectionFactory;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class JavaDSLWmq {\n" +
                                "    @Bean\n" +
                                "    public IntegrationFlow jmsInbound(ConnectionFactory connectionFactory) {\n" +
                                "        return IntegrationFlows.from(\n" +
                                "                        Jms.inboundAdapter(connectionFactory).destination(\"Q1\"))\n" +
                                "                .log()\n" +
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
        assertThat(applicationProperty.get(0).print()).contains("ibm.mq.queueManager=QM1");
        assertThat(applicationProperty.get(0).print()).contains("ibm.mq.channel=Channel1");
        assertThat(applicationProperty.get(0).print()).contains("ibm.mq.connName=localhost(1414)");
        assertThat(applicationProperty.get(0).print()).contains("ibm.mq.user=username");
        assertThat(applicationProperty.get(0).print()).contains("ibm.mq.password=password");

        List<Dependency> declaredDependencies = projectContext.getBuildFile().getDeclaredDependencies();
        checkDependency(declaredDependencies, "com.ibm.mq", "mq-jms-spring-boot-starter", "2.6.4");
        checkDependency(declaredDependencies, "org.springframework.integration", "spring-integration-jms", "5.5.8");
    }

    private void checkDependency(List<Dependency> declaredDependencies,
                                 String groupId,
                                 String artifactId,
                                 String version) {
        boolean foundDependency = declaredDependencies.stream()
                .anyMatch(d ->
                        d.getGroupId().equals(groupId)
                                && d.getArtifactId().equals(artifactId)
                                && (d.getVersion() != null || d.getVersion().equals(version)));
        assertThat(foundDependency).isTrue();
    }
}
