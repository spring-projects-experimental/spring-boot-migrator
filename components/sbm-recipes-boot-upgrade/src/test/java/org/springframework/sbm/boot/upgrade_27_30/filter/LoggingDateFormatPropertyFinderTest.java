package org.springframework.sbm.boot.upgrade_27_30.filter;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.filter.LoggingDateFormatPropertyFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoggingDateFormatPropertyFinderTest {

    private static final String APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT = "foo=bar\n" +
                                                                              "migrate=true\n" +
                                                                              "logging.pattern.dateformat=xyz\n";

    @Test
    public void givenProjectWithLogDateFormatCustomization_findResources_returnResource(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT)
                .build();

        LoggingDateFormatPropertyFinder loggingDateFormatPropertyFinder = new LoggingDateFormatPropertyFinder();
        List<? extends PropertiesSource> propertiesSources = loggingDateFormatPropertyFinder.apply(projectContext.getProjectResources());

        assertThat(propertiesSources.size()).isEqualTo(1);
        assertThat(propertiesSources.get(0).getProperty("logging.pattern.dateformat").isPresent()).isTrue();
    }

    @Test
    public void givenMultiModuleProjectWithLogDateFormatCustomization_findResources_returnResource(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .addProjectResource(Path.of("module1","src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT)
                .addProjectResource(Path.of("module2","src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT)
                .build();

        LoggingDateFormatPropertyFinder loggingDateFormatPropertyFinder = new LoggingDateFormatPropertyFinder();
        List<? extends PropertiesSource> propertiesSources = loggingDateFormatPropertyFinder.apply(projectContext.getProjectResources());

        assertThat(propertiesSources.size()).isEqualTo(2);
        assertThat(propertiesSources.get(0).getProperty("logging.pattern.dateformat").isPresent()).isTrue();
        assertThat(propertiesSources.get(1).getProperty("logging.pattern.dateformat").isPresent()).isTrue();
    }
}
