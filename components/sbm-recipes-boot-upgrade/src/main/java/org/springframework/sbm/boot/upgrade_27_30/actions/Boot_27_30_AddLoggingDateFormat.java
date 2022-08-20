package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.properties.finder.SpringBootDefaultPropertiesFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.util.Optional;

public class Boot_27_30_AddLoggingDateFormat extends AbstractAction {

    public static final String LOGGING_PATTERN_DATEFORMAT = "logging.pattern.dateformat";

    @Override
    public void apply(ProjectContext context) {
        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();

        context.getApplicationModules()
                .getTopmostApplicationModules()
                .stream()
                .map(m -> m.searchMainResources(springBootDefaultPropertiesFinder))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(p -> {
                    if(!p.getProperty(LOGGING_PATTERN_DATEFORMAT).isPresent()) {
                        p.setProperty(LOGGING_PATTERN_DATEFORMAT, "yyyy-MM-dd HH:mm:ss.SSS");
                    }
                });
    }
}
