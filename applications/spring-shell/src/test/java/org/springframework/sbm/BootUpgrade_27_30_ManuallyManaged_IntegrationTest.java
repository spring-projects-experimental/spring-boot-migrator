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

package org.springframework.sbm;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BootUpgrade_27_30_ManuallyManaged_IntegrationTest extends IntegrationTestBaseClass {
    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30-manual-managed";
    }

    @Test
    @Tag("integration")
    void migrateManuallyManagedApplication() {
        intializeTestProject();

        scanProject();

        applyRecipe("boot-2.7-3.0-dependency-version-update");

        buildProject();

        verifyManagedDependency("spring-boot-starter-web", "3.0.0-M3");
        verifyManagedDependency("spring-boot-starter-test", "3.0.0-M3");
        verifyManagedDependency("metrics-annotation", "4.2.9");
    }

    private void verifyManagedDependency(String artifactId, String version) {
        Xml.Document mavenAsXMLDocument = getRootBuildFile();

        List<Xml.Tag> dependencies = mavenAsXMLDocument
                .getRoot()
                .getChildren("dependencyManagement")
                .get(0)
                .getChildren("dependencies")
                .get(0)
                .getChildren("dependency");

        for (Xml.Tag dependency : dependencies) {
            if (dependency.getChildValue("artifactId").isPresent() && dependency.getChildValue("artifactId").get().equals(artifactId)) {
                assertThat(dependency.getChildValue("version").isPresent()).isTrue();
                assertThat(dependency.getChildValue("version").get()).isEqualTo(version);
            }
        }
    }
    @NotNull
    private Xml.Document getRootBuildFile() {
        return parsePom(loadFile(Path.of("pom.xml")));
    }

    @NotNull
    private Xml.Document parsePom(String pomContent) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(pomContent).get(0);
    }

    private void buildProject() {
        executeMavenGoals(getTestDir(), "clean", "verify");
    }
}
