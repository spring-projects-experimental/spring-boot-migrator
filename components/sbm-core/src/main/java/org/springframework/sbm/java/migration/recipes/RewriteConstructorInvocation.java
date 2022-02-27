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
package org.springframework.sbm.java.migration.recipes;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.AddImport;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.NewClass;
import org.openrewrite.java.tree.TypeUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RewriteConstructorInvocation extends Recipe {
	
	final private Predicate<NewClass> checkConstructorInvocation;
	final private Transformer transformer;

	@Override
	public String getDisplayName() {
		return "Rewrite constructor invocation";
	}
	
	@Override
	protected TreeVisitor<?, ExecutionContext> getVisitor() {
		return new JavaVisitor<>() {

			
			@Override
			public J visitNewClass(NewClass newClass, ExecutionContext p) {
				NewClass n = (NewClass) super.visitNewClass(newClass, p);
				if (test(n)) {
					return transformer.transform(this, n, this::addImport);
				}
				return n;
			}
			
			private boolean test(NewClass n) {
				return checkConstructorInvocation.test(n);
			}

			private void addImport(String fqName) {
		        AddImport<ExecutionContext> op = new AddImport<>(fqName, null, false);
		        if (!getAfterVisit().contains(op)) {
		            doAfterVisit(op);
		        }
			}

		};
	}
	
	public static Predicate<NewClass> constructorMatcher(String typeFqName, String... parameterTypes) {
		return n -> {
			if (n.getConstructorType() != null 
					&& n.getConstructorType().getResolvedSignature() != null
					&& typeFqName.equals(n.getConstructorType().getDeclaringType().getFullyQualifiedName())) {
				String[] paramTypes = n.getConstructorType().getResolvedSignature().getParamTypes()
						.stream()
						.map(t -> TypeUtils.asFullyQualified(t).getFullyQualifiedName())
						.toArray(String[]::new);
				return Arrays.equals(parameterTypes, paramTypes);
			}
			return false;
		};
	}
	
	public static interface Transformer {
		
		J transform(JavaVisitor<ExecutionContext> visitor, NewClass n, Consumer<String> addImport);
		
	}



}
