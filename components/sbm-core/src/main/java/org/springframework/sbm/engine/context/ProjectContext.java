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
package org.springframework.sbm.engine.context;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaParser;
import org.springframework.sbm.build.api.ApplicationModules;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.RootBuildFileFilter;
import org.springframework.sbm.build.filter.BuildFileProjectResourceFilter;
import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.java.impl.ProjectJavaSourcesImpl;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProjectContext {

    private final Path projectRootDirectory;
    private final JavaRefactoringFactory javaRefactoringFactory;
    private BasePackageCalculator basePackageCalculator;
    private final ProjectResourceSet projectResources;
    private String revision;
    private final JavaParser javaParser;
    private final ExecutionContext executionContext;

    public ProjectContext(JavaRefactoringFactory javaRefactoringFactory, Path projectRootDirectory, ProjectResourceSet projectResources, BasePackageCalculator basePackageCalculator, JavaParser javaParser, ExecutionContext executionContext) {
        this.projectRootDirectory = projectRootDirectory.toAbsolutePath();
        this.projectResources = projectResources;
        this.javaRefactoringFactory = javaRefactoringFactory;
        this.basePackageCalculator = basePackageCalculator;
        this.javaParser = javaParser;
        this.executionContext = executionContext;
    }

    public ProjectResourceSet getProjectResources() {
        return projectResources;
    }

    /**
     * @deprecated
     * Use {@link #getApplicationModules()} instead.
     * TODO: Make method private
     */
    @Deprecated(forRemoval = false)
    public List<Module> getModules() {
        return search(new BuildFileProjectResourceFilter()).stream()
                .map(this::mapToModule)
                .collect(Collectors.toList());
    }

    private Module mapToModule(BuildFile buildFile) {
        String buildFileName = "";
        Path modulePath = projectRootDirectory.relativize(buildFile.getAbsolutePath().getParent());
        return new Module(buildFileName, buildFile, projectRootDirectory, modulePath, getProjectResources(), javaRefactoringFactory, basePackageCalculator, javaParser, executionContext);
    }

    /**
     * This is a legacy way of retrieving applications build file.
     * This function does not generalise for situations where application is under a multi-module maven structure
     * Use {@link #getApplicationModules()} instead of getBuildFile()
     * If one would want to retrieve the root build file use:
     * {@link #getApplicationModules()} and then call to get root module using: {@link ApplicationModules#getRootModule()}
     * */
    @Deprecated(forRemoval = true)
    public BuildFile getBuildFile() {
        return search(new RootBuildFileFilter());
    }

    public <T> T search(ProjectResourceFinder<T> finder) {
        return finder.apply(getProjectResources());
    }

    public ProjectJavaSources getProjectJavaSources() {
        ProjectJavaSources projectJavaSources = new ProjectJavaSourcesImpl(projectResources, javaRefactoringFactory.createRefactoring());
        return projectJavaSources;
    }

    public ApplicationModules getApplicationModules() {
        return new ApplicationModules(getModules());
    }

    public void apply(Recipe recipe) {
        List<? extends SourceFile> rewriteSourceFiles = this.search(new OpenRewriteSourceFilesFinder());
        List<Result> results = recipe.run(rewriteSourceFiles, executionContext).getResults();
        resultMerger.mergeResults(this, results);
    }
}
