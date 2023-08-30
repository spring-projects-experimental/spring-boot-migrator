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
package org.springframework.sbm.jee.jpa.actions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
public class RenameUnitNameOfPersistenceContextAnnotationsToDefault extends AbstractAction {

    public static final String PERSISTENCE_CONTEXT = "javax.persistence.PersistenceContext";

    @Override
    public void apply(ProjectContext context) {
        List<JavaSource> javaSources = context
                .getProjectJavaSources()
                .asStream()
                .filter(s -> s
                        .getTypes()
                        .stream()
                        .flatMap(t -> t.getMembers().stream())
                        .anyMatch(m -> m.hasAnnotation(PERSISTENCE_CONTEXT)))
                .collect(Collectors.toList());

                javaSources.forEach(s -> {
                    s.getTypes()
                    .stream()
                    .flatMap(t -> t.getMembers().stream())
                    .filter(m -> m.hasAnnotation(PERSISTENCE_CONTEXT))
                    .map(m -> m.getAnnotation(PERSISTENCE_CONTEXT))
                    .findFirst()
                    .get()
                    .setAttribute("unitName", "default", String.class);
                });
    }
}
