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
package org.openrewrite.maven;

import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.*;
import org.springframework.sbm.build.api.RepositoryDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Fabian Krüger
 */
public class ResolvedPomResolveDependencdiesTest {

    @Test
    void test_renameMe() {
        @Language("xml") String pom = """
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>foo</groupId>
                    <artifactId>bar</artifactId>
                    <version>1.0.0</version>
                    <dependencies>
                      <dependency>
                            <groupId>com.google.cloud</groupId>
                            <artifactId>google-cloud-pubsub</artifactId>
                            <version>1.105.0</version>
                        </dependency>
                    <dependency>
                            <groupId>io.grpc</groupId>
                            <artifactId>grpc-netty-shaded</artifactId>
                            <version>1.42.1</version>
                            <type>pom</type>
                        </dependency>
                    </dependencies>
                </project>
                """;
        MavenParser
                .builder()
                .build()
                .parse(pom)
                .get(0)
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get();
    }

}
