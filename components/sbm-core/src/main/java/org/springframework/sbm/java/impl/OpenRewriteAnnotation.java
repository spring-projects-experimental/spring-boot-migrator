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
package org.springframework.sbm.java.impl;

import org.openrewrite.java.AddOrUpdateAnnotationAttribute;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import lombok.Getter;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.JavaType.FullyQualified;
import org.openrewrite.java.tree.TypeUtils;
import org.springframework.rewrite.parsers.JavaParserBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class OpenRewriteAnnotation implements Annotation {

    private final J.Annotation wrapped;
    private final JavaRefactoring refactoring;
    private final JavaParserBuilder javaParserBuilder;

    public OpenRewriteAnnotation(J.Annotation a, JavaRefactoring refactoring, JavaParserBuilder javaParserBuilder) {
        this.wrapped = a;
        this.refactoring = refactoring;
        this.javaParserBuilder = javaParserBuilder;
    }

    // FIXME: [FK] thoroughly test this method
    @Override
    public Map<String, Expression> getAttributes() {
        Map<String, Expression> attrs = new HashMap<>();
        List<org.openrewrite.java.tree.Expression> arguments = wrapped.getArguments();
        if (arguments != null) {
            for (var e : arguments) {
                if (e.getClass().isAssignableFrom(J.Assignment.class)) {
                    J.Assignment assign = (J.Assignment) e;
                    String key = assign.getVariable().printTrimmed();
                    Expression expr = new OpenRewriteExpression(e, refactoring, javaParserBuilder);
                    attrs.put(key, expr);
                } else {
                    attrs.put("value", new OpenRewriteExpression(e, refactoring, javaParserBuilder));
                }
            }
        }
        return attrs;
    }

    @Override
    public String getFullyQualifiedName() {
        @Nullable JavaType type = wrapped.getAnnotationType().getType();
        FullyQualified fullyQualified = TypeUtils.asFullyQualified(type);
        // FIXME: Do not return null bt throw Exception as this indicates a missing dependency.
        return fullyQualified == null ? null : fullyQualified.getFullyQualifiedName();
    }

    @Override
    public boolean hasAttribute(String attribute) {
        return getAttributes().containsKey(attribute);
    }

    @Override
    public void setAttribute(String attribute, Object value, Class valueType) {
        AddOrUpdateAnnotationAttribute recipe = new AddOrUpdateAnnotationAttribute(this.getFullyQualifiedName(), attribute, value.toString(), false);
        refactoring.refactor(recipe);
    }

    @Override
    public String toString() {
        return "OpenRewriteAnnotation(" + wrapped.print() + ")";
    }
}