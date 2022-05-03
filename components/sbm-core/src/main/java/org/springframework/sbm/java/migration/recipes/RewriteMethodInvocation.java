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

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.AddImport;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.MethodInvocation;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class RewriteMethodInvocation extends Recipe {
	
	final private Predicate<MethodInvocation> checkMethodInvocation;
	final private Transformer transformer;

	@Override
	public String getDisplayName() {
		return "Rewritre method invocation";
	}
	
	@Override
	protected TreeVisitor<?, ExecutionContext> getVisitor() {
		return new JavaVisitor<ExecutionContext>() {

			@Override
			public J visitMethodInvocation(MethodInvocation method, ExecutionContext p) {
				MethodInvocation m = (MethodInvocation) super.visitMethodInvocation(method, p);
				if (checkMethodInvocation.test(m)) {
					return transformer.transform(this, m, this::addImport);
				}
				return m;
			}
			
			private void addImport(String fqName) {
		        AddImport<ExecutionContext> op = new AddImport<>(fqName, null, false);
		        if (!getAfterVisit().contains(op)) {
		            doAfterVisit(op);
		        }
			}
		};
	}

	public static Predicate<MethodInvocation> methodInvocationMatcher(String signature) {
		MethodMatcher methodMatcher = new MethodMatcher(signature);
		return mi -> methodMatcher.matches(mi);
	}
	
	public interface Transformer {
		
		J transform(JavaVisitor<ExecutionContext> visitor, MethodInvocation currentInvocation, Consumer<String> addImport);
		
	}
	
	public static RewriteMethodInvocation renameMethodInvocation(Predicate<MethodInvocation> matcher, String newName, String newType) {
		JavaType type = JavaType.buildType(newType);
		return new RewriteMethodInvocation(matcher, (v, m, a) -> {
			return m
					.withName(m.getName().withSimpleName(newName))
					.withSelect(m.getSelect().withType(type))
					.withMethodType(m.getMethodType());
		});
	}

}
