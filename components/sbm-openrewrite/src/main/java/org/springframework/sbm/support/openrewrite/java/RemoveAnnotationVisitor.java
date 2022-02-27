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
package org.springframework.sbm.support.openrewrite.java;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType.FullyQualified;
import org.openrewrite.java.tree.TypeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveAnnotationVisitor extends JavaIsoVisitor<ExecutionContext> {

    private final String fqAnnotationName;

    private final J target;

    public RemoveAnnotationVisitor(J target, String fqAnnotationName) {
        this.fqAnnotationName = fqAnnotationName;
        this.target = target;
    }

    public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration cd, ExecutionContext executionContext) {
        J.ClassDeclaration classDecl = super.visitClassDeclaration(cd, executionContext);
        if (target == classDecl) {
            List<J.Annotation> keptAnnotations = classDecl.getLeadingAnnotations()
                    .stream()
                    .filter(a -> {
                        FullyQualified fullyQualified = TypeUtils.asFullyQualified(a.getType());
                        return fullyQualified == null || !fullyQualified.getFullyQualifiedName().equals(fqAnnotationName);
                    })
                    .collect(Collectors.toList());
            if (classDecl.getLeadingAnnotations().size() != keptAnnotations.size()) {
                // TODO: Analyze annotation for more types referenced by annotation and maybeRemoveImports, then remove the call to removeUnusedImports in MigrateJeeTransactionsToSpringBootAction
                maybeRemoveImport(fqAnnotationName);
                classDecl = classDecl.withLeadingAnnotations(keptAnnotations);
            }
        }
        return classDecl;
    }

    public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration md, ExecutionContext executionContext) {
        J.MethodDeclaration methodDecl = super.visitMethodDeclaration(md, executionContext);
        if (target.getId().equals(methodDecl.getId())) {
            List<J.Annotation> annotations = methodDecl.getLeadingAnnotations()
                    .stream()
                    .filter(a -> {
                        FullyQualified fullyQualified = TypeUtils.asFullyQualified(a.getType());
                        return fullyQualified == null || !fullyQualified.getFullyQualifiedName().equals(fqAnnotationName);
                    })
                    .collect(Collectors.toList());
            if (methodDecl.getLeadingAnnotations().size() != annotations.size()) {
                maybeRemoveImport(fqAnnotationName);
                methodDecl = methodDecl.withLeadingAnnotations(annotations);
            }
        }
        return methodDecl;
    }

    @Override
    public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations mv, ExecutionContext executionContext) {
        J.VariableDeclarations multiVariable = super.visitVariableDeclarations(mv, executionContext);
        if (target == multiVariable) {
            List<J.Annotation> annotations = multiVariable.getLeadingAnnotations().stream()
                    .filter(a -> {
                        FullyQualified fullyQualified = TypeUtils.asFullyQualified(a.getType());
                        return fullyQualified == null || !fullyQualified.getFullyQualifiedName().equals(fqAnnotationName);
                    })
                    .collect(Collectors.toList());
            if (multiVariable.getLeadingAnnotations().size() != annotations.size()) {
                maybeRemoveImport(fqAnnotationName);
                multiVariable = multiVariable.withLeadingAnnotations(annotations);
            }
        }
        return multiVariable;
    }

}
