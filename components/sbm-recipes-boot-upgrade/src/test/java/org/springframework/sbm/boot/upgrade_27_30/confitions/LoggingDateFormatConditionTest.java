package org.springframework.sbm.boot.upgrade_27_30.confitions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.upgrade_27_30.conditions.LoggingDateFormatCondition;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoggingDateFormatConditionTest {

    private static final String APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT = "foo=bar\n" +
            "migrate=true\n" +
            "logging.pattern.dateformat=xyz\n";

    private static final String APPLICATION_PROPERTIES_WITHOUT_LOG_DATE_FORMAT = "foo=bar\n" +
            "migrate=true\n";


    @Test
    public void givenProjectWithLogDateFormatCustomization_evaluateCondition_expectFalse(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT)
                .build();

        LoggingDateFormatCondition condition = new LoggingDateFormatCondition();

        assertThat(condition.evaluate(projectContext)).isFalse();
    }

    @Test
    public void givenProjectWithoutLogDateFormatCustomization_evaluateCondition_expectTrue(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITHOUT_LOG_DATE_FORMAT)
                .build();

        LoggingDateFormatCondition condition = new LoggingDateFormatCondition();

        assertThat(condition.evaluate(projectContext)).isTrue();
    }
}
