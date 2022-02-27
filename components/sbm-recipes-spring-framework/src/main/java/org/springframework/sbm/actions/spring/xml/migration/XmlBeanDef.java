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
package org.springframework.sbm.actions.spring.xml.migration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@ToString
public class XmlBeanDef {
    private final String name;
    private final BeanDefinition beanDefinition;

    public XmlBeanDef(String name, BeanDefinition beanDefinition) {
        this.name = name;
        this.beanDefinition = beanDefinition;
    }

    public Optional<Class> getTypeOfProperty(ClassLoader classLoader, String property) {
        boolean hasProperty = getBeanDefinition().getPropertyValues().stream()
                .filter(v -> v.getValue().getClass().isAssignableFrom(TypedStringValue.class))
                .map(v -> (TypedStringValue) v.getValue())
                .filter(v -> v.getValue().equals("${" + property + "}"))
                .findFirst()
                .isPresent();
        if(hasProperty) {
            try {
                Class<?> aClass = classLoader.loadClass(beanDefinition.getBeanClassName());
                Class<?> aClass1 = Arrays.stream(aClass.getMethods())
                        .filter(m -> m.getName().equals("set" + Helper.uppercaseFirstChar(property)))
                        .map(m -> m.getParameterTypes()[0])
                        .findFirst().get();
                return Optional.of(aClass1);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } else {
            return Optional.empty();
        }

    }
}