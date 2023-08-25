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
package org.springframework.sbm.boot.upgrade_27_30.checks;

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JerseyTemporarilyRemovedSectionBuilder implements Sbu30_UpgradeSectionBuilder {

    private final JerseyTemporarilyRemovedFinder finder;

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return !finder.findMatches(projectContext).isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {
        return ChangeSection.RelevantChangeSection.builder()
                .title("Support for Jersey has been temporarily removed")
                .paragraph("Support for Jersey has been temporarily removed as it does not yet support Spring Framework 6.")
                .relevanceSection()
                .paragraph("""
                            The scan found one or more dependencies to jersey. 
                            Support for Jersey has been temporarily removed as it does not yet support Spring Framework 6. 
                            Please try again when Jersey is support.
                            """
                )
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                                .text("Remove/replace Jersey or wait for a release with Jersey support added.")
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
