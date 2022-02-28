package org.springframework.sbm.mule.api.toplevel;

import org.mulesoft.schema.mule.core.SubFlowType;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SubflowTopLevelElementFactory implements TopLevelElementFactory {
    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;

    public SubflowTopLevelElementFactory(List<? extends MuleComponentToSpringIntegrationDslTranslator> translators) {
        translatorsMap = translators.stream()
                .collect(Collectors.toMap(MuleComponentToSpringIntegrationDslTranslator::getSupportedMuleType, Function.identity()));
    }


    @Override
    public Class<?> getSupportedTopLevelType() {
        return SubFlowType.class;
    }

    @Override
    public TopLevelElement buildDefinition(JAXBElement topLevelElement, MuleConfigurations muleConfigurations) {
        SubFlowType sft = ((SubFlowType) topLevelElement.getValue());
        String flowName = translateToJavaName(sft.getName());

        List<JAXBElement> l = new ArrayList<>();
        l.addAll(sft.getMessageProcessorOrOutboundEndpoint());

        return new SubflowTopLevelElement(flowName, l, muleConfigurations, translatorsMap);
    }

    private String translateToJavaName(String name) {
        String resultName = name;
        int varSuffixIndex = name.indexOf("$");
        if (varSuffixIndex != -1) {
            resultName = resultName.substring(0, varSuffixIndex - 1);
        }
        return Character.toLowerCase(resultName.charAt(0)) + resultName.substring(1);
    }
}
