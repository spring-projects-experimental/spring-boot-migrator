package org.springframework.sbm.boot.common.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class IsSpringBootProject implements Condition {
    private HasDecalredSpringBootStarterParent parentCondition;
    private HasSpringBootDependencyImport importCondition;
    private HasSpringBootDependencyManuallyManaged manualManagedCondition;

    public void setVersionPattern(String versionPattern) {
        parentCondition = new HasDecalredSpringBootStarterParent();
        parentCondition.setVersionPattern(versionPattern);

        importCondition = new HasSpringBootDependencyImport();
        importCondition.setVersionPattern(versionPattern);

        manualManagedCondition = new HasSpringBootDependencyManuallyManaged();
        manualManagedCondition.setVersionPattern(versionPattern);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        return parentCondition.evaluate(context) ||
                importCondition.evaluate(context) ||
                manualManagedCondition.evaluate(context);
    }
}
