package org.springframework.sbm.mule.api.toplevel;

import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChoiceTopLevelElement extends AbstractTopLevelElement {

    public String renderDslSnippet() {
        StringBuilder sb = new StringBuilder();
        sb.append(composePrefixDslCode());
        String dsl = getDslSnippets().stream().map(DslSnippet::getRenderedSnippet).collect(Collectors.joining("\n"));
        sb.append(dsl).append("\n");
        Set<String> requiredImports = getRequiredImports();
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlow");
        requiredImports.add("org.springframework.integration.dsl.IntegrationFlows");
        requiredImports.add("org.springframework.integration.amqp.dsl.Amqp");
        getDslSnippets().forEach(ds -> requiredImports.addAll(ds.getRequiredImports()));
        return sb.toString();
    }


    protected String composePrefixDslCode() {
        return "sf -> sf";
    }

    public ChoiceTopLevelElement(String flowName,
                                 List<JAXBElement<?>> elements,
                                 MuleConfigurations muleConfigurations,
                                 Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {
        super(flowName, elements, muleConfigurations, translatorsMap);
    }
}
