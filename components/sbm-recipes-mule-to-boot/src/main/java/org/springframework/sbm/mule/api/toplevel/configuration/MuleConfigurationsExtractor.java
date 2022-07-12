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
package org.springframework.sbm.mule.api.toplevel.configuration;

import lombok.RequiredArgsConstructor;
import org.mulesoft.schema.mule.core.AbstractConnectorType;
import org.mulesoft.schema.mule.core.ConfigurationType;
import org.mulesoft.schema.mule.core.MuleType;
import org.mulesoft.schema.mule.db.DatabaseConfigType;
import org.mulesoft.schema.mule.http.ListenerConfigType;
import org.mulesoft.schema.mule.http.RequestConfigType;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MuleConfigurationsExtractor {

    private final ConfigurationTypeAdapterFactory configurationTypeAdapterFactory;

    public Map<String, ConfigurationTypeAdapter<?>> extractAllConfigurations(List<MuleType> muleTypes) {

        return muleTypes.stream()
                .flatMap(m -> m.getBeansOrBeanOrPropertyPlaceholder().stream())
                .filter(JAXBElement.class::isInstance)
                .map(JAXBElement.class::cast)
                .filter(MuleConfigurationsExtractor::isConfigType)
                .map(e -> configurationTypeAdapterFactory.createAdapter(e.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(a -> a.getName(), a -> a));
    }


    public static boolean isConfigType(JAXBElement jaxbElement) {
        Class<?> aClass = jaxbElement.getValue().getClass();
        Set<Class> configTypes = Set.of(RequestConfigType.class,
                ConfigurationType.class,
                AbstractConnectorType.class,
                ListenerConfigType.class,
                DatabaseConfigType.class
        );
        return configTypes.stream()
                .anyMatch(ct -> ct.isAssignableFrom(aClass));
    }
}
