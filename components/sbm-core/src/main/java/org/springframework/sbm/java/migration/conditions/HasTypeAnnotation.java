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
package org.springframework.sbm.java.migration.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HasTypeAnnotation implements Condition {

    @Setter
    @Getter
    private String annotation;

    @Override
    public String getDescription() {
        return "If there are any types annotated with " + annotation;
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectJavaSources().asStream()
                .flatMap(js -> js.getTypes().stream())
                .anyMatch(t -> t.hasAnnotation(annotation));
    }

}
