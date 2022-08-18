package org.springframework.sbm.build.impl.inner;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 TODO/think about:
 * should we validate url format ?
 * should we validate other fields layout, looks like enum
 */
class PluginRepositoryHandlerTest {

    private final PluginRepositoryHandler sut = new PluginRepositoryHandler();
    private final MavenParser mavenParser = MavenParser.builder().build();

    @Test
    public void shouldConvertAllFieldsOfPluginRepo() {

        Xml.Document sourceFile = getSourceFile("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>boot-upgrade-27_30</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>boot-upgrade-27_30</name>
                    <description>boot-upgrade-27_30</description>

                    <pluginRepositories>
                        <pluginRepository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <layout>default</layout>
                            <snapshots>
                                  <enabled>true</enabled>
                            </snapshots>
                        </pluginRepository>
                    </pluginRepositories>
                </project>
                """);
        List<RepositoryDefinition> output = sut.getRepositoryDefinitions(sourceFile);
        assertThat(output).hasSize(1);

        assertThat(output.get(0).getUrl()).isEqualTo("https://repo.spring.io/milestone");
        assertThat(output.get(0).getId()).isEqualTo("spring-milestone");
        assertThat(output.get(0).getLayout()).isEqualTo("default");
        assertThat(output.get(0).getSnapshotsEnabled()).isTrue();
    }

    @Test
    public void shouldThrowExceptionForMissingIdMandatoryAttribute() {

        Xml.Document sourceFile = getSourceFile("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>boot-upgrade-27_30</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>boot-upgrade-27_30</name>
                    <description>boot-upgrade-27_30</description>

                    <pluginRepositories>
                        <pluginRepository>
                            <url>https://repo.spring.io/milestone</url>
                        </pluginRepository>
                    </pluginRepositories>
                </project>

                """);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> sut.getRepositoryDefinitions(sourceFile));
        assertThat(runtimeException).hasMessageContaining("id");
    }

    @Test
    public void shouldThrowExceptionForMissingUrlMandatoryAttribute() {

        Xml.Document sourceFile = getSourceFile("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>boot-upgrade-27_30</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>boot-upgrade-27_30</name>
                    <description>boot-upgrade-27_30</description>

                    <pluginRepositories>
                        <pluginRepository>
                            <id>someId</id>
                        </pluginRepository>
                    </pluginRepositories>
                </project>

                """);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> sut.getRepositoryDefinitions(sourceFile));
        assertThat(runtimeException).hasMessageContaining("url");
    }

    private Xml.Document getSourceFile(@Language("xml") String xml) {

        return mavenParser.parse(xml).get(0);
    }
}
