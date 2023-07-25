package org.springframework.sbm.parsers;/*
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

import org.apache.maven.Maven;
import org.apache.maven.artifact.repository.*;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests adherence to settings in '.mvn/maven.config' by {@link MavenConfigFileParser},
 *
 * @author Fabian Krüger
 */
public class MavenConfigFileParserTest {

    private final MavenConfigFileParser mavenConfigFileParser = new MavenConfigFileParser();

    @Test
    @DisplayName("Should read define options from .mvn/maven.config")
    void shouldReadDefineOptionsFromMvnMavenConfig() {
        Path baseDir = Path.of("./testcode/maven-projects/maven-config").toAbsolutePath().normalize();
        Map<String, String> props = mavenConfigFileParser.getUserProperties(baseDir);
        assertThat(props).hasSize(2);
        assertThat(props.get("revision")).isEqualTo("42");
        assertThat(props.get("validation-api.version")).isEqualTo("2.0.1.Final");
    }

    @Test
    @DisplayName("Should read active profile from .mvn/maven.config")
    void shouldReadActiveProfileFromMvnMavenConfig() {
        Path baseDir = Path.of("./testcode/maven-projects/maven-config").toAbsolutePath().normalize();
        List<String> props = mavenConfigFileParser.getActivatedProfiles(baseDir);

        assertThat(props).hasSize(2);
        assertThat(props).containsExactlyInAnyOrderElementsOf(List.of("some-profile","another-profile"));
    }

}
