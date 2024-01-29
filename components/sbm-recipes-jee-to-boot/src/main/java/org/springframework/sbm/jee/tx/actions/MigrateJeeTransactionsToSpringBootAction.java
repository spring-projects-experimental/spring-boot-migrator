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
package org.springframework.sbm.jee.tx.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Method;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.java.tree.J;

import java.util.List;
import java.util.stream.Collectors;

public class MigrateJeeTransactionsToSpringBootAction extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        List<RewriteSourceFileHolder<J.CompilationUnit>> relevantSources = collectRelevantSources(context);
        replaceAnnotationsOnTypeLevel(relevantSources);
        replaceAnnotationsOnMethodLevel(relevantSources);
        relevantSources.forEach(r -> ((JavaSource) r).removeUnusedImports()); // because maybeRemoveImport() fails to remove import of TransactionManagementType in MigrateJeeTransactionsToSpringBootAction since 7.16.3
    }

    private List<RewriteSourceFileHolder<J.CompilationUnit>> collectRelevantSources(ProjectContext context) {
        return context.getProjectJavaSources().list()
                .stream()
                .filter(this::filterTypesWithTransactionAnnotations)
                .filter(r -> RewriteSourceFileHolder.class.isAssignableFrom(r.getClass()))
                .map(r -> (RewriteSourceFileHolder<J.CompilationUnit>) r)
                .collect(Collectors.toList());
    }

    private boolean filterTypesWithTransactionAnnotations(JavaSource javaSource) {
        List<String> referencedTypes = javaSource.getReferencedTypes();
        return referencedTypes.contains("javax.ejb.TransactionAttribute") || referencedTypes.contains("javax.ejb.TransactionManagementType");
    }

    private void replaceAnnotationsOnMethodLevel(List<RewriteSourceFileHolder<J.CompilationUnit>> relevantSources) {
        relevantSources.stream()
                .map(JavaSource.class::cast)
                .flatMap(js -> js.getTypes().stream())
                .flatMap(t -> t.getMethods().stream())
                .forEach(m -> this.transformMethodAnnotations(m));
    }

    private void replaceAnnotationsOnTypeLevel(List<RewriteSourceFileHolder<J.CompilationUnit>> relevantSources) {
        relevantSources.stream()
                .map(JavaSource.class::cast)
                .flatMap(js -> js.getTypes().stream())
                .forEach(t -> {
                    transformTypeAnnotations(t);
                });
    }

    private void transformTypeAnnotations(Type type) {
        List<Annotation> annotations = type.getAnnotations();
        for (Annotation annotation : annotations) {
            switch (annotation.getFullyQualifiedName()) {
                case "javax.ejb.TransactionManagement":
                    type.removeAnnotation(annotation);
                    break;
                case "javax.ejb.TransactionAttribute":
                    String assignment = annotation.getAttribute("value").printAssignment();
                    String newAssignment = mapAssignment(assignment);
                    type.addAnnotation("@Transactional(propagation = Propagation." + newAssignment + ")",
                            "org.springframework.transaction.annotation.Transactional",
                            "org.springframework.transaction.annotation.Propagation");
                    type.removeAnnotation(annotation);
                    break;
                default:
            }
        }
    }

    void transformMethodAnnotations(Method method) {
        List<Annotation> annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            switch (annotation.getFullyQualifiedName()) {
                case "javax.ejb.TransactionAttribute":
                    String assignment = annotation.getAttribute("value").printAssignment();
                    String newAssignment = mapAssignment(assignment);
                    method.removeAnnotation(annotation);
                    method.addAnnotation("@Transactional(propagation = Propagation." + newAssignment + ")",
                            "org.springframework.transaction.annotation.Transactional",
                            "org.springframework.transaction.annotation.Propagation");
                    break;
                default:
            }
        }
    }

    @NotNull
    private String mapAssignment(String assignment) {
        String newAssignment = "MANDATORY";
        if (assignment.contains("REQUIRES_NEW")) {
            newAssignment = "REQUIRES_NEW";
        }
        if (assignment.contains("REQUIRED")) {
            newAssignment = "REQUIRED";
        }
        if (assignment.contains("NOT_SUPPORTED")) {
            newAssignment = "NOT_SUPPORTED";
        }
        if (assignment.contains("NEVER")) {
            newAssignment = "NEVER";
        }
        if (assignment.contains("SUPPORTS")) {
            newAssignment = "SUPPORTS";
        }
        return newAssignment;
    }
}
