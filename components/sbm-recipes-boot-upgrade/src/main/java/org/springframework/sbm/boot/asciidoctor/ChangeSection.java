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






}