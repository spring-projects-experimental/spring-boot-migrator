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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DependenciesChangedEventHandler {
    private final ProjectContextHolder projectContextHolder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final JavaParser javaParser;

    @EventListener
    public void onDependenciesChanged(DependenciesChangedEvent event) {
        if (projectContextHolder.getProjectContext() != null) {
            Set<Parser.Input> compilationUnitsSet = projectContextHolder.getProjectContext().getProjectJavaSources().stream()
                    .map(js -> js.getResource().getSourceFile())
                    .map(js -> new Parser.Input(js.getSourcePath(), () -> new ByteArrayInputStream(js.printAll().getBytes(StandardCharsets.UTF_8))))
                    .collect(Collectors.toSet());
            List<Parser.Input> compilationUnits = new ArrayList<>(compilationUnitsSet);

            Path projectRootDirectory = projectContextHolder.getProjectContext().getProjectRootDirectory();
            // FIXME: #7 only affected modules and source sets must be parsed
            javaParser = JavaParser.fromJavaVersion().classpath(ClasspathRegistry.getInstance().getCurrentDependencies()).build();
            //javaParser.setClasspath(ClasspathRegistry.getInstance().getCurrentDependencies());
            // FIXME: #7 handle "test"
            // FIXME: #7 Provide a unified interface that calculates source set names by path
            javaParser.setSourceSet("main");
            List<J.CompilationUnit> parsedCompilationUnits = javaParser.parseInputs(compilationUnits, null, new RewriteExecutionContext(applicationEventPublisher));
            // ((J.VariableDeclarations)parsedCompilationUnits.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType()
            parsedCompilationUnits.forEach(cu -> {
                projectContextHolder.getProjectContext().getProjectJavaSources().asStream()
                        .filter(js -> js.getResource().getAbsolutePath().equals(projectRootDirectory.resolve(cu.getSourcePath()).normalize()))
                        .forEach(js -> js.getResource().replaceWith(cu));
            });
        }
    }
}

