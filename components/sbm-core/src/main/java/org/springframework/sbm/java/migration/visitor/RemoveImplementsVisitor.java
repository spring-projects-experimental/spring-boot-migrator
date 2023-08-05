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
package org.springframework.sbm.java.migration.visitor;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveImplementsVisitor extends JavaIsoVisitor<ExecutionContext> {

    private final String[] fqNames;
    private final J.ClassDeclaration scope;

    public RemoveImplementsVisitor(J.ClassDeclaration scope, String[] fqNames) {
        this.scope = scope;
        this.fqNames = fqNames;
    }

    @Override
    public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext p) {
        if (scope.isScope(classDecl)) {

            List<TypeTree> types = classDecl.getImplements();
            List<TypeTree> newTypes = new ArrayList<>();
            if (types != null) {
                for (TypeTree tr : types) {
                    JavaType type = tr.getType();
                    if (type instanceof JavaType.FullyQualified) {
                        JavaType.FullyQualified fqType = (JavaType.FullyQualified) type;
                        String typeFqName = fqType.getFullyQualifiedName();
                        if (Arrays.asList(fqNames).contains(typeFqName)) {
                            continue;
                        }
                    }
                    newTypes.add(tr);
                }

                for (String fqName : fqNames) {
                    maybeRemoveImport(fqName);
                }

                if (newTypes.size() != types.size()) {
                    if (newTypes.isEmpty()) {
                        classDecl = classDecl.withImplements(null);
                    } else {
                        classDecl = classDecl.withImplements(newTypes);
                    }
                }
            }
        }

        return classDecl;
    }


}
