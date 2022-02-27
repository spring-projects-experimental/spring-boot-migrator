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

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.sbm.java.impl.Utils;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.Identifier;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;

import java.util.Map;
import java.util.function.Supplier;

public class FindReplaceFieldAccessors extends Recipe {
	
	final private String findFqName;
	final private String replaceFqName;
	final private Map<String, String> mappings;
	
	final private String simpleReplaceFqName;
	private Supplier<JavaParser> javaParserSupplier;

	@Override
	public String getDisplayName() {
		return "Find field access for class " + findFqName + " and replace with field accesses from class " + replaceFqName; 
	}
	
	@JsonCreator
	public FindReplaceFieldAccessors(Supplier<JavaParser> parserSupplier, String findFqName, String replaceFqName, Map<String, String> mappings) {
		this.findFqName = findFqName;
		this.replaceFqName = replaceFqName;
		this.mappings = mappings;
		this.simpleReplaceFqName = replaceFqName == null ? null :  Utils.getSimpleName(replaceFqName);
		javaParserSupplier = parserSupplier;
	}

	@Override
	protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaVisitor<ExecutionContext>() {

            @Override
            public J visitFieldAccess(J.FieldAccess fieldAccess, ExecutionContext ctx) {
                J.FieldAccess fa = (J.FieldAccess) super.visitFieldAccess(fieldAccess, ctx);
               	Expression target = fa.getTarget();
                JavaType.FullyQualified asClass = TypeUtils.asFullyQualified(target.getType());
                
            	String replaceField = mappings.get(fa.getName().getSimpleName());
                if (asClass != null && asClass.getFullyQualifiedName().equals(findFqName) &&
                		replaceField != null) {
                	maybeRemoveImportAndParentTypeImports(findFqName);
                	maybeAddImport(replaceFqName);
                   	JavaType replaceType = JavaType.buildType(replaceFqName);
					fa = fa
                   			.withName(fa.getName().withName(replaceField))
                   			.withType(replaceType)
                   			.withTarget(Identifier.build(target.getId(), target.getPrefix(), target.getMarkers(), simpleReplaceFqName, replaceType));                   
                }
                return fa;
            }
            
            private void maybeRemoveImportAndParentTypeImports(String fqName) {
            	int idx = fqName.lastIndexOf('.');
            	String simpleName = idx > 0 && idx < fqName.length() - 1 ? fqName.substring(idx + 1) : fqName;
            	if (Character.isUpperCase(simpleName.charAt(0))) {
            		maybeRemoveImport(fqName);
            		maybeRemoveImportAndParentTypeImports(fqName.substring(0, idx));
            	}
            }
                        
			@Override
			public @Nullable J preVisit(J tree, ExecutionContext p) {
				// FIXME: remove parser passing
				getCursor().putMessage("java-parser", javaParserSupplier);
				return super.preVisit(tree, p);
			}
            
        };
	}


}
