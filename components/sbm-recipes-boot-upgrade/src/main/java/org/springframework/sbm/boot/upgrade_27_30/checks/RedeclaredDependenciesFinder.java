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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedManagedDependency;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RedeclaredDependenciesFinder implements Sbm30_Finder<Set<Dependency>> {
    private final Set<String> analysingDependencies = new HashSet<>();

    public RedeclaredDependenciesFinder() {
    }

    public RedeclaredDependenciesFinder(Set<String> analysingDependencies) {
        this.analysingDependencies.addAll(analysingDependencies);
    }

    @NotNull
    @Override
    public Set<RedeclaredDependency> findMatches(ProjectContext context) {
        Set<RedeclaredDependency> result = new HashSet<>();
        context.getApplicationModules().stream()
                .forEach(am -> {
                    List<org.openrewrite.maven.tree.Dependency> requestedDependencies = ((MavenResolutionResult) (((OpenRewriteMavenBuildFile) am.getBuildFile()).getPom())).getPom().getRequestedDependencies();
                    Map<String, String> managedMap = prepareManagedDependenciesMap(am);
                    List<Dependency> declaredDependencies = am.getBuildFile().getDeclaredDependencies();
                    Set<RedeclaredDependency> redeclaredDependencies = requestedDependencies.stream()
                            .filter(d -> analysingDependencies.isEmpty() || analysingDependencies.contains(getGroupAndArtifactKey(d)))
                            .filter(d -> d.getVersion() != null && !d.getVersion().isEmpty())
                            .filter(d -> managedMap.containsKey(getGroupAndArtifactKey(d)))
                            .map(d -> {
                                Dependency dependency = Dependency.builder().groupId(d.getGroupId()).artifactId(d.getArtifactId()).version(d.getVersion()).build();
                                return new RedeclaredDependency(dependency, managedMap.get(getGroupAndArtifactKey(d)));
                            })
                            .collect(Collectors.toSet());
                    result.addAll(redeclaredDependencies);
                });
        return result;
    }

    @NotNull
    private Map<String, String> prepareManagedDependenciesMap(ApplicationModule am) {
        List<ResolvedManagedDependency> managedDependencies = ((MavenResolutionResult) (((OpenRewriteMavenBuildFile) am.getBuildFile()).getPom())).getPom().getDependencyManagement();
        return managedDependencies.stream()
                .filter(d -> d.getVersion() != null && !d.getVersion().isEmpty())
                .collect(Collectors.toMap(this::getGroupAndArtifactKeyResolved, ResolvedManagedDependency::getVersion, (d1, d2) -> d2));
    }

    @NotNull
    private String getGroupAndArtifactKeyResolved(ResolvedManagedDependency d) {
        return d.getGroupId() + ":" + d.getArtifactId();
    }

    @NotNull
    private String getGroupAndArtifactKey(org.openrewrite.maven.tree.Dependency d) {
        return d.getGroupId() + ":" + d.getArtifactId();
    }

    public record RedeclaredDependency(Dependency redeclaredDependency,
                                       String originalVersion) {

        public Dependency getRedeclaredDependency() {
            return redeclaredDependency;
        }

        public String getOriginalVersion() {
            return originalVersion;
        }
    }
}
