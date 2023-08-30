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

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.SearchResult;

import java.util.UUID;

public class FilterInterfaceVisitor extends Recipe {

    private final UUID id = Tree.randomId();

    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                J.ClassDeclaration a = super.visitClassDeclaration(classDecl, executionContext);
                if (classDecl.getKind() == J.ClassDeclaration.Kind.Type.Interface) {
                    a = a.withMarkers(a.getMarkers().addIfAbsent(new SearchResult(FilterInterfaceVisitor.this.id, "FilterInterfaceVisitor")));
                }

                return a;
            }
        };
    }

    @Override
    public String getDisplayName() {
        return "Filter interfaces";
    }
}
