package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.Test;
import org.openrewrite.semver.LatestRelease;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RemoveManagedDependenciesTest {

    @Test
    public void givenProjectWithManagedDependency_removeSpringManagedDependencies_expectHibernateDependencyRemoved(){

        LatestRelease latestRelease = new LatestRelease(null);
        System.out.println(latestRelease.compare(null, "5.6.11.Final", "5.6.11.Final"));

        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.6.11.Final";
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        final ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates)
                .build();

        RemoveManagedDependencies removeManagedDependencies = new RemoveManagedDependencies();
        removeManagedDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile()
                .getDeclaredDependencies()
                .stream()
                .map(Dependency::getCoordinates)
                .anyMatch(hibernateCoordinates::equals)
        ).isFalse();
    }

    @Test
    public void givenProjectWithLowerVersionedManagedDependency_removeSpringManagedDependencies_expectDependencyRemoved(){
        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.3.2.Final";
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        final ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates)
                .build();

        RemoveManagedDependencies removeManagedDependencies = new RemoveManagedDependencies();
        removeManagedDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile()
                .getDeclaredDependencies()
                .stream()
                .map(Dependency::getCoordinates)
                .anyMatch(hibernateCoordinates::equals)
        ).isFalse();
    }

    @Test
    public void givenProjectWithHigherVersionedManagedDependency_removeSpringManagedDependencies_expectDependencyRemoved(){
        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.12.2.Final";
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        final ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates)
                .build();

        RemoveManagedDependencies removeManagedDependencies = new RemoveManagedDependencies();
        removeManagedDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile()
                .getDeclaredDependencies()
                .stream()
                .map(Dependency::getCoordinates)
                .anyMatch(hibernateCoordinates::equals)
        ).isTrue();
    }
}
