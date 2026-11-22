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
package org.springframework.sbm.boot.upgrade_27_30;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.TypeTree;

/**
 * Rewrites a specific fully-qualified type reference in imports and type usages directly on AST level,
 * without requiring the type to be resolvable on the compile classpath.
 *
 * @author Kishore S
 */
@Getter
@Setter
@NoArgsConstructor
public class MigrateTypeReferences extends Recipe {

    private String oldFullyQualifiedTypeName;
    private String newFullyQualifiedTypeName;

    public MigrateTypeReferences(String oldFullyQualifiedTypeName, String newFullyQualifiedTypeName) {
        this.oldFullyQualifiedTypeName = oldFullyQualifiedTypeName;
        this.newFullyQualifiedTypeName = newFullyQualifiedTypeName;
    }

    @Override
    public String getDisplayName() {
        return "Migrate type references from " + oldFullyQualifiedTypeName + " to " + newFullyQualifiedTypeName;
    }

    @Override
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.Import visitImport(J.Import _import, ExecutionContext ctx) {
                J.Import i = super.visitImport(_import, ctx);
                String typeName = i.getTypeName();
                if (oldFullyQualifiedTypeName != null && oldFullyQualifiedTypeName.equals(typeName)) {
                    TypeTree newQualid = TypeTree.build(newFullyQualifiedTypeName).withPrefix(Space.format(" "));
                    i = i.withQualid((J.FieldAccess) newQualid);
                }
                return i;
            }

            @Override
            public J.FieldAccess visitFieldAccess(J.FieldAccess fieldAccess, ExecutionContext ctx) {
                J.FieldAccess f = super.visitFieldAccess(fieldAccess, ctx);
                if (getCursor().firstEnclosing(J.Import.class) != null) {
                    return f;
                }
                if (getCursor().getParent() != null && getCursor().getParent().getValue() instanceof J.FieldAccess) {
                    return f;
                }
                String printed = f.printTrimmed(getCursor());
                if (oldFullyQualifiedTypeName != null && oldFullyQualifiedTypeName.equals(printed)) {
                    TypeTree newTree = TypeTree.build(newFullyQualifiedTypeName).withPrefix(f.getPrefix());
                    if (newTree instanceof J.FieldAccess) {
                        return (J.FieldAccess) newTree;
                    }
                }
                return f;
            }
        };
    }
}
