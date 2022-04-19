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
package org.springframework.sbm.project.parser;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.ResourceHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathScannerTest {

    private static final String TESTCODE_DIR = "./testcode/path-scanner/module1/src/main/webapp/META-INF";

    @Test
    void absoluteIgnorePatternShouldThrowException() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        assertThatThrownBy(() ->  applicationProperties.setIgnoredPathsPatterns(List.of("/**/foo.bar","**/ok/**","/fails/**")))
                .hasMessage("Found absolute ignore paths patterns defined in sbm.ignoredPathsPatterns. Patterns must be relative and not start with '/'. Invalid patterns found: ['/**/foo.bar', '/fails/**'].")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void returnsAllWhenNoPatternMatches() {

        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setIgnoredPathsPatterns(List.of("**/foo.bar"));
        PathScanner sut = new PathScanner(applicationProperties, new ResourceHelper(new DefaultResourceLoader()));
        List<Resource> resources = sut.scan(Path.of(TESTCODE_DIR).toAbsolutePath().normalize());
        assertThat(resources).hasSize(3);
    }

    @Test
    void returnsFilteredResourcesWhenPatternMatches() {

        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setIgnoredPathsPatterns(List.of("**/*.xslt", "**/*.wsdl"));
        PathScanner sut = new PathScanner(applicationProperties, new ResourceHelper(new DefaultResourceLoader()));
        List<Resource> resources = sut.scan(Path.of(TESTCODE_DIR).toAbsolutePath().normalize());

        assertThat(resources).hasSize(1);
    }

}