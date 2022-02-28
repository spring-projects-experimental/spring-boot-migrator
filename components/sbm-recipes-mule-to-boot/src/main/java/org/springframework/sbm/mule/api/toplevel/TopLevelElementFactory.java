package org.springframework.sbm.mule.api.toplevel;

import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;

import javax.xml.bind.JAXBElement;

public interface TopLevelElementFactory {

    Class<?> getSupportedTopLevelType();
    TopLevelElement buildDefinition(JAXBElement topLevelElement, MuleConfigurations muleConfigurations);
}
