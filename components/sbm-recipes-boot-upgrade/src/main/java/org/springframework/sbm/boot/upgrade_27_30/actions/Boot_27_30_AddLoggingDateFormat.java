package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.java.filter.AnnotatedJavaClassFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import java.util.List;

public class Boot_27_30_AddLoggingDateFormat extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(context.getProjectResources());

        if(springBootApplicationProperties.size() > 0)
            updatePropertyFiles(springBootApplicationProperties);
        else
            addPropertyFile(context);
    }

    private void addPropertyFile(ProjectContext context) {
        AnnotatedJavaClassFinder annotatedJavaClassFinder = new AnnotatedJavaClassFinder("SpringBootApplication");

        context.getApplicationModules()
                .stream()
                .filter(a -> annotatedJavaClassFinder.apply(a.getMainJavaSourceSet()).size() > 0)
                .map(ApplicationModule::getModulePath)
                .forEach(p -> context.getProjectResources().add(SpringBootApplicationProperties.newApplicationProperties(context.getProjectRootDirectory().toAbsolutePath(),p)));

    }

    private void updatePropertyFiles(List<SpringBootApplicationProperties>  applicationPropertyFiles) {
        applicationPropertyFiles
                .stream()
                .parallel()
                .forEach(x -> x.setProperty("logging.pattern.dateformat", "yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
