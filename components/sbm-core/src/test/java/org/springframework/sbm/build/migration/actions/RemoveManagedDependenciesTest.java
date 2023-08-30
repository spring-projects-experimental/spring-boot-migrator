/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.Test;
import org.openrewrite.semver.LatestRelease;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.ActionTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RemoveManagedDependenciesTest {

    @Test
    public void givenProjectWithManagedDependency_removeSpringManagedDependencies_expectHibernateDependencyRemoved(){

        LatestRelease latestRelease = new LatestRelease(null);
        System.out.println(latestRelease.compare(null, "5.6.11.Final", "5.6.11.Final"));

        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.6.11.Final";
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        ActionTest.withProjectContext(TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates))
                .actionUnderTest(new RemoveManagedDependencies())
                .verify(projectContext -> assertThat(projectContext.getBuildFile()
                                                             .getDeclaredDependencies()
                                                             .stream()
                                                             .map(Dependency::getCoordinates)
                                                             .anyMatch(hibernateCoordinates::equals)).isFalse()
                );

    }

    @Test
    public void givenProjectWithLowerVersionedManagedDependency_removeSpringManagedDependencies_expectDependencyRemoved(){
        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.6.10.Final";
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
    public void givenProjectWithSameVersionedManagedDependency_removeSpringManagedDependencies_expectDependencyRemoved(){
        // explicitly declared
        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.6.11.Final";
        // brings managed hibernate 5.6.11.Final
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        ActionTest.withProjectContext(TestProjectContext.buildProjectContext().withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates))
                .actionUnderTest(new RemoveManagedDependencies())
                .verify(projectContext -> {
                    List<Dependency> declaredDependencies = projectContext
                            .getApplicationModules()
                            .getRootModule()
                            .getBuildFile()
                            .getDeclaredDependencies();

                    // only one dependency left
                    assertThat(declaredDependencies.size()).isEqualTo(1);
                    // dependency to older hibernate was removed
                    assertThat(declaredDependencies
                                       .get(0)
                                       .getCoordinates())
                            .isEqualTo(springBootDataJpaCoordinates);
                });
    }

    @Test
    public void givenProjectWithHigherVersionedManagedDependency_removeSpringManagedDependencies_expectDependencyKept(){
        final String hibernateCoordinates = "org.hibernate:hibernate-core:5.6.12.Final";
        // brings older hibernate 5.6.11.Final
        final String springBootDataJpaCoordinates = "org.springframework.boot:spring-boot-starter-data-jpa:2.7.4";

        RemoveManagedDependencies removeManagedDependencies = new RemoveManagedDependencies();
        ActionTest.withProjectContext(
                    TestProjectContext.buildProjectContext().withBuildFileHavingDependencies(hibernateCoordinates, springBootDataJpaCoordinates)
                )
                .actionUnderTest(removeManagedDependencies)
                .verify(projectContext -> {
                    List<Dependency> declaredDependencies = projectContext
                            .getApplicationModules()
                            .getRootModule()
                            .getBuildFile()
                            .getDeclaredDependencies();

                    // both dependencies kept
                    assertThat(declaredDependencies.size()).isEqualTo(2);
                    assertThat(declaredDependencies
                                       .get(0)
                                       .getCoordinates())
                            .isEqualTo(hibernateCoordinates);
                    assertThat(declaredDependencies
                                       .get(1)
                                       .getCoordinates())
                            .isEqualTo(springBootDataJpaCoordinates);
                });
    }
}
