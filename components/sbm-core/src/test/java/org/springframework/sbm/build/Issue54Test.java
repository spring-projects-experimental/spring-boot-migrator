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
package org.springframework.sbm.build;

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.internal.RawPom;
import org.openrewrite.maven.tree.Dependency;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@GitHubIssue("https://github.com/spring-projects-experimental/spring-boot-migrator/issues/54")
public class Issue54Test {

    @Test
    void scanMultiModuleProjectWithOptionalPropertyProvidedByParent() {

        @Language("xml")
        String pomA =
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>a</artifactId>
                            <version>1.1.0</version>
                            <packaging>pom</packaging>
                            <properties>
                                <boolean-variable>false</boolean-variable>
                            </properties>
                        </project>
                        """;

        @Language("xml")
        String pomB =
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>a</artifactId>
                                <version>1.1.0</version>
                            </parent>
                            <artifactId>b</artifactId>
                            <dependencies>
                                <dependency>
                                    <groupId>jakarta.validation</groupId>
                                    <artifactId>jakarta.validation-api</artifactId>
                                    <version>3.0.2</version>
                                    <optional>${boolean-variable}</optional>
                                </dependency>
                            </dependencies>
                        </project>
                        """;

        List<Xml.Document> poms = MavenParser.builder().build().parse(pomA, pomB).map(Xml.Document.class::cast).toList();

        assertThat(
                poms.get(1).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getProperties().get("boolean-variable")
        ).isEqualTo("false");

        List<Dependency> requestedDependencies = poms.get(1).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequestedDependencies();

        assertThat(requestedDependencies).hasSize(1);
        assertThat(requestedDependencies.get(0).getOptional()).isEqualTo("${boolean-variable}");

        ProjectContext projectContext = TestProjectContext
                .buildProjectContext()
                .withMavenBuildFileSource("pom.xml", pomA)
                .withMavenBuildFileSource("a/pom.xml", pomB)
                .build();
        assertThat(projectContext.getApplicationModules().getModule("").getBuildFile().getProperty("boolean-variable")).isEqualTo("false");
        assertThat(projectContext.getApplicationModules().getModule("a").getBuildFile().getRequestedDependencies()).hasSize(1);
    }

    @Test
    void scanMultiModuleProjectWithVersionPropertyProvidedByParent() {

        @Language("xml")
        String pomA =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>a</artifactId>
                    <version>1.1.0</version>
                    <packaging>pom</packaging>
                    <properties>
                        <scope-variable>3.0.2</scope-variable>
                    </properties>
                </project>
                """;

        @Language("xml")
        String pomB =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>a</artifactId>
                        <version>1.1.0</version>
                    </parent>
                    <artifactId>b</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>jakarta.validation</groupId>
                            <artifactId>jakarta.validation-api</artifactId>
                            <version>${scope-variable}</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        List<Xml.Document> poms = MavenParser.builder().build().parse(pomA, pomB).map(Xml.Document.class::cast).toList();

        assertThat(poms
                           .get(1)
                           .getMarkers()
                           .findFirst(MavenResolutionResult.class)
                           .get().getPom().getProperties().get("scope-variable")).isEqualTo("3.0.2");

        List<Dependency> requestedDependencies = poms
                .get(1)
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getRequestedDependencies();

        assertThat(requestedDependencies).hasSize(1);
        assertThat(requestedDependencies.get(0).getVersion()).isEqualTo("${scope-variable}");

        ProjectContext projectContext = TestProjectContext
                .buildProjectContext()
                .withMavenBuildFileSource("pom.xml", pomA)
                .withMavenBuildFileSource("a/pom.xml", pomB)
                .build();
        assertThat(projectContext.getApplicationModules().getModule("").getBuildFile().getProperty("scope-variable")).isEqualTo("3.0.2");
        assertThat(projectContext.getApplicationModules().getModule("a").getBuildFile().getRequestedDependencies()).hasSize(1);
        assertThat(projectContext.getApplicationModules().getModule("a").getBuildFile().getRequestedDependencies().get(0).getVersion()).isEqualTo("3.0.2");
    }

}
