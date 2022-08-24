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
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_27_30.checks.RedeclaredDependenciesFinder.RedeclaredDependency;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

public class RedeclaredDependenciesBuilderTest {

    @Test
    void shouldBuildSectionWhenFinderHasMatches() {
        Set<RedeclaredDependency> matches = Set.of(new RedeclaredDependency(
                Dependency.builder()
                        .groupId("test.group")
                        .artifactId("test-artifact")
                        .version("2.0.0")
                        .build(),
                "1.0.0"
        ));

        ProjectContext context = mock(ProjectContext.class);
        RedeclaredDependenciesFinder finder = mock(RedeclaredDependenciesFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        RedeclaredDependenciesBuilder builder = new RedeclaredDependenciesBuilder(finder);
        assertThat(builder.isApplicable(context)).isTrue();
    }

    @Test
    void shouldBuildSectionWhenFinderHasNoMatches() {
        Set<RedeclaredDependency> matches = Set.of();
        ProjectContext context = mock(ProjectContext.class);
        RedeclaredDependenciesFinder finder = mock(RedeclaredDependenciesFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        RedeclaredDependenciesBuilder builder = new RedeclaredDependenciesBuilder(finder);
        assertThat(builder.isApplicable(context)).isFalse();
    }

    @Test
    void assertContent() {

        Set<RedeclaredDependency> matches = Set.of(new RedeclaredDependency(
                Dependency.builder()
                        .groupId("test.group")
                        .artifactId("test-artifact")
                        .version("2.0.0")
                        .build(),
                "1.0.0"
        ),
                new RedeclaredDependency(
                        Dependency.builder()
                                .groupId("test.group")
                                .artifactId("test-artifact2")
                                .version("3.0.0")
                                .build(),
                        "2.0.0"
                ));

        ProjectContext context = mock(ProjectContext.class);
        RedeclaredDependenciesFinder finder = mock(RedeclaredDependenciesFinder.class);
        when(finder.findMatches(context)).thenReturn(matches);
        RedeclaredDependenciesBuilder builder = new RedeclaredDependenciesBuilder(finder);

        Section section = builder.build(context);

        assertThat(section).isInstanceOf(ChangeSection.class);
        ChangeSection relevantChangeSection = ChangeSection.class.cast(section);
        assertThat(relevantChangeSection.getTitle()).isEqualTo("Remove redundant explicit version declaration");
        assertThat(relevantChangeSection.getRelevanceSection()).isNotNull();
        assertThat(relevantChangeSection.getRelevanceSection().getParagraphs()).hasSize(1);
        assertThat(relevantChangeSection.getRelevanceSection().getParagraphs().get(0).getText()).isEqualTo("""
                            The scan found one or more redeclared dependencies in build files. 
                            Please check them and remove redundant declarations.
                            """);
        List<TodoList.Todo> todos = relevantChangeSection.getTodoSection().getTodoLists().get(0).getTodos();
        List<String> stringToDos = todos.stream()
                .map(TodoList.Todo::getText)
                .collect(Collectors.toList());
        assertThat(stringToDos).contains("Remove explicit declaration of version for artifact: test.group:test-artifact:2.0.0, its already declared with version 1.0.0");
        assertThat(stringToDos).contains("Remove explicit declaration of version for artifact: test.group:test-artifact2:3.0.0, its already declared with version 2.0.0");
    }
}
