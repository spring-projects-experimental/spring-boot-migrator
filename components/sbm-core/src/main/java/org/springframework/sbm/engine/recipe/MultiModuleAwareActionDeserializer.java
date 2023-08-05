/*
 * Copyright 2021 - 2023 the original author or authors.
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/***
 *
 * @deprecated Use "One Action implementation per use case" instead, see https://github.com/spring-projects-experimental/spring-boot-migrator/discussions/345
 */
@Component
@RequiredArgsConstructor
@Deprecated(forRemoval = true)
public class MultiModuleAwareActionDeserializer implements ActionDeserializer {

    protected final ObjectMapper yamlObjectMapper;

    protected final AutowireCapableBeanFactory beanFactory;

    @Override
    public Action deserialize(ObjectMapper tolerantObjectMapper, Class<? extends Action> actionClass, JsonNode node, AutowireCapableBeanFactory parser) {
        try {
            MultiModuleAwareAction action = (MultiModuleAwareAction) yamlObjectMapper.convertValue(node, actionClass);
            JsonNode multiModuleHandler = node.get("multiModuleHandler");
            if (multiModuleHandler != null) {
//                YamlResourceLoader yamlResourceLoader = new YamlResourceLoader(
//                        new ByteArrayInputStream(multiModuleHandler.toString().getBytes(StandardCharsets.UTF_8)),
//                        URI.create("recipe.yaml"), new Properties());
                String type = multiModuleHandler.get("type").textValue();
                Class<?> aClass = Class.forName(type);
                Constructor<?> constructor = aClass.getConstructor(null);
                Object o = constructor.newInstance();
                aClass.cast(o);
                action.setMultiModuleHandler((MultiModuleHandler) o);
                ((MultiModuleHandler) o).setAction(action);
            }
            return action;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canHandle(Class<? extends Action> actionClass) {
        return ComposableAction.class.isAssignableFrom(actionClass);
    }
}
