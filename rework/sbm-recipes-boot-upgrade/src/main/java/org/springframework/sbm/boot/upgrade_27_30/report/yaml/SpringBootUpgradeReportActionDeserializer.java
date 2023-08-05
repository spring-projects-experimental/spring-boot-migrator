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
package org.springframework.sbm.boot.upgrade_27_30.report.yaml;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.ActionDeserializer;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.recipe.ConditionDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Fabian Krüger
 */
@Component
public class SpringBootUpgradeReportActionDeserializer implements ActionDeserializer {

    protected final AutowireCapableBeanFactory beanFactory;

    public SpringBootUpgradeReportActionDeserializer(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    //        @Override
    public Action deserialize(ObjectMapper tolerantObjectMapper, Class<? extends Action> actionClass, JsonNode node, AutowireCapableBeanFactory beanFactory) {
        SpringBootUpgradeReportAction action = (SpringBootUpgradeReportAction) tolerantObjectMapper.convertValue(node, actionClass);
        beanFactory.autowireBean(action);
        action.getSections().stream().forEach(s -> beanFactory.autowireBean(s));
        return action;
    }

    //    @Override
    public boolean canHandle(Class<? extends Action> key) {
        return SpringBootUpgradeReportAction.class.isAssignableFrom(key);
    }

}
