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
package org.springframework.sbm.build.api;

import org.springframework.sbm.project.resource.ProjectResource;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BuildFile extends ProjectResource {

    List<Dependency> getDeclaredDependencies();

    Set<Pom.Dependency> getEffectiveDependencies(Scope scope);

    /**
     * Check if any declared dependency matches any of the given regex.
     *
     * @param dependencyPatterns the patterns matching against Maven coordinates 'groupId:artifactId:version'
     */
    boolean hasDeclaredDependencyMatchingRegex(String... dependencyPatterns);

    /**
     * Check if any dependency (declared or transitive) matches any of the given regex.
     *
     * @param dependencyPatterns the patterns matching against Maven coordinates 'groupId:artifactId:version'
     */
//    boolean hasDependencyMatchingRegex(String... dependencyPatterns);

    boolean hasExactDeclaredDependency(Dependency dependency);

    void addDependency(Dependency dependency);

    void addDependencies(List<Dependency> dependencies);

    void removeDependencies(List<Dependency> dependencies);

    /**
     * Removes all dependencies matching given regex.
     *
     * @param regex varargs matching dependency coordinates `groupId:artifactId:version`
     */
    void removeDependenciesMatchingRegex(String... regex);

    void removeDependenciesInner(List<Dependency> dependencies);

    List<Dependency> getDependencyManagement();

    void addToDependencyManagement(Dependency dependency);

    void addToDependencyManagementInner(Dependency dependency);

    List<Path> getResolvedDependenciesPaths();

    boolean hasPlugin(Plugin plugin);

    void addPlugin(Plugin plugin);

    List<Path> getSourceFolders();

    List<Path> getTestSourceFolders();

    List<Path> getResourceFolders();

    List<Path> getClasspath();

    List<Path> getTestResourceFolders();

    void setProperty(String key, String value);

    String getProperty(String key);

    String print();

    String getPackaging();

    void setPackaging(String packaging);

    boolean isRootBuildFile();

    List<Plugin> getPlugins();

    void removePluginsMatchingRegex(String... regex);

    void removePlugins(String... coordinates);

    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getCoordinates();

    boolean hasParent();

    void upgradeParentVersion(String version);

    ParentDeclaration getParentPomDeclaration();

    Optional<String> getName();

    Path getMainResourceFolder();

    // TODO: add same method to ApplicationModules to add excludes to all relevant dependencies in all BuildFiles
    /**
     * Adds an exclusion for each of the dependencies in {@code excludedDependencies} to all declared dependencies in this {@code BuildFile} which transitively depend on it.
     */
    void excludeDependencies(List<Dependency> excludedDependencies);

}
