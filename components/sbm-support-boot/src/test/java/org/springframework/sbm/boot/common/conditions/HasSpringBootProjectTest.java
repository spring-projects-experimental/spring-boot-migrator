package org.springframework.sbm.boot.common.conditions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HasSpringBootProjectTest {
    @Mock
    HasDecalredSpringBootStarterParent parentCondition;
    @Mock
    HasSpringBootDependencyImport importCondition;
    @Mock
    HasSpringBootDependencyManuallyManaged manuallyManagedCondition;

    @InjectMocks
    IsSpringBootProject hasSpringBootProject;

    @ParameterizedTest
    @CsvSource(value = {
            //isParentManaged, dependencyManaged, manuallyManaged, expected
            "true,             false,             false,           true",
            "false,            true,              false,           true",
            "false,            false,             true,            true",
            "true,             true,              true,            true",
            "false,            false,             false,           false",

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

    @Test
    public void conditionShouldBeTrueForSpringParent() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.6.0</version>
                        <relativePath/> <!-- lookup parent from repository -->
                    </parent>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                </project>
                        """)
                .build();

        IsSpringBootProject condition = new IsSpringBootProject();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isFalse();
    }

}
