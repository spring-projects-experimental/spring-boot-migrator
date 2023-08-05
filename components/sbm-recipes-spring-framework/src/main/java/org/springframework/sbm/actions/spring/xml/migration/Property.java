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
package org.springframework.sbm.actions.spring.xml.migration;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class Property {
    private final Object key;
    private final Object value;
    private Class<?> propertyType = String.class;

    public Property(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public void setType(Class<?> propertyType) {
        this.propertyType = propertyType;
    }

    public String getFieldName() {
        // TODO: handle '-', '_', ...
        String fieldNameFirstUpperCase = Arrays.stream(key.toString().split("\\."))
                .map(Helper::uppercaseFirstChar)
                .collect(Collectors.joining());
        return Helper.lowercaseFirstChar(fieldNameFirstUpperCase);
    }
}
