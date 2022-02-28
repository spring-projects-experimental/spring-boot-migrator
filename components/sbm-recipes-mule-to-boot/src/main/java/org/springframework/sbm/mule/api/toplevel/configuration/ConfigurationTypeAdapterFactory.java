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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConfigurationTypeAdapterFactory {
    private Map<Class, ConfigurationTypeAdapter> adaptersMap;

    @Autowired
    public ConfigurationTypeAdapterFactory(List<ConfigurationTypeAdapter> adapters) {
        adaptersMap = adapters.stream()
                .collect(Collectors.toMap(ConfigurationTypeAdapter::getMuleConfigurationType, Function.identity()));
    }

    public ConfigurationTypeAdapter createAdapter(Object value) {
        if (!adaptersMap.containsKey(value.getClass())) {
            log.warn("Could not find configuration adapter for '" + value.getClass().getName()+"'");
            return null;
        }
        ConfigurationTypeAdapter adapter = adaptersMap.get(value.getClass());
        adapter.setMuleConfiguration(value);

        return adapter;
    }
}
