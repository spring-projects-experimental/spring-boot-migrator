package org.springframework.sbm.mule.actions.javadsl.translators.core;

import org.mulesoft.schema.mule.core.SelectiveOutboundRouterType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ChoiceTranslator implements MuleComponentToSpringIntegrationDslTranslator<SelectiveOutboundRouterType> {

    @Override
    public Class<SelectiveOutboundRouterType> getSupportedMuleType() {
        return SelectiveOutboundRouterType.class;
    }

    private final static String subflowTemplate =
                    "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to $TRANSLATE_EXPRESSION*/,\n" +
                    "                                        sf -> sf.handle((p , h) -> \"Bonjur!\")\n" +
                    "                                )\n";

    @Override
    public DslSnippet translate(SelectiveOutboundRouterType component,
                                QName name,
                                MuleConfigurations muleConfigurations, String flowName) {

        String subflowMappings = component.getWhen()
                .stream()
                .map(item -> subflowTemplate.replace("$TRANSLATE_EXPRESSION", item.getExpression())).collect(Collectors.joining());
        return new DslSnippet(
                "/*\n" +
                        "                * TODO: LinkedMultiValueMap might not be apt, double check*/\n" +
                        "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                        "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                        "                        m -> m\n" +
                        subflowMappings +
                        "                                .resolutionRequired(false)\n" +
                        "                                .defaultSubFlowMapping(sf -> sf.handle((p, h) -> \"Hello\" ))\n" +
                        "                )",
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
        );
    }
}
