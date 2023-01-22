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
package org.springframework.sbm.jee.jaxrs.recipes;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.search.UsesType;
import org.springframework.sbm.jee.jaxrs.recipes.visitors.CopyAnnotationAttributeVisitor;

/**
 * @author Vincent Botteman
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class CopyAnnotationAttribute extends Recipe {
    @Option(displayName = "Source Annotation Type",
            description = "The fully qualified name of the source annotation.",
            example = "org.junit.Test")
    String sourceAnnotationType;

    @Option(displayName = "Source Attribute name",
            description = "The name of the attribute on the source annotation containing the value to copy.",
            example = "timeout")
    String sourceAttributeName;

    @Option(displayName = "Target Annotation Type",
            description = "The fully qualified name of the target annotation.",
            example = "org.junit.Test")
    String targetAnnotationType;

    @Option(displayName = "Target Attribute name",
            description = "The name of the attribute on the target annotation which must be set to the value of the source attribute.",
            example = "timeout")
    String targetAttributeName;

    @Override
    protected TreeVisitor<?, ExecutionContext> getSingleSourceApplicableTest() {
        return new UsesType<>(sourceAnnotationType);
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Copy an annotation attribute to another annotation";
    }

    @Override
    public @NotNull String getDescription() {
        return "Copy the value of an annotation attribute to another annotation attribute.";
    }

    @Override
    protected @NotNull JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new CopyAnnotationAttributeVisitor(
                sourceAnnotationType,
                sourceAttributeName,
                targetAnnotationType,
                targetAttributeName
        );
    }
}
