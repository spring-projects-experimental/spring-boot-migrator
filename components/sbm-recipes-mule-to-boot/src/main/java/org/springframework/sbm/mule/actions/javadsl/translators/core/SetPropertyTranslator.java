package org.springframework.sbm.mule.actions.javadsl.translators.core;


import org.mulesoft.schema.mule.core.SetPropertyType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;

@Component
public class SetPropertyTranslator implements MuleComponentToSpringIntegrationDslTranslator<SetPropertyType> {
    @Override
    public Class<SetPropertyType> getSupportedMuleType() {
        return SetPropertyType.class;
    }

    @Override
    public DslSnippet translate(SetPropertyType component, QName name, MuleConfigurations muleConfigurations) {
        return new DslSnippet(
                ".enrichHeaders(h -> h.header(\"" + component.getPropertyName() + "\", \"" + component.getValue() + "\"))",
                Collections.emptySet()
        );
    }
}
