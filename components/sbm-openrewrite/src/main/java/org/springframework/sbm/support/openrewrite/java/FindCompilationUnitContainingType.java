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
package org.springframework.sbm.support.openrewrite.java;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.SearchResult;

import java.util.UUID;

@RequiredArgsConstructor
public class FindCompilationUnitContainingType extends Recipe {

    private final JavaType.FullyQualified javaType;

    @Override
    public @NotNull String getDisplayName() {
        return "Find CompilationUnit containing a give JavaType.Class";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

    @Override
    public @NotNull TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            public J.CompilationUnit visitCompilationUnit(J.CompilationUnit cu, ExecutionContext executionContext) {
                J.CompilationUnit compilationUnit = super.visitCompilationUnit(cu, executionContext);
                boolean compilationUnitContainsType = compilationUnit.getClasses().stream()
                        .anyMatch(t -> t.getType().getFullyQualifiedName().equals(javaType.getFullyQualifiedName().trim()));
                if (compilationUnitContainsType) {
                    J.ClassDeclaration classDeclaration = compilationUnit.getClasses().stream()
                            .filter(t -> t.getType().getFullyQualifiedName().equals(javaType.getFullyQualifiedName().trim()))
                            .findFirst()
                            .get();

                    compilationUnit = compilationUnit.withMarkers(classDeclaration.getMarkers().add(new SearchResult(UUID.randomUUID(), "FindCompilationUnitContainingType")));
                }
                return compilationUnit;
            }
        };
    }
}
