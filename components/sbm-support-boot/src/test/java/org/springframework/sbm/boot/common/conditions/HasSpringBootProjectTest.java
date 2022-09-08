package org.springframework.sbm.boot.common.conditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HasSpringBootProjectTest {
    @Mock
    HasSpringBootStarterParent parentCondition;
    @Mock
    HasSpringBootDependencyImport importCondition;
    @Mock
    HasSpringBootDependencyManuallyManaged manuallyManagedCondition;

    @InjectMocks
    HasSpringBootProject hasSpringBootProject;

    @Test
    public void conditionShouldBeTrueIfOneIsTrue() {
        ProjectContext context = TestProjectContext.buildProjectContext().build();
        when(parentCondition.evaluate(context)).thenReturn(true);
        when(importCondition.evaluate(context)).thenReturn(false);
        when(manuallyManagedCondition.evaluate(context)).thenReturn(false);

        boolean result = hasSpringBootProject.evaluate(context);

        assertThat(result).isTrue();
    }


}