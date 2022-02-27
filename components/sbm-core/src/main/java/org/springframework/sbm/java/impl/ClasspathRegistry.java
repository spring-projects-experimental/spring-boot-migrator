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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.project.parser.DependencyHelper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.maven.tree.Maven;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;

/**
 * Used to store and retrieve project dependencies to create {@code JavaParser}.
 * Currently, the classpath contains all dependencies in all scopes and for all modules
 */
@Slf4j
public class ClasspathRegistry {

    private static DependencyHelper dependencyHelper = new DependencyHelper();

    /**
     * Dependencies found during scan.
     * These dependencies are immutable.
     */
    private final ConcurrentSkipListMap<Pom.Dependency, Path> initialDependencies = new ConcurrentSkipListMap<Pom.Dependency, Path>(Comparator.comparing(Pom.Dependency::getCoordinates));

    /**
     * Dependencies at the current state of migration.
     * These dependencies can change over time when dependencies were added or removed by {@code Action}s.
     */
    private final ConcurrentSkipListMap<Pom.Dependency, Path> currentDependencies = new ConcurrentSkipListMap<Pom.Dependency, Path>(Comparator.comparing(Pom.Dependency::getCoordinates));


    private ClasspathRegistry() {
    }

    public static ClasspathRegistry initialize(Set<Pom.Dependency> dependencies) {
        return DependenciesRegistryHolder.initialDependencies(dependencies);
    }

    public static void initialize(List<RewriteSourceFileHolder<Maven>> buildFiles) {
        Set<Pom.Dependency> effectiveDependencies = new HashSet<>();
        buildFiles.forEach(bf -> {
            effectiveDependencies.addAll(bf.getSourceFile().getModel().getDependencies(Scope.Compile));
            effectiveDependencies.addAll(bf.getSourceFile().getModel().getDependencies(Scope.Test));
            effectiveDependencies.addAll(bf.getSourceFile().getModel().getDependencies(Scope.Provided));
            effectiveDependencies.addAll(bf.getSourceFile().getModel().getDependencies(Scope.Runtime));
            // TODO: system ?
        });
        ClasspathRegistry.initialize(effectiveDependencies);
    }

    public static void initializeFromBuildFiles(List<BuildFile> buildFiles) {
        ClasspathRegistry.getInstance().clear();
        Set<Pom.Dependency> effectiveDependencies = new HashSet<>();
        buildFiles.forEach(bf -> {
            effectiveDependencies.addAll(bf.getEffectiveDependencies(Scope.Compile));
            effectiveDependencies.addAll(bf.getEffectiveDependencies(Scope.Test));
            effectiveDependencies.addAll(bf.getEffectiveDependencies(Scope.Provided));
            effectiveDependencies.addAll(bf.getEffectiveDependencies(Scope.Runtime));
            // TODO: system ?
        });
        ClasspathRegistry.initialize(effectiveDependencies);
    }

    public void clear() {
        initialDependencies.clear();
        currentDependencies.clear();
    }

    private static class DependenciesRegistryHolder {
        public static final ClasspathRegistry INSTANCE = new ClasspathRegistry();

        public static ClasspathRegistry initialDependencies(Set<Pom.Dependency> dependencies) {
            INSTANCE.initDependencies(dependencies);
            return INSTANCE;
        }
    }

    public static ClasspathRegistry getInstance() {
        return DependenciesRegistryHolder.INSTANCE;
    }

    public void addDependency(Pom.Dependency... deps) {
        Arrays.asList(deps).forEach(dep -> {
            initDependency(dep, currentDependencies);
        });
    }

    public void removeDependency(Pom.Dependency... deps) {
        Arrays.asList(deps).forEach(currentDependencies::remove);
    }

    public void removeDependency(String... coordinates) {
        Arrays.asList(coordinates)
                .forEach(coordinate -> currentDependencies.keySet().forEach(dep -> {
                    if (dep.getCoordinates().equals(coordinate)) {
                        currentDependencies.remove(coordinate);
                    }
                }));
    }

    public Set<Path> getInitialDependencies() {
        return new HashSet<>(initialDependencies.values());
    }

    public Set<Path> getCurrentDependencies() {
        return new HashSet<>(currentDependencies.values());
    }

    private void initDependencies(Set<Pom.Dependency> deps) {
        initialDependencies.clear();
        currentDependencies.clear();
        deps.forEach(dep -> {
            initDependency(dep, initialDependencies, currentDependencies);
        });
    }

    private void initDependency(Pom.Dependency d, Map<Pom.Dependency, Path>... maps) {
        Optional<Path> dependencyPath = dependencyHelper.downloadArtifact(d);
        if (dependencyPath.isPresent()) {
            Stream.of(maps).forEach(m -> m.put(d, dependencyPath.get()));
        }
    }

}
