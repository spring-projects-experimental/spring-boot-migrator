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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.build.impl.RewriteMavenArtifactDownloader;
import org.springframework.sbm.project.parser.DependencyHelper;

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

	private static final DependencyHelper dependencyHelper = new DependencyHelper();

	/**
	 * Dependencies found during scan. These dependencies are immutable.
	 */
	private final ConcurrentSkipListMap<ResolvedDependency, Path> initialDependencies = new ConcurrentSkipListMap<ResolvedDependency, Path>(
			Comparator.comparing(r -> r.getGav().toString()));

	/**
	 * Dependencies at the current state of migration. These dependencies can change over
	 * time when dependencies were added or removed by {@code Action}s.
	 */
	private final ConcurrentSkipListMap<ResolvedDependency, Path> currentDependencies = new ConcurrentSkipListMap<ResolvedDependency, Path>(
			Comparator.comparing(r -> r.getGav().toString()));

	private ClasspathRegistry() {
	}

	public static ClasspathRegistry initialize(Set<ResolvedDependency> dependencies) {
		return DependenciesRegistryHolder.initialDependencies(dependencies);
	}

	// FIXME: remove unused method
	// public static void initialize(List<RewriteSourceFileHolder<Xml.Document>>
	// buildFiles) {
	// Set<Dependency> effectiveDependencies = new HashSet<>();
	// buildFiles.forEach(bf -> {
	// });
	// ClasspathRegistry.initialize(effectiveDependencies);
	// }

	public static void initializeFromBuildFiles(List<BuildFile> buildFiles) {
		ClasspathRegistry.getInstance().clear();
		Set<ResolvedDependency> effectiveDependencies = new HashSet<>();
		buildFiles.forEach(bf -> {
            Map<Scope, List<ResolvedDependency>> dependencies = ((OpenRewriteMavenBuildFile) bf).getPom().getDependencies();
			// FIXME: #7 respect scope
            effectiveDependencies.addAll(dependencies.get(Scope.Compile));
			effectiveDependencies.addAll(dependencies.get(Scope.Test));
			effectiveDependencies.addAll(dependencies.get(Scope.Provided));
			effectiveDependencies.addAll(dependencies.get(Scope.Runtime));
		});
		ClasspathRegistry.initialize(effectiveDependencies);
	}

	private static org.openrewrite.maven.tree.Dependency mapToRewriteDependency(
			org.springframework.sbm.build.api.Dependency dependency) {
		return null;
	}

	public void clear() {
		initialDependencies.clear();
		currentDependencies.clear();
	}

	private static class DependenciesRegistryHolder {

		public static final ClasspathRegistry INSTANCE = new ClasspathRegistry();

		public static ClasspathRegistry initialDependencies(Set<ResolvedDependency> dependencies) {
			INSTANCE.initDependencies(dependencies);
			return INSTANCE;
		}

	}

	public static ClasspathRegistry getInstance() {
		return DependenciesRegistryHolder.INSTANCE;
	}

	public void addDependency(ResolvedDependency... deps) {
		Arrays.asList(deps).forEach(dep -> {
			initDependency(dep, currentDependencies);
		});
	}

	public void removeDependency(ResolvedDependency... deps) {
		Arrays.asList(deps).forEach(currentDependencies::remove);
	}

	public void removeDependency(String... coordinates) {
		Arrays.asList(coordinates).forEach(coordinate -> currentDependencies.keySet().forEach(dep -> {
			if (dep.getGav().toString().equals(coordinate)) {
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

	private void initDependencies(Set<ResolvedDependency> deps) {
		initialDependencies.clear();
		currentDependencies.clear();
		deps.forEach(dep -> {
			initDependency(dep, initialDependencies, currentDependencies);
		});
	}

	private void initDependency(ResolvedDependency d, Map<ResolvedDependency, Path>... maps) {
		Path dependencyPath = new RewriteMavenArtifactDownloader().downloadArtifact(d);
		if(dependencyPath != null) {
			Stream.of(maps).forEach(m -> m.put(d, dependencyPath));
		} else {
			System.out.println(d.getGav() + " has no jars. It has type " + d.getType());
			initDependencies(new HashSet<>(d.getDependencies()));
		}

//		Optional<Path> dependencyPath = dependencyHelper.downloadArtifact(d);
//		if (dependencyPath.isPresent()) {
//			Stream.of(maps).forEach(m -> m.put(d, dependencyPath.get()));
//		}
	}

}
