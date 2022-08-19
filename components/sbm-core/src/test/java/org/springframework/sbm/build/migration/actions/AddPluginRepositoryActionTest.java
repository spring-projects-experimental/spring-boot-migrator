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

package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

public class AddPluginRepositoryActionTest {

    String pomWithoutRepositories =
            """
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <groupId>some.group.id</groupId>
                <artifactId>with-artifact</artifactId>
                <packaging>jar</packaging>
                <version>100.23.01-SNAPSHOT</version>
            </project>
            """;

    @Test
    void shouldAddPluginRepositoryWhenThereIsNoPluginRepository() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomWithoutRepositories)
                .build();

        AddPluginRepositoryAction sut = new AddPluginRepositoryAction();
        sut.setId("repo-id");
        sut.setName("thename");
        sut.setUrl("https://some.url");
        sut.apply(context);

        assertThat(context.getBuildFile().print()).isEqualTo(
                """
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>some.group.id</groupId>
                            <artifactId>with-artifact</artifactId>
                            <packaging>jar</packaging>
                            <version>100.23.01-SNAPSHOT</version>
                            <pluginRepositories>
                                <pluginRepository>
                                    <id>repo-id</id>
                                    <name>thename</name>
                                    <url>https://some.url</url>
                                </pluginRepository>
                            </pluginRepositories>
                        </project>
                        """
        );
    }
}
