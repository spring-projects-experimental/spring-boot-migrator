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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.marker.Marker;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.maven.BuildFileParser;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * @author Fabian Krüger
 */
class BuildFileParserTest {

    @Nested
    public class GivenSimpleMavenMultiModuleProject {

        @Language("xml")
        private static final String POM_1 =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                                        
                            <groupId>com.example</groupId>
                            <artifactId>parent</artifactId>
                            <version>1.0</version>
                            <modules>
                                <module>module1</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        private static final String POM_2 =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.example</groupId>
                                <artifactId>parent</artifactId>
                                <version>1.0</version>
                            </parent>
                            <artifactId>module1</artifactId>
                            <modules>
                                <module>submodule</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        private static final String POM_3 =
                """
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.example</groupId>
                                <artifactId>module1</artifactId>
                                <version>1.0</version>
                            </parent>
                            <artifactId>submodule</artifactId>
                        </project>
                        """;

        private final BuildFileParser sut = new BuildFileParser();

        @Test
        void filterAndSortBuildFiles_shouldReturnSortedListOfFilteredBuildFiles() {

            // the provided resources have no order and contain non-pom files
            List<Resource> resources = List.of(
                    new DummyResource("src/test/resources/dummy/pom.xml", ""),  // filtered
                    new DummyResource("module1/submodule/pom.xml", POM_3),      // pos. 3
                    new DummyResource("pom.xml", POM_1),                        // pos. 1
                    new DummyResource("module1/pom.xml", POM_2),                // pos. 2
                    new DummyResource("src/main/java/SomeJavaClass.java", "")   // filtered
            );

            // filter and sort build files
            List<Resource> resourceList = sut.filterAndSortBuildFiles(resources);

            // verify result
            assertThat(resourceList).hasSize(3);

            Path resolve = Path.of(".").resolve("pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceList.get(0))).isEqualTo(resolve);

            Path resolve2 = Path.of("module1/pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceList.get(1))).isEqualTo(resolve2);

            Path resolve3 = Path.of("module1/submodule/pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceList.get(2))).isEqualTo(resolve3);
        }

        @Test
        void parseBuildFiles_shouldReturnSortedListOfParsedBuildFiles() {
            Path baseDir = Path.of(".").toAbsolutePath().normalize();

            // List of resources
            Path module1SubmoduleSourcePath = baseDir.resolve("module1/submodule/pom.xml");
            Path parentSourcePath = baseDir.resolve("pom.xml");
            Path module1SourcePath = baseDir.resolve("module1/pom.xml");
            List<Resource> resources = List.of(
                    new DummyResource(module1SubmoduleSourcePath, POM_3),
                    new DummyResource(parentSourcePath, POM_1),
                    new DummyResource(module1SourcePath, POM_2)
            );

            // provenance markers
            Path module1SubmodulePomPath = baseDir.resolve(module1SubmoduleSourcePath);
            Path parentPomPath = baseDir.resolve(parentSourcePath);
            Path module1PomXml = baseDir.resolve(module1SourcePath);
            Map<Path, List<Marker>> provenanceMarkers = Map.of(
                    parentPomPath, List.of(new JavaProject(UUID.randomUUID(), "parent", null)),
                    module1PomXml, List.of(new JavaProject(UUID.randomUUID(), "module1", null)),
                    module1SubmodulePomPath, List.of(new JavaProject(UUID.randomUUID(), "module1/submodule", null))
            );

            ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
            boolean skipMavenParsing = false;

            List<Xml.Document> parsedBuildFiles = sut.parseBuildFiles(
                    baseDir,
                    resources,
                    List.of("default"),
                    executionContext,
                    skipMavenParsing,
                    provenanceMarkers);

            assertThat(parsedBuildFiles).hasSize(3);
            assertThat(parsedBuildFiles.get(0).getMarkers().findFirst(JavaProject.class).get().getProjectName()).isEqualTo("module1/submodule");
            assertThat(parsedBuildFiles.get(1).getMarkers().findFirst(JavaProject.class).get().getProjectName()).isEqualTo("parent");
            assertThat(parsedBuildFiles.get(2).getMarkers().findFirst(JavaProject.class).get().getProjectName()).isEqualTo("module1");
        }

        @Test
        @DisplayName("parse without baseDir should throw exception")
        void parseWithoutBaseDirShouldThrowException() {
            String message = assertThrows(
                    IllegalArgumentException.class,
                    () -> sut.parseBuildFiles(null, List.of(), List.of("default"), new InMemoryExecutionContext(), false, Map.of())
            )
            .getMessage();
            assertThat(message).isEqualTo("Base directory must be provided but was null.");
        }

        @Test
        @DisplayName("parse with empty resources should throw exception")
        void parseWithEmptyResourcesShouldThrowException() {
            String message = assertThrows(
                    IllegalArgumentException.class,
                    () -> sut.parseBuildFiles(Path.of("."), List.of(), List.of(), new InMemoryExecutionContext(), false, Map.of())
            )
            .getMessage();
            assertThat(message).isEqualTo("No build files provided.");
        }

        @Test
        @DisplayName("parse with non-pom resources provided should throw exception")
        void parseWithNonPomResourcesProvidedShouldThrowException() {
            Path baseDir = Path.of(".").toAbsolutePath().normalize();
            Resource nonPomResource = new DummyResource(baseDir, "src/main/java/SomeClass.java", "public class SomeClass {}");
            List<Resource> nonPomResource1 = List.of(nonPomResource);
            String message = assertThrows(
                    IllegalArgumentException.class,
                    () -> sut.parseBuildFiles(baseDir, nonPomResource1, List.of(), new InMemoryExecutionContext(), false, Map.of())
            )
            .getMessage();
            assertThat(message).isEqualTo("Provided resources which are not Maven build files: '["+ baseDir +"/src/main/java/SomeClass.java]'");
        }

        @Test
        @DisplayName("parse with incomplete provenance markers should throw exception")
        void parseWithIncompleteProvenanceMarkersShouldThrowException() {
            Path baseDir = Path.of(".").toAbsolutePath().normalize();

            Path pom1Path = baseDir.resolve("pom.xml");
            Resource pom1 = new DummyResource(pom1Path, "");
            Path pom2Path = baseDir.resolve("module1/pom.xml");
            Resource pom2 = new DummyResource(pom2Path, "");
            List<Resource> poms = List.of(pom1, pom2);

            Map<Path, List<Marker>> provenanceMarkers = Map.of(
                    pom1Path, List.of(new JavaProject(UUID.randomUUID(), "pom.xml", null))
                    // no marker for module1/pom.xml
            );

            String message = assertThrows(
                    IllegalArgumentException.class,
                    () -> sut.parseBuildFiles(baseDir, poms, List.of(), new InMemoryExecutionContext(), false, provenanceMarkers)
            )
                    .getMessage();
            assertThat(message).isEqualTo("No provenance marker provided for these pom files ["+Path.of(".").toAbsolutePath().normalize().resolve("module1/pom.xml]"));
        }

    }

}