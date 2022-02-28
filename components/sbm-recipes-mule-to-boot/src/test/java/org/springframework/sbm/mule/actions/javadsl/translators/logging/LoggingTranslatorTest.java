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
package org.springframework.sbm.mule.actions.javadsl.translators.logging;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.mulesoft.schema.mule.core.FlowType;
import org.mulesoft.schema.mule.core.LoggerType;
import org.mulesoft.schema.mule.core.MuleType;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoggingTranslatorTest {

    @Test
    void noAttributes() {
        // no attributes
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                "        <logger/>\n" +
                "    </flow>\n" +
                "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log()"
        );
    }

    @Test
    void messageOnly() {
        // messsage
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                        "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                        "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                        "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                        "    </http:listener-config>\n" +
                        "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                        "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                        "        <logger message=\"Hello World\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(\"Hello World\")"
        );
    }

    @Test
    void levelOnly() {
        // level
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                "        <logger level=\"ERROR\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                "    </flow>\n" +
                "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(LoggingHandler.Level.ERROR)"
        );
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.handler.LoggingHandler");
    }

    @Test
    void unknownLevelShouldUseInfoAsDefault() {
        // level
        String muleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                "        <logger level=\"UNKNOWN\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                "    </flow>\n" +
                "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(LoggingHandler.Level.INFO)"
        );
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.handler.LoggingHandler");
    }

    @Test
    void categoryOnly() {
        // category
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                "        <logger category=\"logCategory\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                "    </flow>\n" +
                "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(\"logCategory\")"
        );
    }

    @Test
    void levelAndCategory() {
        // category
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                        "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                        "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                        "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                        "    </http:listener-config>\n" +
                        "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                        "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                        "        <logger level=\"WARN\" category=\"logCategory\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        DslSnippet snippet = applySut(muleXml);
        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(LoggingHandler.Level.WARN, \"logCategory\")"
        );
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.handler.LoggingHandler");
    }

    @Test
    void levelAndMessage() {
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                        "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                        "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                        "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                        "    </http:listener-config>\n" +
                        "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                        "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                        "        <logger level=\"INFO\" message=\"Hello World\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        DslSnippet snippet = applySut(muleXml);

        assertThat(snippet.getRenderedSnippet()).isEqualTo(".log(LoggingHandler.Level.INFO, \"Hello World\")");
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.handler.LoggingHandler");
    }

    @Test
    void categoryAndMessage() {
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                        "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                        "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                        "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                        "    </http:listener-config>\n" +
                        "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                        "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                        "        <logger category=\"some.category\" message=\"The Message\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        DslSnippet snippet = applySut(muleXml);

        assertThat(snippet.getRenderedSnippet()).isEqualTo(".log(\"some.category\", \"The Message\")");
        assertThat(snippet.getRequiredImports()).isEmpty();
    }

    @Test
    void allAttributes() {
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "\n" +
                        "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                        "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                        "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                        "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                        "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                        "    </http:listener-config>\n" +
                        "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                        "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                        "        <logger level=\"DEBUG\" category=\"some.category\" message=\"The Message\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                        "    </flow>\n" +
                        "</mule>";

        DslSnippet snippet = applySut(muleXml);

        assertThat(snippet.getRenderedSnippet()).isEqualTo(".log(LoggingHandler.Level.DEBUG, \"some.category\", \"The Message\")");
        assertThat(snippet.getRequiredImports()).contains("org.springframework.integration.handler.LoggingHandler");
    }

    @Test
    void shouldTranslateExpressionLanguage() {
        String muleXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mule xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
                "    xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
                "    http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\">\n" +
                "    <http:listener-config name=\"HTTP_Listener_config\" doc:name=\"HTTP Listener config\" doc:id=\"8f032dc5-c57e-494a-9e27-40731af676be\" basePath=\"/\" >\n" +
                "        <http:listener-connection host=\"0.0.0.0\" port=\"8081\" />\n" +
                "    </http:listener-config>\n" +
                "    <flow name=\"mule-logger-exampleFlow\" doc:id=\"12459985-a49b-4948-b821-9b13793f55d2\" >\n" +
                "        <http:listener doc:name=\"Listener\" doc:id=\"357ad19d-d047-4595-8a55-6c1967f20a33\" config-ref=\"HTTP_Listener_config\" path=\"/\"/>\n" +
                "        <logger message=\"#[payload]\" doc:name=\"Logger\" doc:id=\"cf480901-cd5d-413a-8c50-183faf37addf\"/>\n" +
                "    </flow>\n" +
                "</mule>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/mule/mule.xml", muleXml)
                .addRegistrar(new MuleXmlProjectResourceRegistrar())
                .build();

        ExpressionLanguageTranslator expressionLanguageTranslator = mock(ExpressionLanguageTranslator.class);
        when(expressionLanguageTranslator.translate("#[payload]")).thenReturn("#{payload}");
        LoggingTranslator sut = new LoggingTranslator(expressionLanguageTranslator);
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());
        MuleType muleType = muleXmls.get(0).getMuleType();
        LoggerType loggerType = (LoggerType)  ((FlowType)((JAXBElement)muleType.getBeansOrBeanOrPropertyPlaceholder().get(1)).getValue()).getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor().get(0).getValue();

        DslSnippet snippet = sut.translate(loggerType, new QName(""), new MuleConfigurations(new HashMap<>()));

        assertThat(snippet.getRenderedSnippet()).isEqualTo(
                ".log(\"#{payload}\")"
        );
        verify(expressionLanguageTranslator).translate("#[payload]");
    }

    private DslSnippet applySut(String muleXml) {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/mule/mule.xml", muleXml)
                .addRegistrar(new MuleXmlProjectResourceRegistrar())
                .build();

        return apply(projectContext);
    }

    private DslSnippet apply(ProjectContext projectContext) {
        LoggingTranslator sut = new LoggingTranslator(new ExpressionLanguageTranslator());
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());

        MuleType muleType = muleXmls.get(0).getMuleType();
        LoggerType loggerType = (LoggerType)  ((FlowType)((JAXBElement)muleType.getBeansOrBeanOrPropertyPlaceholder().get(1)).getValue()).getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor().get(0).getValue();
        return sut.translate(loggerType, new QName(""), new MuleConfigurations(new HashMap<>()));
    }
}
