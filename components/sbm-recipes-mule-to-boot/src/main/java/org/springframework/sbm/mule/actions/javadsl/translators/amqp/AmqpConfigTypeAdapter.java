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
package org.springframework.sbm.mule.actions.javadsl.translators.amqp;

import org.springframework.sbm.mule.api.ConfigurationTypeAdapter;
import org.mulesoft.schema.mule.amqp.AmqpConnectorType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

@Component
public class AmqpConfigTypeAdapter extends ConfigurationTypeAdapter<AmqpConnectorType> {

    @Override
    public String getName() {
        return getMuleConfiguration().getName();
    }

    @Override
    public Class getMuleConfigurationType() {
        return AmqpConnectorType.class;
    }

    @Override
    public List<SimpleEntry<String, String>> configProperties() {
        List<SimpleEntry<String, String>> properties = new ArrayList<>();
        properties.add(new SimpleEntry<String, String>("spring.rabbitmq.host", getMuleConfiguration().getHost()));
        properties.add(new SimpleEntry<String, String>("spring.rabbitmq.port", getMuleConfiguration().getPort()));

        return properties;
    }
}
