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
import org.springframework.util.ClassUtils;

import java.io.IOException;

public class ConditionDeserializer extends StdDeserializer<Condition> {

    private final ObjectMapper objectMapper;

    private final AutowireCapableBeanFactory beanFactory;

    public ConditionDeserializer(ObjectMapper objectMapper, AutowireCapableBeanFactory beanFactory) {
        super(Condition.class);
        this.objectMapper = objectMapper;
        this.beanFactory = beanFactory;
    }

    @Override
    public Condition deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode rootNode = codec.readTree(parser);

        JsonNode type = rootNode.get("type");
        if (type == null) {
            throw new IllegalArgumentException("Type of a condition must be specified");
        }

        String clazz = type.textValue();
        Class<? extends Condition> conditionClass;
        try {
            conditionClass = (Class<? extends Condition>) Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown class [" + clazz + "]", e);
        }
        if (!ClassUtils.getAllInterfacesForClassAsSet(conditionClass).contains(Condition.class)) {
            throw new IllegalArgumentException("Class [" + clazz + "] doesn't implement Condition interface");
        }

        Condition condition = objectMapper.convertValue(rootNode, conditionClass);
        beanFactory.autowireBean(condition);
        return condition;
    }
}
