package org.springframework.sbm.boot.upgrade_27_30.filter;

import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.upgrade_27_30.filter.LoggingDateFormatPropertyFinder;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.*;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoggingDateFormatPropertyFinderTest {

    private static final String APPLICATION_PROPERTIES_WITH_LOG_DATE_FORMAT = "foo=bar\n" +
                                                                              "migrate=true\n" +
                                                                              "logging.pattern.dateformat=xyz\n";

    private static final String MULTI_MODULE_POM_XML = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "\n" +
            "    <groupId>org.springframework.sbm</groupId>\n" +
            "    <artifactId>spring-boot-migrator</artifactId>\n" +
            "    <version>0.11.2-SNAPSHOT</version>\n" +
            "    <packaging>pom</packaging>\n" +
            "    <modules>\n" +
            "        <module>module1</module>\n" +
            "        <module>module2</module>\n" +
            "    </modules>\n" +
            "</project>";

    private static final String SUB_MODULE_POM_XML = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <parent>\n" +
            "        <artifactId>spring-boot-migrator</artifactId>\n" +
            "        <groupId>org.springframework.sbm</groupId>\n" +
            "        <version>0.11.2-SNAPSHOT</version>\n" +
            "        <relativePath>../../pom.xml</relativePath>\n" +
            "    </parent>\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "\n" +
            "    <artifactId>{{module}}</artifactId>\n" +
            "</project>";

    @Test
    public void givenProjectWithLogDateFormatCustomization_findResources_returnResource(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
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
                .withMavenRootBuildFileSource(MULTI_MODULE_POM_XML)
                .addProjectResource(Path.of("module1","pom.xml"),SUB_MODULE_POM_XML.replace("{{module}}", "module1"))
                .addProjectResource(Path.of("module2","pom.xml"),SUB_MODULE_POM_XML.replace("{{module}}", "module2"))
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
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
