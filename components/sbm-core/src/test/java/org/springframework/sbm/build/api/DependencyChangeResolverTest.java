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

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.maven.tree.Scope;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.openrewrite.maven.tree.Scope.*;

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

        when(buildFile.getDeclaredDependencies(None,
                Compile,
                Provided,
                Runtime,
                Test,
                System))
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

        when(buildFile.getDeclaredDependencies(
                None,
                Compile,
                Provided,
                Runtime,
                Test,
                System
        ))
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

        when(buildFile.getDeclaredDependencies(
                None,
                Compile,
                Provided,
                Runtime,
                Test,
                System
        ))
                .thenReturn(List.of(existingDependency));

        Pair<List<Dependency>, Optional<Dependency>> pair =
                new DependencyChangeResolver(buildFile, proposedDependency).apply();

        assertThat(!pair.getLeft().isEmpty());
        assertThat(pair.getLeft().get(0).getScope().equals("test"));
        assertThat(pair.getRight().isPresent());
        assertThat(pair.getRight().get().getScope().equals("compile"));
    }

}
