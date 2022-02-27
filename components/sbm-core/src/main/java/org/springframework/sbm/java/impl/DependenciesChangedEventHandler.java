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

import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import lombok.RequiredArgsConstructor;
import org.openrewrite.Parser;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DependenciesChangedEventHandler {
    private final ProjectContextHolder projectContextHolder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void onDependenciesChanged(DependenciesChangedEvent event) {
        if (projectContextHolder.getProjectContext() != null) {
            JavaParser currentJavaParser = JavaParserFactory.getCurrentJavaParser();
            List<Parser.Input> compilationUnits = projectContextHolder.getProjectContext().getProjectJavaSources().asStream()
                    .map(js -> js.getResource().getSourceFile())
                    .map(js -> new Parser.Input(js.getSourcePath(), () -> new ByteArrayInputStream(js.printAll().getBytes(StandardCharsets.UTF_8))))
                    .collect(Collectors.toList());

            Path projectRootDirectory = projectContextHolder.getProjectContext().getProjectRootDirectory();
            List<J.CompilationUnit> parsedCompilationUnits = currentJavaParser.parseInputs(compilationUnits, projectRootDirectory, new RewriteExecutionContext(applicationEventPublisher));

            parsedCompilationUnits.forEach(cu -> {
                projectContextHolder.getProjectContext().getProjectJavaSources().asStream()
                        .filter(js -> js.getResource().getAbsolutePath().equals(projectRootDirectory.resolve(cu.getSourcePath()).normalize()))
                        .forEach(js -> js.getResource().replaceWith(cu));
            });
        }
    }
}

