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

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MavenConfigHandlerTest {


    @Test
    public void shouldNotErrorWhenMavenConfigIsWrong(@TempDir Path tempDir) throws IOException {

        List<String> contents = List.of(
                "-Horror Story:",
                "A developer used PHP",
                "** Scary music Intensifies **"
        );

        writeToMavenConfig(tempDir, contents);

        MavenConfigHandler target = new MavenConfigHandler();

        int propertySizeBefore = System.getProperties().keySet().size();
        target.injectMavenConfigIntoSystemProperties(tempDir);
        int propertySizeAfter = System.getProperties().keySet().size();

        assertThat(propertySizeAfter).isEqualTo(propertySizeBefore);
    }

    @Test
    public void parsesMavenConfig(@TempDir Path tempDir) throws IOException {

        List<String> contents = List.of(
                "-Drevision = 1.0.0",
                "-Dlicense.projectName=projectName",
                "helloworld=hello"
        );

        writeToMavenConfig(tempDir, contents);

        MavenConfigHandler target = new MavenConfigHandler();

        target.injectMavenConfigIntoSystemProperties(tempDir);

        assertThat(System.getProperty("revision")).isEqualTo("1.0.0");
        assertThat(System.getProperty("license.projectName")).isEqualTo("projectName");
        assertThat(System.getProperty("helloworld")).isNull();
    }

    @NotNull
    private Path writeToMavenConfig(Path tempDir,List<String> lines) throws IOException {
        Path mavenConfigDirectory = tempDir.resolve(".mvn");

        mavenConfigDirectory.toFile().mkdir();

        Path mavenConfigFile = mavenConfigDirectory.resolve("maven.config");

        Files.write(mavenConfigFile, lines);
        return mavenConfigFile;
    }
}
