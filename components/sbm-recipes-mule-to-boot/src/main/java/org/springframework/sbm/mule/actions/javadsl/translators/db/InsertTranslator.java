package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.mulesoft.schema.mule.db.InsertMessageProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;

@Component
public class InsertTranslator implements MuleComponentToSpringIntegrationDslTranslator<InsertMessageProcessorType> {
    @Override
    public Class<InsertMessageProcessorType> getSupportedMuleType() {
        return InsertMessageProcessorType.class;
    }

    @Override
    public DslSnippet translate(int id,
                                InsertMessageProcessorType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        return DslSnippet.builder()
                .renderedSnippet(
                        "                 // TODO: payload type might not be always LinkedMultiValueMap please change it to appropriate type \n" +
                                "                 // TODO: mule expression language is not converted to java, do it manually. example: #[payload] etc \n" +
                                "                .<LinkedMultiValueMap<String, String>>handle((p, h) -> {\n" +
                                "                      jdbcTemplate.execute(\"" + DBCommons.escapeDoubleQuotes(component.getParameterizedQuery()) + "\");\n" +
                                "                      return p;\n" +
                                "                })")
                .requiredImports(Set.of("org.springframework.util.LinkedMultiValueMap"))
                .build();
    }
}
