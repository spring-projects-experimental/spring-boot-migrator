package org.springframework.sbm.boot;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.migration.actions.AddRepositoryAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

public class AddRepositoryActionTest {

    @Test
    public void shouldAddRepository() {
        AddRepositoryAction addRepositoryAction = new AddRepositoryAction();
        addRepositoryAction.setId("myId");
        addRepositoryAction.setUrl("myUrl");
        ProjectContext context = TestProjectContext.buildProjectContext().build();
        addRepositoryAction.apply(context);

        assertThat(context.getBuildFile().getRepositories()).hasSize(1);
    }
}
