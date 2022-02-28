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

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

public abstract class ConfigurationTypeAdapter<T> {

    private T muleConfiguration;

    public void setMuleConfiguration(T muleConfiguration) {
        this.muleConfiguration = muleConfiguration;
    }

    public T getMuleConfiguration() {
        return muleConfiguration;
    }

    abstract public String getName();

    abstract public Class getMuleConfigurationType();

    abstract public List<SimpleEntry<String, String>> configProperties();
}
