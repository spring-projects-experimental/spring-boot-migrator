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
package org.springframework.sbm.java.migration.recipes;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.*;
import org.openrewrite.java.tree.J.MethodDeclaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ChangeMethodReturnTypeRecipe extends Recipe {
	
	final private Predicate<MethodDeclaration> methodCheck;
	final private String returnTypeExpression;
	final private String[] imports;

	@Override
	public String getDisplayName() {
		return "Change method return type";
	}

	@Override
	protected TreeVisitor<?, ExecutionContext> getVisitor() {
		return new JavaIsoVisitor<>() {

			@Override
			public MethodDeclaration visitMethodDeclaration(MethodDeclaration method, ExecutionContext p) {
				MethodDeclaration m = super.visitMethodDeclaration(method, p);
				if (methodCheck.test(m)) {
					@Nullable
					TypeTree returnType = m.getReturnTypeExpression();

					if (returnType instanceof J.Identifier) {
						JavaType t = returnType.getType();
						maybeRemoveImport(TypeUtils.asFullyQualified(t));
					} else {
						Set<String> foundTypes = find(returnType);
						for (String removeFqName : foundTypes) {
							maybeRemoveImport(removeFqName);
						}
					}
					
					for (String i : imports) {
						maybeAddImport(i);
					}
					return m.withReturnTypeExpression(TypeTree.build(returnTypeExpression).withPrefix(Space.build(" ", List.of())));
				}
				return m;
			}
			
		};
	}

	public static Set<String> find(J j) {
        JavaIsoVisitor<Set<String>> findVisitor = new JavaIsoVisitor<Set<String>>() {

            @Override
            public <N extends NameTree> N visitTypeName(N name, Set<String> ns) {
                N n = super.visitTypeName(name, ns);
                JavaType.FullyQualified asClass = TypeUtils.asFullyQualified(n.getType());
                if (asClass != null &&
                        getCursor().firstEnclosing(J.Import.class) == null) {
                    ns.add(asClass.getFullyQualifiedName());
                }
                return n;
            }

            @Override
            public J.FieldAccess visitFieldAccess(J.FieldAccess fieldAccess, Set<String> ns) {
                J.FieldAccess fa = super.visitFieldAccess(fieldAccess, ns);
                JavaType.FullyQualified targetClass = TypeUtils.asFullyQualified(fa.getTarget().getType());
                if (targetClass != null &&
                        fa.getName().getSimpleName().equals("class")) {
                    ns.add(targetClass.getFullyQualifiedName());
                }
                return fa;
            }
        };

        Set<String> ts = new HashSet<>();
        findVisitor.visit(j, ts);
        return ts;
    }


}
