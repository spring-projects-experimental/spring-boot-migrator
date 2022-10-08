package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.properties.finder.SpringBootDefaultPropertiesFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.util.Optional;

public class Boot_27_30_JmxEndpointExposureAction extends AbstractAction {

    public static final String JMX_ENDPOINT = "management.endpoints.jmx.exposure.include";

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
                    if (!p.getProperty(JMX_ENDPOINT).isPresent()) {
                        p.setProperty(JMX_ENDPOINT, "*");
                    }
                });
    }
}
