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
package org.springframework.sbm.parsers.gradle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.springframework.sbm.parsers.RewriteGradleProjectParser;

/**
 * @author Fabian Krüger
 */
public class RewriteGradleParserTest {
	
    private final RewriteGradleProjectParser sut = new RewriteGradleProjectParser();

    
    @Test
    @DisplayName("Should ")
    void should() {
        File baseDir = Path.of("./testcode/gradle-projects/gs-rest-service-complete").toAbsolutePath().normalize().toFile();
        File buildFile = new File(baseDir, "build.gradle");
        List<SourceFile> files = sut.parse(baseDir, buildFile, new InMemoryExecutionContext(t -> fail(t.getMessage()))).collect(Collectors.toList());
        assertThat(files).hasSize(16);
    }
}
