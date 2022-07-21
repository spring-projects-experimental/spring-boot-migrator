package org.springframework.sbm.boot.upgrade.como.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class BootHasAutoconfigurationConditionTest {

    private final String content = "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ";

    @Test
    void shouldBeTrueForSpringFactoriesWithEnableAutoConfigurationKey() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        content
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isTrue();
    }

    @Test
    void shouldOnlyDetectIfItsInCorrectPath() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/META-INF/spring.factories",
                        content
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isFalse();

        context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/spring.factories",
                        content
                )
                .build();
        assertThat(condition.evaluate(context)).isFalse();
    }

    @Test
    void shouldOnlyDetectIfFileAndContentIsRight() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        "Hello World"
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isFalse();
    }

    @Test
    void shouldOnlyDetectIfPropertyKeyIsRight() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        "Hello World org.springframework.boot.autoconfigure.EnableAutoConfiguration"
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isFalse();
    }

    @Test
    void itCanDoMultiLine() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=XTZ\
                                abc
                                """
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isTrue();
    }
}
