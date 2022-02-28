/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.mule.api;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.project.resource.filter.GenericTypeListFilter;
import org.springframework.sbm.properties.api.PropertiesSource;
import lombok.RequiredArgsConstructor;
import org.mulesoft.schema.mule.core.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MuleMigrationContextFactory {

    private final MuleConfigurationsExtractor muleConfigurationsExtractor;
    public MuleMigrationContext createMuleMigrationContext(ProjectContext projectContext) {
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());
        List<JAXBElement> topLevelElements = findTopLevelElements(muleXmls);
        Map<String, ? extends ConfigurationTypeAdapter> allAvailableConfigurations = this.findAllAvailableConfigurations(muleXmls);
        MuleConfigurations muleConfigurations = new MuleConfigurations(allAvailableConfigurations);
        List<PropertiesSource> propertiesFiles = findAvailablePropertiesFiles(projectContext);
        return new MuleMigrationContext(topLevelElements, muleConfigurations, propertiesFiles);
    }

    private Map<String, ConfigurationTypeAdapter<?>> findAllAvailableConfigurations(List<MuleXml> muleXmls) {

        List<MuleType> allMuleTypes = muleXmls.stream().map(MuleXml::getMuleType).collect(Collectors.toList());
        return muleConfigurationsExtractor.extractAllConfigurations(allMuleTypes);
    }

    private List<JAXBElement> findTopLevelElements(List<MuleXml> muleXmls) {
        return muleXmls.stream()
                .map(MuleXml::getMuleType)
                .flatMap(mt -> mt.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .collect(Collectors.toList());
    }

    private List<PropertiesSource> findAvailablePropertiesFiles(ProjectContext projectContext) {
        return projectContext.search(new GenericTypeListFilter<>(PropertiesSource.class));
    }
}
