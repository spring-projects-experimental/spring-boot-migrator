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
package org.openrewrite.maven;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.cache.ReadOnlyLocalMavenArtifactCache;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.sbm.parsers.maven.RewriteMavenArtifactDownloader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.ibm.icu.impl.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class MavenParserTest {
    
    @Test
    @DisplayName("parse pom with relocated dependency")
    void parsePomWithRelocatedDependency() {
        @Language("xml")
        String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                                
                    <groupId>com.acme</groupId>
                    <artifactId>app</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    
                    <dependencies>
                        <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.33</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        List<SourceFile> parse = MavenParser.builder().build().parse(pom).toList();
        assertThat(parse).isNotNull();

//        MavenArtifactDownloader mavenArtifactDownloader = new MavenArtifactDownloader(new ReadOnlyLocalMavenArtifactCache(Path.of(System.getProperty("user.home")).resolve(".m2/repository")), null, e -> {
//            throw new RuntimeException(e);
//        });

        MavenResolutionResult mavenResolutionResult = parse.get(0).getMarkers().findFirst(MavenResolutionResult.class).get();
        List<ResolvedDependency> resolvedDependencies = mavenResolutionResult.getDependencies().get(Scope.Compile);

        MavenArtifactDownloader mavenArtifactDownloader = new RewriteMavenArtifactDownloader(
                new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".m2", "repository"))
                        .orElse(
                            new LocalMavenArtifactCache(Paths.get(System.getProperty("user.home"), ".rewrite", "cache", "artifacts")
                        )
                ),
                null,
                t -> {throw new RuntimeException(t);}
        );
        List<Path> list = resolvedDependencies
                // FIXME: 945 - deal with dependencies to projects in reactor
                //
                .stream()
                .filter(rd -> rd.getRepository() != null)
                .map(rd -> mavenArtifactDownloader.downloadArtifact(rd))
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        assertThat(list).isNotNull();
    }
    
    @Test
    @DisplayName("Should Read .mvn/maven.config")
    @ExpectedToFail("See https://github.com/openrewrite/rewrite/issues/3409")
    void shouldReadMvnMavenConfig() {
        String mavenConfig = """
                -Drevision=42
                -Dvalidation-api.version=2.0.1.Final
                -Psome-profile,another-profile
                """;

        Path baseDir = Path.of("./testcode/maven-projects/maven-config");
        Stream<SourceFile> parse = MavenParser.builder()
                .mavenConfig(baseDir.resolve(".mvn/maven.config"))
                .build()
                .parse(
                        List.of(baseDir.resolve("pom.xml")),
                        null,
                        new InMemoryExecutionContext(t -> {
                            t.printStackTrace();
                            fail("exception");
                        })
                );
    }

    @Test
    @DisplayName("testRegex")
    // TODO: Tryout of the regex that leads to the failing test (above), remove this test when issue #3409 is fixed.
    void testRegex() {

        String regex = "(?:$|\\s)-P\\s+([^\\s]+)";
        assertThat(Pattern.compile(regex).matcher("\n-P someProfile\n").find()).isTrue();
        assertThat(Pattern.compile(regex).matcher("""
                -Psome-profile,another-profile
                """).find()).isFalse();
        assertThat(Pattern.compile(regex).matcher("-PsomeProfile").find()).isFalse();
        assertThat(Pattern.compile(regex).matcher("-P someProfile").find()).isFalse();
    }
}
