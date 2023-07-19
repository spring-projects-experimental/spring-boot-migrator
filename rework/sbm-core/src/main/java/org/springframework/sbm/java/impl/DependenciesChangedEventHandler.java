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
package org.springframework.sbm.java.impl;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import lombok.RequiredArgsConstructor;
import org.openrewrite.Parser;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DependenciesChangedEventHandler {
    private final ProjectContextHolder projectContextHolder;
    private final JavaParser javaParser;
    private final ExecutionContext executionContext;

    @EventListener
    public void onDependenciesChanged(DependenciesChangedEvent event) {
        if (projectContextHolder.getProjectContext() != null) {
            Set<Parser.Input> compilationUnitsSet = projectContextHolder.getProjectContext().getProjectJavaSources().stream()
                    .map(js -> js.getResource().getSourceFile())
                    .map(js -> new Parser.Input(js.getSourcePath(), () -> new ByteArrayInputStream(js.printAll().getBytes(StandardCharsets.UTF_8))))
                    .collect(Collectors.toSet());
            List<Parser.Input> compilationUnits = new ArrayList<>(compilationUnitsSet);

            Path projectRootDirectory = projectContextHolder.getProjectContext().getProjectRootDirectory();

            Stream<SourceFile> parsedCompilationUnits = javaParser.parseInputs(compilationUnits, null, executionContext);
            // ((J.VariableDeclarations)parsedCompilationUnits.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType()
            parsedCompilationUnits
                    .filter(J.CompilationUnit.class::isInstance)
                    .map(J.CompilationUnit.class::cast)
                    .forEach(cu -> {
                        projectContextHolder.getProjectContext().getProjectJavaSources().stream()
                                .filter(js -> js.getResource().getAbsolutePath().equals(projectRootDirectory.resolve(cu.getSourcePath()).normalize()))
                                .forEach(js -> js.getResource().replaceWith(cu));
                    });
        }
    }
}

