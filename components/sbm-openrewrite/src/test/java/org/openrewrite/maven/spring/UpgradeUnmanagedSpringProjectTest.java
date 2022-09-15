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

package org.openrewrite.maven.spring;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.RecipeRun;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UpgradeUnmanagedSpringProjectTest {

    @Test
    void shouldUpdateBomVersionTo30() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <version>2.7.3</version>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <version>2.7.3</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.example</groupId>
                            <artifactId>explicit-deps-app</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                            <name>explicit-deps-app</name>
                            <description>explicit-deps-app</description>
                            <properties>
                                <java.version>17</java.version>
                                <maven.compiler.source>17</maven.compiler.source>
                                <maven.compiler.target>17</maven.compiler.target>
                            </properties>
                                          
                            <repositories>
                                <repository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </repository>
                            </repositories>
                    
                            <dependencies>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-web</artifactId>
                                    <version>3.0.0-M3</version>
                                </dependency>
                                <dependency>
                                    <groupId>io.dropwizard.metrics</groupId>
                                    <artifactId>metrics-annotation</artifactId>
                                    <version>4.2.9</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-test</artifactId>
                                    <version>3.0.0-M3</version>
                                    <scope>test</scope>
                                </dependency>
                            </dependencies>
                                                
                            <build>
                                <plugins>
                                    <plugin>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                                    </plugin>
                                </plugins>
                            </build>
                        </project>
                        """);
    }

    @Test
    void shouldNotUpdateSinceTheProjectIsNotSpring() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });
        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencies>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                    </dependencies>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();
        assertThat(result).hasSize(0);
    }

    @Test
    void shouldNotUpdateBomForOldVersion() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <version>2.6.0</version>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(0);
    }

    @Test
    void shouldNotUpdateIfSpringParent() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
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
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(0);
    }

    @Test
    public void shouldNotUpdateIfSpringDependencyManagement() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencyManagement>
                         <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>2.7.0</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(0);
    }

    @Test
    void shouldUpdateBomVersionTo30ForDependencyManaged() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-web</artifactId>
                                <version>2.7.3</version>
                            </dependency>
                            <dependency>
                                <groupId>io.dropwizard.metrics</groupId>
                                <artifactId>metrics-annotation</artifactId>
                                <version>4.2.8</version>
                            </dependency>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-starter-test</artifactId>
                                <version>2.7.3</version>
                                <scope>test</scope>
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
                                """);
        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.example</groupId>
                            <artifactId>explicit-deps-app</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                            <name>explicit-deps-app</name>
                            <description>explicit-deps-app</description>
                            <properties>
                                <java.version>17</java.version>
                                <maven.compiler.source>17</maven.compiler.source>
                                <maven.compiler.target>17</maven.compiler.target>
                            </properties>

                            <repositories>
                                <repository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </repository>
                            </repositories>
                                                
                            <dependencyManagement>
                                <dependencies>
                                    <dependency>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-starter-web</artifactId>
                                        <version>3.0.0-M3</version>
                                    </dependency>
                                    <dependency>
                                        <groupId>io.dropwizard.metrics</groupId>
                                        <artifactId>metrics-annotation</artifactId>
                                        <version>4.2.9</version>
                                    </dependency>
                                    <dependency>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-starter-test</artifactId>
                                        <version>3.0.0-M3</version>
                                        <scope>test</scope>
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
                        """);

    }

    @Test
    void shouldUpdateVersionsWithPropertyVariable() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <spring.version>2.7.3</spring.version>
                    </properties>

                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <version>${spring.version}</version>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <version>${spring.version}</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.example</groupId>
                            <artifactId>explicit-deps-app</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                            <name>explicit-deps-app</name>
                            <description>explicit-deps-app</description>
                            <properties>
                                <java.version>17</java.version>
                                <maven.compiler.source>17</maven.compiler.source>
                                <maven.compiler.target>17</maven.compiler.target>
                                <spring.version>3.0.0-M3</spring.version>
                            </properties>
                            
                            <repositories>
                                <repository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </repository>
                            </repositories>
   
                            <dependencies>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-web</artifactId>
                                    <version>${spring.version}</version>
                                </dependency>
                                <dependency>
                                    <groupId>io.dropwizard.metrics</groupId>
                                    <artifactId>metrics-annotation</artifactId>
                                    <version>4.2.9</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-test</artifactId>
                                    <version>${spring.version}</version>
                                    <scope>test</scope>
                                </dependency>
                            </dependencies>
                                                
                            <build>
                                <plugins>
                                    <plugin>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                                    </plugin>
                                </plugins>
                            </build>
                        </project>
                        """);
    }

    @Test
    void shouldCheckVersionsWithNewSpringBoot() {
        Recipe recipe = new UpgradeUnmanagedSpringProject("3.0.0-M3", "2\\.7\\..*|3\\.0\\..*");

        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <spring.version>3.0.0-M1</spring.version>
                    </properties>
                    
                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>
                    
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <version>${spring.version}</version>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <version>${spring.version}</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.example</groupId>
                            <artifactId>explicit-deps-app</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                            <name>explicit-deps-app</name>
                            <description>explicit-deps-app</description>
                            <properties>
                                <java.version>17</java.version>
                                <maven.compiler.source>17</maven.compiler.source>
                                <maven.compiler.target>17</maven.compiler.target>
                                <spring.version>3.0.0-M3</spring.version>
                            </properties>
                        
                            <repositories>
                                <repository>
                                    <id>spring-milestone</id>
                                    <url>https://repo.spring.io/milestone</url>
                                    <snapshots>
                                        <enabled>false</enabled>
                                    </snapshots>
                                </repository>
                            </repositories>
                        
                            <dependencies>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-web</artifactId>
                                    <version>${spring.version}</version>
                                </dependency>
                                <dependency>
                                    <groupId>io.dropwizard.metrics</groupId>
                                    <artifactId>metrics-annotation</artifactId>
                                    <version>4.2.9</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-test</artifactId>
                                    <version>${spring.version}</version>
                                    <scope>test</scope>
                                </dependency>
                            </dependencies>
                        
                            <build>
                                <plugins>
                                    <plugin>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-maven-plugin</artifactId>
                                    </plugin>
                                </plugins>
                            </build>
                        </project>
                        """);
    }

    @Test
    void shouldBuildCorrectPomModelAfterUpdateTo30() {
        Recipe recipe = new UpgradeUnmanagedSpringProject(
                "3.0.0-M3",
                "2\\.7\\..*");

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            throw new RuntimeException("Error due UpgradeUnmanagedSpringProject recipe: " + ex.getMessage(), ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>explicit-deps-app</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>explicit-deps-app</name>
                    <description>explicit-deps-app</description>
                    <properties>
                        <java.version>17</java.version>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <repositories>
                        <repository>
                            <id>spring-milestone</id>
                            <url>https://repo.spring.io/milestone</url>
                            <snapshots>
                                <enabled>false</enabled>
                            </snapshots>
                        </repository>
                    </repositories>
                
                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                            <version>2.7.3</version>
                        </dependency>
                        <dependency>
                            <groupId>io.dropwizard.metrics</groupId>
                            <artifactId>metrics-annotation</artifactId>
                            <version>4.2.8</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <version>2.7.3</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                </project>
                """);

        List<Result> result = recipe.run(documentList, ctx).getResults();

        assertThat(result).hasSize(1);

        Optional<MavenResolutionResult> mavenResolutionResult = result.get(0).getAfter().getMarkers().findFirst(MavenResolutionResult.class);
        assertThat(mavenResolutionResult).isPresent();

        List<ResolvedDependency> resolvedTestDependencies = mavenResolutionResult.get().getDependencies().get(Scope.Test);
        assertThat(resolvedTestDependencies).isNotEmpty();

        List<ResolvedDependency> resolvedDependencies = mavenResolutionResult.get().getDependencies().get(Scope.Compile);
        assertThat(resolvedDependencies).isNotEmpty();

        Condition<ResolvedDependency> cSpringStarterWeb = new Condition<>(
                rd -> rd.getArtifactId().equals("spring-boot-starter-web") && rd.getVersion().equals("3.0.0-M3"),
                "spring-boot-starter-web in version 3.0.0-M3");
        assertThat(resolvedDependencies).haveExactly(1, cSpringStarterWeb);

        Condition<ResolvedDependency> cMetrics = new Condition<>(
                rd -> rd.getArtifactId().equals("metrics-annotation") && rd.getVersion().equals("4.2.9"),
                "metrics-annotation in version 4.2.9");
        assertThat(resolvedDependencies).haveExactly(1, cMetrics);

        Condition<ResolvedDependency> cSpringStarterTest = new Condition<>(
                rd -> rd.getArtifactId().equals("spring-boot-starter-test") && rd.getVersion().equals("3.0.0-M3"),
                "spring-boot-starter-test in version 3.0.0-M3");
        assertThat(resolvedTestDependencies).haveExactly(1, cSpringStarterTest);
    }
}
