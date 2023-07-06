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
package org.springframework.sbm.parsers;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.marker.Marker;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class BuildFileParserTest {

    @Nested
    public class GivenSimpleMavenMultiModuleProject {

        @Language("xml")
        private String pom1 =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>com.example</groupId>
                    <artifactId>parent-module</artifactId>
                    <version>1.0</version>
                    <modules>
                        <module>module1</module>
                    </modules>
                </project>
                """;

        @Language("xml")
        private String pom2 =
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
        private String pom3 =
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

        private BuildFileParser sut = new BuildFileParser(new MavenModelReader(), new ParserSettings());

        @Test
        void filterAndSortBuildFiles_shouldReturnSortedListOfFilteredBuildFiles() {

            // the poms have no order
            List<Resource> resources = List.of(
                    new DummyResource("src/test/resources/dummy/pom.xml", ""),
                    new DummyResource("module1/submodule/pom.xml", pom3),
                    new DummyResource("pom.xml", pom1),
                    new DummyResource("module1/pom.xml", pom2),
                    new DummyResource("src/main/java/SomeJavaClass.java", "")
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
            List<Resource> filteredAndSortedBuildFiles = List.of(
                    new DummyResource("module1/submodule/pom.xml", pom3),
                    new DummyResource("pom.xml", pom1),
                    new DummyResource("module1/pom.xml", pom2)
            );
            Map<Resource, List<Marker>> provenanceMarkers = new HashMap<>();
            ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
            boolean skipMavenParsing = false;
            Map<Resource, Xml.Document> parsedBuildFiles = sut.parseBuildFiles(
                    Path.of("."),
                    filteredAndSortedBuildFiles,
                    executionContext,
                    skipMavenParsing,
                    provenanceMarkers);
        }
    }

}