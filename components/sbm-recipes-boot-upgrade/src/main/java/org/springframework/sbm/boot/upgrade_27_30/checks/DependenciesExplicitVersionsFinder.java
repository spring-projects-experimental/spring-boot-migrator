package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedManagedDependency;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DependenciesExplicitVersionsFinder implements Sbm30_Finder<Set<Dependency>> {
    private final Set<String> analysingDependencies = new HashSet<>();

    public DependenciesExplicitVersionsFinder() {
    }

    public DependenciesExplicitVersionsFinder(Set<String> analysingDependencies) {
        this.analysingDependencies.addAll(analysingDependencies);
    }

    @NotNull
    @Override
    public Set<Dependency> findMatches(ProjectContext context) {
        Set<Dependency> result = new HashSet<>();
        context.getApplicationModules().stream()
                .forEach(am -> {
                    List<org.openrewrite.maven.tree.Dependency> requestedDependencies = ((MavenResolutionResult) (((OpenRewriteMavenBuildFile) am.getBuildFile()).getPom())).getPom().getRequestedDependencies();
                    Map<String, String> managedMap = prepareManagedDependenciesMap(am);
                    List<Dependency> declaredDependencies = am.getBuildFile().getDeclaredDependencies();
                    Set<Dependency> dependenciesWithExplicitVersions = requestedDependencies.stream()
                            .filter(d -> analysingDependencies.isEmpty() || analysingDependencies.contains(getGroupAndArtifactKey(d)))
                            .filter(d -> d.getVersion() != null && !d.getVersion().isEmpty())
                            .filter(d -> managedMap.containsKey(getGroupAndArtifactKey(d)))
                            .filter(d -> !managedMap.get(getGroupAndArtifactKey(d)).equals(d.getVersion()))
                            .map(d -> Dependency.builder().groupId(d.getGroupId()).artifactId(d.getArtifactId()).version(d.getVersion()).build())
                            .collect(Collectors.toSet());
                    result.addAll(dependenciesWithExplicitVersions);
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
}
