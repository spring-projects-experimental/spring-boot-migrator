package org.springframework.sbm.boot.upgrade.como.conditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class BootHasAutoconfigurationConditionTest {

    private static final String content = "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ";

    @ParameterizedTest
    @CsvSource(value = {
            "src/main/resources/META-INF/spring.factories," + content + ",true",
            "src/main/resources/META-INF/META-INF/spring.factories," + content + ",false",
            "src/main/resources/META-INF/spring.factories,Hello World,false",
            "src/main/resources/META-INF/spring.factories,Hello World org.springframework.boot.autoconfigure.EnableAutoConfiguration,false",
    }, delimiter = ',')
    void conditionTests(String filePath, String fileContent, boolean expectation) {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        filePath,
                        fileContent
                )
                .build();

        BootHasAutoconfigurationCondition condition = new BootHasAutoconfigurationCondition();
        assertThat(condition.evaluate(context)).isEqualTo(expectation);
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
