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
import org.springframework.sbm.mule.resource.MuleXml;
import org.springframework.sbm.mule.resource.MuleXmlProjectResourceFilter;
import org.springframework.sbm.project.resource.filter.GenericTypeListFilter;
import org.springframework.sbm.properties.api.PropertiesSource;
import lombok.RequiredArgsConstructor;
import org.mulesoft.schema.mule.core.*;
import org.mulesoft.schema.mule.tls.TlsContextType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MuleMigrationContextFactory {

    private final MuleConfigurationsExtractor muleConfigurationsExtractor;
    public MuleMigrationContext createMuleMigrationContext(ProjectContext projectContext) {
        List<MuleXml> muleXmls = projectContext.search(new MuleXmlProjectResourceFilter());
        Map<String, ? extends ConfigurationTypeAdapter> allAvailableConfigurations = this.findAllAvailableConfigurations(muleXmls);
        MuleConfigurations muleConfigurations = new MuleConfigurations(allAvailableConfigurations);
        List<JAXBElement> availableFlows = findAvailableMuleFlows(muleXmls);
        List<SubFlowType> availableMuleSubFlows = findAvailableMuleSubFlows(muleXmls);
        List<PropertiesSource> propertiesFiles = findAvailablePropertiesFiles(projectContext);
        List<JAXBElement> nonSupportedTypes = findNonSupportedTypes(muleXmls, allAvailableConfigurations);
        return new MuleMigrationContext(availableFlows, availableMuleSubFlows, muleConfigurations, propertiesFiles, nonSupportedTypes);
    }

    private List<JAXBElement> findNonSupportedTypes(List<MuleXml> muleXmls, Map<String, ? extends ConfigurationTypeAdapter> allAvailableConfigurations) {

        Set<Class<? extends Object>> configmap = allAvailableConfigurations.values()
                .stream()
                .map(configurationTypeAdapter -> configurationTypeAdapter.getMuleConfiguration().getClass())
                .collect(Collectors.toSet());

        configmap.add(FlowType.class);
        configmap.add(SubFlowType.class);

        return muleXmls.stream()
                .map(MuleXml::getMuleType)
                .flatMap(mt -> mt.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .filter(e -> !(configmap.contains(e.getValue().getClass())))
                .collect(Collectors.toList());
    }

    private Map<String, ConfigurationTypeAdapter<?>> findAllAvailableConfigurations(List<MuleXml> muleXmls) {

        List<MuleType> allMuleTypes = muleXmls.stream().map(MuleXml::getMuleType).collect(Collectors.toList());
        return muleConfigurationsExtractor.extractAllConfigurations(allMuleTypes);
    }

    private List<JAXBElement> findAvailableMuleFlows(List<MuleXml> muleXmls) {
        return muleXmls.stream()
                .map(MuleXml::getMuleType)
                .flatMap(mt -> mt.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .filter(e -> e.getValue() instanceof FlowType)
                .collect(Collectors.toList());
    }

    private List<SubFlowType> findAvailableMuleSubFlows(List<MuleXml> muleXmls) {
        return muleXmls.stream()
                .map(m -> m.getMuleType())
                .flatMap(mt -> mt.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .map(JAXBElement::getValue)
                .filter(e -> SubFlowType.class.isInstance(e))
                .map(SubFlowType.class::cast)
                .collect(Collectors.toList());
    }

    private List<JAXBElement> findAvailableTlsContext(List<MuleXml> muleXmls) {
        return muleXmls.stream()
                .map(MuleXml::getMuleType)
                .flatMap(mt -> mt.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .filter(e -> e.getValue() instanceof TlsContextType)
                .collect(Collectors.toList());
    }

    private List<PropertiesSource> findAvailablePropertiesFiles(ProjectContext projectContext) {
        return projectContext.search(new GenericTypeListFilter<>(PropertiesSource.class));
    }
}
