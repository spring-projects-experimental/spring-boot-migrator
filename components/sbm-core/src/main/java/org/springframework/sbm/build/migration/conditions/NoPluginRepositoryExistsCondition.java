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
package org.springframework.sbm.build.migration.conditions;

import lombok.Setter;
import org.springframework.sbm.build.api.RepositoryDefinition;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

@Setter
public class NoPluginRepositoryExistsCondition implements Condition {
    private String url;

    @Override
    public String getDescription() {
        return "Check that no Plugin Repository definition with same id or url exists";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        // if name is set and repo

        return !context.getBuildFile()
                .getPluginRepositories().stream()
                .anyMatch(this::urlsAreEqual);
    }

    private boolean urlsAreEqual(RepositoryDefinition r) {
        return r.getUrl() != null && r.getUrl().equals(url);
    }
}
