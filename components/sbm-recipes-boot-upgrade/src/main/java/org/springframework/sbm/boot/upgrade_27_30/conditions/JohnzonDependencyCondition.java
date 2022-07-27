package org.springframework.sbm.boot.upgrade_27_30.filters;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;

/**
 * This filter finds out if any of the resource uses Johnzon
 */
public class JohnzonDependencyCondition implements Condition {

    @Override
    public String getDescription() {
        return "Checks if any of the class in the project imports Apache Jhonzon classes";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectJavaSources()
                .asStream()
                .anyMatch(js -> js.hasImportStartingWith("org.apache.johnzon.core"));
    }
}
