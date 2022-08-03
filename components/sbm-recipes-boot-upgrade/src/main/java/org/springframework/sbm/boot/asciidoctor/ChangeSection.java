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
package org.springframework.sbm.boot.asciidoctor;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class ChangeSection implements Section {
    private String title;
    private RelevanceSection relevanceSection;
    private TodoSection todoSection;
    @Singular
    private List<Paragraph> paragraphs;

    @Builder
    @Getter
    @Deprecated
    public static class RelevanceSection {
        @Singular
        private List<Paragraph> paragraphs;
    }

    @Builder
    @Getter
    public static class TodoSection {
        @Singular
        private List<TodoList> todoLists;
        @Singular
        private List<Paragraph> paragraphs;
    }







    public class RelevantChangeSection {

        private ChangeSection.ChangeSectionBuilder changeSectionBuilder;

        public static RelevantChangeSectionTitleBuilder builder() {
            return new RelevantChangeSectionTitleBuilder(ChangeSection.builder());
        }



        public static class RelevantChangeSectionBuilder {
            private final ChangeSection.ChangeSectionBuilder builder;

            public RelevantChangeSectionBuilder(ChangeSection.ChangeSectionBuilder builder) {
                this.builder = builder;
            }

            public RelevantChangeSectionBuilder paragraph(String releaseNote) {
                builder.paragraph(Paragraph.builder()
                        .text(releaseNote)
                        .build());
                return this;
            }

            public RelevanceSectionBuilder relevanceSection() {
                return new RelevanceSectionBuilder(builder);
            }

        }

        public static class RelevanceSectionBuilder {

            private final ChangeSection.ChangeSectionBuilder builder;
            private final ChangeSection.RelevanceSection.RelevanceSectionBuilder relevanceSectionBuilder;

            public RelevanceSectionBuilder(ChangeSection.ChangeSectionBuilder builder) {
                this.builder = builder;
                relevanceSectionBuilder = ChangeSection.RelevanceSection.builder();

            }

            public RelevanceSectionBuilder paragraph(String text) {
                relevanceSectionBuilder.paragraph(Paragraph.builder()
                        .text(text)
                        .build());
                return this;
            }

            public RelevanceSectionBuilder table(Table table) {
                relevanceSectionBuilder.paragraph(Paragraph.builder()
                        .table(table)
                        .build());
                return this;
            }

            public TodoSectionBuilder todoSection() {
                builder.relevanceSection(relevanceSectionBuilder.build());
                return new TodoSectionBuilder(builder);
            }

        }

        public static class TodoSectionBuilder {
            private final ChangeSection.ChangeSectionBuilder builder;
            private final ChangeSection.TodoSection.TodoSectionBuilder todoSectionBuilder;

            public TodoSectionBuilder(ChangeSection.ChangeSectionBuilder builder) {
                this.builder = builder;
                this.todoSectionBuilder = ChangeSection.TodoSection.builder();
            }

            public TodoSectionBuilder todoList(TodoList todoList) {
                todoSectionBuilder.todoList(todoList);
                return this;
            }

            public ChangeSection build() {
                builder.todoSection(todoSectionBuilder.build());
                return builder.build();
            }

            public TodoSectionBuilder paragraph(String paragraph) {
                todoSectionBuilder.paragraph(Paragraph.builder()
                        .text(paragraph)
                        .build());
                return this;
            }
        }

        public static class RelevantChangeSectionTitleBuilder {
            private final ChangeSection.ChangeSectionBuilder builder;

            public RelevantChangeSectionTitleBuilder(ChangeSection.ChangeSectionBuilder builder) {
                this.builder = builder;
            }

            public RelevantChangeSectionBuilder title(String title) {
                builder.title(title);
                return new RelevantChangeSectionBuilder(builder);
            }
        }
    }


}