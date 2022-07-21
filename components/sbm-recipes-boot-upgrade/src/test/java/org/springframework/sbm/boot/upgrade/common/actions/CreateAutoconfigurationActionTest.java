package org.springframework.sbm.boot.upgrade.common.actions;


import org.junit.jupiter.api.Test;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAutoconfigurationActionTest {

    @Test
    public void autoConfigurationImportsIsGenerated() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ"
                )
                .build();

        CreateAutoconfigurationAction action = new CreateAutoconfigurationAction();

        action.apply(context);

        assertThat(context
                .search(
                        new PathPatternMatchingProjectResourceFinder("/**/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
                )
        ).hasSize(1);
    }
}
