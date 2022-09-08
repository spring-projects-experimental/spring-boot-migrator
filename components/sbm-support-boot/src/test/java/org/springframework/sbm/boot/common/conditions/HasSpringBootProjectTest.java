package org.springframework.sbm.boot.common.conditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HasSpringBootProjectTest {
    @Mock
    HasSpringBootStarterParent parentCondition;
    @Mock
    HasSpringBootDependencyImport importCondition;
    @Mock
    HasSpringBootDependencyManuallyManaged manuallyManagedCondition;

    @InjectMocks
    HasSpringBootProject hasSpringBootProject;

    @ParameterizedTest
    @CsvSource(value = {
            //isParentManaged, dependencyManaged, manuallyManaged, expected
            "true,             false,             false,           true"
    })
    public void conditionShouldBeTrueIfOneIsTrue(boolean isParentManaged,
                                                 boolean dependencyManaged,
                                                 boolean manuallyManaged,
                                                 boolean expectedResult) {
        ProjectContext context = TestProjectContext.buildProjectContext().build();
        when(parentCondition.evaluate(context)).thenReturn(isParentManaged);
        when(importCondition.evaluate(context)).thenReturn(dependencyManaged);
        when(manuallyManagedCondition.evaluate(context)).thenReturn(manuallyManaged);

        assertThat(hasSpringBootProject.evaluate(context)).isEqualTo(expectedResult);
    }
}
