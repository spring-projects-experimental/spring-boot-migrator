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
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.sbm.build.api.ApplicationModules;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.parsers.SortedProjects;
import org.springframework.sbm.parsers.SourceFileParser;
import org.springframework.sbm.utils.JavaHelper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.*;
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

    private SourceFileParser sourceFileParser;
    /**
     * Handle changes of the dependency list in {@code currentBuildFile}.
     * First the affected java sources in the module of the {@code currentBuildFile} are recompiled.
     * It then recursively finds the modules depending on the current currentBuildFile and recompiles affected sources with the new classpath.
     *
     * @return
     */
    public void handleDependencyChanges(OpenRewriteMavenBuildFile currentBuildFile) {
        SortedProjects sortedProjects = new SortedProjects();
        sourceFileParser.parseOtherSourceFiles(
                currentBuildFile.getAbsoluteProjectDir(),
                sortedProjects,
                pathToDocumentMap,
                resources,
                provenanceMarkers,
                styles,
                executionContext
            );

        ProjectContext projectContext = projectContextHolder.getProjectContext();
        // create a mapping dependency -> dependant module information
        Map<String, List<DependingModuleInfo>> dependencyModuleMap = createDependencyToModuleMappings(projectContext.getApplicationModules());
        // track recompiled sources
        List<SourceFile> recompiledClasses = new ArrayList<>();
        // get current module
        Module module = getModuleForBuildFile(currentBuildFile, projectContext);
        // recompile module
        List<SourceFile> recompiledModuleClasses = recompileModuleClasses(module);
        recompiledClasses.addAll(recompiledModuleClasses);
        // recompile directly or transitively affected modules
        List<SourceFile> recompiledClassesFromAffectedModules = recompileAffectedModules(dependencyModuleMap, module);
        recompiledClasses.addAll(recompiledClassesFromAffectedModules);
        // update the wrapped SourceFiles
        updateSourceFileHolder(projectContext, recompiledClasses);
    }

    private static Module getModuleForBuildFile(OpenRewriteMavenBuildFile currentBuildFile, ProjectContext projectContext) {
        return projectContext.getApplicationModules().findModule(currentBuildFile.getGav()).orElseThrow(() -> new IllegalStateException("Could not find Module for given build file %s".formatted(currentBuildFile.getSourcePath())));
    }

    private Map<String, List<DependingModuleInfo>> createDependencyToModuleMappings(ApplicationModules applicationModules) {
        Map<String, List<DependingModuleInfo>> map = new HashMap<>();
        for(Module m : applicationModules.list()) {
            m.getBuildFile().getDeclaredDependencies().forEach(d -> {
                String scope = JavaHelper.uppercaseFirstChar(d.getEffectiveScope().toLowerCase());
                DependingModuleInfo moduleInfo = new DependingModuleInfo(d.getGav(), Scope.valueOf(scope), m);
                map.computeIfAbsent(d.getGav(), k -> new ArrayList<>()).add(moduleInfo);
            });
        }
        return map;
    }

    private void updateSourceFileHolder(ProjectContext projectContext, List<SourceFile> recompiledClasses) {
        List<String> recompiledClassesPaths = recompiledClasses.stream()
                .map(c -> projectContext.getProjectRootDirectory().resolve(c.getSourcePath().toString()))
                .map(Path::toString)
                .toList();

        projectContext.getProjectResources().stream()
                .forEach(r -> {
                    if(recompiledClassesPaths.contains(r.getAbsolutePath().toString())) {
                        int i = recompiledClassesPaths.indexOf(r.getAbsolutePath().toString());
                        SourceFile sourceFile = recompiledClasses.get(i);
                        r.replaceWith(sourceFile);
                    }
                });
    }

    private List<SourceFile> recompileAffectedModules(Map<String, List<DependingModuleInfo>> dependencyModuleMap, Module module) {
        List<SourceFile> recompiledSourceFiles = new ArrayList<>();
        recursivelyRecompileAffectedModules(dependencyModuleMap, module, recompiledSourceFiles);
        return recompiledSourceFiles;
    }

    private void recursivelyRecompileAffectedModules(Map<String, List<DependingModuleInfo>> dependencyModuleMap, Module module, List<SourceFile> recompiledSourceFiles) {
        List<DependingModuleInfo> affectedModules = dependencyModuleMap.getOrDefault(module.getBuildFile().getGav(), List.of());
        affectedModules.forEach(affectedModuleInfo -> {
            Module affectedModule = affectedModuleInfo.module();
            recompiledSourceFiles.addAll(recompileModuleClasses(affectedModule));
            recursivelyRecompileAffectedModules(dependencyModuleMap, affectedModule, recompiledSourceFiles);
        });
    }

    private List<SourceFile> recompileModuleClasses(Module module) {
        Map<Scope, Set<Path>> resolvedDependenciesMap = module.getBuildFile().getResolvedDependenciesMap();
        Map<Scope, Set<Path>> scopeListMap = flattenToCompileAndTestScope(resolvedDependenciesMap);
        List<Parser.Input> mainSources = module.getMainJavaSourceSet().list().stream()
                .map(ja -> new Parser.Input(ja.getAbsolutePath(), () -> new ByteArrayInputStream(ja.print().getBytes())))
                .toList();
        // FIXME: reuse logic from parser here. it must be guaranteed that markers are added
        Set<Path> compileClasspath = scopeListMap.get(Scope.Compile);
        List<SourceFile> main = javaParserBuilder.classpath(compileClasspath).build().parseInputs(mainSources, module.getProjectRootDir(), executionContext).toList();
        JavaSourceSet javaSourceSet = module.getMainJavaSourceSet().list().get(0).getResource().getSourceFile().getMarkers().findFirst(JavaSourceSet.class).get();
        List<Parser.Input> testSources = module.getTestJavaSourceSet().list().stream()
                .map(ja -> new Parser.Input(ja.getAbsolutePath(), () -> new ByteArrayInputStream(ja.print().getBytes())))
                .toList();
        List<SourceFile> test = javaParserBuilder.classpath(scopeListMap.get(Scope.Test)).build().parseInputs(testSources, module.getProjectRootDir(), executionContext).toList();
        List<SourceFile> result = new ArrayList<>();
        result.addAll(main);
        result.addAll(test);
        return result;
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

    private Map<Scope, Set<Path>> flattenToCompileAndTestScope(Map<Scope, Set<Path>> resolvedDependencies) {
        Map<Scope, Set<Path>> boiled = new HashMap<>();
        Arrays.stream(Scope.values())
                .forEach(scope -> {
                    Set<Path> paths = resolvedDependencies.get(scope);
                    if(paths != null) {
                        if(scope.isInClasspathOf(Scope.Compile)) {
                            boiled.computeIfAbsent(Scope.Compile, k -> new HashSet<>()).addAll(paths);
                        }
                        if(scope.isInClasspathOf(Scope.Test)) {
                            boiled.computeIfAbsent(Scope.Test, k -> new HashSet<>()).addAll(paths);
                        }
                    }
                });
        return boiled;
    }

    private record DependingModuleInfo(String gav, Scope scope, Module module) {
    }
}
