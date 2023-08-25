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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.sbm.java.migration.conditions.HasMemberAnnotation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class ConditionDeserializerTest {
    @Test
    void deserializeCondition() throws JsonProcessingException {
        String yaml =
                """
                type: org.springframework.sbm.java.migration.conditions.HasMemberAnnotation
                annotation: "some.Annotation"
                """;

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule moule = new SimpleModule("ConditionModule");
        AutowireCapableBeanFactory beanFactory = Mockito.mock(AutowireCapableBeanFactory.class);
        moule.addDeserializer(Condition.class, new ConditionDeserializer(objectMapper, beanFactory));
        objectMapper.registerModule(moule);

        Condition condition = objectMapper.readValue(yaml, Condition.class);

        assertThat(condition).isInstanceOf(HasMemberAnnotation.class);
        HasMemberAnnotation typedCondition = (HasMemberAnnotation) condition;
        assertThat(typedCondition.getDescription()).isEqualTo("If there are any fields annotated with some.Annotation");
        assertThat(typedCondition).hasFieldOrPropertyWithValue("annotation", "some.Annotation");
    }
}