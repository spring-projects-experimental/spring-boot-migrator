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
package org.openrewrite.java.spring;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.SearchResult;

import java.util.UUID;

@RequiredArgsConstructor
public class SpringBeanDeclarationFinder extends Recipe {

    private final String returningType;

    @Override
    public String getDisplayName() {
        return "Find Spring bean method declarations returning a bean of given type.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
                J.MethodDeclaration md = super.visitMethodDeclaration(method, executionContext);
                if(isBeanDeclarationMethod(md) && returnsGivenType(md)) {
                    return md.withMarkers(md.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "Found Spring bean method declaration returning a bean of type " + returningType)));
                }
                return md;
            }

            private boolean returnsGivenType(J.MethodDeclaration md) {
                return ((JavaType.FullyQualified) md.getReturnTypeExpression().getType()).getFullyQualifiedName().equals(returningType);
            }

            private boolean isBeanDeclarationMethod(J.MethodDeclaration md) {
                return md
                        .getLeadingAnnotations()
                        .stream()
                        .anyMatch(a -> JavaType.FullyQualified.class.isAssignableFrom(
                                a.getType().getClass()) && JavaType.FullyQualified.class
                                .cast(a.getType().getClass())
                                .getFullyQualifiedName()
                                .equals("org.springframework.context.annotation.Bean"));
            }
        };
    }
}
