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
package org.springframework.sbm.java.impl;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathRegistryTest {
    @Test
    void classpathRegistryShouldKeepOnlyExternalDependencies() {

        @Language("xml")
        String parentPom =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>com.acme</groupId>
                            <artifactId>dummy</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                            <packaging>pom</packaging>
                            <modules>
                                <module>pom1</module>
                                <module>pom2</module>
                            </modules>
                        </project>
                        """;

        @Language("xml")
        String pom1 =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>dummy</artifactId>
                                <version>0.0.1-SNAPSHOT</version>
                                <relativePath>../</relativePath>
                            </parent>
                            <artifactId>pom1</artifactId>
                        </project>
                        """;

        @Language("xml")
        String pom2 =
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <parent>
                                <groupId>com.acme</groupId>
                                <artifactId>dummy</artifactId>
                                <version>0.0.1-SNAPSHOT</version>
                                <relativePath>../</relativePath>
                            </parent>
                            <artifactId>pom2</artifactId>
                            <dependencies>
                                <dependency>
                                    <groupId>com.acme</groupId>
                                    <artifactId>pom1</artifactId>
                                    <version>0.0.1-SNAPSHOT</version>
                                </dependency>
                                <dependency>
                                    <groupId>javax.validation</groupId>
                                    <artifactId>validation-api</artifactId>
                                    <version>2.0.1.Final</version>
                                </dependency>
                            </dependencies>
                        </project>
                        """;

        ClasspathRegistry sut = ClasspathRegistry.getInstance();
        sut.clear();

        assertThat(sut.getCurrentDependencies()).isEmpty();
        assertThat(sut.getInitialDependencies()).isEmpty();

        ExecutionContext executionContext = new RewriteExecutionContext();
        List<Xml.Document> poms = new RewriteMavenParser(
                new MavenSettingsInitializer(),
                executionContext
        ).parse(parentPom, pom1, pom2)
                .map(Xml.Document.class::cast)
                .toList();

        Set<ResolvedDependency> resolvedDependencies = poms
                .get(2)
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getDependencies()
                .get(Scope.Compile)
                .stream()
                .collect(Collectors.toSet());

        ClasspathRegistry registry = ClasspathRegistry.initialize(resolvedDependencies);
        assertThat(registry.getCurrentDependencies()).hasSize(1);
        assertThat(registry.getInitialDependencies()).hasSize(1);
    }
}
