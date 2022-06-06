package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLTransactionalTest extends JavaDSLActionBaseTest {

    @Test
    public void detectsTransactional() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                "      xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "      xmlns:spring=\"http://www.springframework.org/schema/beans\"\n" +
                "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "      xsi:schemaLocation=\"\n" +
                "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
                "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
                "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\">\n" +
                "    <flow name=\"foreach\">\n" +
                "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/foreach\" doc:name=\"HTTP\"/>\n" +
                "        <transactional>\n" +
                "            <foreach collection=\"#[['apple', 'banana', 'orange']]\">\n" +
                "                <logger message=\"#[payload]\" level=\"INFO\" />\n" +
                "            </foreach>\n" +
                "            <logger message=\"Done with for looping\" level=\"INFO\" />\n" +
                "        </transactional>\n" +
                "    </flow>\n" +
                "</mule>";

        addXMLFileToResource(xml);
        runAction();

        assertThat(getGeneratedConfigFile()).isEqualTo("package com.example.javadsl;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                "import org.springframework.integration.dsl.IntegrationFlows;\n" +
                "import org.springframework.integration.http.dsl.Http;\n" +
                "\n" +
                "@Configuration\n" +
                "public class FlowConfigurations {\n" +
                "    @Bean\n" +
                "    IntegrationFlow foreach() {\n" +
                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/foreach\")).handle((p, h) -> p)\n" +
                "                //FIXME: element is not supported for conversion: <transactional/>\n" +
                "                .get();\n" +
                "    }}");
    }

    private String getGeneratedConfigFile() {
        return projectContext.getProjectJavaSources().list().get(0).print();
    }
}
