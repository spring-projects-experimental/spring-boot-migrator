package org.springframework.sbm.mule.api.toplevel;

import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForeachTopLevelElement extends AbstractTopLevelElement{

    protected String composePrefixDslCode() {
        return "";
    }

    public String renderDslSnippet() {
        StringBuilder sb = new StringBuilder();
        String dsl = getDslSnippets().stream().map(DslSnippet::getRenderedSnippet).collect(Collectors.joining("\n"));
        sb.append(dsl);
        return sb.toString();
    }

    public ForeachTopLevelElement(String flowName,
                                  List<JAXBElement<?>> elements,
                                  MuleConfigurations muleConfigurations,
                                  Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        super(flowName, elements, muleConfigurations, translatorsMap);
    }
}
