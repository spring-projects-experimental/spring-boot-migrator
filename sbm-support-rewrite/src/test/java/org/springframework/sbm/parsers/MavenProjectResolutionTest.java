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

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenProjectResolutionTest {

    @Test
    @DisplayName("Factory should create fully initialized MavenProject")
    void verifyMavenProjectRetrievedFromSession(@TempDir Path tempDir) throws Exception {
        @Language("xml")
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>the-example</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>the-name</name>
                    <properties>
                        <java.version>11</java.version>
                        <spring-boot.version>3.1.2</spring-boot.version>
                    </properties>
                    <repositories>
                        <repository>
                            <id>jcenter</id>
                            <name>jcenter</name>
                            <url>https://jcenter.bintray.com</url>
                        </repository>
                        <repository>
                            <id>mavencentral</id>
                            <name>mavencentral</name>
                            <url>https://repo.maven.apache.org/maven2</url>
                        </repository>
                    </repositories>
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>${spring-boot.version}</version>
                                <type>pom</type>
                                <scope>import</scope>
                           </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                                <version>${spring-boot.version}</version>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                """;

        Path pomFile = tempDir.resolve("pom.xml");
        Files.writeString(pomFile, pomXml);

        MavenPlexusContainer plexusContainerFactory = new MavenPlexusContainer();
        MavenExecutionRequestFactory requestFactory = new MavenExecutionRequestFactory(new MavenConfigFileParser());
        MavenExecutor mavenExecutor = new MavenExecutor(requestFactory, plexusContainerFactory);
        mavenExecutor.onProjectSucceededEvent(tempDir, List.of("dependency:resolve"), event -> {
            MavenProject mavenProject = event.getSession().getCurrentProject();
            assertThat(mavenProject.getName()).isEqualTo("the-name");
            assertThat(mavenProject.getArtifactId()).isEqualTo("the-example");
            assertThat(mavenProject.getGroupId()).isEqualTo("com.example");

            List<String> mainDeps = List.of(
                    tempDir.resolve("target/classes").toString(),
                    dep("org/springframework/boot/spring-boot-starter/3.1.2/spring-boot-starter-3.1.2.jar"),
                    dep("org/springframework/boot/spring-boot/3.1.2/spring-boot-3.1.2.jar"),
                    dep("org/springframework/spring-context/6.0.11/spring-context-6.0.11.jar"),
                    dep("org/springframework/spring-aop/6.0.11/spring-aop-6.0.11.jar"),
                    dep("org/springframework/spring-beans/6.0.11/spring-beans-6.0.11.jar"),
                    dep("org/springframework/spring-expression/6.0.11/spring-expression-6.0.11.jar"),
                    dep("org/springframework/boot/spring-boot-autoconfigure/3.1.2/spring-boot-autoconfigure-3.1.2.jar"),
                    dep("org/springframework/boot/spring-boot-starter-logging/3.1.2/spring-boot-starter-logging-3.1.2.jar"),
                    dep("ch/qos/logback/logback-classic/1.4.8/logback-classic-1.4.8.jar"),
                    dep("ch/qos/logback/logback-core/1.4.8/logback-core-1.4.8.jar"),
                    dep("org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar"),
                    dep("org/apache/logging/log4j/log4j-to-slf4j/2.20.0/log4j-to-slf4j-2.20.0.jar"),
                    dep("org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar"),
                    dep("org/slf4j/jul-to-slf4j/2.0.7/jul-to-slf4j-2.0.7.jar"),
                    dep("jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar"),
                    dep("org/springframework/spring-core/6.0.11/spring-core-6.0.11.jar"),
                    dep("org/springframework/spring-jcl/6.0.11/spring-jcl-6.0.11.jar"),
                    dep("org/yaml/snakeyaml/1.33/snakeyaml-1.33.jar")
            );
            try {
                assertThat(mavenProject.getCompileClasspathElements()).containsExactlyInAnyOrder(mainDeps.toArray(new String[]{}));
                List<String> testDeps = new ArrayList<>();
                testDeps.addAll(mainDeps);
                testDeps.add(tempDir.resolve("target/test-classes").toString());
                testDeps.add(dep("javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar"));
                assertThat(mavenProject.getTestClasspathElements()).containsExactlyInAnyOrder(testDeps.toArray(new String[]{}));
            } catch (DependencyResolutionRequiredException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String dep(String s) {
        Path m2Repo = Path.of(System.getProperty("user.home")).resolve(".m2/repository/").resolve(s);
        return m2Repo.toString();
    }


}