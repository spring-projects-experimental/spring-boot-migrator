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

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class WMQFlowTest extends JavaDSLActionBaseTest {

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


    @Test
    public void wmq() {
        addXMLFileToResource(wmqXML);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(getGeneratedJavaFile())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                        "import org.springframework.integration.handler.LoggingHandler;\n" +
                        "import org.springframework.integration.jms.dsl.Jms;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow wmqtestFlow(javax.jms.ConnectionFactory connectionFactory) {\n" +
                        "        return IntegrationFlows.from(Jms.inboundAdapter(connectionFactory).destination(\"TestQueue\")).handle((p, h) -> p)\n" +
                        "                .log(LoggingHandler.Level.INFO)\n" +
                        "                .get();\n" +
                        "    }\n" +
                        "}"
                );
    }
}
