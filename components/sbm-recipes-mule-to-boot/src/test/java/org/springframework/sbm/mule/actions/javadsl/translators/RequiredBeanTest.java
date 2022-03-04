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

class RequiredBeanTest {

    @Test
    public void shouldReturnSimpleClassName() {
        RequiredBean bean = new RequiredBean("beanName", "org.springframework.integration.dsl.IntegrationFlow");
        assertThat(bean.getBeanClassSimpleName()).isEqualTo("IntegrationFlow");
    }

    @Test
    public void shouldThrowErrorWhenBeanClassIsNull() {
        RequiredBean bean = new RequiredBean("beanName", null);
        assertThrows(IllegalStateException.class, bean::getBeanClassSimpleName);
    }

    @Test
    public void shouldThrowErrorWhenBeanClassIsEmpty() {
        RequiredBean bean = new RequiredBean("beanName", "");
        assertThrows(IllegalStateException.class, bean::getBeanClassSimpleName);
    }

    @Test
    public void shouldReturnSimpleClassNameIfBeanClassContainsSimpleClassName() {
        RequiredBean bean = new RequiredBean("beanName", "IntegrationFlow");
        assertThat(bean.getBeanClassSimpleName()).isEqualTo("IntegrationFlow");
    }
}
