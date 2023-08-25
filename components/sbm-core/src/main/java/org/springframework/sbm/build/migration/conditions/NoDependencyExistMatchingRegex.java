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

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoDependencyExistMatchingRegex implements Condition {

    private List<String> dependencies = new ArrayList<>();

    @Override
    public String getDescription() {
        String description = String.format("Buildfile does not contain dependencies matching '%s'", String.join("', '", dependencies));
        return description;
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return dependencies.stream().allMatch(d ->
                context.getModules().stream()
                        .map(Module::getBuildFile)
                        .noneMatch(b -> b.hasDeclaredDependencyMatchingRegex(d))
        );
    }
}
