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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import lombok.RequiredArgsConstructor;
import org.openrewrite.Parser;
import org.openrewrite.java.tree.J;
import org.springframework.context.event.EventListener;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles {@link DependenciesChangedEvent}s.
 * The provided {@link BuildFile} allows to fin
 *
 * @author Fabian Krueger
 */
@Component
@RequiredArgsConstructor
public class DependenciesChangedEventHandler {
    private final DependencyChangeHandler dependencyChangedHandler;

    private final ProjectContextHolder projectContextHolder;
    private final JavaParserBuilder javaParserBuilder;
    private final ExecutionContext executionContext;

    @EventListener
    public void onDependenciesChanged(DependenciesChangedEvent event) {
        dependencyChangedHandler.handleDependencyChanges(event.getOpenRewriteMavenBuildFile(), event.getResolvedDependencies());
    }
}

