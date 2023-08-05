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
package org.springframework.sbm.boot;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class OverviewSectionBuilder {
    private OverviewSection overviewSection;

    public OverviewSectionBuilder(String title) {
        overviewSection = new OverviewSection();
        overviewSection.setTitle(title);
    }

    public static OverviewSectionBuilder title(String title) {
        return new OverviewSectionBuilder(title);
    }

    public OverviewSectionBuilder addParagraph(String paragraph) {
        this.overviewSection.getParagraphs().add(paragraph);
        return this;
    }

    public OverviewSection build() {
        return overviewSection;
    }

    @Data
    public class OverviewSection {
        private String title;
        private List<String> paragraphs = new ArrayList<>();

        public String render() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n").append("= ").append(title).append("\n\n");
            paragraphs.forEach(p -> stringBuilder.append(p).append("\n"));
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
    }
}
