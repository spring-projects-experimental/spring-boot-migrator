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
package org.springframework.sbm.build.api;

import org.openrewrite.java.JavaParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.springframework.sbm.build.impl.JavaSourceSetImpl;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.common.util.Verify;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Module {

    @Getter
    private final String name;
    @Getter
    private final BuildFile buildFile;
    @Getter
    private final Path projectRootDir;
    @Getter
    private final Path modulePath;
    private final ProjectResourceSet projectResourceSet;
    private final JavaRefactoringFactory javaRefactoringFactory;
    private final BasePackageCalculator basePackageCalculator;
    private final JavaParser javaParser;

    public JavaSourceLocation getBaseJavaSourceLocation() {
        return getMainJavaSourceSet().getJavaSourceLocation();
    }

    public JavaSourceLocation getBaseTestJavaSourceLocation() {
        return getTestJavaSourceSet().getJavaSourceLocation();
    }

    public JavaSourceSet getTestJavaSourceSet() {
        Path testJavaPath = Path.of("src/test/java");
        // FIXME: #7 JavaParser
        return new JavaSourceSetImpl(projectResourceSet, projectRootDir, modulePath, testJavaPath, javaRefactoringFactory, basePackageCalculator, javaParser);
    }

    public List<? extends JavaSource> getMainJavaSources() {
        // FIXME:
        return List.of();
    }

    public List<? extends JavaSource> getTestJavaSources() {
        // FIXME:
        return List.of();
    }

    public JavaSourceSet getMainJavaSourceSet() {
        Path mainJavaPath = Path.of("src/main/java");
//        return new JavaSourceSetImpl(projectResourceSet, projectRootDir.resolve(modulePath).resolve(mainJavaPath), javaRefactoringFactory);
        return new JavaSourceSetImpl(projectResourceSet, projectRootDir, modulePath, mainJavaPath, javaRefactoringFactory, basePackageCalculator, javaParser);
    }

    private List<JavaSource> cast(List<RewriteSourceFileHolder<? extends SourceFile>> filter) {
        return filter.stream()
                .filter(r -> r.getType().isAssignableFrom(r.getClass()))
                .map(this::cast)
                .collect(Collectors.toList());
    }

    private JavaSource cast(RewriteSourceFileHolder<? extends SourceFile> r) {
        return (JavaSource) r;
    }

    private List<RewriteSourceFileHolder<? extends SourceFile>> filter(ProjectResourceSet projectResourceSet) {
        // filter all in main source dir as defined in the pom.xml of this module
        Path mainSource = getBuildFile().getSourceFolders().stream().filter(sf -> sf.toString().contains("/main/")).findFirst().get();
        return projectResourceSet
                .stream()
                .filter(r -> r.getAbsolutePath().toString().startsWith(mainSource.toString()))
                .collect(Collectors.toList());
    }

    public Path getProjectRootDirectory() {
        return projectRootDir;
    }

    public ResourceSet getMainResourceSet() {
        Path mainResourceSet = buildFile.getMainResourceFolder();
        return new ResourceSet(projectResourceSet, projectRootDir, modulePath, mainResourceSet);
    }

    public ResourceSet getTestResourceSet() {
        Path testResourceSet = buildFile.getTestResourceFolder();
        return new ResourceSet(projectResourceSet, projectRootDir, modulePath, testResourceSet);
    }

    public List<Module> getModules() {
        Optional<MavenResolutionResult> mavenResolution = MavenBuildFileUtil.findMavenResolution(((OpenRewriteMavenBuildFile) buildFile).getSourceFile());
        List<MavenResolutionResult> modulesMarker = mavenResolution.get().getModules();
        if (!modulesMarker.isEmpty()) {
            return modulesMarker
                    .stream()
                    .map(m -> new Module(m.getPom().getGav().toString(), this.buildFile, projectRootDir, modulePath,
                                         projectResourceSet, javaRefactoringFactory, basePackageCalculator, javaParser))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<String> getDeclaredModules() {
        return buildFile.getDeclaredModules();
    }

    public <T> T search(ProjectResourceFinder<T> finder) {
        List<RewriteSourceFileHolder<? extends SourceFile>> resources = getModuleResources();
        ProjectResourceSet filteredProjectResourceSet = new ProjectResourceSet(resources);
        return finder.apply(filteredProjectResourceSet);
    }

    private List<RewriteSourceFileHolder<? extends SourceFile>> getModuleResources() {
        return projectResourceSet.stream()
                .filter(r -> r.getAbsolutePath().toString().startsWith(projectRootDir.resolve(modulePath).toString()))
                .collect(Collectors.toList());
    }

    public <T> T searchMainResources(ProjectResourceFinder<T> finder) {
        ProjectResourceSet resourceSet = new ImmutableFilteringProjectResourceSet(projectResourceSet, (RewriteSourceFileHolder<? extends SourceFile> r) -> r.getAbsolutePath().normalize().startsWith(getMainResourceSet().getAbsolutePath().toAbsolutePath().normalize()));
        return finder.apply(resourceSet);
    }

    public <T> T searchMainJava(ProjectResourceFinder<T> finder) {
        ProjectResourceSet resourceSet = new ImmutableFilteringProjectResourceSet(projectResourceSet, (RewriteSourceFileHolder<? extends SourceFile> r) -> r.getAbsolutePath().normalize().startsWith(getMainJavaSourceSet().getAbsolutePath().toAbsolutePath().normalize()));
        return finder.apply(resourceSet);
    }

    public <T> T searchTestResources(ProjectResourceFinder<T> finder) {
        ProjectResourceSet resourceSet = new ImmutableFilteringProjectResourceSet(projectResourceSet, (RewriteSourceFileHolder<? extends SourceFile> r) -> r.getAbsolutePath().normalize().startsWith(getTestResourceSet().getAbsolutePath().toAbsolutePath().normalize()));
        return finder.apply(resourceSet);
    }

    public <T> T searchTestJava(ProjectResourceFinder<T> finder) {
        ProjectResourceSet resourceSet = new ImmutableFilteringProjectResourceSet(projectResourceSet, (RewriteSourceFileHolder<? extends SourceFile> r) -> r.getAbsolutePath().normalize().startsWith(getTestJavaSourceSet().getAbsolutePath().toAbsolutePath().normalize()));
        return finder.apply(resourceSet);
    }

    /**
     * Checks if any resources of this {@code Module} matches the given {@code resourcePath}.
     */
    public boolean contains(Path resourcePath) {
        Verify.absolutePath(resourcePath);
        return getModuleResources().stream().anyMatch(r -> r.getAbsolutePath().toString().equals(resourcePath.toString()));
    }

    /**
     * Class provides filtering on the list of resources in a {@code ProjectResourceSet}.
     * As all read methods rely on {@code stream()}, only this stream has to be filtered. :fingers_crossed:
     *
     * It's a private inner class as it is currently only used here and quite hacky overwriting only parts of
     * {@code ProjectResourceSet} with a lot of assumptions of the inner workings of other classes.
     */
    private class ImmutableFilteringProjectResourceSet extends ProjectResourceSet{
        private final ProjectResourceSet projectResourceSet;
        private final Predicate<RewriteSourceFileHolder<? extends SourceFile>> predicate;

        public ImmutableFilteringProjectResourceSet(ProjectResourceSet projectResourceSet, Predicate<RewriteSourceFileHolder<? extends SourceFile>> predicate) {
            this.projectResourceSet = projectResourceSet;
            this.predicate = predicate;
        }

        @Override
        public Stream<RewriteSourceFileHolder<? extends SourceFile>> stream() {
            return projectResourceSet.stream()
                    .filter(this.predicate);
        }
    }
}
