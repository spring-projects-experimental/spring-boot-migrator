package org.springframework.sbm.mule.api.toplevel;

import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;

public interface TopLevelTypeFactory {

    Class<?> getSupportedTopLevelType();
    TopLevelDefinition buildDefinitionSnippet(JAXBElement topLevelElement, MuleConfigurations muleConfigurations);
}
