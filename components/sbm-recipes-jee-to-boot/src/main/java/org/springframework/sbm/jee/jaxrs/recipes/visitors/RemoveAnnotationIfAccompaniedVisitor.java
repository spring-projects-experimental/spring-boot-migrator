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
package org.springframework.sbm.jee.jaxrs.recipes.visitors;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.RemoveAnnotation;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

@Value
@EqualsAndHashCode(callSuper = true)
public class RemoveAnnotationIfAccompaniedVisitor extends JavaIsoVisitor<ExecutionContext> {
    private static final String ANNOTATION_REMOVED_KEY = "annotationRemoved";
    String annotationTypeToRemove;
    String additionalAnnotationType;

    @Override
    public J.VariableDeclarations visitVariableDeclarations(@NotNull J.VariableDeclarations multiVariable, @NotNull ExecutionContext ctx) {
        J.VariableDeclarations m = super.visitVariableDeclarations(multiVariable, ctx);

        if (variableDeclarationContainsAnnotationType(m, annotationTypeToRemove) && variableDeclarationContainsAnnotationType(m, additionalAnnotationType)) {
            JavaIsoVisitor<ExecutionContext> removeAnnotationVisitor = new RemoveAnnotation("@" + annotationTypeToRemove)
                    .getVisitor();
            m = (J.VariableDeclarations) removeAnnotationVisitor.visit(m, ctx, getCursor());
            this.maybeRemoveImport(TypeUtils.asFullyQualified(JavaType.buildType(annotationTypeToRemove)));
        }

        return m;
    }

    private boolean variableDeclarationContainsAnnotationType(J.VariableDeclarations variableDeclaration, String annotationType) {
        return variableDeclaration.getLeadingAnnotations().stream().anyMatch(annotation -> TypeUtils.isOfClassType(annotation.getType(), annotationType));
    }
}
