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

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.Dependency;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedManagedDependency;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        Xml.Document rootBuildFile = getRootBuildFile();
        verifyManagedDependency(rootBuildFile, "spring-boot-starter-test", "3.0.0");
        verifyManagedDependency(rootBuildFile, "metrics-annotation", "4.2.13");

        Xml.Document applicationBuildFile = getApplicationBuildFile();
        verifyProperty(applicationBuildFile, "spring-boot-starter-web.version", "3.0.0");
        verifyDependencyWithClassifier(applicationBuildFile, "ehcache", "3.10.8", "jakarta");

        verifyConstructorBindingRemoval();
    }

    private void verifyManagedDependency(Xml.Document mavenAsXMLDocument, String artifactId, String version) {
        Optional<ResolvedManagedDependency> managedDependency = getManagedDependencyByArtifactId(mavenAsXMLDocument, artifactId);
        assertThat(managedDependency).isPresent();
        assertThat(managedDependency.get().getVersion()).isEqualTo(version);
    }

    private void verifyDependencyWithClassifier(Xml.Document mavenAsXMLDocument, String artifactId, String version, String classifier) {
        Optional<Dependency> dependency = getDependencyByArtifactId(mavenAsXMLDocument, artifactId);
        assertThat(dependency).isPresent();
        assertThat(dependency.get().getVersion()).isEqualTo(version);
        if (classifier != null) {
            assertThat(dependency.get().getClassifier()).isEqualTo(classifier);
        }
    }

    @NotNull
    private Optional<Dependency> getDependencyByArtifactId(Xml.Document mavenAsXMLDocument, String artifactId) {
        List<Dependency> dependencies = getDependencies(mavenAsXMLDocument);
        return dependencies
                .stream()
                .filter(dependency -> dependency.getArtifactId().equals(artifactId))
                .findAny();
    }

    @NotNull
    private List<Dependency> getDependencies(Xml.Document mavenAsXMLDocument) {
        return mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getRequestedDependencies();
    }

    private void verifyProperty(Xml.Document mavenAsXMLDocument, String name, String value) {
        Map<String, String> props = getProperties(mavenAsXMLDocument);
        assertThat(props.containsKey(name)).isTrue();
        assertThat(props.get(name)).isEqualTo(value);
    }

    @NotNull
    private Map<String, String> getProperties(Xml.Document mavenAsXMLDocument) {
        return mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getProperties();
    }

    @NotNull
    Optional<ResolvedManagedDependency> getManagedDependencyByArtifactId(Xml.Document mavenAsXMLDocument, String artifactId) {
        return getManagedDependencies(mavenAsXMLDocument)
                .stream()
                .filter(md -> md.getArtifactId().equals(artifactId))
                .findAny();
    }

    @NotNull
    private List<ResolvedManagedDependency> getManagedDependencies(Xml.Document mavenAsXMLDocument) {
        return mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getDependencyManagement();
    }

    @NotNull
    private Xml.Document getRootBuildFile() {
        return parsePom(loadFile(Path.of("pom.xml")));
    }

    @NotNull
    private Xml.Document getApplicationBuildFile() {
        return parsePom(loadFile(Path.of("spring-app/pom.xml")), loadFile(Path.of("pom.xml")));
    }

    @NotNull
    private Xml.Document parsePom(@Language("xml") String... pomContents) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(pomContents).get(0);
    }

    private void verifyConstructorBindingRemoval() {
        String constructorBindingConfigClass = loadJavaFileFromSubmodule("spring-app/", "org.springboot.example.upgrade", "ConstructorBindingConfig");
        assertThat(constructorBindingConfigClass).isEqualTo("package org.springboot.example.upgrade;\n" +
                "\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "\n" +
                "@ConfigurationProperties(prefix = \"mail\")\n" +
                "public class ConstructorBindingConfig {\n" +
                "    private String hostName;\n" +
                "\n" +
                "    public ConstructorBindingConfig(String hostName) {\n" +
                "        this.hostName = hostName;\n" +
                "    }\n" +
                "}" +
                "\n");
    }

    private void buildProject() {
        executeMavenGoals(getTestDir(), "clean", "verify");
    }
}
