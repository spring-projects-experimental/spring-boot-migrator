package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Boot_27_30_UpgradeReplaceJohnzonDependenciesTest {

    private static final String JHONZON_UNSUPPORTED_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.11";
    private static final String JHONZON_UPDATED_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.18-jakarta";
    private static final String JAVA_CLASS_WITH_JHONZON_IMPORT =  "package org.spring.boot.migration;"
                                                                + "import org.apache.johnzon.core.JsonStringImpl;"
                                                                + "public class BootReplaceJohnzonDependencyDummyClass{"
                                                                + "}";


    private static final String JAVA_CLASS_WITHOUT_JHONZON_IMPORT =   "package org.spring.boot.migration;"
                                                                    + "public class BootReplaceJohnzonDependencyDummyClass{"
                                                                    + "}";


    @Test
    public void givenProjectWithSelfManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithJohnzonDependency(JAVA_CLASS_WITH_JHONZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertTrue(projectContext
                    .getBuildFile()
                    .hasExactDeclaredDependency(Dependency.fromCoordinates(JHONZON_UPDATED_DEPENDENCY))
        );
    }


    @Test
    public void givenProjectWithSpringManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithoutJohnzonDependency(JAVA_CLASS_WITH_JHONZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertTrue(projectContext
                .getBuildFile()
                .hasExactDeclaredDependency(Dependency.fromCoordinates(JHONZON_UPDATED_DEPENDENCY))
        );
    }

    @Test
    public void givenProjectWithoutJohnzonImport_checkActionApplicability_expectFalse(){
        ProjectContext projectContext = getProjectContextWithoutJohnzonDependency(JAVA_CLASS_WITHOUT_JHONZON_IMPORT);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();

        assertFalse(upgradeReplaceJohnzonDependencies.isApplicable(projectContext));
    }

    private ProjectContext getProjectContextWithJohnzonDependency(String... javaSources){
        return TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(JHONZON_UNSUPPORTED_DEPENDENCY)
                .withJavaSources(javaSources)
                .build();
    }

    private ProjectContext getProjectContextWithoutJohnzonDependency(String... javaSources){
        return TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withJavaSources(javaSources)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .build();
    }
}
