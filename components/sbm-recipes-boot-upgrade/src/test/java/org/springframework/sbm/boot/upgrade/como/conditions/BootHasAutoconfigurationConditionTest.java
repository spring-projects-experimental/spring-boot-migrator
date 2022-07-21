package org.springframework.sbm.boot.upgrade.como.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class BootHasAutoconfigurationConditionTest {

    @Test
    void shouldBeTrueForSpringFactoriesWithEnableAutoConfigurationKey() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/resources/META-INF/spring.factories", "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ")
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isTrue();
    }

//    @Test
//    void shouldBeFalseForSpringFactoriesWithoutEnableAutoConfigurationKey() {
//        ProjectContext context = TestProjectContext.buildProjectContext()
//                .addProjectResource("META-INF/spring.factories", "test=XYZ")
//                .build();
//
//        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
//        assertThat(condition.evaluate(context)).isFalse();
//    }
//
//    @Test
//    void shouldBeFalseForNotExistingSpringFactories() {
//        ProjectContext context = TestProjectContext.buildProjectContext()
//                .addProjectResource("META-INF/xyz", "test=XYZ")
//                .build();
//
//        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
//        assertThat(condition.evaluate(context)).isFalse();
//    }
//
}