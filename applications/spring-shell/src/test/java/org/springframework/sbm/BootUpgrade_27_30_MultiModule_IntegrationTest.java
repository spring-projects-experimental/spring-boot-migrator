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
import org.openrewrite.maven.tree.Dependency;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BootUpgrade_27_30_MultiModule_IntegrationTest  extends IntegrationTestBaseClass {
    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30-multi-module";
    }

    @Test
    @Tag("integration")
    void migrateMultiModuleApplication() {
        intializeTestProject();

        scanProject();

        applyRecipe("boot-2.7-3.0-dependency-version-update");

        buildProject();

        verifyParentPomVersion();
        verifyEhCacheVersionIsUpgraded();
    }

    private void verifyEhCacheVersionIsUpgraded() {
        Optional<Dependency> ehcacheResult = getDependencyByArtifactId("ehcache", "spring-app/");

        assertThat(ehcacheResult).isPresent();

        Dependency ehcacheDependency = ehcacheResult.get();

        assertThat(ehcacheDependency.getArtifactId()).isEqualTo("ehcache");
        assertThat(ehcacheDependency.getGav().getGroupId()).isEqualTo("org.ehcache");
        assertThat(ehcacheDependency.getGav().getVersion()).isNull();
        assertThat(ehcacheDependency.getClassifier()).isEqualTo("jakarta");
    }

    private Optional<Dependency> getDependencyByArtifactId(String artifactId, String module) {
        Xml.Document mavenAsXMLDocument = getBuildFileByModule(module);
        List<Dependency> dependencies = getDependencies(mavenAsXMLDocument);
        return dependencies
                .stream()
                .filter(dependency -> dependency.getArtifactId().equals(artifactId))
                .findFirst();
    }

    private List<Dependency> getDependencies(Xml.Document mavenAsXMLDocument) {
        return mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getRequestedDependencies();
    }

    private void verifyParentPomVersion() {
        Xml.Document mavenAsXMLDocument = getRootBuildFile();

        Xml.Tag parentTag = mavenAsXMLDocument
                .getRoot()
                .getChildren("parent").get(0);

        String version = parentTag.getChildValue("version").get();

        String groupId = parentTag.getChildValue("groupId").get();
        String artifactId = parentTag.getChildValue("artifactId").get();

        assertThat(version).isEqualTo("3.0.0-M3");
        assertThat(groupId).isEqualTo("org.springframework.boot");
        assertThat(artifactId).isEqualTo("spring-boot-starter-parent");
    }


    @NotNull
    private Xml.Document getBuildFileByModule(String app) {

        return parsePom(loadFile(Path.of(app + "pom.xml")));
    }


    @NotNull
    private Xml.Document getRootBuildFile() {

        return getBuildFileByModule("");
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
