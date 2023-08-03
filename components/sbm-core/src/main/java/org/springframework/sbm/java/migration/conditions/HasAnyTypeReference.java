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
package org.springframework.sbm.java.migration.conditions;

import lombok.*;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HasAnyTypeReference implements Condition {

    private List<String> fqTypeNames;

    @Override
    public String getDescription() {
        return "Verifies if a type references on of the given types ["+ fqTypeNames +"]";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectJavaSources().asStream()
                .anyMatch(js -> js.getReferencedTypes().stream().anyMatch(rt -> fqTypeNames.contains(rt)));
    }
}
