package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Boot_27_30_UpgradeReplaceJohnzonDependenciesTest {

    private static final String JOHNZON_UNSUPPORTED_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.11";
    private static final String JAVA_CLASS_WITH_JOHNZON_IMPORT =  "package org.spring.boot.migration;"
                                                                + "import org.apache.johnzon.core.JsonStringImpl;"
                                                                + "public class BootReplaceJohnzonDependencyDummyClass{"
                                                                + "}";

    private static final String EXPECTED_POM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <groupId>com.example</groupId>\n" +
            "    <artifactId>dummy-root</artifactId>\n" +
            "    <version>0.1.0-SNAPSHOT</version>\n" +
            "    <packaging>jar</packaging>\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.apache.johnzon</groupId>\n" +
            "            <artifactId>johnzon-core</artifactId>\n" +
            "            <version>1.2.18-jakarta</version>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "\n" +
            "</project>" +
            "\n";

    private static final String JAVA_CLASS_WITHOUT_JOHNZON_IMPORT =   "package org.spring.boot.migration;"
                                                                    + "public class BootReplaceJohnzonDependencyDummyClass{"
                                                                    + "}";


    @Test
    public void givenProjectWithSelfManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithJohnzonDependency(JAVA_CLASS_WITH_JOHNZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile().print()).isEqualToIgnoringNewLines(EXPECTED_POM);
    }


    @Test
    public void givenProjectWithSpringManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithoutJohnzonDependency(JAVA_CLASS_WITH_JOHNZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile().print()).isEqualToIgnoringNewLines(EXPECTED_POM);
    }

    @Test
    public void givenProjectWithoutJohnzonImport_checkActionApplicability_expectFalse(){
        ProjectContext projectContext = getProjectContextWithoutJohnzonDependency(JAVA_CLASS_WITHOUT_JOHNZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();

        assertFalse(upgradeReplaceJohnzonDependencies.isApplicable(projectContext));
    }

    private ProjectContext getProjectContextWithJohnzonDependency(String... javaSources){
        return TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(JOHNZON_UNSUPPORTED_DEPENDENCY)
                .withJavaSources(javaSources)
                .build();
    }

    private ProjectContext getProjectContextWithoutJohnzonDependency(String... javaSources){
        return TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withJavaSources(javaSources)
                .build();
    }
}
