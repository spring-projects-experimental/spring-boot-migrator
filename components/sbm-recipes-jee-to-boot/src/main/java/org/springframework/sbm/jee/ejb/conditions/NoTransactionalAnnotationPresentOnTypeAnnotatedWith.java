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
package org.springframework.sbm.jee.ejb.conditions;

import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.Setter;

/**
 * Condition is true if any type found annotated with {@code typeAnnotatedWith} but no {@code org.springframework.transaction.annotation.Transactional} annotation is present.
 */
@Getter
@Setter
public class NoTransactionalAnnotationPresentOnTypeAnnotatedWith implements Condition {

    private String typeAnnotatedWith;
    private static final String TRANSACTION_ANNOTATION = "org.springframework.transaction.annotation.Transactional";

    @Override
    public String getDescription() {
        return "Add @Transactional annotation to types annotated with " + typeAnnotatedWith + ".";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectJavaSources().asStream()
                .flatMap(js -> js.getTypes().stream())
                .anyMatch(t -> t.hasAnnotation(typeAnnotatedWith) && !t.hasAnnotation(TRANSACTION_ANNOTATION));
    }
}
