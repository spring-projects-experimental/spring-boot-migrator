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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private String sectionTitle;
    private RelevanceSection relevanceSection;
    private TodoSection todoSection;
    private List<String> paragraphs = new ArrayList<>();

    public static SectionBuilder buildSection(String sectionTitle) {
        SectionBuilder sectionBuilder = new SectionBuilder();
        sectionBuilder.setTitle(sectionTitle);
        return sectionBuilder;
    }

    private void setTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public RelevanceSection addRelevance() {
        RelevanceSection relevanceSection = new RelevanceSection();
        setRelevanceSection(relevanceSection);
        return relevanceSection;
    }

    private void setRelevanceSection(RelevanceSection relevanceSection) {
        this.relevanceSection = relevanceSection;
    }

    private void setTodoSection(TodoSection todoSection) {
        this.todoSection = todoSection;
    }

    public SectionBuilder addParagraph(String paragraph) {
        this.paragraphs.add(paragraph);
        return this;
    }


    @Getter
    public class RelevanceSection {
        private List<String> paragraphs = new ArrayList<>();

        public RelevanceSection addParagraph(String paragraph) {
            this.paragraphs.add(paragraph);
            return this;
        }

        public TodoSection addTodoSection() {
            TodoSection todoSection = new TodoSection();
            setTodoSection(todoSection);
            return todoSection;
        }


    }

    @Getter
    public class TodoSection {
        private List<String> todos = new ArrayList<>();

        public TodoSection addTodo(String todo) {
            this.todos.add(todo);
            return this;
        }

        public Section build() {
            return new Section(sectionTitle, paragraphs, relevanceSection, todoSection);
        }
    }

    @Getter
    public class Section {
        private final String title;
        private final RelevanceSection relevanceSection;
        private final TodoSection todoSection;
        private final List<String> paragraphs;

        public Section(String title, List<String> paragraphs, RelevanceSection relevanceSection, TodoSection todoSection) {
            this.title = title;
            this.paragraphs = paragraphs;
            this.relevanceSection = relevanceSection;
            this.todoSection = todoSection;
        }
    }
}
