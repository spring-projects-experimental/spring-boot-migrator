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
