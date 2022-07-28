package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
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
                null
        );

        List<Throwable> errors = new ArrayList<>();
        InMemoryExecutionContext ctx = new InMemoryExecutionContext((ex) -> {
            ex.printStackTrace();
            errors.add(ex);
        });

        MavenParser parser = MavenParser.builder().build();
        List<Xml.Document> documentList = parser.parse("""
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>test</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0.0-SNAPSHOT</version>
                                
                    <name>Test</name>
                                
                    <dependencyManagement>
                        <dependencies>
                            <dependency>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>2.7.1</version>
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

        List<Result> result = recipe.run(documentList, ctx);

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
                                        
                            <name>Test</name>
                                        
                            <dependencyManagement>
                                <dependencies>
                                    <dependency>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-dependencies</artifactId>
                                        <version>3.0.0-M3</version>
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

    // TODO: handle variable reference in pom
}
