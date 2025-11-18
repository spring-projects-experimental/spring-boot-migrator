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
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.tree.Scope;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.marker.Markers;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class RootBuildFileFilterTest {

    /**
     * Fake SourceFile that allows us to control the sourcePath.
     */
    static class DummySourceFile implements SourceFile {
        private final Path sourcePath;

        DummySourceFile(Path sourcePath) {
            this.sourcePath = sourcePath;
        }

        @Override
        public Path getSourcePath() {
            return sourcePath;
        }

        @Override
        public <T extends SourceFile> T withSourcePath(Path path) {
            return null;
        }

        @Override
        public @Nullable Charset getCharset() {
            return null;
        }

        @Override
        public <T extends SourceFile> T withCharset(Charset charset) {
            return null;
        }

        @Override
        public boolean isCharsetBomMarked() {
            return false;
        }

        @Override
        public <T extends SourceFile> T withCharsetBomMarked(boolean marked) {
            return null;
        }

        @Override
        public @Nullable Checksum getChecksum() {
            return null;
        }

        @Override
        public <T extends SourceFile> T withChecksum(@Nullable Checksum checksum) {
            return null;
        }

        @Override
        public @Nullable FileAttributes getFileAttributes() {
            return null;
        }

        @Override
        public <T extends SourceFile> T withFileAttributes(@Nullable FileAttributes fileAttributes) {
            return null;
        }

        @Override
        public UUID getId() {
            return null;
        }

        @Override
        public Markers getMarkers() {
            return Markers.EMPTY;
        }

        @Override
        public <T extends Tree> T withId(UUID id) {
            return null;
        }

        @Override
        public <P> boolean isAcceptable(TreeVisitor<?, P> v, P p) {
            return false;
        }

        @Override
        public SourceFile withMarkers(Markers markers) {
            return this;
        }
    }

    /**
     * Dummy BuildFile implementation based on RewriteSourceFileHolder.
     */
    static class DummyBuildFile extends RewriteSourceFileHolder<SourceFile> implements BuildFile {

        DummyBuildFile(Path projectDir, Path sourcePath) {
            super(projectDir, new DummySourceFile(sourcePath));
        }

        @Override
        public List<Dependency> getDeclaredDependencies(Scope... scopes) {
            return List.of();
        }

        @Override
        public List<Dependency> getRequestedDependencies() {
            return List.of();
        }

        @Override
        public Set<Dependency> getEffectiveDependencies(Scope scope) {
            return Set.of();
        }

        @Override
        public Set<Dependency> getEffectiveDependencies() {
            return Set.of();
        }

        @Override
        public boolean hasDeclaredDependencyMatchingRegex(String... dependencyPatterns) {
            return false;
        }

        @Override
        public boolean hasEffectiveDependencyMatchingRegex(String... dependencyPatterns) {
            return false;
        }

        @Override
        public boolean hasExactDeclaredDependency(Dependency dependency) {
            return false;
        }

        @Override
        public void addDependency(Dependency dependency) {

        }

        @Override
        public void addDependencies(List<Dependency> dependencies) {

        }

        @Override
        public void removeDependencies(List<Dependency> dependencies) {

        }

        @Override
        public void removeDependenciesMatchingRegex(String... regex) {

        }

        @Override
        public void removeDependenciesInner(List<Dependency> dependencies) {

        }

        @Override
        public List<Dependency> getEffectiveDependencyManagement() {
            return List.of();
        }

        @Override
        public List<Dependency> getRequestedDependencyManagement() {
            return List.of();
        }

        @Override
        public List<Dependency> getRequestedManagedDependencies() {
            return List.of();
        }

        @Override
        public void addToDependencyManagement(Dependency dependency) {

        }

        @Override
        public void addToDependencyManagementInner(Dependency dependency) {

        }

        @Override
        public List<Path> getResolvedDependenciesPaths() {
            return List.of();
        }

        @Override
        public boolean hasPlugin(Plugin plugin) {
            return false;
        }

        @Override
        public void addPlugin(Plugin plugin) {

        }

        @Override
        public List<Path> getClasspath() {
            return List.of();
        }

        @Override
        public List<Path> getSourceFolders() {
            return List.of();
        }

        @Override
        public List<Path> getTestSourceFolders() {
            return List.of();
        }

        @Override
        public List<Path> getResourceFolders() {
            return List.of();
        }

        @Override
        public List<Path> getTestResourceFolders() {
            return List.of();
        }

        @Override
        public Path getTestResourceFolder() {
            return null;
        }

        @Override
        public Path getMainResourceFolder() {
            return null;
        }

        @Override
        public void setProperty(String key, String value) {

        }

        @Override
        public String getProperty(String key) {
            return "";
        }

        @Override
        public void deleteProperty(String key) {

        }

        @Override
        public String getPackaging() {
            return "";
        }

        @Override
        public void setPackaging(String packaging) {

        }

        @Override
        public boolean isRootBuildFile() {
            // for this test we force it to return false
            // so that RootBuildFileFilter must use the fallback logic.
            return false;
        }

        @Override
        public List<Plugin> getPlugins() {
            return List.of();
        }

        @Override
        public void removePluginsMatchingRegex(String... regex) {

        }

        @Override
        public void removePlugins(String... coordinates) {

        }

        @Override
        public String getGroupId() {
            return "";
        }

        @Override
        public String getArtifactId() {
            return "";
        }

        @Override
        public String getVersion() {
            return "";
        }

        @Override
        public String getCoordinates() {
            return "";
        }

        @Override
        public boolean hasParent() {
            return false;
        }

        @Override
        public void upgradeParentVersion(String version) {

        }

        @Override
        public Optional<ParentDeclaration> getParentPomDeclaration() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getName() {
            return Optional.empty();
        }

        @Override
        public void excludeDependencies(List<Dependency> excludedDependencies) {

        }

        @Override
        public void addRepository(RepositoryDefinition repository) {

        }

        @Override
        public void addPluginRepository(RepositoryDefinition repository) {

        }

        @Override
        public List<RepositoryDefinition> getRepositories() {
            return List.of();
        }

        @Override
        public List<RepositoryDefinition> getPluginRepositories() {
            return List.of();
        }

        @Override
        public List<String> getDeclaredModules() {
            return List.of();
        }

        @Override
        public Optional<Plugin> findPlugin(String groupId, String artifactId) {
            return Optional.empty();
        }

        // other BuildFile methods can be left unimplemented for now,
        // or given simple no-op bodies if the interface requires more.
    }

    @Test
    void choosesBuildFileWithShortestPathAsRootWhenNoneMarkedAsRoot() {
        Path projectDir = Paths.get("C:\\fake-project").toAbsolutePath();

        // Simulate two pom.xml files:
        // - "parent/pom.xml"        (depth 2)  -> this should be chosen as root
        // - "parent/module/pom.xml" (depth 3)
        DummyBuildFile parentPom = new DummyBuildFile(projectDir, Path.of("parent/pom.xml"));
        DummyBuildFile modulePom = new DummyBuildFile(projectDir, Path.of("parent/module/pom.xml"));

        ProjectResourceSet prs = new ProjectResourceSet();
        prs.add(parentPom);
        prs.add(modulePom);

        RootBuildFileFilter filter = new RootBuildFileFilter();

        BuildFile root = filter.apply(prs);

        assertThat(root.getSourcePath())
                .isEqualTo(Path.of("parent", "pom.xml"));
    }
}
