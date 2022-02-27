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
package org.springframework.sbm.jee.jms;

import org.springframework.sbm.jee.jms.actions.AddJmsConfigAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AddJmsConfigTest {


    private final AddJmsConfigAction sut = new AddJmsConfigAction();
    private Configuration configuration;

    @BeforeEach
    void setUp() throws IOException {
        Version version = new Version("2.3.0");
        configuration = new Configuration(version);
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));
        sut.setConfiguration(configuration);
    }

    @Test
    void testAddJmsConfig() {

        String javaSource =
                "package com.example.foo;\n" +
                        "import javax.ejb.MessageDriven;\n"
                        + "import javax.jms.Message;\n"
                        + "import javax.annotation.Resource;\n"
                        + "import javax.jms.Queue;\n"
                        + "\n"
                        + "@MessageDriven\n"
                        + "public class CargoHandled {\n"
                        + "\n"
                        + "    @Resource(name = \"ChatBean\")\n"
                        + "    private Queue questionQueue;\n"
                        + "\n"
                        + "    @Resource(name = \"AnswerQueue\")\n"
                        + "    private Queue answerQueue;\n"
                        + "}\n"
                        + "";

        String expected =
                "package com.example.foo;\n"
                        + "\n"
                        + "import javax.jms.ConnectionFactory;\n"
                        + "\n"
                        + "import javax.jms.Queue;\n"
                        + "import org.apache.activemq.command.ActiveMQQueue;\n"
                        + "import javax.jms.JMSException;\n"
                        + "\n"
                        + "import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;\n"
                        + "import org.springframework.context.annotation.Bean;\n"
                        + "import org.springframework.context.annotation.Configuration;\n"
                        + "import org.springframework.jms.annotation.EnableJms;\n"
                        + "import org.springframework.jms.config.DefaultJmsListenerContainerFactory;\n"
                        + "import org.springframework.jms.config.JmsListenerContainerFactory;\n"
                        + "\n"
                        + "@Configuration\n"
                        + "@EnableJms\n"
                        + "public class JmsConfig {\n"
                        + "\n"
                        + "    @Bean\n"
                        + "    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(\n"
                        + "           ConnectionFactory connectionFactory,\n"
                        + "           DefaultJmsListenerContainerFactoryConfigurer configurer) {\n"
                        + "        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();\n"
                        + "        configurer.configure(factory, connectionFactory);\n"
                        + "        return factory;\n"
                        + "    }\n"
                        + "\n"
                        + "    @Bean\n"
                        + "    Queue answerQueue(ConnectionFactory connectionFactory) throws JMSException {\n"
                        + "        ActiveMQQueue activeMQQueue = new ActiveMQQueue(\"AnswerQueue\");\n"
                        + "        return activeMQQueue;\n"
                        + "    }\n"
                        + "\n"
                        + "    @Bean\n"
                        + "    Queue questionQueue(ConnectionFactory connectionFactory) throws JMSException {\n"
                        + "        ActiveMQQueue activeMQQueue = new ActiveMQQueue(\"ChatBean\");\n"
                        + "        return activeMQQueue;\n"
                        + "    }\n"
                        + "\n"
                        + "}\n"
                        + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:7.0")
                .withJavaSources(javaSource)
                .build();

        sut.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(1).getResource().print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void testAddJmsConfigNoQueues() {

        String javaSource =
                "package the.pckg.name;\n"
                        + "\n"
                        + "import javax.ejb.MessageDriven;\n"
                        + "import javax.jms.Message;\n"
                        + "import javax.annotation.Resource;\n"
                        + "import javax.jms.Queue;\n"
                        + "\n"
                        + "@MessageDriven\n"
                        + "public class CargoHandled {\n"
                        + "\n"
                        + "    private Queue questionQueue;\n"
                        + "\n"
                        + "    @Resource(name = \"AnswerQueue\")\n"
                        + "    private String answerQueue;\n"
                        + "}\n"
                        + "";

        String expected =
                "package the.pckg.name;\n"
                        + "\n"
                        + "import javax.jms.ConnectionFactory;\n"
                        + "\n"
                        + "\n"
                        + "import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;\n"
                        + "import org.springframework.context.annotation.Bean;\n"
                        + "import org.springframework.context.annotation.Configuration;\n"
                        + "import org.springframework.jms.annotation.EnableJms;\n"
                        + "import org.springframework.jms.config.DefaultJmsListenerContainerFactory;\n"
                        + "import org.springframework.jms.config.JmsListenerContainerFactory;\n"
                        + "\n"
                        + "@Configuration\n"
                        + "@EnableJms\n"
                        + "public class JmsConfig {\n"
                        + "\n"
                        + "    @Bean\n"
                        + "    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(\n"
                        + "           ConnectionFactory connectionFactory,\n"
                        + "           DefaultJmsListenerContainerFactoryConfigurer configurer) {\n"
                        + "        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();\n"
                        + "        configurer.configure(factory, connectionFactory);\n"
                        + "        return factory;\n"
                        + "    }\n"
                        + "\n"
                        + "}\n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:7.0")
                .withJavaSources(javaSource)
                .build();

        sut.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(1).getResource().print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }


}
