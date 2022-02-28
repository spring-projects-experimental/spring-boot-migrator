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
import org.springframework.sbm.mule.actions.javadsl.translators.core.SetPayloadTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.api.*;
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

public class MuleToJavaDSLApikitTest {
    private final String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<mule xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:apikit=\"http://www.mulesoft.org/schema/mule/apikit\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/apikit http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd\n" +
            "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.1.xsd\">\n" +
            "    <flow name=\"get:/helloworld:helloword-config\">\n" +
            "        <set-payload value=\"{&#xA;&quot;message&quot;: &quot;Hello worldXXX&quot;&#xA;}\" doc:name=\"Set Payload\"/>\n" +
            "    </flow>\n" +
            "</mule>";

    private JavaDSLAction2 myAction;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new SetPayloadTranslator(),
                new LoggingTranslator(new ExpressionLanguageTranslator())
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
    public void generatesApikitDSLStatements() {

        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-apikit-flow.xml", muleXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
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
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.http.HttpMethod;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
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
                                "    }}");
    }
}
