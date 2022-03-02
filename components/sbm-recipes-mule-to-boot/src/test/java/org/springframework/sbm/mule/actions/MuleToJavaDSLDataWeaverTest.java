package org.springframework.sbm.mule.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerConfigTypeAdapter;
import org.springframework.sbm.mule.actions.javadsl.translators.http.HttpListenerTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.logging.LoggingTranslator;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.FlowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.SubflowTopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapterFactory;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MuleToJavaDSLDataWeaverTest {
    private final static String xmlDW =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "\n" +
                    "<mule xmlns     = \"http://www.mulesoft.org/schema/mule/core\" \n" +
                    "      xmlns:dw  = \"http://www.mulesoft.org/schema/mule/ee/dw\"\n" +
                    "      xmlns:doc = \"http://www.mulesoft.org/schema/mule/documentation\"\n" +
                    "      xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "      \n" +
                    "      xsi:schemaLocation=\"http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\n" +
                    "                          http://www.mulesoft.org/schema/mule/core  http://www.mulesoft.org/schema/mule/core/current/mule.xsd\">\n" +
                    "\n" +
                    "    <sub-flow name=\"customErrorResponseTransformSubFlow\">\n" +
                    "\n" +
                    "        <dw:transform-message                                          doc:name=\"mapClientRiskRatingResponseErrorJSON\">\n" +
                    "            <dw:set-payload resource=\"classpath:dwl/mapClientRiskRatingResponseErrorJSON.dwl\"/>\n" +
                    "        </dw:transform-message>\n" +
                    "        \n" +
                    "    </sub-flow>\n" +
                    "    \n" +
                    "</mule>";

    private JavaDSLAction2 myAction;
    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    public void setup() {
        List<MuleComponentToSpringIntegrationDslTranslator> translators = List.of(
                new HttpListenerTranslator(),
                new LoggingTranslator(new ExpressionLanguageTranslator()));
        List<TopLevelElementFactory> topLevelTypeFactories = List.of(
                new FlowTopLevelElementFactory(translators),
                new SubflowTopLevelElementFactory(translators)
        );

        ConfigurationTypeAdapterFactory configurationTypeAdapterFactory = new ConfigurationTypeAdapterFactory(List.of(new HttpListenerConfigTypeAdapter()));
        MuleMigrationContextFactory muleMigrationContextFactory = new MuleMigrationContextFactory(new MuleConfigurationsExtractor(configurationTypeAdapterFactory));
        myAction = new JavaDSLAction2(muleMigrationContextFactory, topLevelTypeFactories);
        myAction.setEventPublisher(eventPublisher);
    }

    @Test
    public void shouldGenerateCommentForDWStatement() {
        System.out.println(xmlDW);
        MuleXmlProjectResourceRegistrar registrar = new MuleXmlProjectResourceRegistrar();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setDefaultBasePackage("com.example.javadsl");

        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .addProjectResource("src/main/resources/mule-simple-http-flow.xml", xmlDW)
                .withApplicationProperties(applicationProperties)
                .withBuildFileHavingDependencies(
                        "org.springframework:spring-context:5.3.1",
                        "org.springframework:spring-beans:5.3.1",
                        "org.springframework.integration:spring-integration-core:5.5.8",
                        "org.springframework.integration:spring-integration-http:5.5.8",
                        "org.springframework.boot:spring-boot-starter-integration:2.6.3"
                )
                .addRegistrar(registrar)
                .build();
        myAction.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(1);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print())
                .isEqualTo("package com.example.javadsl;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.integration.dsl.IntegrationFlow;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "public class FlowConfigurations {\n" +
                        "    @Bean\n" +
                        "    IntegrationFlow customErrorResponseTransformSubFlow() {\n" +
                        "        return flow -> {\n" +
                        "            // FIXME: Conversion is not supported for Mule type: org.mulesoft.schema.mule.ee.dw.TransformMessageType\n" +
                        "        };\n" +
                        "    }}");
    }
}
