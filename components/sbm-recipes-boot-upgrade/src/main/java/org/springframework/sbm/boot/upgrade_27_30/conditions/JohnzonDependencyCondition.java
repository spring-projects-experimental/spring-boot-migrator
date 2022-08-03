package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

/**
 * This filter finds out if any of the resource uses Johnzon
 */
public class JohnzonDependencyCondition implements Condition {

    private static final String JOHNZON_DEPENDENCY_PATTERN = "org\\.apache\\.johnzon\\:johnzon-core\\:.*";

    @Override
    public String getDescription() {
        return "Checks if the project has declared dependency on Johnzon library";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getApplicationModules()
                .stream()
                .map(ApplicationModule::getBuildFile)
                .anyMatch(b -> b.hasDeclaredDependencyMatchingRegex(JOHNZON_DEPENDENCY_PATTERN));
    }
}
