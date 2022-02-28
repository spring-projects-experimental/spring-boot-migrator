package org.springframework.sbm.mule.api.toplevel;

import org.mulesoft.schema.mule.core.FlowType;
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
public class FlowTopLevelElementFactory implements TopLevelElementFactory {
    private List<String> requiredImports = new ArrayList<>();
    private final Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap;


    public FlowTopLevelElementFactory(List<? extends MuleComponentToSpringIntegrationDslTranslator> translators) {
        translatorsMap = translators.stream()
                .collect(Collectors.toMap(MuleComponentToSpringIntegrationDslTranslator::getSupportedMuleType, Function.identity()));
    }

    @Override
    public Class<?> getSupportedTopLevelType() {
        return FlowType.class;
    }

    @Override
    public TopLevelElement buildDefinition(JAXBElement topLevelElement, MuleConfigurations muleConfigurations) {
        FlowType ft = ((FlowType) topLevelElement.getValue());
        if (ApiRouterKitFlowTopLevelElement.isApiRouterKitName(ft.getName())) {
            return new ApiRouterKitFlowTopLevelElement(ft.getName(), extractFlowElements(ft), muleConfigurations, translatorsMap);
        } else {
            return new FlowTopLevelElement(ft.getName(), extractFlowElements(ft), muleConfigurations, translatorsMap);
        }
    }

    private List<JAXBElement> extractFlowElements(FlowType ft) {
        List<JAXBElement> l = new ArrayList<>();
        if (ft.getAbstractMessageSource() != null) {
            l.add(ft.getAbstractMessageSource());
        }
        if (ft.getAbstractInboundEndpoint() != null) {
            l.add(ft.getAbstractInboundEndpoint());
        }
        if (ft.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor() != null) {
            l.addAll(ft.getAbstractMessageProcessorOrAbstractOutboundEndpointOrAbstractMixedContentMessageProcessor());
        }
        return l;
    }

}
