package org.springframework.sbm.boot.properties.finder;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootDefaultPropertiesFinderTest {

    @Test
    public void givenAProjectWithDefaultSpringBootProperties_applyFinder_expectPropertyFile(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource(Path.of("src","main", "resources", "application.properties"), "foo=bar")
                .build();

        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();
        assertThat(springBootDefaultPropertiesFinder.apply(projectContext.getProjectResources()).isPresent()).isTrue();
    }

    @Test
    public void givenAProjectWithoutDefaultSpringBootProperties_applyFinder_expectPropertyFile(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .build();

        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();
        assertThat(springBootDefaultPropertiesFinder.apply(projectContext.getProjectResources()).isEmpty()).isTrue();
    }
}
