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
package org.springframework.sbm.java.migration.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
public class RemoveTypeAnnotationAction extends AbstractAction {

    @NotEmpty
    private String annotation;

    @Override
    public void apply(ProjectContext context) {
        context.getProjectJavaSources().list()
                .stream()
                .flatMap(js -> js.getTypes().stream())
                .filter(type -> type.hasAnnotation(annotation))
                .forEach(type -> type.removeAnnotation(annotation));
    }
}
