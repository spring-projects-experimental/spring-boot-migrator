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
package org.springframework.sbm.engine.recipe;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class YamlObjectMapperConfiguration {

    @Bean
    YAMLMapper yamlObjectMapper() {
        YAMLMapper yamlMapper = new YAMLMapper();
        return yamlMapper;
    }

    @Bean
    ActionDeserializationDispatcher actionDeserializationDispatcher(AutowireCapableBeanFactory beanFactory, YAMLMapper yamlObjectMapper, ActionDeserializerRegistry actionDeserializers) {
        ActionDeserializationDispatcher actionDeserializationDispatcher = new ActionDeserializationDispatcher(yamlObjectMapper, beanFactory, actionDeserializers);
        yamlObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module =
                new SimpleModule("CustomDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Condition.class, new ConditionDeserializer(yamlObjectMapper, beanFactory));
        module.addDeserializer(Action.class, actionDeserializationDispatcher);
        yamlObjectMapper.registerModule(module);
        return actionDeserializationDispatcher;
    }
}
