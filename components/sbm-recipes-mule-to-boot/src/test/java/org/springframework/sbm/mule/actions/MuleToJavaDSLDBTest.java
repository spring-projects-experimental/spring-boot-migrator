package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDBTest extends JavaDSLActionBaseTest  {

    @Test
    public void sbmHasKnowledgeOfDBNamespace() {
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
                "        <db:select config-ref=\"MySQL_Configuration\" doc:name=\"Database\">\n" +
                "            <db:parameterized-query/>\n" +
                "        </db:select>\n" +
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
                                "    void dbMysql_config() {\n" +
                                "        //FIXME: element is not supported for conversion: <db:mysql-config/>\n" +
                                "    }\n" +
                                "\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow dbFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO)\n" +
                                "                //FIXME: element is not supported for conversion: <db:select/>\n" +
                                "                .get();\n" +
                                "    }}");
    }
}
