package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.springframework.sbm.boot.upgrade_27_30.filter.LoggingDateFormatPropertyFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

public class LoggingDateFormatCondition implements Condition {

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.search(new LoggingDateFormatPropertyFinder()).isEmpty();
    }
}
