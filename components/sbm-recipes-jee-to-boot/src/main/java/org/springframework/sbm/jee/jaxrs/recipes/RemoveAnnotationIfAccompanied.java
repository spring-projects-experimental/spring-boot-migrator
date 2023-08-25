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
package org.springframework.sbm.jee.jaxrs.recipes;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.search.UsesType;
import org.springframework.sbm.jee.jaxrs.recipes.visitors.RemoveAnnotationIfAccompaniedVisitor;

/**
 * @author Vincent Botteman
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class RemoveAnnotationIfAccompanied extends Recipe {
    @Option(displayName = "Annotation Type to remove",
            description = "The fully qualified name of the annotation to remove.",
            example = "org.junit.Test")
    String annotationTypeToRemove;

    @Option(displayName = "Annotation Type which must also be present",
            description = "The fully qualified name of the annotation that must also be present.",
            example = "org.junit.Test")
    String additionalAnnotationType;

    @Override
    public @NotNull String getDisplayName() {
        return "Remove annotation if accompanied by the other annotation";
    }

    @Override
    public @NotNull String getDescription() {
        return "Remove matching annotation if the other annotation is also present.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getSingleSourceApplicableTest() {
        return new UsesType<>(annotationTypeToRemove);
    }

    @Override
    public @NotNull RemoveAnnotationIfAccompaniedVisitor getVisitor() {
        return new RemoveAnnotationIfAccompaniedVisitor(annotationTypeToRemove, additionalAnnotationType);
    }
}