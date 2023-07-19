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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.GitProvenance;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.tree.*;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.events.*;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.scopes.ProjectMetadata;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Parse a Maven project on disk into a list of {@link org.openrewrite.SourceFile} including
 * Maven, Java, YAML, properties, and XML AST representations of sources and resources found.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MavenProjectParser {

    private final ResourceParser resourceParser;
    private final RewriteMavenParser mavenParser;
    private final MavenArtifactDownloader artifactDownloader;
    private final ApplicationEventPublisher eventPublisher;
    private final JavaProvenanceMarkerFactory javaProvenanceMarkerFactory;
    private final JavaParser javaParser;
    private final MavenConfigHandler mavenConfigHandler;
    private final ProjectMetadata projectMetadata;
    private final ExecutionContext executionContext;
    private final RewriteProjectParser parser;

    public List<SourceFile> parse(Path projectDirectory, List<Resource> resources) {
        return parser.parse(projectDirectory, resources, executionContext).sourceFiles();
    }


}
