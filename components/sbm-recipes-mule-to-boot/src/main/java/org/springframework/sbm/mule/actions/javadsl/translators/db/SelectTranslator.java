package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.mulesoft.schema.mule.db.SelectMessageProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Map;

@Component
public class SelectTranslator implements MuleComponentToSpringIntegrationDslTranslator<SelectMessageProcessorType> {

    @Override
    public Class<SelectMessageProcessorType> getSupportedMuleType() {
        return SelectMessageProcessorType.class;
    }

    @Override
    public DslSnippet translate(SelectMessageProcessorType component, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        return new DslSnippet("// TODO: substitute expression language with appropriate java code \n" +
                "                .handle((p, h) -> jdbcTemplate.queryForList(\"SELECT * FROM STUDENTS LIMIT 500\"))",
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );
    }
}
