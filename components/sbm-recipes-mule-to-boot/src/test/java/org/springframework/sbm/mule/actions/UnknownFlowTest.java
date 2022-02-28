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
import org.springframework.sbm.mule.actions.javadsl.translators.core.FlowRefTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
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

public class UnknownFlowTest {

    private final String muleMultiFlow = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\">\n" +
            "<catch-exception-strategy name=\"exceptionStrategy\"/>\n" +
            "</mule>";

    private JavaDSLAction2 myAction2;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new HttpListenerTranslator(),
                new LoggingTranslator(new ExpressionLanguageTranslator()),
                new FlowRefTranslator());
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(
                new FlowTopLevelElementFactory(translators),
                new SubflowTopLevelElementFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(new AmqpConfigTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction2 = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction2.setEventPublisher(eventPublisher);
    }

    @Test
    public void shouldTranslateUnknownFlow() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-multi-flow.xml", muleMultiFlow)
                .addRegistrar(registrar)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-stream:5.4.4"
                )
                .build();
        myAction2.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);


        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    void catch_exception_strategy() {\n" +
                        "        //FIXME: element is not supported for conversion: <catch-exception-strategy/>\n" +
                        "    }}"
                );
    }
}
