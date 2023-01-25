package org.springframework.sbm.build.migration.actions;

import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.SpringManagedDependencies;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

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

    @Override
    public void apply(ProjectContext context) {
        //FIXME handle multi-module projects
        final List<Dependency> springManagedDependencies = context.getBuildFile()
                .getDeclaredDependencies(Compile)
                .stream()
                .filter(this::isSpringFrameworkDependency)
                .map(d -> SpringManagedDependencies.by(d.getGroupId(),d.getArtifactId(),d.getVersion()))
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
