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
package org.springframework.sbm.parsers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.tree.J;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.sbm.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.sbm.parsers.maven.RewriteMavenProjectParser;
import org.springframework.sbm.parsers.maven.SbmTestConfiguration;
import org.springframework.sbm.test.util.TestProjectHelper;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
@SpringBootTest(classes = {SbmSupportRewriteConfiguration.class, SbmTestConfiguration.class})
public class RewriteProjectParserIntegrationTest {

    @Autowired
    RewriteProjectParser sut;

    @Autowired
    ProjectScanner projectScanner;

    @Autowired
    RewriteMavenProjectParser mavenProjectParser;

    @Test
    @DisplayName("parseCheckstyle")
    void parseCheckstyle() {
        Path baseDir = TestProjectHelper.getMavenProject("checkstyle");
        List<Resource> resources = projectScanner.scan(baseDir);
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, new InMemoryExecutionContext(t -> {throw new RuntimeException(t);}));
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/rules.xml");
        assertThat(parsingResult.sourceFiles().stream().map(sf -> sf.getSourcePath().toString()).toList()).contains("checkstyle/suppressions.xml");
    }

    @Test
    @DisplayName("testFailingProject")
        // FIXME: Succeeds with RewriteMavenProjectParser
    void testFailingProject() {
        Path baseDir = Path.of("./testcode/maven-projects/failing");
        RewriteProjectParsingResult parsingResult = sut.parse(baseDir);
        assertThat(parsingResult.sourceFiles().get(1)).isInstanceOf(J.CompilationUnit.class);
        J.CompilationUnit cu = (J.CompilationUnit) parsingResult.sourceFiles().get(1);
        assertThat(cu.getTypesInUse().getTypesInUse().stream().map(t -> t.toString()).anyMatch(t -> t.equals("javax.validation.constraints.Min"))).isTrue();
    }

    @Test
    @DisplayName("parseResources")
    void parseResources() {
        Path baseDir = TestProjectHelper.getMavenProject("resources");
        List<Resource> resources = projectScanner.scan(baseDir);

//        RewriteProjectParsingResult parsingResult1 = mavenProjectParser.parse(baseDir);
//        assertThat(parsingResult1.sourceFiles()).hasSize(5);

        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, new InMemoryExecutionContext(t -> {
            throw new RuntimeException(t);
        }));
        assertThat(parsingResult.sourceFiles()).hasSize(5);
    }

    @Test
    @DisplayName("parse4Modules")
    void parse4Modules() {
        Path baseDir = TestProjectHelper.getMavenProject("4-modules");
        List<Resource> resources = projectScanner.scan(baseDir);

        assertThat(resources).hasSize(4);

        RewriteProjectParsingResult parsingResult = sut.parse(baseDir, resources, new InMemoryExecutionContext(t -> {throw new RuntimeException(t);}));
        assertThat(parsingResult.sourceFiles()).hasSize(4);
    }

}
