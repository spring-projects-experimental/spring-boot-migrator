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
package org.springframework.sbm.boot.upgrade_27_30;

import jnr.ffi.annotations.In;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.UpgradeDependencyVersion;
import org.openrewrite.xml.tree.Xml;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpgradeBomTo30Test {

    @Test
    void shouldUpdateBomVersionTo30() {
        Recipe recipe = new UpgradeDependencyVersion(
                "org.springframework.boot",
                "spring-boot-dependencies",
                "3.0.0-M3",
                null,
                null,
                null
        );

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            ex.printStackTrace();
            errors.add(ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<SourceFile> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>test</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                                
                    <properties>
                        <spring.version>2.7.1</spring.version>
                    </properties>
                    <name>Test</name>
                                
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>${spring.version}</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <pluginRepositories>
                        <pluginRepository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </pluginRepository>
                    </pluginRepositories>
                </project>
                """)
                .toList();

        List<Result> result = recipe.run(new InMemoryLargeSourceSet(documentList), ctx).getChangeset().getAllResults();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo("""
                        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                                        
                            <groupId>test</groupId>
                            <artifactId>test</artifactId>
                            <version>1.0.0-SNAPSHOT</version>
                            
                            <properties>
                                <spring.version>3.0.0-M3</spring.version>
                            </properties>
                            <name>Test</name>
                            
                            <dependencyManagement>
                                <dependencies>
                                    <dependency>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-dependencies</artifactId>
                                        <version>${spring.version}</version>
                                        <type>pom</type>
                                        <scope>import</scope>
                                    </dependency>
                                </dependencies>
                            </dependencyManagement>
                            <repositories>
                                <repository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </repository>
                            </repositories>

                            <pluginRepositories>
                                <pluginRepository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </pluginRepository>
                            </pluginRepositories>
                        </project>
                        """);
    }

    @Test
    public void whenThereIsNoBomNoChanges() {
        Recipe recipe = new UpgradeDependencyVersion(
                "org.springframework.boot",
                "spring-boot-dependencies",
                "3.0.0-M3",
                null,
                null,
                null
        );

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            ex.printStackTrace();
            errors.add(ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<SourceFile> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>test</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                                
                    <properties>
                        <spring.version>2.7.1</spring.version>
                    </properties>
                    <name>Test</name>
                                
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>3.0.0-M3</version>
                    </parent>
                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <pluginRepositories>
                        <pluginRepository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </pluginRepository>
                    </pluginRepositories>
                </project>
                """)
                .toList();

        List<Result> result = recipe.run(new InMemoryLargeSourceSet(documentList), ctx).getChangeset().getAllResults();

        assertThat(result).hasSize(0);
    }
}
