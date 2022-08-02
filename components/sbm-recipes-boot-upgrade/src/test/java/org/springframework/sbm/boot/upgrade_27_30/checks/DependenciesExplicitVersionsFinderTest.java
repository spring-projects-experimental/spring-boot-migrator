package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DependenciesExplicitVersionsFinderTest {

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
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact1</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                            <dependency>
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact2</artifactId>
                                <version>3.0.0</version>
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
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact1</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact1</artifactId>
                            <version>2.0.0</version>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact2</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact3</artifactId>
                            <version>1.0.0</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        String explicitVersionDependencyCoordinates = "com.dependency.group:artifact1:2.0.0";
        DependenciesExplicitVersionsFinder finder = new DependenciesExplicitVersionsFinder(Set.of("com.dependency.group:artifact1", "com.dependency.group:artifact2"));
        Set<Dependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(1);
        assertThat(matches.iterator().next().getCoordinates()).isEqualTo(explicitVersionDependencyCoordinates);
    }

    @Test
    void shouldIgnoreSameVersion() {
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
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact1</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                            <dependency>
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact2</artifactId>
                                <version>3.0.0</version>
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
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact1</artifactId>
                            <version>3.0.0</version>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact2</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact3</artifactId>
                            <version>3.0.0</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        DependenciesExplicitVersionsFinder finder = new DependenciesExplicitVersionsFinder();
        Set<Dependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).isEmpty();
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
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact1</artifactId>
                                <version>3.0.0</version>
                            </dependency>
                            <dependency>
                                <groupId>com.dependency.group</groupId>
                                <artifactId>artifact2</artifactId>
                                <version>3.0.0</version>
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
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact1</artifactId>
                            <version>2.0.0</version>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact2</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>com.dependency.group</groupId>
                            <artifactId>artifact3</artifactId>
                            <version>1.0.0</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("", parentPomXml)
                .withMavenBuildFileSource("module1", module1PomXml)
                .build();

        String explicitVersionDependencyCoordinates = "com.dependency.group:artifact1:2.0.0";
        DependenciesExplicitVersionsFinder finder = new DependenciesExplicitVersionsFinder(Set.of("com.dependency.group:artifact1", "com.dependency.group:artifact2"));
        Set<Dependency> matches = finder.findMatches(context);
        assertThat(context.getApplicationModules().list()).hasSize(2);
        assertThat(matches).isNotEmpty();
        assertThat(matches).hasSize(1);
        assertThat(matches.iterator().next().getCoordinates()).isEqualTo(explicitVersionDependencyCoordinates);
    }
}