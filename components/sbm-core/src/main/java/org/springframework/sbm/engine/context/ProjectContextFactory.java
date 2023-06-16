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
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.filter.BuildFileProjectResourceFilter;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.resource.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectContextFactory {

    private final ProjectResourceWrapperRegistry resourceWrapperRegistry;
    private final ProjectResourceSetHolder projectResourceSetHolder;
    private final JavaRefactoringFactory javaRefactoringFactory;
    private final BasePackageCalculator basePackageCalculator;
    private final JavaParser javaParser;
    private final ExecutionContext executionContext;

    @NotNull
    public ProjectContext createProjectContext(Path projectDir, ProjectResourceSet projectResourceSet) {
        projectResourceSetHolder.setProjectResourceSet(projectResourceSet);
        applyProjectResourceWrappers(projectResourceSet);
        List<BuildFile> buildFiles = new BuildFileProjectResourceFilter().apply(projectResourceSet);
        ClasspathRegistry.initializeFromBuildFiles(buildFiles);
        ProjectContext projectContext = new ProjectContext(javaRefactoringFactory, projectDir, projectResourceSet, basePackageCalculator, javaParser, executionContext);
        return projectContext;
    }

    private void applyProjectResourceWrappers(ProjectResourceSet projectResourceSet) {
        projectResourceSet.list().forEach(pr -> {
            Optional<ProjectResourceWrapper> wrapper = resourceWrapperRegistry.findWrapper(pr);
            if (wrapper.isPresent()) {
                projectResourceSet.replace(pr.getAbsolutePath(), wrapper.get().wrapRewriteSourceFileHolder(pr));
            }
        });
    }

}
