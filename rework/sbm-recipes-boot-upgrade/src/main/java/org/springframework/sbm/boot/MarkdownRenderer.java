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
package org.springframework.sbm.boot;

import java.util.ArrayList;
import java.util.List;

public class MarkdownRenderer {
    private List<SectionBuilder.Section> sections = new ArrayList<>();
    private OverviewSectionBuilder.OverviewSection overviewSection;

    public void addSection(SectionBuilder.Section section) {
        this.sections.add(section);
    }

    public String render() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(overviewSection.render());
        sections.forEach(s -> {
            stringBuilder.append(renderSection(s));
        });
        return stringBuilder.toString();
    }

    private String renderSection(SectionBuilder.Section s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("== ").append(s.getTitle()).append("\n\n");
        s.getParagraphs().forEach(p -> stringBuilder.append(p).append("<br>\n\n"));
        stringBuilder.append(renderRelevanceSection(s.getRelevanceSection()));
        stringBuilder.append(renderTodoSection(s.getTodoSection()));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String renderTodoSection(SectionBuilder.TodoSection todoSection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== ").append("Todo").append("\n\n");
        todoSection.getTodos().forEach(todo -> {
            stringBuilder.append("- [ ] ").append(todo).append("\n");
        });
        return stringBuilder.toString();
    }

    private String renderRelevanceSection(SectionBuilder.RelevanceSection relevanceSection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== ").append("Relevance").append("\n\n");
        relevanceSection.getParagraphs().forEach(p -> {
            stringBuilder.append(p).append("\n");
        });
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void addSection(OverviewSectionBuilder.OverviewSection overview) {
        this.overviewSection = overview;
    }
}
