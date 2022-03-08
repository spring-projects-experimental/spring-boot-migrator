package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

public class MuleToJavaDSLDWLTransformTest extends JavaDSLActionBaseTest {

    private static final String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "\n" +
            "<mule xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "    xmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\">\n" +
            "    <flow name=\"dwlFlow\">\n" +
            "        <http:listener config-ref=\"HTTP_Listener_Configuration\" path=\"/dwl\" doc:name=\"HTTP\"/>\n" +
            "    \n" +
            "        <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "        \n" +
            "        <dw:transform-message doc:name=\"action transform\">\n" +
            "            <dw:set-payload><![CDATA[%dw 1.0\n" +
            "%output application/json\n" +
            "---\n" +
            "{\n" +
            "    action_Code: 10,\n" +
            "    returnCode:  20\n" +
            "}]]></dw:set-payload>\n" +
            "        </dw:transform-message>\n" +
            "        \n" +
            "         <logger message=\"payload to be sent: #[new String(payload)]\" level=\"INFO\" doc:name=\"Log the message content to be sent\"/>\n" +
            "    </flow>\n" +
            "</mule>\n";

    @Test
    public void test() {
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-transform.xml", muleXml)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework.boot:spring-boot-starter-web:2.5.5",
                        "org.springframework.boot:spring-boot-starter-integration:2.5.5",
                        "org.springframework.integration:spring-integration-amqp:5.4.4",
                        "org.springframework.integration:spring-integration-stream:5.4.4",
                        "org.springframework.integration:spring-integration-http:5.4.4"
                )
                .addRegistrar(registrar)
                .build();

        myAction.apply(projectContext);

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
                                "class ActionTransform {\n" +
                                "    /*\n" +
                                "     * TODO:\n" +
                                "     *\n" +
                                "     * Please add necessary transformation for below snippet\n" +
                                "     * %dw 1.0\n" +
                                "     * %output application/json\n" +
                                "     * ---\n" +
                                "     * {\n" +
                                "     *     action_Code: 10,\n" +
                                "     *     returnCode:  20\n" +
                                "     * }\n" +
                                "     * */\n" +
                                "    public static ActionTransform createActionTransformer(Object payload) {\n" +
                                "\n" +
                                "        return new ActionTransform();\n" +
                                "    }\n" +
                                "}\n" +
                                "@Configuration\n" +
                                "public class FlowConfigurations {\n" +
                                "\n" +
                                "    @Bean\n" +
                                "    IntegrationFlow dwlFlow() {\n" +
                                "        return IntegrationFlows.from(Http.inboundChannelAdapter(\"/dwl\")).handle((p, h) -> p)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .transform(ActionTransform::createActionTransformer)\n" +
                                "                .log(LoggingHandler.Level.INFO, \"payload to be sent: #[new String(payload)]\")\n" +
                                "                .get();\n" +
                                "    }}");
    }
}
