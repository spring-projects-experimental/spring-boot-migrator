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
package org.springframework.sbm.project.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.SourceFile;
import org.springframework.core.io.Resource;
import org.springframework.rewrite.RewriteProjectParser;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Parse a Maven project on disk into a list of {@link org.openrewrite.SourceFile} including
 * Maven, Java, YAML, properties, and XML AST representations of sources and resources found.
 *
 * @deprecated
 * Use {@code RewriteProjectParser} from {@code sbm-rewrite-launcher} instead
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated(forRemoval = true)
public class MavenProjectParser {

    private final RewriteProjectParser parser;

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        return parser.parse(projectDirectory, resources).sourceFiles();
    }


}
