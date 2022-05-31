package org.springframework.sbm.mule.actions.db;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.actions.JavaDSLActionBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDBInsertTest extends JavaDSLActionBaseTest {

    @Test
    public void dbInsert() {
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <flow name=\"dbFlow\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/\" doc:name=\"HTTP\"/>\n" +
                "        <logger level=\"INFO\" doc:name=\"Logger\"/>\n" +
                "        <db:insert config-ref=\"Oracle_Configuration\" doc:name=\"Database\">\n" +
                "            <db:parameterized-query><![CDATA[INSERT INTO STUDENTS (NAME, AGE, CITY) VALUES (#[payload.name], #[payload.age], #[payload.city])]]></db:parameterized-query>\n" +
                "        </db:insert>" +
                "    </flow>\n" +
                "</mule>\n";

        addXMLFileToResource(muleXml);
        runAction();

        assertThat(getGeneratedJavaFile()).isEqualTo("package com.example.javadsl;\n" +
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
                "    IntegrationFlow dbFlow() {\n" +
                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/\")).handle((p, h) -> p)\n" +
                "                .log(LoggingHandler.Level.INFO)\n" +
                "                .<LinkedMultiValueMap<String, String>>handle((p, h) -> {\n" +
                "                      jdbcTemplate.execute(\"INSERT INTO STUDENTS (NAME, AGE, CITY) VALUES (#[payload.name], #[payload.age], #[payload.city])\");\n" +
                "                      return p;\n" +
                "                })\n" +
                "                .get();\n" +
                "    }}");
    }
}
