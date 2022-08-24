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
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JerseyTemporarilyRemovedSectionBuilderTest {

    @Test
    void shouldBuildSectionWhenFinderHasMatches(){
        Set<Module> matches = Set.of(mock(Module.class));
        ProjectContext context = mock(ProjectContext.class);
        JerseyTemporarilyRemovedFinder finder = mock(JerseyTemporarilyRemovedFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        JerseyTemporarilyRemovedSectionBuilder sut = new JerseyTemporarilyRemovedSectionBuilder(finder);
        assertThat(sut.isApplicable(context)).isTrue();
    }

    @Test
    void shouldBuildSectionWhenFinderHasNoMatches(){
        Set<Module> matches = Set.of();
        ProjectContext context = mock(ProjectContext.class);
        JerseyTemporarilyRemovedFinder finder = mock(JerseyTemporarilyRemovedFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        JerseyTemporarilyRemovedSectionBuilder sut = new JerseyTemporarilyRemovedSectionBuilder(finder);
        assertThat(sut.isApplicable(context)).isFalse();
    }

    @Test
    void buildSection() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.glassfish.jersey.connectors:jersey-jdk-connector:2.35")
                .build();

        Set<Module> matches = Set.of();

        JerseyTemporarilyRemovedFinder finder = mock(JerseyTemporarilyRemovedFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        JerseyTemporarilyRemovedSectionBuilder sut = new JerseyTemporarilyRemovedSectionBuilder(finder);

        Section section = sut.build(context);

        assertThat(section).isInstanceOf(ChangeSection.class);
        ChangeSection relevantChangeSection = ChangeSection.class.cast(section);
        assertThat(relevantChangeSection.getTitle()).isEqualTo("Support for Jersey has been temporarily removed");
        assertThat(relevantChangeSection.getTodoSection().getTodoLists().get(0).getTodos().get(0).getText()).isEqualTo("Remove/replace Jersey or wait for a release with Jersey support added.");
    }
}
