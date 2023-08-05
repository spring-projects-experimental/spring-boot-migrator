/*
 * Copyright 2021 - 2022 the original author or authors.
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
package org.springframework.sbm.build.migration.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Fabian Krüger
 */
class NoMoreRecentManagedDependencyExistsTest {

    @Test
    void shouldReturnFalseForManagedDependencyWithSameVersion() {
        ProjectContext projectContext = createProjectContextWithPom("2.7.3");

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-dependencies");
        sut.setVersion("2.7.3");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForManagedDependencyWithMoreRecentVersion() {
        ProjectContext projectContext = createProjectContextWithPom("2.7.4");

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-dependencies");
        sut.setVersion("2.7.3");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForManagedDependencyWithLowerVersion() {
        ProjectContext projectContext = createProjectContextWithPom("2.7.2");

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-dependencies");
        sut.setVersion("2.7.3");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForManagedDependencyWithDifferentGroupId() {
        ProjectContext projectContext = createProjectContextWithPom("2.7.3");

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.different");
        sut.setArtifactId("spring-boot-dependencies");
        sut.setVersion("2.7.2");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForManagedDependencyWithDifferentArtifactId() {
        ProjectContext projectContext = createProjectContextWithPom("2.7.3");

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-different");
        sut.setVersion("2.7.2");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueWhenNoManagedDependencyExists() {
        String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                	<modelVersion>4.0.0</modelVersion>
                	<groupId>com.example</groupId>
                	<artifactId>demo</artifactId>
                	<version>0.0.1-SNAPSHOT</version>
                	<name>demo</name>
                	<description>Demo project for Spring Boot</description>
                	<properties>
                		<java.version>17</java.version>
                	</properties>
                </project>
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pom).build();

        NoMoreRecentManagedDependencyExists sut = new NoMoreRecentManagedDependencyExists();
        sut.setGroupId("org.springframework.boot");
        sut.setArtifactId("spring-boot-different");
        sut.setVersion("2.7.2");

        boolean result = sut.evaluate(projectContext);
        assertThat(result).isTrue();
    }


    private ProjectContext createProjectContextWithPom(String version) {
        return TestProjectContext.buildProjectContext().withMavenRootBuildFileSource("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                	<modelVersion>4.0.0</modelVersion>
                	<groupId>com.example</groupId>
                	<artifactId>demo</artifactId>
                	<version>0.0.1-SNAPSHOT</version>
                	<name>demo</name>
                	<description>Demo project for Spring Boot</description>
                	<properties>
                		<java.version>17</java.version>
                	</properties>
                	<dependencyManagement>
                		<dependencies>
                			<dependency>
                				<groupId>org.springframework.boot</groupId>
                				<artifactId>spring-boot-dependencies</artifactId>
                				<version>%s</version>
                				<type>pom</type>
                				<scope>import</scope>
                			</dependency>
                		</dependencies>
                	</dependencyManagement>
                </project>
                """.formatted(version))
                .build();
    }



}