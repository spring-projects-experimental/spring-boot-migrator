package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_27_30_AddLoggingDateFormatTest {

    private static final String DUMMY_PROPERTY_FILE = "foo=bar\n" +
            "defaultBasePackage=org.springframework.sbm";

    @Test
    public void givenAProjectWithoutLoggingDateFormatOverride_andSpringBootProperties_applyAction_expectPropertyAdded(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource("main/resources/application.properties", DUMMY_PROPERTY_FILE)
                .build();

        Boot_27_30_AddLoggingDateFormat action = new Boot_27_30_AddLoggingDateFormat();
        action.apply(projectContext);

        List<SpringBootApplicationProperties> bootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(projectContext.getProjectResources());
        assertThat(bootApplicationProperties.size()).isEqualTo(1);
        assertThat(bootApplicationProperties.get(0).getProperty("logging.pattern.dateformat").isPresent()).isTrue();
    }
}
