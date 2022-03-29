package org.springframework.sbm.mule.actions.javadsl.translators.core;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mulesoft.schema.mule.core.SelectiveOutboundRouterType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.ChoiceTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ChoiceTranslator implements MuleComponentToSpringIntegrationDslTranslator<SelectiveOutboundRouterType> {

    @Override
    public Class<SelectiveOutboundRouterType> getSupportedMuleType() {
        return SelectiveOutboundRouterType.class;
    }

    private final static String subflowTemplate =
                    "                                .subFlowMapping(\"dataValue\" /*TODO: Translate dataValue to $TRANSLATE_EXPRESSION*/,\n" +
                    "                                        $SUBFLOW_CONTENT\n" +
                    "                                )\n";

    @Override
    public DslSnippet translate(SelectiveOutboundRouterType component,
                                QName name,
                                MuleConfigurations muleConfigurations,
                                String flowName,
                                Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        List<ImmutablePair<String, ChoiceTopLevelElement>> list = component
                .getWhen()
                .stream()
                .map(item -> new ImmutablePair<>(item.getExpression(), new ChoiceTopLevelElement(
                        flowName,
                        item.getMessageProcessorOrOutboundEndpoint(),
                        muleConfigurations,
                        translatorsMap)))
                .collect(Collectors.toList());

        String subflowMappings = list
                .stream()
                .map(item ->
                        subflowTemplate
                                .replace("$TRANSLATE_EXPRESSION", item.getLeft())
                                .replace("$SUBFLOW_CONTENT",  item.getValue().renderDslSnippet())
                )
                .collect(Collectors.joining());


        Set<String> requiredImports = list.stream()
                .map(item -> item.getValue().getRequiredImports())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<String> requiredDependencies = list.stream()
                .map(item -> item.getValue().getRequiredDependencies())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return new DslSnippet(
                "/*\n" +
                        "                * TODO: LinkedMultiValueMap might not be apt, substitute with right input type*/\n" +
                        "                .<LinkedMultiValueMap<String, String>, String>route(\n" +
                        "                        p -> p.getFirst(\"dataKey\") /*TODO: use apt condition*/,\n" +
                        "                        m -> m\n" +
                                                        subflowMappings +
                        "                                .resolutionRequired(false)\n" +
                        "                                .defaultSubFlowMapping(sf -> sf.handle((p, h) -> \"Hello\" ))\n" +
                        "                )",
                requiredImports,
                requiredDependencies,
                Collections.emptySet()
        );
    }
}
