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

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.checks.RedeclaredDependenciesFinder.RedeclaredDependency;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedeclaredDependenciesBuilderTest {

    @Test
    void shouldBuildSectionWhenFinderHasNoMatches(){
        Set<RedeclaredDependency> matches = Set.of();
        ProjectContext context = mock(ProjectContext.class);
        RedeclaredDependenciesFinder finder = mock(RedeclaredDependenciesFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        RedeclaredDependenciesBuilder sut = new RedeclaredDependenciesBuilder(finder);
        assertThat(sut.isApplicable(context)).isFalse();
    }
}
