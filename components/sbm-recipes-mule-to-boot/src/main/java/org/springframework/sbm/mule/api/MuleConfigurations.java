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

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MuleConfigurations <T> {
    @Getter
    private Map<String, ? extends ConfigurationTypeAdapter> configurations = new HashMap<>();

    public MuleConfigurations(Map<String, ? extends ConfigurationTypeAdapter> allAvailableConfigurations) {
        if (allAvailableConfigurations != null) {
            configurations = allAvailableConfigurations;
        }
    }

    public Optional<ConfigurationTypeAdapter> find(String configRef) {
        return Optional.ofNullable(configurations.get(configRef));
    }

    public
    Optional<? extends ConfigurationTypeAdapter> findByType(Class<ConfigurationTypeAdapter> cls) {

        return configurations
                .values()
                .stream()
                .filter(adapter -> cls.isAssignableFrom(adapter.getClass()))
                .findAny();
    }
}
