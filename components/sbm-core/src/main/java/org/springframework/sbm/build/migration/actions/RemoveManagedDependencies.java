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
package org.springframework.sbm.build.migration.actions;

import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.SpringManagedDependencies;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.scopeplayground.ExecutionScope;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.openrewrite.maven.tree.Scope.Compile;

/**
 * The action removes the dependencies directly managed by Spring from the project dependencies
 * Add this action at the end of recipe so that any spring artifact inclusions as part of the
 * other actions are also included while removing the dependencies.
 */
public class RemoveManagedDependencies extends AbstractAction {

    @Autowired
    private ExecutionContext executionContext;

    @Override
    public void apply(ProjectContext context) {
        //FIXME handle multi-module projects
        final List<Dependency> springManagedDependencies = context.getBuildFile()
                .getDeclaredDependencies(Compile)
                .stream()
                .filter(this::isSpringFrameworkDependency)
                .map(d -> SpringManagedDependencies.by(d.getGroupId(),d.getArtifactId(),d.getVersion(), executionContext))
                .flatMap(SpringManagedDependencies::stream)
                .distinct()
                .collect(Collectors.toList());

        Predicate<Dependency> isAlreadyManagedBySpring = d -> springManagedDependencies
                                                                    .stream()
                                                                    .filter(d::equals)
                                                                    .anyMatch(s -> s.isRecentThen(d));

        final List<Dependency> dependenciesToBeRemoved = context.getBuildFile()
                                                             .getDeclaredDependencies(Compile)
                                                             .stream()
                                                             .filter(isAlreadyManagedBySpring)
                                                             .collect(Collectors.toList());

        RemoveDependencies removeDependenciesAction = new RemoveDependencies();
        removeDependenciesAction.setDependencies(dependenciesToBeRemoved);
        removeDependenciesAction.apply(context);
    }

    private boolean isSpringFrameworkDependency(Dependency dependency){
        return dependency.getGroupId().startsWith("org.springframework");
    }
}
