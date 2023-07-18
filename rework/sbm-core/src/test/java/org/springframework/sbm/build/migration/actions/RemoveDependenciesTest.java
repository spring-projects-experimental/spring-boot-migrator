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
package org.springframework.sbm.build.migration.actions;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class RemoveDependenciesTest {

    @Test
    void shouldDeleteDependencies() {
        RemoveDependencies sut = new RemoveDependencies();

        Dependency dependency = createDependency();
        List<Dependency> dependencies = List.of(dependency);
        sut.setDependencies(dependencies);

        BuildFile buildFile = mock(BuildFile.class);
        when(buildFile.isRootBuildFile()).thenReturn(true);

        ProjectContext projectContext = mock(ProjectContext.class);
        when(projectContext.getBuildFile()).thenReturn(buildFile);

        sut.apply(projectContext);

        verify(buildFile).removeDependencies(dependencies);
    }

    private Dependency createDependency() {
        return Dependency.builder()
                .groupId("group-id")
                .artifactId("artifact-id")
                .version("version")
                .build();
    }
}
