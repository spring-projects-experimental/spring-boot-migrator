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
package org.springframework.sbm.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.sbm.engine.recipe.FrameworkSupportAction;
import org.springframework.sbm.engine.recipe.ActionDeserializer;
import org.springframework.sbm.engine.recipe.ActionDeserializerRegistry;
import org.springframework.sbm.engine.context.RewriteJavaSearchActionDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class OpenRewriteObjectMapperConfiguration {


    @Autowired
    private ActionDeserializerRegistry actionDeserializerRegistry;

    @Autowired
    private ObjectMapper yamlObjectMapper;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @PostConstruct
    void registerActionDeserializer() {
        ActionDeserializer actionDeserializer = new RewriteJavaSearchActionDeserializer(yamlObjectMapper, beanFactory);
        actionDeserializerRegistry.register(FrameworkSupportAction.class, actionDeserializer);
    }
}
