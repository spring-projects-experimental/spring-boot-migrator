/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.build.api;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationModulesTest {

    private static ApplicationModules sut;

    @Language("xml")
    private static final String PARENT_POM = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <groupId>org.example</groupId>
                <artifactId>parent</artifactId>
                <version>1.0-SNAPSHOT</version>
                <packaging>pom</packaging>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
                <modules>
                    <module>module1</module>
                    <module>module2</module>
                </modules>
            </project>
            """;

    @Language("xml")
    private static final String APPLICATION_POM = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <artifactId>module1</artifactId>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
                <dependencies>
                    <dependency>
                        <groupId>org.example</groupId>
                        <artifactId>module2</artifactId>
                        <version>${project.version}</version>
                    </dependency>
                </dependencies>
            </project>
            """;

    @Language("xml")
    private static final String COMPONENT_POM = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                <parent>
                    <groupId>org.example</groupId>
                    <artifactId>parent</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <relativePath>../pom.xml</relativePath>
                </parent>
                <artifactId>module2</artifactId>
                <properties>
                    <maven.compiler.source>17</maven.compiler.source>
                    <maven.compiler.target>17</maven.compiler.target>
                </properties>
            </project>
            """;

    @BeforeAll
    static void beforeAll() {
        sut = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(PARENT_POM)
                .withMavenBuildFileSource("module1/pom.xml", APPLICATION_POM)
                .withMavenBuildFileSource("module2/pom.xml", COMPONENT_POM)
                .build()
                .getApplicationModules();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /**
     * Creates a mock Module whose BuildFile stubs all fields consulted by
     * RootBuildFileSelector.score(): isRootBuildFile, getSourcePath,
     * getPackaging, and getDeclaredModules.
     *
     * Previously the helper only stubbed isRootBuildFile + getSourcePath,
     * which caused getPackaging/getDeclaredModules to return null and pushed
     * every module to score 3 (non-POM fallback). Tests passed but for the
     * wrong reason — the aggregator/plain-POM scoring paths were never reached.
     */
    private static Module moduleWithPom(String sourcePath,
                                        boolean isRoot,
                                        String packaging,
                                        List<String> declaredModules) {
        BuildFile buildFile = mock(BuildFile.class);
        when(buildFile.isRootBuildFile()).thenReturn(isRoot);
        when(buildFile.getSourcePath()).thenReturn(Path.of(sourcePath));
        when(buildFile.getPackaging()).thenReturn(packaging);
        when(buildFile.getDeclaredModules()).thenReturn(declaredModules);

        Module module = mock(Module.class);
        when(module.getBuildFile()).thenReturn(buildFile);
        return module;
    }

    /** Convenience overload: plain-POM module, not explicitly marked as root. */
    private static Module pomModule(String sourcePath, List<String> declaredModules) {
        return moduleWithPom(sourcePath, false, "pom", declaredModules);
    }

    /** Convenience overload: jar module, not explicitly marked as root. */
    private static Module jarModule(String sourcePath) {
        return moduleWithPom(sourcePath, false, "jar", List.of());
    }

    /**
     * Creates a mock Module for getTopmostApplicationModules / getComponentModules tests.
     *
     * getRequestedDependencies() is deliberately empty to avoid exercising the
     * coordinate-matching path inside noOtherPomDependsOn(). getCoordinates() is
     * stubbed anyway so the mock stays robust if a future change adds dependencies
     * to this helper.
     */
    private static Module moduleWithPackaging(String packaging) {
        BuildFile buildFile = mock(BuildFile.class);
        when(buildFile.getPackaging()).thenReturn(packaging);
        // Deliberately empty: avoids exercising dependency coordinate matching
        // in noOtherPomDependsOn(). If dependencies are ever added here,
        // getCoordinates() below ensures the mock won't throw unexpectedly.
        when(buildFile.getRequestedDependencies()).thenReturn(List.of());
        when(buildFile.getCoordinates()).thenReturn("org.example:dummy:1.0");
        when(buildFile.getParentPomDeclaration()).thenReturn(Optional.empty());

        Module module = mock(Module.class);
        when(module.getBuildFile()).thenReturn(buildFile);
        return module;
    }

    // ── integration tests (TestProjectContext) ────────────────────────────────

    @Test
    void shouldBeRecognizedAsMultiModuleProject() {
        assertThat(sut.isSingleModuleApplication()).isFalse();
        assertThat(sut.list()).hasSize(3);
    }

    @Test
    void shouldFindRootModule() {
        Module rootModule = sut.getRootModule();
        assertThat(rootModule.getModulePath()).isEqualTo(Path.of(""));
        assertThat(rootModule.getBuildFile().getCoordinates()).isEqualTo("org.example:parent:1.0-SNAPSHOT");
        assertThat(rootModule.getDeclaredModules()).hasSize(2);
        assertThat(rootModule.getDeclaredModules().get(0)).isEqualTo("org.example:module1:1.0-SNAPSHOT");
        assertThat(rootModule.getDeclaredModules().get(1)).isEqualTo("org.example:module2:1.0-SNAPSHOT");
    }

    @Test
    void shouldNotFindRootModuleForMissingRootBuildFile() {
        ApplicationModules applicationModules = new ApplicationModules(List.of());

        assertThatThrownBy(applicationModules::getRootModule)
                .isInstanceOf(RootBuildFileNotFoundException.class);
    }

    @Test
    void getModule() {
        Module parentModule = sut.findModule("org.example:parent:1.0-SNAPSHOT").get();
        assertThat(parentModule.getBuildFile().getCoordinates()).isEqualTo("org.example:parent:1.0-SNAPSHOT");

        Module module1 = sut.findModule("org.example:module1:1.0-SNAPSHOT").get();
        assertThat(module1.getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");

        Module module2 = sut.findModule("org.example:module2:1.0-SNAPSHOT").get();
        assertThat(module2.getBuildFile().getCoordinates()).isEqualTo("org.example:module2:1.0-SNAPSHOT");
    }

    @Test
    void applicationModule() {
        assertThat(sut.getTopmostApplicationModules()).hasSize(1);
        Module applicationModule = sut.getTopmostApplicationModules().get(0);
        assertThat(applicationModule.getModulePath()).isEqualTo(Path.of("module1"));
        assertThat(applicationModule.getBuildFile().getCoordinates()).isEqualTo("org.example:module1:1.0-SNAPSHOT");
    }

    @Test
    void componentModule() {
        assertThat(sut.getComponentModules()).hasSize(1);
        Module componentModule = sut.getComponentModules().get(0);
        assertThat(componentModule.getModulePath()).isEqualTo(Path.of("module2"));
        assertThat(componentModule.getBuildFile().getCoordinates()).isEqualTo("org.example:module2:1.0-SNAPSHOT");
    }

    // ── unit tests: getRootModule fallback logic (Mockito) ────────────────────

    @Test
    void getRootModule_fallback_choosesShallowerPom_whenNoneMarkedAsRoot() {
        // Both are plain POMs with no declared modules; depth decides.
        Module parent = pomModule("parent/pom.xml", List.of());
        Module child  = pomModule("parent/module/pom.xml", List.of());

        Module root = new ApplicationModules(List.of(parent, child)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("parent/pom.xml"));
    }

    @Test
    void getRootModule_explicitRoot_takesPriority_overShallowerPath() {
        // Explicit flag beats depth — deeper module wins when it is marked as root.
        Module shallower  = pomModule("parent/pom.xml", List.of());
        Module markedRoot = moduleWithPom("parent/module/pom.xml", true, "pom", List.of());

        Module root = new ApplicationModules(List.of(shallower, markedRoot)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("parent/module/pom.xml"));
    }

    @Test
    void getRootModule_aggregatorPom_beatsPlainPomAtSameDepth() {
        // repo/pom.xml and parent/pom.xml are at the same depth (2 name components).
        // The aggregator score (1) must beat the plain-POM score (2) — not depth.
        Module plainPom      = pomModule("repo/pom.xml",   List.of());
        Module aggregatorPom = pomModule("parent/pom.xml", List.of("module-a"));
        Module jar           = jarModule("module-a/pom.xml");

        Module root = new ApplicationModules(List.of(plainPom, aggregatorPom, jar)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("parent/pom.xml"));
    }

    @Test
    void getRootModule_aggregatorPom_beatsJarAtAnyDepth() {
        // An aggregator POM deeper than a jar module still wins on score alone.
        Module deepAggregator = pomModule("a/b/pom.xml", List.of("module-a"));
        Module shallowJar     = jarModule("app/pom.xml");

        Module root = new ApplicationModules(List.of(shallowJar, deepAggregator)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("a/b/pom.xml"));
    }

    @Test
    void getRootModule_depthBreaksTie_betweenTwoAggregators() {
        // Both are aggregator POMs; the shallower one should win.
        Module shallowAggregator = pomModule("parent/pom.xml",       List.of("child"));
        Module deepAggregator    = pomModule("parent/child/pom.xml", List.of("grandchild"));

        Module root = new ApplicationModules(List.of(deepAggregator, shallowAggregator)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("parent/pom.xml"));
    }

    @Test
    void getRootModule_unsupportedOperationOnDeclaredModules_treatedAsPlainPom() {
        // Simulates a non-Maven BuildFile that throws UnsupportedOperationException
        // from getDeclaredModules(). The selector must not propagate the exception
        // and must fall back to treating the file as a plain POM (score 2).
        BuildFile unsupportedBf = mock(BuildFile.class);
        when(unsupportedBf.isRootBuildFile()).thenReturn(false);
        when(unsupportedBf.getSourcePath()).thenReturn(Path.of("parent/pom.xml"));
        when(unsupportedBf.getPackaging()).thenReturn("pom");
        when(unsupportedBf.getDeclaredModules()).thenThrow(new UnsupportedOperationException("not supported"));

        Module unsupported = mock(Module.class);
        when(unsupported.getBuildFile()).thenReturn(unsupportedBf);

        // A jar build file scores 3; the plain-POM fallback (score 2) must win.
        Module deepJar = jarModule("parent/module/pom.xml");

        Module root = new ApplicationModules(List.of(unsupported, deepJar)).getRootModule();

        assertThat(root.getBuildFile().getSourcePath())
                .isEqualTo(Path.of("parent/pom.xml"));
    }

    // ── unit tests: getTopmostApplicationModules packaging filter ───

    @Test
    void getTopmostApplicationModules_ignoresPomPackaging_asApplicationModule() {
        // A module with pom packaging is never an application module.
        Module pomModule = moduleWithPackaging("pom");
        ApplicationModules am = new ApplicationModules(List.of(pomModule));

        assertThat(am.getTopmostApplicationModules()).isEmpty();
    }

    @Test
    void getTopmostApplicationModules_includesWarPackaging() {
        // war packaging must be treated the same as jar for application module detection.
        Module warModule = moduleWithPackaging("war");
        ApplicationModules am = new ApplicationModules(List.of(warModule));

        assertThat(am.getTopmostApplicationModules()).containsExactly(warModule);
    }
}
