package org.springframework.sbm.mule.actions.javadsl.translators.core;

import org.mulesoft.schema.mule.core.ForeachProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.ForeachTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

import static java.util.Collections.emptySet;

@Component
public class ForeachTranslator implements MuleComponentToSpringIntegrationDslTranslator<ForeachProcessorType> {
    @Override
    public Class<ForeachProcessorType> getSupportedMuleType() {
        return ForeachProcessorType.class;
    }

    @Override
    public DslSnippet translate(int id,
                                ForeachProcessorType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap
    ) {

        ForeachTopLevelElement forEachTopLevelTranslations =
                new ForeachTopLevelElement(
                        flowName,
                        component.getMessageProcessorOrOutboundEndpoint(),
                        muleConfigurations,
                        translatorsMap
                );
        return new DslSnippet(
                "                //TODO: translate expression " + component.getCollection() + " which must produces an array\n" +
                "                // to iterate over\n" +
                "                .split()\n" +
                "                "+ forEachTopLevelTranslations.renderDslSnippet() +"\n" +
                "                .aggregate()",
                emptySet(),
                emptySet(),
                emptySet()
        );
    }
}
