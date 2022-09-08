package org.springframework.sbm.boot.common.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class HasSpringBootProject implements Condition {
    private HasSpringBootStarterParent parentCondition;
    private HasSpringBootDependencyImport importCondition;
    private HasSpringBootDependencyManuallyManaged manualManagedCondition;

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
