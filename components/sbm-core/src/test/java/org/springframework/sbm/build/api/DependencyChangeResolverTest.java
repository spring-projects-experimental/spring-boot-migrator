package org.springframework.sbm.build.api;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DependencyChangeResolverTest {

    @Mock
    OpenRewriteMavenBuildFile buildFile;

    @Test
    public void givenBuildFile_addNewDependency_withScopeCompile_expectNewDependencyAdded(){
        Dependency proposedDependency =
                new Dependency.DependencyBuilder()
                .groupId("org.springframework.sbm")
                .artifactId("directDependency")
                .version("1.0")
                .scope("compile")
                .build();

        when(buildFile.getEffectiveDependencies())
                .thenReturn(Collections.emptySet());

        Pair<List<Dependency>, Optional<Dependency>> pair =
                new DependencyChangeResolver(buildFile, proposedDependency).apply();

        assertThat(pair.getLeft().isEmpty());
        assertThat(pair.getRight().isPresent());
        assertThat(pair.getRight().get().equals(proposedDependency));
    }

    @Test
    public void givenBuildFile_addExistingTransitiveDependency_withLowerScope_expectNoOp(){
        Dependency proposedDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("test")
                        .build();

        Dependency existingDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("compile")
                        .build();

        when(buildFile.getEffectiveDependencies())
                .thenReturn(Set.of(existingDependency));

        when(buildFile.getDeclaredDependencies(any()))
                .thenReturn(Collections.emptyList());

        Pair<List<Dependency>, Optional<Dependency>> pair =
                new DependencyChangeResolver(buildFile, proposedDependency).apply();

        assertThat(pair.getLeft().isEmpty());
        assertThat(pair.getRight().isEmpty());
    }

    @Test
    public void givenBuildFile_addExistingTransitiveDependency_withHigherScope_expectNoOp(){
        Dependency proposedDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("compile")
                        .build();

        Dependency existingDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("test")
                        .build();

        when(buildFile.getEffectiveDependencies())
                .thenReturn(Set.of(existingDependency));

        when(buildFile.getDeclaredDependencies(any()))
                .thenReturn(Collections.emptyList());

        Pair<List<Dependency>, Optional<Dependency>> pair =
                new DependencyChangeResolver(buildFile, proposedDependency).apply();

        assertThat(pair.getLeft().isEmpty());
        assertThat(pair.getRight().isPresent());
        assertThat(pair.getRight().get().equals(proposedDependency));
    }

    @Test
    public void givenBuildFile_addExistingDirectDependency_withHigherScope_expectNoOp(){
        Dependency proposedDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("compile")
                        .build();

        Dependency existingDependency =
                new Dependency.DependencyBuilder()
                        .groupId("org.springframework.sbm")
                        .artifactId("directDependency")
                        .version("1.0")
                        .scope("test")
                        .build();

        when(buildFile.getEffectiveDependencies())
                .thenReturn(Set.of(existingDependency));

        when(buildFile.getDeclaredDependencies(any()))
                .thenReturn(List.of(existingDependency));

        Pair<List<Dependency>, Optional<Dependency>> pair =
                new DependencyChangeResolver(buildFile, proposedDependency).apply();

        assertThat(!pair.getLeft().isEmpty());
        assertThat(pair.getLeft().get(0).getScope().equals("test"));
        assertThat(pair.getRight().isPresent());
        assertThat(pair.getRight().get().getScope().equals("compile"));
    }

}
