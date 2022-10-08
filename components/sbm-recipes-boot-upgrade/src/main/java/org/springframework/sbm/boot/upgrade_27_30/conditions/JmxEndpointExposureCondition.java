package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.springframework.sbm.boot.upgrade_27_30.filter.JmxEndpointExposureFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class JmxEndpointExposureCondition implements Condition {
    @Override
    public String getDescription() {
        return "Check if 'management.endpoints.jmx.exposure.include' is declared.";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.search(new JmxEndpointExposureFinder()).isEmpty();
    }
}
