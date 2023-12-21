/*
 * Copyright 2021 - 2023 the original author or authors.
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
                """
                package com.example.foo;
                import javax.ejb.MessageDriven;
                import javax.jms.Message;
                import javax.annotation.Resource;
                import javax.jms.Queue;
                                
                @MessageDriven
                public class CargoHandled {
                                
                    @Resource(name = "ChatBean")
                    private Queue questionQueue;
                                
                    @Resource(name = "AnswerQueue")
                    private Queue answerQueue;
                }       
                """;

        String expected =
                """
                package com.example.foo;
                                        
                import javax.jms.ConnectionFactory;
                                        
                import javax.jms.Queue;
                import org.apache.activemq.command.ActiveMQQueue;
                import javax.jms.JMSException;
                                        
                import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
                import org.springframework.context.annotation.Bean;
                import org.springframework.context.annotation.Configuration;
                import org.springframework.jms.annotation.EnableJms;
                import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
                import org.springframework.jms.config.JmsListenerContainerFactory;
                                        
                @Configuration
                @EnableJms
                public class JmsConfig {
                                        
                    @Bean
                    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
                           ConnectionFactory connectionFactory,
                           DefaultJmsListenerContainerFactoryConfigurer configurer) {
                        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
                        configurer.configure(factory, connectionFactory);
                        return factory;
                    }
                                        
                    @Bean
                    Queue answerQueue(ConnectionFactory connectionFactory) throws JMSException {
                        ActiveMQQueue activeMQQueue = new ActiveMQQueue("AnswerQueue");
                        return activeMQQueue;
                    }
                                        
                    @Bean
                    Queue questionQueue(ConnectionFactory connectionFactory) throws JMSException {
                        ActiveMQQueue activeMQQueue = new ActiveMQQueue("ChatBean");
                        return activeMQQueue;
                    }
                                        
                }
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:7.0")
                .withJavaSources(javaSource)
                .build();

        sut.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(1).getResource().print();
        assertThat(actual.replace("\r\n", "\n").replace("\r", "\n"))
                .as(TestDiff.of(actual, expected))
                .isEqualToNormalizingNewlines(expected);
    }

    @Test
    void testAddJmsConfigNoQueues() {

        String javaSource =
                """
                package the.pckg.name;
                                
                import javax.ejb.MessageDriven;
                import javax.jms.Message;
                import javax.annotation.Resource;
                import javax.jms.Queue;
                                
                @MessageDriven
                public class CargoHandled {
                                
                    private Queue questionQueue;
                                
                    @Resource(name = "AnswerQueue")
                    private String answerQueue;
                }            
                """;

        String expected =
                """
                package the.pckg.name;
                                
                import javax.jms.ConnectionFactory;
                                
                                
                import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
                import org.springframework.context.annotation.Bean;
                import org.springframework.context.annotation.Configuration;
                import org.springframework.jms.annotation.EnableJms;
                import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
                import org.springframework.jms.config.JmsListenerContainerFactory;
                                
                @Configuration
                @EnableJms
                public class JmsConfig {
                                
                    @Bean
                    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
                           ConnectionFactory connectionFactory,
                           DefaultJmsListenerContainerFactoryConfigurer configurer) {
                        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
                        configurer.configure(factory, connectionFactory);
                        return factory;
                    }
                                
                }
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:7.0")
                .withJavaSources(javaSource)
                .build();

        sut.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(1).getResource().print();
        assertThat(actual.replace("\r\n", "\n").replace("\r", "\n"))
                .as(TestDiff.of(actual, expected))
                .isEqualToNormalizingNewlines(expected);
    }


}
