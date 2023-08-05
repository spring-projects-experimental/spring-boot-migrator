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
package org.springframework.sbm.build.impl.inner;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                                <checksumPolicy>fail</checksumPolicy>
                                <enabled>true</enabled>
                                <updatePolicy>daily</updatePolicy>
                            </snapshots>
                            <releases>
                                <checksumPolicy>warn</checksumPolicy>
                                <enabled>false</enabled>
                                <updatePolicy>always</updatePolicy>
                            </releases>
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
        assertThat(output.get(0).getSnapshotsChecksumPolicy()).isEqualTo("fail");
        assertThat(output.get(0).getSnapShotsUpdatePolicy()).isEqualTo("daily");
        assertThat(output.get(0).getReleasesEnabled()).isFalse();
        assertThat(output.get(0).getReleasesChecksumPolicy()).isEqualTo("warn");
        assertThat(output.get(0).getReleasesUpdatePolicy()).isEqualTo("always");
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

        return mavenParser.parse(xml).map(Xml.Document.class::cast).toList().get(0);
    }
}
