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
package org.springframework.sbm.mule.actions.wmq;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/195")
public class MuleToJavaDSLWmqTest extends JavaDSLActionBaseTest {
    private final static String muleXml = """
            <mule xmlns:wmq="http://www.mulesoft.org/schema/mule/ee/wmq" xmlns:amqp="http://www.mulesoft.org/schema/mule/amqp" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
            xmlns:spring="http://www.springframework.org/schema/beans"\s
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
            http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
            http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
            http://www.mulesoft.org/schema/mule/amqp http://www.mulesoft.org/schema/mule/amqp/current/mule-amqp.xsd
            http://www.mulesoft.org/schema/mule/ee/wmq http://www.mulesoft.org/schema/mule/ee/wmq/current/mule-wmq-ee.xsd">
            <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="9081" doc:name="HTTP Listener Configuration"/>
            <wmq:connector name="WMQ" hostName="localhost" port="1414" queueManager="QM1" channel="Channel1" username="username" password="password" transportType="CLIENT_MQ_TCPIP" targetClient="JMS_COMPLIANT" validateConnections="true" doc:name="WMQ"/>
            <flow name="wmq-flow">
            <wmq:inbound-endpoint queue="Q1" doc:name="WMQ" connector-ref="WMQ"/>
            <logger level="INFO" doc:name="Logger" doc:id="4585ec7f-2d4a-4d86-af24-b678d4a99227" />
            <wmq:outbound-endpoint queue="Q2" targetClient="JMS_COMPLIANT" connector-ref="WMQ" doc:name="WMQ"/>
            </flow>
            </mule>
            """;

    @Test
    public void shouldGenerateWmqOutboundStatements() {
        addXMLFileToResource(muleXml);
        runAction(projectContext -> {
            assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
            assertThat(getGeneratedJavaFile())
                    .isEqualTo("""
                               package com.example.javadsl;
                               import org.springframework.context.annotation.Bean;
                               import org.springframework.context.annotation.Configuration;
                               import org.springframework.integration.dsl.IntegrationFlow;
                               import org.springframework.integration.dsl.IntegrationFlows;
                               import org.springframework.integration.handler.LoggingHandler;
                               import org.springframework.integration.jms.dsl.Jms;
                                                              
                               @Configuration
                               public class FlowConfigurations {
                                   @Bean
                                   IntegrationFlow wmq_flow(javax.jms.ConnectionFactory connectionFactory) {
                                       return IntegrationFlows.from(Jms.inboundAdapter(connectionFactory).destination("Q1")).handle((p, h) -> p)
                                               .log(LoggingHandler.Level.INFO)
                                               .handle(Jms.outboundAdapter(connectionFactory).destination("Q2"))
                                               .get();
                                   }
                               }
                               """);

            String applicationProperty = getApplicationPropertyContent();
            assertThat(applicationProperty).contains("ibm.mq.queueManager=QM1");
            assertThat(applicationProperty).contains("ibm.mq.channel=Channel1");
            assertThat(applicationProperty).contains("ibm.mq.connName=localhost(1414)");
            assertThat(applicationProperty).contains("ibm.mq.user=username");
            assertThat(applicationProperty).contains("ibm.mq.password=password");

            List<Dependency> declaredDependencies = projectContext.getBuildFile().getDeclaredDependencies();
            checkDependency(declaredDependencies, "com.ibm.mq", "mq-jms-spring-boot-starter", "2.6.4");
            checkDependency(declaredDependencies, "org.springframework.integration", "spring-integration-jms", "5.5.8");
        });
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
