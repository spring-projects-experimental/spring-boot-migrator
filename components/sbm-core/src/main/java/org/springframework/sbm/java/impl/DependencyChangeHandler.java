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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Component
@RequiredArgsConstructor
public class DependencyChangeHandler {
    private final ProjectContextHolder projectContextHolder;
    private final JavaParserBuilder javaParserBuilder;
    private final ExecutionContext executionContext;

    /**
     * Handle changes of the dependency list in {@code currentBuildFile}.
     * First the affected java sources in the module of the {@code currentBuildFile} are recompiled.
     * It then recursively finds the modules depending on the current currentBuildFile and recompiles affected sources with the new classpath.
     *
     * @return
     */
    public List<J.CompilationUnit> handleDependencyChanges(OpenRewriteMavenBuildFile currentBuildFile, Map<Scope, List<ResolvedDependency>> effectiveDependencies) {

        // parse current module
        Map<Scope, List<ResolvedDependency>> compileAndTestDependencies = boilDownEffectiveDependenciesToCompileAndTestScope(effectiveDependencies);
        Module currentModule = projectContextHolder.getProjectContext().getApplicationModules().findModule(currentBuildFile.getGav())
                .orElseThrow(() -> new IllegalStateException("Could not find matching module for build file with GAV: '%s'".formatted(currentBuildFile.getGav())));

        List<J.CompilationUnit> mainSources = compileModuleSources(currentModule.getMainJavaSourceSet().list(), compileAndTestDependencies.get(Scope.Compile));
        List<J.CompilationUnit> testSources = compileModuleSources(currentModule.getTestJavaSourceSet().list(), compileAndTestDependencies.get(Scope.Test));

        List<J.CompilationUnit> recompiledSources = new ArrayList<>();
        recompiledSources.addAll(mainSources);
        recompiledSources.addAll(testSources);
        return recompiledSources;
    }

    void otherCode() {

//        replaceWrappedCompilationUnits();
//
//        // TODO: replace wrapped cu in RSFHs
//
//        ProjectContext projectContext = projectContextHolder.getProjectContext();
//        if (projectContext != null) {
//            Set<Parser.Input> compilationUnitsSet = projectContext.getProjectJavaSources().stream()
//                    .map(js -> js.getResource().getSourceFile())
//                    .map(js -> new Parser.Input(js.getSourcePath(), () -> new ByteArrayInputStream(js.printAll().getBytes(StandardCharsets.UTF_8))))
//                    .collect(Collectors.toSet());
//            List<Parser.Input> compilationUnits = new ArrayList<>(compilationUnitsSet);
//
//            Path projectRootDirectory = projectContext.getProjectRootDirectory();
//
//            // recompile affected classes in all affected modules
//            // get affected modules directly depending on changed module and recompile affected classes
//            // now these modules provide a new classpath to the modules depending on the current module.
//            // this ends when all modules were recompiled
//
//            OpenRewriteMavenBuildFile currentBuildFile = event.openRewriteMavenBuildFile();
//            Map<Scope, List<ResolvedDependency>> resolvedDependencies = event.getResolvedDependencies();
//            parseDependantModules(projectContext, currentBuildFile, resolvedDependencies);
//
//
//            event.resolvedDependencies().keySet().stream()
//                    .forEach(scope -> {
//                        OpenRewriteMavenBuildFile openRewriteMavenBuildFile = event.getOpenRewriteMavenBuildFile();
//                        Map<Scope, List<Module>> modulesWithDependencyTo = projectContext.getApplicationModules().findModulesWithDeclaredDependencyTo(openRewriteMavenBuildFile.getGav());
//                        modulesWithDependencyTo.keySet().stream()
//                                .forEach(s -> );
//                    });
//
//            javaParserBuilder.classpath();
//
//            List<J.CompilationUnit> parsedCompilationUnits = javaParserBuilder.parseInputs(compilationUnits, null, executionContext);
//            // ((J.VariableDeclarations)parsedCompilationUnits.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType()
//            parsedCompilationUnits.forEach(cu -> {
//                projectContext.getProjectJavaSources().stream()
//                        .filter(js -> js.getResource().getAbsolutePath().equals(projectRootDirectory.resolve(cu.getSourcePath()).normalize()))
//                        .forEach(js -> js.getResource().replaceWith(cu));
//            });
//        }
    }

//    private static void replaceWrappedCompilationUnits() {
//        parsedCompilationUnits.forEach(cu -> {
//            projectContext.getProjectJavaSources().stream()
//                    .filter(js -> js.getResource().getAbsolutePath().equals(projectRootDirectory.resolve(cu.getSourcePath()).normalize()))
//                    .forEach(js -> js.getResource().replaceWith(cu));
//        });
//    }

    private List<J.CompilationUnit> compileModuleSources(List<JavaSource> javaSources, List<ResolvedDependency> resolvedDependencies) {
        // TODO: Create SBM wrapper bean for MavenArtifactDownloader in ScanScope during parsing and use it here
        Path localRepo = Path.of(System.getProperty("user.home")).resolve(".m2/repository");
        MavenSettings mavenSettings = MavenExecutionContextView.view(executionContext).getSettings();
        MavenArtifactDownloader mavenArtifactDownloader = new MavenArtifactDownloader(new LocalMavenArtifactCache(localRepo), mavenSettings, error -> {
            throw new RuntimeException(error);
        });
        List<Path> classpath = resolvedDependencies.stream()
                .map(mavenArtifactDownloader::downloadArtifact)
                .toList();

        List<Parser.Input> inputs = mapToParserInputs(javaSources);

        return javaParserBuilder.classpath(classpath).build().parseInputs(inputs, projectContextHolder.getProjectContext().getProjectRootDirectory(), executionContext)
                .filter(J.CompilationUnit.class::isInstance)
                .map(J.CompilationUnit.class::cast)
                .toList();
    }

    private Map<Scope, List<ResolvedDependency>> boilDownEffectiveDependenciesToCompileAndTestScope(Map<Scope, List<ResolvedDependency>> effectiveDependencies) {
        // FIXME:
        return effectiveDependencies;
    }
/*
    private void parseDependantModules(ProjectContext projectContext, OpenRewriteMavenBuildFile currentBuildFile, Map<Scope, List<ResolvedDependency>> resolvedDependencies) {
        Map<Scope, List<Module>> dependantModules = projectContext.getApplicationModules().findModulesWithDeclaredDependencyTo(currentBuildFile.getGav());
        Map<Scope, List<ResolvedDependency>> compileAndTestScopeDependencies = boilDownToCompileAndTestScope(resolvedDependencies);
        dependantModules.stream().forEach(m -> {

            List<ResolvedDependency> compileScopeDependencies = compileAndTestScopeDependencies.get(Scope.Compile);
            @Nullable Path relativeTo = projectContext.getProjectRootDirectory();
            if(!compileScopeDependencies.isEmpty()) {
                List<Parser.Input> mainJavaSources = mapToParserInputs(m.getMainJavaSources());
                Stream<SourceFile> compileScopeSourceFiles = javaParserBuilder.classpath(compileScopeDependencies).build().parseInputs(mainJavaSources, relativeTo, executionContext);
                updateRewriteSourceFileHolders(projectContext, compileScopeSourceFiles);
            }

            List<ResolvedDependency> testScopeDependencies = compileAndTestScopeDependencies.get(Scope.Test);
            if(!testScopeDependencies.isEmpty()) {
                List<Parser.Input> testJavaSources = mapToParserInputs(m.getTestJavaSources());
                Stream<SourceFile> testScopeSourceFiles = javaParserBuilder.classpath(testScopeDependencies).build().parseInputs(testJavaSources, relativeTo, executionContext);
                updateRewriteSourceFileHolders(projectContext, testScopeSourceFiles);
            }
        });
    }
 */

    @NotNull
    private static List<Parser.Input> mapToParserInputs(List<? extends JavaSource> mainJavaSources1) {
        List<Parser.Input> mainJavaSources = mainJavaSources1.stream()
                .map(js -> new Parser.Input(js.getSourcePath(), () -> new ByteArrayInputStream(js.print().getBytes())))
                .toList();
        return mainJavaSources;
    }

    private Map<Scope, List<ResolvedDependency>> boilDownToCompileAndTestScope(Map<Scope, List<ResolvedDependency>> resolvedDependencies) {
        List<ResolvedDependency> resolvedDependencies1 = resolvedDependencies.get(Scope.Compile);

        return null;
    }
}
