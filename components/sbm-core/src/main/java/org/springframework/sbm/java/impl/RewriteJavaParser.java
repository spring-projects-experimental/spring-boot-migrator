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

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.scopes.annotations.ScanScope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
@ScanScope
public class RewriteJavaParser implements JavaParser {

    private final SbmApplicationProperties sbmApplicationProperties;
    @Getter
    private final JavaParser javaParser;
    private final ExecutionContext executionContext;


    // satisfies DI
    public RewriteJavaParser(SbmApplicationProperties sbmApplicationProperties, ExecutionContext executionContext) {
        this.sbmApplicationProperties = sbmApplicationProperties;
        this.executionContext = executionContext;
        javaParser = buildJavaParser(Collections.emptySet());
    }

    @NotNull
    private JavaParser buildJavaParser(Collection<Path> classpath) {
        Builder<? extends JavaParser, ?> builder = JavaParser.fromJavaVersion()
                .logCompilationWarningsAndErrors(sbmApplicationProperties.isJavaParserLoggingCompilationWarningsAndErrors());
        if (!classpath.isEmpty()) {
            builder.classpath(classpath);
        }
        return builder.build();
    }

    public List<J.CompilationUnit> parseInputsToCompilationUnits(Iterable<Input> sources, @Nullable Path relativeTo, ExecutionContext ctx) {
        reset();
        return parseInputs(sources, relativeTo, executionContext)
                .filter(J.CompilationUnit.class::isInstance)
                .map(J.CompilationUnit.class::cast)
                .toList();
    }

    public Stream<SourceFile> parseInputs(Iterable<Input> sources, @Nullable Path relativeTo, ExecutionContext ctx) {
        reset();
        return this.javaParser.parseInputs(sources, relativeTo, ctx);
    }

    @Override
    public JavaParser reset() {
        return this.javaParser.reset();
    }

    @Override
    public JavaParser reset(Collection<URI> uris) {
        return javaParser.reset();
    }

    @Override
    public void setClasspath(Collection<Path> classpath) {
        this.javaParser.setClasspath(classpath);
    }

    public Stream<SourceFile> parse(List<Path> javaResources, ExecutionContext executionContext) {
        reset();
        return this.parse(javaResources, null, executionContext);
    }

    @Override
    public Stream<SourceFile> parse(String... sources) {
        return this.parse(executionContext, sources);
    }
}
