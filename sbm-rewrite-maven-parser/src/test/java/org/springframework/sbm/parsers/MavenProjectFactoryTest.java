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

import org.apache.maven.Maven;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.project.*;
import org.codehaus.plexus.PlexusContainer;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
class MavenProjectFactoryTest {

    @Test
    @DisplayName("Factory should create fully initialized MavenProject")
    void factoryShouldCreateFullyInitializedMavenProject(@TempDir Path tempDir) throws Exception {
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.7.1</version>
                        <relativePath/> <!-- lookup parent from repository -->
                    </parent>
                    <groupId>com.example</groupId>
                    <artifactId>demo-spring-song-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <properties>
                        <java.version>11</java.version>
                        <spring-cloud.version>2021.0.4</spring-cloud.version>
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
                            <artifactId>spring-boot-starter-web</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.ehcache</groupId>
                            <artifactId>ehcache</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-data-jpa</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>com.h2database</groupId>
                            <artifactId>h2</artifactId>
                            <scope>runtime</scope>
                        </dependency>
                        <dependency>
                            <groupId>org.hibernate.validator</groupId>
                            <artifactId>hibernate-validator</artifactId>
                        </dependency>
                                
                                
                        <dependency>
                            <groupId>com.github.tomakehurst</groupId>
                            <artifactId>wiremock-jre8</artifactId>
                            <version>2.35.0</version>
                        </dependency>
                        <dependency>
                            <groupId>org.apache.johnzon</groupId>
                            <artifactId>johnzon-core</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.cloud</groupId>
                                <artifactId>spring-cloud-dependencies</artifactId>
                                <version>${spring-cloud.version}</version>
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
                            </plugin>
                        </plugins>
                    </build>
                                
                </project>
                """;

        MavenProjectFactory sut = new MavenProjectFactory(new MavenPlexusContainerFactory());
        Path pomFile = tempDir.resolve("pom.xml");
        Files.writeString(pomFile, pomXml);
        MavenProject mavenProject = sut.createMavenProject(pomFile.toFile());
    }


}