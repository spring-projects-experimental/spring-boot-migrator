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
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultActionDeserializer implements ActionDeserializer {
    @Override
    public Action deserialize(ObjectMapper tolerantObjectMapper, Class<? extends Action> actionClass, JsonNode node, AutowireCapableBeanFactory beanFactory) {
        Action action = tolerantObjectMapper.convertValue(node, actionClass);
        beanFactory.autowireBean(action);
        return action;
    }

    @Override
    public boolean canHandle(Class<? extends Action> actionClass) {
        // TODO: 270 extend hierarchy by DefaultAction type because ComposableAction and DefaultAction will be an AbstractAction
        return AbstractAction.class.isAssignableFrom(actionClass) && !ComposableAction.class.isAssignableFrom(actionClass);
    }
}
