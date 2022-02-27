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

import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class HasNoTypeAnnotation implements Condition {

    private final HasTypeAnnotation hasTypeAnnotation;

    public HasNoTypeAnnotation() {
        this.hasTypeAnnotation = new HasTypeAnnotation();
    }

    @Override
    public String getDescription() {
        return "If there are NO types annotated with " + hasTypeAnnotation.getAnnotation();
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return !hasTypeAnnotation.evaluate(context);
    }

    public void setAnnotation(String annotation) {
        hasTypeAnnotation.setAnnotation(annotation);
    }
}
