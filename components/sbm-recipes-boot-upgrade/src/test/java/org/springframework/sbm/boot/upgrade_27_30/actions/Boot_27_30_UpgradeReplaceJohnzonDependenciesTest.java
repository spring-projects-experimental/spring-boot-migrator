/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_27_30_UpgradeReplaceJohnzonDependenciesTest {

    private static final String SELF_MANAGED_JOHNZON_UNSUPPORTED_DEPENDENCY = "org.apache.johnzon:johnzon-core:1.2.11";


    private static final String SPRING_MANAGED_DEPENDENCY_POM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <parent>\n" +
            "       <groupId>org.springframework.boot</groupId>\n" +
            "       <artifactId>spring-boot-starter-parent</artifactId>\n" +
            "       <version>2.7.1</version>\n" +
            "       <relativePath/>\n" +
            "     </parent>\n" +
            "    <groupId>com.example</groupId>\n" +
            "    <artifactId>dummy-root</artifactId>\n" +
            "    <version>0.1.0-SNAPSHOT</version>\n" +
            "    <packaging>pom</packaging>\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.apache.johnzon</groupId>\n" +
            "            <artifactId>johnzon-core</artifactId>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "\n" +
            "</project>" +
            "\n";


    private static final String SPRING_MANAGED_DEPENDENCY_EXPECTED_POM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <parent>\n" +
            "        <groupId>org.springframework.boot</groupId>\n" +
            "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
            "        <version>2.7.1</version>\n" +
            "        <relativePath/>\n" +
            "    </parent>\n" +
            "    <groupId>com.example</groupId>\n" +
            "    <artifactId>dummy-root</artifactId>\n" +
            "    <version>0.1.0-SNAPSHOT</version>\n" +
            "    <packaging>pom</packaging>\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.apache.johnzon</groupId>\n" +
            "            <artifactId>johnzon-core</artifactId>\n" +
            "            <version>1.2.18</version>\n" +
            "            <classifier>jakarta</classifier>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "\n" +
            "</project>" +
            "\n";


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
            "            <version>1.2.18</version>\n" +
            "            <classifier>jakarta</classifier>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "\n" +
            "</project>" +
            "\n";



    @Test
    public void givenProjectWithSelfManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithJohnzonDependency(SELF_MANAGED_JOHNZON_UNSUPPORTED_DEPENDENCY);
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile().print()).isEqualToIgnoringNewLines(EXPECTED_POM);
    }


    @Test
    public void givenProjectWithSpringManagedJohnzonDependency_migrate_expectJohnzonDependencyUpdated(){
        ProjectContext projectContext = getProjectContextWithSpringManagedJohnzonDependency();
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();
        upgradeReplaceJohnzonDependencies.apply(projectContext);

        assertThat(projectContext.getBuildFile().print()).isEqualToIgnoringNewLines(SPRING_MANAGED_DEPENDENCY_EXPECTED_POM);
    }

    @Test
    public void givenProjectWithoutJohnzonDependency_checkActionApplicability_expectFalse(){
        ProjectContext projectContext = getProjectContextWithoutJohnzonDependency();
        Boot_27_30_UpgradeReplaceJohnzonDependencies upgradeReplaceJohnzonDependencies = new Boot_27_30_UpgradeReplaceJohnzonDependencies();

        assertThat(upgradeReplaceJohnzonDependencies.isApplicable(projectContext)).isFalse();
    }

    private ProjectContext getProjectContextWithJohnzonDependency(String dependencyCoordinates){
        return TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(dependencyCoordinates)
                .build();
    }

    private ProjectContext getProjectContextWithoutJohnzonDependency(){
        return TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .build();
    }

    private ProjectContext getProjectContextWithSpringManagedJohnzonDependency(){
        return TestProjectContext.buildProjectContext()
                .withProjectResource(Path.of("pom.xml"), SPRING_MANAGED_DEPENDENCY_POM)
                .build();
    }
}
