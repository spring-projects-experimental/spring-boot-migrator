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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

/**
 * Check if any effective (declared or transitive) dependency exists in any build file found in {@link ProjectContext}.
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnyEffectiveDependencyExistMatchingRegex implements Condition {

    private List<String> dependencies;

    @Override
    public String getDescription() {
        String description = String.format("Buildfile contains any dependency matching '%s'", String.join("', '", dependencies));
        return description;
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return dependencies.stream().anyMatch(d ->
                context.getBuildFile().hasEffectiveDependencyMatchingRegex(d)
        );
    }
}
