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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanTest {

    @Test
    public void shouldReturnSimpleClassName() {
        Bean bean = new Bean("beanName", "org.springframework.integration.dsl.IntegrationFlow");
        assertThat(bean.getDependentBeanSimpleName()).isEqualTo("IntegrationFlow");
    }

    @Test
    public void shouldThrowErrorWhenBeanClassIsNull() {
        Bean bean = new Bean("beanName", null);
        assertThrows(IllegalStateException.class, bean::getDependentBeanSimpleName);
    }

    @Test
    public void shouldThrowErrorWhenBeanClassIsEmpty() {
        Bean bean = new Bean("beanName", "");
        assertThrows(IllegalStateException.class, bean::getDependentBeanSimpleName);
    }

    @Test
    public void shouldReturnSimpleClassNameIfBeanClassContainsSimpleClassName() {
        Bean bean = new Bean("beanName", "IntegrationFlow");
        assertThat(bean.getDependentBeanSimpleName()).isEqualTo("IntegrationFlow");
    }
}
