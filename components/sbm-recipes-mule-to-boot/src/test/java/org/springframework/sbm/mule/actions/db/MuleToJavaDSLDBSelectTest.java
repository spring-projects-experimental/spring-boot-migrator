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
package org.springframework.sbm.mule.actions.db;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDBSelectTest extends JavaDSLActionBaseTest {

    @Test
    public void translateDbSelectDynamicQuery() {
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <db:mysql-config name=\"MySQL_Configuration\" host=\"localhost\" port=\"3036\" user=\"root\" password=\"root\" doc:name=\"MySQL Configuration\"/>\n" +
                "    <flow name=\"dbFlow\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/\" doc:name=\"HTTP\"/>\n" +
                "        <logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
                "        <db:select config-ref=\"MySQL_Configuration\" doc:name=\"Database\" fetchSize=\"500\" maxRows=\"500\">\n" +
                "            <db:dynamic-query><![CDATA[SELECT * FROM STUDENTS]]></db:dynamic-query>\n" +
                "        </db:select>" +
                "    </flow>\n" +
                "</mule>\n";

        addXMLFileToResource(muleXml);
        runAction();

        Set<String> listOfImportedArtifacts = projectContext
                .getBuildFile()
                .getDeclaredDependencies()
                .stream()
                .map(Dependency::getArtifactId)
                .collect(Collectors.toSet());

        assertThat(listOfImportedArtifacts).contains("spring-integration-jdbc");
        assertThat(listOfImportedArtifacts).contains("spring-boot-starter-jdbc");
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(getGeneratedJavaFile())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.handler.LoggingHandler;\n" +
                                "import org.springframework.integration.http.dsl.Http;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow dbFlow(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO)\n" +
                                "                // TODO: substitute expression language with appropriate java code \n" +
                                "                // TODO: use appropriate translation for pagination \n" +
                                "                .handle((p, h) -> jdbcTemplate.queryForList(\"SELECT * FROM STUDENTS\"))\n" +
                                "                .get();\n" +
                                "    }}");
    }

    @Test
    public void translateDbSelectParameterisedQuery() {
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <db:mysql-config name=\"MySQL_Configuration\" host=\"localhost\" port=\"3036\" user=\"root\" password=\"root\" doc:name=\"MySQL Configuration\"/>\n" +
                "    <flow name=\"dbFlow\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/\" doc:name=\"HTTP\"/>\n" +
                "        <logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
                "        <db:select config-ref=\"MySQL_Configuration\" doc:name=\"Database\" fetchSize=\"500\" maxRows=\"500\">\n" +
                "            <db:parameterized-query><![CDATA[SELECT * FROM STUDENTS]]></db:parameterized-query>\n" +
                "        </db:select>" +
                "    </flow>\n" +
                "</mule>\n";

        addXMLFileToResource(muleXml);
        runAction();
        assertThat(projectContext.getProjectJavaSources().list()).hasSize(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo(
                        "package com.example.javadsl;\n" +
                                "import org.springframework.context.annotation.Bean;\n" +
                                "import org.springframework.context.annotation.Configuration;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                                "import org.springframework.integration.handler.LoggingHandler;\n" +
                                "import org.springframework.integration.http.dsl.Http;\n" +
                                "\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow dbFlow(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO)\n" +
                                "                // TODO: substitute expression language with appropriate java code \n" +
                                "                // TODO: use appropriate translation for pagination \n" +
                                "                .handle((p, h) -> jdbcTemplate.queryForList(\"SELECT * FROM STUDENTS\"))\n" +
                                "                .get();\n" +
                                "    }}");
    }
}
