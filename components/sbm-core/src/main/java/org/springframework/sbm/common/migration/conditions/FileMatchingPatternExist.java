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
package org.springframework.sbm.common.migration.conditions;

import lombok.*;
import org.springframework.sbm.utils.OsAgnosticPathMatcher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.util.PathMatcher;

/**
 * Condition resolves to {@code true} if given {@code antPath} matches any file.
 * Uses Spring's <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html">{@code AntPathMatcher}</a>.
 * <p>
 * Note: A pattern and a path must both be absolute or must both be relative in order for the two to match.
 * Therefore it is recommended that users of this implementation to sanitize patterns in order to prefix them
 * with "/" as it makes sense in the context in which they're used.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileMatchingPatternExist implements Condition {

    private String pattern;
    private final PathMatcher pathMatcher = new OsAgnosticPathMatcher();

    @Override
    public String getDescription() {
        return "an app contains a file matching Ant-style path pattern '" + pattern + "'";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectResources().stream()
                .anyMatch(f -> !f.isDeleted() && pathMatcher.match(pattern, f.getAbsolutePath().toString()));
    }
}
