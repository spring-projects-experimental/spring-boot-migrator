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
package org.springframework.sbm.boot.upgrade_27_30.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.ActionDeserializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Fabian Krüger
 */
@Component
public class SpringBootUpgradeReportActionDeserializer implements ActionDeserializer {
    @Override
    public Action deserialize(ObjectMapper tolerantObjectMapper, Class<? extends Action> actionClass, JsonNode node, AutowireCapableBeanFactory beanFactory) {
        try {
            SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) tolerantObjectMapper.convertValue(node, actionClass);

            JsonNode dataProviderNode = node.get("dataProvider");
            if(dataProviderNode != null) {
                String className = dataProviderNode.textValue();
                if(className == null || className.isEmpty()) {
                    throw new IllegalArgumentException("dataProvider for Action '"+action.getDescription()+"' was found but no fully qualified classname was provided.");
                }
                Class<?> aClass = Class.forName(className);
                Constructor<?> constructor = aClass.getConstructor(null);
                Object o = constructor.newInstance();
                SpringBootUpgradeReportAction.DataProvider dataProvider = (SpringBootUpgradeReportAction.DataProvider) o;
                action.setDataProvider(dataProvider);
            }

            JsonNode sections = node.get("sections");
            if (sections != null) {
                if(ArrayNode.class.isInstance(sections)) {
                    ArrayNode arrayNode = (ArrayNode) sections;
                    for(int i=0; i < arrayNode.size(); i++) {
                        JsonNode helper = arrayNode.get(i).get("helper");
                        String type = helper.textValue();
                        Class<?> aClass = Class.forName(type);
                        Constructor<?> constructor = aClass.getConstructor(null);
                        Object o = constructor.newInstance();
                        SpringBootUpgradeReportSection.Helper cast = (SpringBootUpgradeReportSection.Helper) aClass.cast(o);
                        SpringBootUpgradeReportSection section = action.getSections().get(i);
                        section.setHelper(cast);
                        beanFactory.autowireBean(section);
                    }
                }
            } else {
                throw new IllegalArgumentException("Could not find required field 'sections' for SpringBootUpgradeReportAction with description '"+action.getDescription()+"'.");
            }
            beanFactory.autowireBean(action);
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
    public boolean canHandle(Class<? extends Action> key) {
        return SpringBootUpgradeReportAction.class.isAssignableFrom(key);
    }
}
