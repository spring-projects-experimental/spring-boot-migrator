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
package org.springframework.sbm.mule.actions.javadsl.translators;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class RequiredBean {
    private final String beanName;
    /**
     * Fully qualified name of the bean type
     */
    private final String beanClass;


    public String getBeanClassSimpleName() {

        if (beanClass == null || beanClass.isEmpty()) {
            throw  new IllegalStateException("Bean class is empty");
        }

        String[] classTree = beanClass.split("\\.");
        return classTree[classTree.length - 1];
    }
}
