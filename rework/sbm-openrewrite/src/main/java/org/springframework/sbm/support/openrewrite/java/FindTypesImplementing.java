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
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType.FullyQualified;
import org.openrewrite.marker.SearchResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FindTypesImplementing extends Recipe {
    private final List<FullyQualified> interfaces;

    public FindTypesImplementing(List<FullyQualified> interfaces) {
        this.interfaces = interfaces;
    }

    private final UUID id = Tree.randomId();

    @Override
    public String getDisplayName() {
        return "Find types implementing ";
    }

    @Override
    public String getDescription() {
        return "Find types implementing";
    }

    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                J.ClassDeclaration a = super.visitClassDeclaration(classDecl, executionContext);
                if (classDecl.getKind() == J.ClassDeclaration.Kind.Type.Class &&
                        implementsInterface(classDecl)) {
                    a = a.withMarkers(a.getMarkers().addIfAbsent(new SearchResult(FindTypesImplementing.this.id, "FindTypesImplementing")));
                }
                return a;
            }
        };
    }

    private boolean implementsInterface(J.ClassDeclaration classDecl) {
        if (classDecl.getImplements() == null) {
            return false;
        }

        List<String> fqns = interfaces.stream().map(fqn -> fqn.getFullyQualifiedName()).collect(Collectors.toList());

        return classDecl.getType().getInterfaces().stream()
                .filter(fqn -> !fqns.contains(fqn.getFullyQualifiedName()))
                .findFirst()
                .isEmpty();
    }
}
