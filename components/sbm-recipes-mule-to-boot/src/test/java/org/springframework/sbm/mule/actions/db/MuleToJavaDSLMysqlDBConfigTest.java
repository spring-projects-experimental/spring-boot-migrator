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
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;


import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class MuleToJavaDSLMysqlDBConfigTest extends JavaDSLActionBaseTest {

    private final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<mule xmlns:scripting=\"http://www.mulesoft.org/schema/mule/scripting\"\n" +
            "\txmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:api-platform-gw=\"http://www.mulesoft.org/schema/mule/api-platform-gw\" xmlns:apikit=\"http://www.mulesoft.org/schema/mule/apikit\" xmlns:cmis=\"http://www.mulesoft.org/schema/mule/cmis\" xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:ee=\"http://www.mulesoft.org/schema/mule/ee/core\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/scripting http://www.mulesoft.org/schema/mule/scripting/current/mule-scripting.xsd\n" +
            "http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/api-platform-gw http://www.mulesoft.org/schema/mule/api-platform-gw/current/mule-api-platform-gw.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\n" +
            "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/apikit http://www.mulesoft.org/schema/mule/apikit/current/mule-apikit.xsd\n" +
            "http://www.mulesoft.org/schema/mule/cmis http://www.mulesoft.org/schema/mule/cmis/current/mule-cmis.xsd\n" +
            "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/core http://www.mulesoft.org/schema/mule/ee/core/current/mule-ee.xsd\">\n" +
            "   <db:mysql-config name=\"MySQL_Configuration\" host=\"localhost\" port=\"3306\" user=\"root\" password=\"pw\" database=\"mule\" doc:name=\"MySQL Configuration\"/>\n" +
            "    <http:listener-config name=\"http-lc-0.0.0.0-8081\" host=\"0.0.0.0\" port=\"8080\" protocol=\"HTTP\"/>\n" +
            "</mule>";


    @Test
    public void fillApplicationPropertiesForDBConnection() {
        addXMLFileToResource(xml);
        runAction(projectContext -> {
            assertThat(getApplicationPropertyContent()).isEqualTo(
                    """
                    server.port=8080
                    spring.datasource.url=--INSERT--DB-URL-HERE-Example:--INSERT--DB-URL-HERE-Example:jdbc:mysql://localhost:3306/sonoo
                    spring.datasource.username=--INSERT-USER-NAME--
                    spring.datasource.password=--INSERT-PASSWORD--
                    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
                    spring.jpa.show-sql=true"""
            );
        });
    }



    @Test
    public void importsOracleDrivers() {
        addXMLFileToResource(xml);
        runAction(projectContext -> {
            Set<String> declaredDependencies = projectContext
                    .getBuildFile().getDeclaredDependencies()
                    .stream()
                    .map(dependency -> dependency.getGroupId() + ":" + dependency.getArtifactId() + ":" + dependency.getVersion())
                    .collect(Collectors.toSet());

            assertThat(declaredDependencies).contains("mysql:mysql-connector-java:8.0.29");
        });
    }
}
