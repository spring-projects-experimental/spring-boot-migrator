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
package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.checks.RedeclaredDependenciesFinder.RedeclaredDependency;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RedeclaredDependenciesFinderTest {

    @Test
    void shouldFindDependencyRedefinedParentVersion() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module1</module>
                    </modules>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <artifactId>validation-api</artifactId>
                                <version>2.0.0.Final</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
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
                        <version>1.0.0</version>
                    </parent>
                    <artifactId>module1</artifactId>
                    <packaging>jar</packaging>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>1.1.0.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder();
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).hasSize(1);
        RedeclaredDependency explicitDependency = matches.iterator().next();
        String explicitVersionDependencyCoordinates = "javax.validation:validation-api:1.1.0.Final";
        assertThat(explicitDependency.getRedeclaredDependency().getGav()).isEqualTo(explicitVersionDependencyCoordinates);
        assertThat(explicitDependency.getOriginalVersion()).isEqualTo("2.0.0.Final");
    }

    @Test
    void shouldReportSameVersion() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <artifactId>validation-api</artifactId>
                                <version>2.0.0.Final</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
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
                        <version>1.0.0</version>
                    </parent>
                    <artifactId>module1</artifactId>
                    <packaging>jar</packaging>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.0.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder();
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).hasSize(1);
        RedeclaredDependency explicitDependency = matches.iterator().next();
        String explicitVersionDependencyCoordinates =  "javax.validation:validation-api:2.0.0.Final";
        assertThat(explicitDependency.getRedeclaredDependency().getGav()).isEqualTo(explicitVersionDependencyCoordinates);
        assertThat(explicitDependency.getOriginalVersion()).isEqualTo("2.0.0.Final");
    }

    @Test
    void shouldFindDependencyRedefinedBomVersion() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>bom</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.el</groupId>
                                <artifactId>javax.el-api</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <artifactId>validation-api</artifactId>
                                <version>2.0.0.Final</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                </project>
                """;

        @Language("xml")
        String module1PomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>module1</artifactId>
                    <version>1.0.0</version>
                    <packaging>jar</packaging>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>com.example</groupId>
                                <artifactId>bom</artifactId>
                                <version>1.0.0</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>javax.el</groupId>
                            <artifactId>javax.el-api</artifactId>
                            <version>2.2.5</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        String explicitVersionDependencyCoordinates = "javax.el:javax.el-api:2.2.5";
        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder(Set.of("javax.el:javax.el-api", "javax.validation:validation-api"));
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(1);
        assertThat(matches.iterator().next().getRedeclaredDependency().getGav()).isEqualTo(explicitVersionDependencyCoordinates);
    }

    @Test
    void shouldIgnoreWithoutDependencyManagement() {
        @Language("xml")
        String module1PomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>module1</artifactId>
                    <version>1.0.0</version>
                    <packaging>jar</packaging>
                    <dependencies>
                        <dependency>
                            <groupId>javax.el</groupId>
                            <artifactId>javax.el-api</artifactId>
                            <version>3.0.0</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.0.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder(Set.of("javax.el:javax.el-api", "javax.validation:validation-api"));
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(1);
        assertThat(matches).isEmpty();
    }

    @Test
    void shouldFindAllRedefinedDependencies() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module1</module>
                    </modules>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.el</groupId>
                                <artifactId>javax.el-api</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <artifactId>validation-api</artifactId>
                                <version>2.0.1.Final</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
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
                        <version>1.0.0</version>
                        <relativePath/>
                    </parent>
                    <artifactId>module1</artifactId>
                    <packaging>jar</packaging>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.transaction</groupId>
                                <artifactId>javax.transaction-api</artifactId>
                                <version>1.3</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>

                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>1.1.0.Final</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.el</groupId>
                            <artifactId>javax.el-api</artifactId>
                            <version>2.2.5</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.transaction</groupId>
                            <artifactId>javax.transaction-api</artifactId>
                            <version>1.2</version>
                        </dependency>           
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
//                .serializeProjectContext(Path.of("./target/test"));
                .build();

        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder(Set.of());
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).hasSize(3);
        assertThat(matches).contains(new RedeclaredDependency(
                Dependency.builder()
                        .groupId("javax.validation")
                        .artifactId("validation-api")
                        .version("1.1.0.Final").build(), "2.0.1.Final"));
        assertThat(matches).contains(new RedeclaredDependency(
                Dependency.builder()
                        .groupId("javax.el")
                        .artifactId("javax.el-api")
                        .version("2.2.5").build(), "3.0.0"));
        assertThat(matches).contains(new RedeclaredDependency(
                Dependency.builder()
                        .groupId("javax.transaction")
                        .artifactId("javax.transaction-api")
                        .version("1.2").build(), "1.3"));
    }

    @Test
    void shouldFindRedeclaredDependenciesOnlyFromList() {
        @Language("xml")
        String parentPomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0.0</version>
                    <packaging>pom</packaging>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>javax.validation</groupId>
                                <artifactId>validation-api</artifactId>
                                <version>2.0.1.Final</version>
                            </dependency>
                            <dependency>
                                <groupId>javax.annotation</groupId>
                                <artifactId>javax.annotation-api</artifactId>
                                <version>1.3.2</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <modules>
                        <module>module1</module>
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
                        <version>1.0.0</version>
                    </parent>
                    <artifactId>module1</artifactId>
                    <packaging>jar</packaging>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>1.1.0.Final</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.annotation</groupId>
                            <artifactId>javax.annotation-api</artifactId>
                            <version>1.3</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        RedeclaredDependenciesFinder finder = new RedeclaredDependenciesFinder(Set.of("javax.validation:validation-api"));
        Set<RedeclaredDependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).hasSize(1);
        RedeclaredDependency explicitDependency = matches.iterator().next();
        String explicitVersionDependencyCoordinates = "javax.validation:validation-api:1.1.0.Final";
        assertThat(explicitDependency.getRedeclaredDependency().getGav()).isEqualTo(explicitVersionDependencyCoordinates);
        assertThat(explicitDependency.getOriginalVersion()).isEqualTo("2.0.1.Final");
    }

}
