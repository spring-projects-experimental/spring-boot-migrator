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

package org.springframework.sbm.parsers;

import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenProjectAnalyzerTest {

    @Nested
    class CompareWithMaven {
        @Test
        @DisplayName("compare MavenProject.getCollectedProjects()")
        void compareMavenProjectGetCollectedProjects(@TempDir Path tmpDir) {
            @Language("xml")
            String parentPom =
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                <modelVersion>4.0.0</modelVersion>
                                <groupId>com.acme</groupId>
                                <artifactId>parent</artifactId>
                                <version>0.1.0-SNAPSHOT</version>
                                <packaging>pom</packaging>
                                <modules>
                                    <module>module-a</module>
                                    <module>module-b</module>
                                    <module>parent-b</module>
                                </modules>
                            </project>
                            """;

            @Language("xml")
            String moduleAPom =
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                <modelVersion>4.0.0</modelVersion>
                                <parent>
                                    <groupId>com.acme</groupId>
                                    <artifactId>parent</artifactId>
                                    <version>0.1.0-SNAPSHOT</version>
                                </parent>
                                <artifactId>module-a</artifactId>
                            </project>
                            """;

            @Language("xml")
            String moduleBPom =
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                <modelVersion>4.0.0</modelVersion>
                                <parent>
                                    <groupId>com.acme</groupId>
                                    <artifactId>parent</artifactId>
                                    <version>0.1.0-SNAPSHOT</version>
                                </parent>
                                <artifactId>module-b</artifactId>
                                <dependencies>
                                    <dependency>
                                        <groupId>com.acme</groupId>
                                        <artifactId>module-a</artifactId>
                                        <version>${project.version}</version>
                                    </dependency>
                                </dependencies>
                            </project>
                            """;

            @Language("xml")
            String parentPomB =
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                <modelVersion>4.0.0</modelVersion>
                                <parent>
                                    <groupId>com.acme</groupId>
                                    <artifactId>parent</artifactId>
                                    <version>0.1.0-SNAPSHOT</version>
                                </parent>
                                <artifactId>parent-b</artifactId>
                                <packaging>pom</packaging>
                                <modules>
                                    <module>module-1</module>
                                </modules>
                            </project>
                            """;

            @Language("xml")
            String module1Pom =
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                <modelVersion>4.0.0</modelVersion>
                                <parent>
                                    <groupId>com.acme</groupId>
                                    <artifactId>parent-b</artifactId>
                                    <version>0.1.0-SNAPSHOT</version>
                                </parent>
                                <artifactId>module-1</artifactId>
                            </project>
                            """;

            Path baseDir = tmpDir;

            List<Resource> resources = List.of(
                    new DummyResource(baseDir.resolve("pom.xml"), parentPom),
                    new DummyResource(baseDir.resolve("module-a/pom.xml"), moduleAPom),
                    new DummyResource(baseDir.resolve("module-b/pom.xml"), moduleBPom),
                    new DummyResource(baseDir.resolve("parent-b/pom.xml"), parentPomB),
                    new DummyResource(baseDir.resolve("parent-b/module-1/pom.xml"), module1Pom)
            );

            MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
            writeToDisk(baseDir, resources);
            MavenSession mavenSession = startMavenSession(baseDir);

            List<MavenProject> mavenSorted = mavenSession.getProjectDependencyGraph().getSortedProjects();
            List<SbmMavenProject> sbmSorted = sut.getSortedProjects(baseDir, resources);

            assertThat(mavenSorted).hasSize(5);
            assertThat(mavenSorted.size()).isEqualTo(sbmSorted.size());

            assertThat(mavenSorted.get(0).getGroupId()).isEqualTo("com.acme");
            assertThat(mavenSorted.get(0).getGroupId()).isEqualTo(sbmSorted.get(0).getGroupId());

            assertThat(mavenSorted.get(0).getArtifactId()).isEqualTo("parent");
            assertThat(mavenSorted.get(0).getArtifactId()).isEqualTo(sbmSorted.get(0).getArtifactId());

            assertThat(mavenSorted.get(0).getCollectedProjects()).hasSize(4);
            assertThat(mavenSorted.get(0).getCollectedProjects().size()).isEqualTo(sbmSorted.get(0).getCollectedProjects().size());

            List<String> projectsCollectedByMaven = mavenSorted.get(0).getCollectedProjects().stream().map(p -> p.getArtifactId()).toList();
            assertThat(projectsCollectedByMaven).containsExactlyInAnyOrder(
                    "module-a", "module-b", "module-1", "parent-b"
            );

            assertThat(sbmSorted.get(0).getCollectedProjects().stream().map(p -> p.getArtifactId()).toList()).hasSameElementsAs(projectsCollectedByMaven);
        }

        private void writeToDisk(Path baseDir, List<Resource> resources) {
            resources.stream()
                    .forEach(r -> {
                                try {
                                    Path resolve = ResourceUtil.getPath(r);
                                    Files.createDirectories(resolve.getParent());
                                    Files.writeString(resolve, ResourceUtil.getContent(r));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
        }

        private MavenSession startMavenSession(Path baseDir) {
            List<String> goals = List.of("clean", "package");
            MavenExecutor mavenExecutor = new MavenExecutor(new MavenExecutionRequestFactory(new MavenConfigFileParser()), new MavenPlexusContainer());
            AtomicReference<MavenSession> mavenSession = new AtomicReference<>();
            mavenExecutor.onProjectSucceededEvent(baseDir, goals, event -> mavenSession.set(event.getSession()));
            return mavenSession.get();
        }

    }


    /**
     * The simplest possible Maven project.
     */
    @Test
    @DisplayName("projectWithSinglePom")
    void projectWithSinglePom() {
        @Language("xml")
        String singlePom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <version>0.1.0-SNAPSHOT</version>
                            <artifactId>example</artifactId>
                        </project>
                        """;

        List<Resource> resources = List.of(new DummyResource(Path.of("pom.xml"), singlePom));
        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        Path baseDir = Path.of(".").toAbsolutePath().normalize();
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(baseDir, resources);
        assertThat(sortedProjects).hasSize(1);
    }

    /**
     * A simple reactor build with one parent and one module pom.
     */
    @Test
    @DisplayName("reactorBuild")
    void reactorBuild() {
        @Language("xml")
        String parentPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>parent</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                            <packaging>pom</packaging>
                            <modules>
                                <module>example</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        String modulePom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>parent</artifactId>
                                <version>0.1.0-SNAPSHOT</version>
                            </parent>
                            <artifactId>example</artifactId>
                        </project>
                        """;

        List<Resource> resources = List.of(
                new DummyResource(Path.of("pom.xml"), parentPom),
                new DummyResource(Path.of("example/pom.xml"), modulePom)
        );

        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(Path.of(".").toAbsolutePath(), resources);

        assertThat(sortedProjects).hasSize(2);

        String parentPomPath = Path.of(".").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(0).getBasedir().toString()).isEqualTo(parentPomPath);

        String modulePomPath = Path.of(".").resolve("example").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(1).getBasedir().toString()).isEqualTo(modulePomPath);
    }

    /**
     * Two pom files building a rector build should be returned.
     * The dangling pom not belonging to the reactor build defined through parent pom will be ignored.
     */
    @Test
    @DisplayName("reactorBuildWithDanglingPom")
    void reactorBuildWithDanglingPom() {
        @Language("xml")
        String parentPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>parent</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                            <packaging>pom</packaging>
                            <modules>
                                <module>example</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        String modulePom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>parent</artifactId>
                                <version>0.1.0-SNAPSHOT</version>
                            </parent>
                            <artifactId>example</artifactId>
                        </project>
                        """;

        @Language("xml")
        String danglingPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>dangling</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </project>
                        """;

        List<Resource> resources = List.of(
                new DummyResource(Path.of("pom.xml"), parentPom),
                new DummyResource(Path.of("example/pom.xml"), modulePom),
                new DummyResource(Path.of("dangling/pom.xml"), danglingPom)
        );

        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(Path.of(".").toAbsolutePath(), resources);

        assertThat(sortedProjects).hasSize(2);

        String parentPomPath = Path.of(".").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(0).getBasedir().toString()).isEqualTo(parentPomPath);

        String modulePomPath = Path.of(".").resolve("example").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(1).getBasedir().toString()).isEqualTo(modulePomPath);
    }

    /**
     * A project with three Maven pom files.
     * Two of them build a reactor.
     * The third is not part of the reactor but a dependency of the child module in the reactor build.
     */
    @Test
    @DisplayName("reactorBuildWithDanglingPomWhichAReactorModuleDependsOn")
    void reactorBuildWithDanglingPomWhichAReactorModuleDependsOn() {
        @Language("xml")
        String parentPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>parent</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                            <packaging>pom</packaging>
                            <modules>
                                <module>example</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        String modulePom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>parent</artifactId>
                                <version>0.1.0-SNAPSHOT</version>
                            </parent>
                            <artifactId>example</artifactId>
                            <dependencies>
                                <dependency>
                                    <groupId>com.acme</groupId>
                                    <artifactId>dangling</artifactId>
                                    <version>0.1.0-SNAPSHOT</version>
                                </dependency>
                            </dependencies>
                        </project>
                        """;

        @Language("xml")
        String danglingPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>dangling</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </project>
                        """;

        List<Resource> resources = List.of(
                new DummyResource(Path.of("pom.xml"), parentPom),
                new DummyResource(Path.of("example/pom.xml"), modulePom),
                new DummyResource(Path.of("dangling/pom.xml"), danglingPom)
        );

        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(Path.of(".").toAbsolutePath(), resources);

        assertThat(sortedProjects).hasSize(2);

        String parentPomPath = Path.of(".").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(0).getBasedir().toString()).isEqualTo(parentPomPath);

        String modulePomPath = Path.of(".").resolve("example").toAbsolutePath().normalize().toString();
        assertThat(sortedProjects.get(1).getBasedir().toString()).isEqualTo(modulePomPath);
    }

    /**
     * A reactor project with four modules provided in "wrong" order.
     * The returned order differs and reflects the order of the modules in reactor build.
     */
    @Test
    @DisplayName("theReactorBuildOrderIsReturned")
    void theReactorBuildOrderIsReturned() {
        @Language("xml")
        String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>parent</artifactId>
                    <packaging>pom</packaging>
                    <version>0.1.0-SNAPSHOT</version>
                    <modules>
                        <module>module-a</module>
                        <module>module-b</module>
                        <module>module-c</module>
                    </modules>
                </project>
                """;

        @Language("xml")
        String moduleAPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-a</artifactId>
                </project>                
                """;

        @Language("xml")
        String moduleBPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-b</artifactId>
                </project>                         
                """;

        @Language("xml")
        String moduleCPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-c</artifactId>
                </project>                         
                """;

        // Provided unordered
        List<Resource> resources = List.of(
                new DummyResource(Path.of("module-b/pom.xml"), moduleBPom),
                new DummyResource(Path.of("module-a/pom.xml"), moduleAPom),
                new DummyResource(Path.of("module-c/pom.xml"), moduleCPom),
                new DummyResource(Path.of("pom.xml"), parentPom)
        );

        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(Path.of(".").toAbsolutePath(), resources);

        // Returned ordered
        assertThat(sortedProjects).hasSize(4);
        assertThat(sortedProjects.get(0).getModuleDir().toString()).isEqualTo("");
        assertThat(sortedProjects.get(1).getModuleDir().toString()).isEqualTo("module-a");
        assertThat(sortedProjects.get(2).getModuleDir().toString()).isEqualTo("module-b");
        assertThat(sortedProjects.get(3).getModuleDir().toString()).isEqualTo("module-c");
    }

    /**
     * Provided unordered list of resources
     * Order in modules is not correct
     * Order is defined by dependencies
     */
    @Test
    @DisplayName("moreComplex")
    void moreComplex() {

        // Modules declared in order a,b,c
        @Language("xml")
        String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module-a</module>
                        <module>module-b</module>
                        <module>module-c</module>
                    </modules>
                </project>
                """;

        // Module A depends on C, so C must be built first effectively changing the order in <modules>
        @Language("xml")
        String moduleAPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-a</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>module-c</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </dependency>
                    </dependencies>
                </project>                
                """;

        @Language("xml")
        String moduleBPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-b</artifactId>
                </project>                         
                """;

        // C depends on B
        @Language("xml")
        String moduleCPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-c</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>module-b</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </dependency>
                    </dependencies>
                </project>                         
                """;

        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();

        List<Resource> resources = List.of(
                new DummyResource(Path.of("module-b/pom.xml"), moduleBPom),
                new DummyResource(Path.of("module-a/pom.xml"), moduleAPom),
                new DummyResource(Path.of("module-c/pom.xml"), moduleCPom),
                new DummyResource(Path.of("pom.xml"), parentPom)
        );


        // Provided unordered
        List<SbmMavenProject> sortedProjects = sut.getSortedProjects(Path.of(".").toAbsolutePath(), resources);

        // Expected order is parent, module-b, module-c, module-a
        assertThat(sortedProjects).hasSize(4);

        assertThat(sortedProjects.get(0).getModuleDir().toString()).isEqualTo("");
        assertThat(sortedProjects.get(1).getModuleDir().toString()).isEqualTo("module-b");
        assertThat(sortedProjects.get(2).getModuleDir().toString()).isEqualTo("module-c");
        assertThat(sortedProjects.get(3).getModuleDir().toString()).isEqualTo("module-a");
    }

    @Test
    @DisplayName("sortModels")
    void sortModels() {


        // Modules declared in order a,b,c
        @Language("xml")
        String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module-a</module>
                        <module>module-b</module>
                        <module>module-c</module>
                    </modules>
                </project>
                """;

        // Module A depends on C, so C must be built first effectively changing the order in <modules>
        @Language("xml")
        String moduleAPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-a</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>module-c</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </dependency>
                    </dependencies>
                </project>                
                """;

        @Language("xml")
        String moduleBPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-b</artifactId>
                </project>                         
                """;

        // C depends on B
        @Language("xml")
        String moduleCPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </parent>
                    <artifactId>module-c</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>module-b</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </dependency>
                    </dependencies>
                </project>                         
                """;

        // Provided unordered
        MavenProjectAnalyzer sut = new MavenProjectAnalyzer();
        List<MavenProjectAnalyzer.Model> models = List.of(
                new MavenProjectAnalyzer.Model(new DummyResource(Path.of("module-b/pom.xml"), moduleBPom)),
                new MavenProjectAnalyzer.Model(new DummyResource(Path.of("module-a/pom.xml"), moduleAPom)),
                new MavenProjectAnalyzer.Model(new DummyResource(Path.of("module-c/pom.xml"), moduleCPom)),
                new MavenProjectAnalyzer.Model(new DummyResource(Path.of("pom.xml"), parentPom))
        );

        // Expected order is parent, module-b, module-c, module-a
        List<MavenProjectAnalyzer.Model> sorted = sut.sortModels(models);
        assertThat(sorted.get(0).getArtifactId()).isEqualTo("parent");
        assertThat(sorted.get(1).getArtifactId()).isEqualTo("module-b");
        assertThat(sorted.get(2).getArtifactId()).isEqualTo("module-c");
        assertThat(sorted.get(3).getArtifactId()).isEqualTo("module-a");
    }

    // TODO: Test with parent pom that has boot-starter as parent
}