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
package org.springframework.sbm.java.migration.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class AddTypeAnnotationToTypeAnnotatedWith extends AbstractAction {

    /**
     * Any type annotated with {@code annotatedWith}
     */
    private String annotatedWith;

    /**
     * Will be annotated with {@code annotation}
     */
    private String annotation;

    /**
     * {@code addAnnotationIfExists=false} will only  add {@code annotation} if the type is not already annotated with {@code annotation}.
     * {@code addAnnotationIfExists=true} will also add {@code annotation}, if the type is already annotated with {@code annotation}.
     * default is {@ode true}
     */
    private boolean addAnnotationIfExists = true;

    @Override
    public void apply(ProjectContext context) {
        context.getProjectJavaSources().asStream()
                .map(JavaSource::getTypes)
                .flatMap(List::stream)
                .filter(t -> t.hasAnnotation(annotatedWith))
                .filter(this::filterIfExists)
                .forEach(t -> t.addAnnotation(annotation));
    }

    private boolean filterIfExists(Type t) {
        return addAnnotationIfExists ? true : ! t.hasAnnotation(annotation);
    }
}
