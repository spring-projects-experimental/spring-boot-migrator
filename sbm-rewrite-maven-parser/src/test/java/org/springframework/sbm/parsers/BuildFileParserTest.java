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
import java.util.stream.Stream;

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

        private BuildFileParser sut = new BuildFileParser(new MavenModelReader());

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
            Stream<Resource> resourceStream = sut.filterAndSortBuildFiles(resources);

            // verify result
            List<Resource> resourceStreamList = resourceStream.toList();
            assertThat(resourceStreamList).hasSize(3);

            Path resolve = Path.of(".").resolve("pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceStreamList.get(0))).isEqualTo(resolve);

            Path resolve2 = Path.of("module1/pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceStreamList.get(1))).isEqualTo(resolve2);

            Path resolve3 = Path.of("module1/submodule/pom.xml").toAbsolutePath().normalize();
            assertThat(ResourceUtil.getPath(resourceStreamList.get(2))).isEqualTo(resolve3);
        }

        @Test
        void parseBuildFiles_shouldReturnSortedListOfParsedBuildFiles() {
            Stream<Resource> filteredAndSortedBuildFiles = Stream.of(
                    new DummyResource("module1/submodule/pom.xml", pom3),
                    new DummyResource("pom.xml", pom1),
                    new DummyResource("module1/pom.xml", pom2)
            );
            Map<Resource, List<? extends Marker>> provenanceMarkers = new HashMap<>();
            ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
            boolean skipMavenParsing = false;
            Map<Resource, Xml.Document> parsedBuildFiles = sut.parseBuildFiles(filteredAndSortedBuildFiles, provenanceMarkers, executionContext, skipMavenParsing);
        }
    }

}