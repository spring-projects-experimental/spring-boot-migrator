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

import org.junit.jupiter.api.Test;
import org.openrewrite.*;
import org.openrewrite.marker.Markers;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class RootBuildFileFilterTest {

    /**
     * Minimal BuildFile stub for filter tests.
     * Only the parts relevant to root selection are configurable.
     */
    static class PomStub extends RewriteSourceFileHolder<SourceFile> implements BuildFile {

        private final boolean isRoot;
        private final String packaging;
        private final List<String> declaredModules;
        private final boolean declaredModulesUnsupported;

        PomStub(String sourcePath,
                boolean isRoot,
                String packaging,
                List<String> declaredModules) {
            this(sourcePath, isRoot, packaging, declaredModules, false);
        }

        PomStub(String sourcePath,
                boolean isRoot,
                String packaging,
                List<String> declaredModules,
                boolean declaredModulesUnsupported) {
            super(Path.of(".").toAbsolutePath(), stubSourceFile(Path.of(sourcePath)));
            this.isRoot = isRoot;
            this.packaging = packaging;
            this.declaredModules = declaredModules;
            this.declaredModulesUnsupported = declaredModulesUnsupported;
        }

        private static SourceFile stubSourceFile(Path sourcePath) {
            return new SourceFile() {
                @Override public Path getSourcePath() { return sourcePath; }
                @Override public <T extends SourceFile> T withSourcePath(Path p) { return null; }
                @Override public UUID getId() { return UUID.randomUUID(); }
                @Override public Markers getMarkers() { return Markers.EMPTY; }
                @Override public <T extends Tree> T withId(UUID id) { return null; }
                @Override public <P> boolean isAcceptable(TreeVisitor<?, P> v, P p) { return false; }
                @Override public SourceFile withMarkers(Markers m) { return this; }
                @Override public Charset getCharset() { return null; }
                @Override public <T extends SourceFile> T withCharset(Charset c) { return null; }
                @Override public boolean isCharsetBomMarked() { return false; }
                @Override public <T extends SourceFile> T withCharsetBomMarked(boolean b) { return null; }
                @Override public Checksum getChecksum() { return null; }
                @Override public <T extends SourceFile> T withChecksum(Checksum c) { return null; }
                @Override public FileAttributes getFileAttributes() { return null; }
                @Override public <T extends SourceFile> T withFileAttributes(FileAttributes f) { return null; }
            };
        }

        @Override public boolean isRootBuildFile() { return isRoot; }

        @Override public String getPackaging() { return packaging; }

        @Override
        public List<String> getDeclaredModules() {
            if (declaredModulesUnsupported) {
                throw new UnsupportedOperationException("declaredModules not supported");
            }
            return declaredModules == null ? List.of() : declaredModules;
        }

        // ── BuildFile contract: no-ops, not relevant for this test ──────────
        @Override public List<Dependency> getDeclaredDependencies(org.openrewrite.maven.tree.Scope... s) { return List.of(); }
        @Override public List<Dependency> getRequestedDependencies() { return List.of(); }
        @Override public Set<Dependency> getEffectiveDependencies(org.openrewrite.maven.tree.Scope s) { return Set.of(); }
        @Override public Set<Dependency> getEffectiveDependencies() { return Set.of(); }
        @Override public boolean hasDeclaredDependencyMatchingRegex(String... p) { return false; }
        @Override public boolean hasEffectiveDependencyMatchingRegex(String... p) { return false; }
        @Override public boolean hasExactDeclaredDependency(Dependency d) { return false; }
        @Override public void addDependency(Dependency d) {}
        @Override public void addDependencies(List<Dependency> d) {}
        @Override public void removeDependencies(List<Dependency> d) {}
        @Override public void removeDependenciesMatchingRegex(String... r) {}
        @Override public void removeDependenciesInner(List<Dependency> d) {}
        @Override public List<Dependency> getEffectiveDependencyManagement() { return List.of(); }
        @Override public List<Dependency> getRequestedDependencyManagement() { return List.of(); }
        @Override public List<Dependency> getRequestedManagedDependencies() { return List.of(); }
        @Override public void addToDependencyManagement(Dependency d) {}
        @Override public void addToDependencyManagementInner(Dependency d) {}
        @Override public List<Path> getResolvedDependenciesPaths() { return List.of(); }
        @Override public boolean hasPlugin(Plugin p) { return false; }
        @Override public void addPlugin(Plugin p) {}
        @Override public List<Path> getClasspath() { return List.of(); }
        @Override public List<Path> getSourceFolders() { return List.of(); }
        @Override public List<Path> getTestSourceFolders() { return List.of(); }
        @Override public List<Path> getResourceFolders() { return List.of(); }
        @Override public List<Path> getTestResourceFolders() { return List.of(); }
        @Override public Path getTestResourceFolder() { return null; }
        @Override public Path getMainResourceFolder() { return null; }
        @Override public void setProperty(String k, String v) {}
        @Override public String getProperty(String k) { return ""; }
        @Override public void deleteProperty(String k) {}
        @Override public void setPackaging(String p) {}
        @Override public List<Plugin> getPlugins() { return List.of(); }
        @Override public void removePluginsMatchingRegex(String... r) {}
        @Override public void removePlugins(String... c) {}
        @Override public String getGroupId() { return ""; }
        @Override public String getArtifactId() { return ""; }
        @Override public String getVersion() { return ""; }
        @Override public String getCoordinates() { return ""; }
        @Override public boolean hasParent() { return false; }
        @Override public void upgradeParentVersion(String v) {}
        @Override public Optional<ParentDeclaration> getParentPomDeclaration() { return Optional.empty(); }
        @Override public Optional<String> getName() { return Optional.empty(); }
        @Override public void excludeDependencies(List<Dependency> d) {}
        @Override public void addRepository(RepositoryDefinition r) {}
        @Override public void addPluginRepository(RepositoryDefinition r) {}
        @Override public List<RepositoryDefinition> getRepositories() { return List.of(); }
        @Override public List<RepositoryDefinition> getPluginRepositories() { return List.of(); }
        @Override public Optional<Plugin> findPlugin(String g, String a) { return Optional.empty(); }
    }

    @Test
    void fallback_choosesShallowestPom_whenNoneMarkedAsRoot() {
        ProjectResourceSet prs = new ProjectResourceSet(List.of(
                new PomStub("parent/pom.xml",        false, "pom", List.of()),
                new PomStub("parent/module/pom.xml", false, "pom", List.of())
        ));

        assertThat(new RootBuildFileFilter().apply(prs).getSourcePath())
                .isEqualTo(Path.of("parent/pom.xml"));
    }

    @Test
    void explicitRoot_takesPriority_overShallowerPath() {
        ProjectResourceSet prs = new ProjectResourceSet(List.of(
                new PomStub("parent/pom.xml",        false, "pom", List.of()),
                new PomStub("parent/module/pom.xml", true,  "pom", List.of())
        ));

        assertThat(new RootBuildFileFilter().apply(prs).getSourcePath())
                .isEqualTo(Path.of("parent/module/pom.xml"));
    }

    @Test
    void aggregatorPom_wins_over_plainPom_whenDepthIsEqual() {
        // Both candidates have equal depth (3); scoring must decide — aggregator (1) > plain POM (2).
        ProjectResourceSet prs = new ProjectResourceSet(List.of(
                new PomStub("parent/a/pom.xml", false, "pom", List.of()),           // plain POM,      depth 3
                new PomStub("parent/b/pom.xml", false, "pom", List.of("module1"))   // aggregator POM, depth 3
        ));

        assertThat(new RootBuildFileFilter().apply(prs).getSourcePath())
                .isEqualTo(Path.of("parent/b/pom.xml"));
    }

    @Test
    void pom_wins_over_jar_even_ifJarIsShallower() {
        // JAR is genuinely shallower (depth 2) than the POM candidate (depth 3),
        // but semantic score takes priority over path depth: POM (2) beats JAR (3).
        ProjectResourceSet prs = new ProjectResourceSet(List.of(
                new PomStub("module/pom.xml",   false, "jar", List.of()),   // depth 2 — shallower but non-POM
                new PomStub("parent/a/pom.xml", false, "pom", List.of())    // depth 3 — deeper but POM
        ));

        assertThat(new RootBuildFileFilter().apply(prs).getSourcePath())
                .isEqualTo(Path.of("parent/a/pom.xml"));
    }
}
