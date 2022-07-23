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
package org.springframework.sbm.common.migration.actions;

import lombok.Setter;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.ProjectResource;

/**
 * Delete files matching path pattern.
 *
 * see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html">AntPathMatcher</a> for pattern reference.
 */
public class DeleteFileMatchingPattern extends AbstractAction {

    @Setter
    private String pattern;

    @Override
    public void apply(ProjectContext context) {
        context.search(new PathPatternMatchingProjectResourceFinder(pattern)).forEach(ProjectResource::delete);
    }
}
