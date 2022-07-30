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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.tree.Scope;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JerseyTemporarilyRemovedFinderTest {

    @Test
    void finderShouldFindAnyJerseyDependency() {
        String dependencyCoordinates = "org.glassfish.jersey.connectors:jersey-jdk-connector:2.35";
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(dependencyCoordinates)
                .build();

        JerseyTemporarilyRemovedFinder sut = new JerseyTemporarilyRemovedFinder();
        Set<ApplicationModule> matches = sut.findMatches(context);
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(1);
        assertThat(matches.iterator().next().getBuildFile().getDeclaredDependencies(Scope.Compile).get(0).getCoordinates()).isEqualTo(dependencyCoordinates);
    }

    @Test
    void finderShouldFindOnlyJerseyDependency() {

        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module1</module>
                        <module>module2</module>
                    </modules>
                </project>
                """;


        @Language("xml")
        String module1PomXml =
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>com.example</groupId>
                            <artifactId>parent</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </parent>
                        <artifactId>module1</artifactId>
                        <packaging>jar</packaging>
                        <dependencies>
                            <dependency>
                                <groupId>org.glassfish</groupId>
                                <artifactId>javax.el</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                        </dependencies>
                    </project>
                    """;

        @Language("xml")
        String module2PomXml =
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>com.example</groupId>
                            <version>0.1.0-SNAPSHOT</version>
                            <artifactId>parent</artifactId>
                        </parent>
                        <artifactId>module2</artifactId>
                        <packaging>jar</packaging>
                        <dependencies>
                            <dependency>
                                <groupId>org.glassfish.jersey.connectors</groupId>
                                <artifactId>jersey-jdk-connector</artifactId>
                                <version>2.35</version>
                            </dependency>
                                    
                        </dependencies>
                    </project>
                    """;

        String jerseyDependencyCoordinates = "org.glassfish.jersey.connectors:jersey-jdk-connector:2.35";
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .withMavenBuildFileSource("module2", module2PomXml)
                .build();

        JerseyTemporarilyRemovedFinder sut = new JerseyTemporarilyRemovedFinder();
        Set<ApplicationModule> matches = sut.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(3);
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(1);
        assertThat(matches.iterator().next().getBuildFile().getDeclaredDependencies(Scope.Compile).get(0).getCoordinates()).isEqualTo(jerseyDependencyCoordinates);
    }

}
