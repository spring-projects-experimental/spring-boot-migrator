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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ActionDeserializationDispatcher extends StdDeserializer<Action> {

    private final ObjectMapper tolerantObjectMapper;

    private final AutowireCapableBeanFactory beanFactory;

    private final ActionDeserializerRegistry actionDeserializers;

    public ActionDeserializationDispatcher(ObjectMapper tolerantObjectMapper, AutowireCapableBeanFactory beanFactory, ActionDeserializerRegistry actionDeserializers) {
        super(Action.class);
        this.tolerantObjectMapper = tolerantObjectMapper;
        this.beanFactory = beanFactory;
        this.actionDeserializers = actionDeserializers;
    }

    @Override
    public Action deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        JsonNode type = node.get("type");
        if (type == null) {
            throw new IllegalArgumentException("Type of an action must be specified");
        }

        String clazz = type.textValue();
        Class<? extends Action> actionClass;
        try {
            actionClass = (Class<? extends Action>) Class.forName(clazz);
            if (!Action.class.isAssignableFrom(actionClass)) {
                throw new IllegalArgumentException("The class given as type attribute of action [" + actionClass + "] is not of type Action.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Type attribute of action references an unknown class [" + clazz + "]", e);
        }

        ActionDeserializer actionDeserializer = actionDeserializers.get(actionClass);
        Action action = actionDeserializer.deserialize(tolerantObjectMapper, actionClass, node, beanFactory);
        return action;
    }

}
