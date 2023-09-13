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

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.util.Assert;

public class Wrappers {

    public static org.springframework.sbm.java.api.Expression wrap(Expression e, JavaRefactoring refactoring, JavaParserBuilder javaParserBuilder) {
        return new OpenRewriteExpression(e, refactoring, javaParserBuilder);
    }

    public static Expression unwrap(org.springframework.sbm.java.api.Expression e) {
        Assert.isInstanceOf(OpenRewriteExpression.class, e);
        return ((OpenRewriteExpression) e).getWrapped();
    }

    public static Annotation wrap(J.Annotation a, JavaRefactoring refactoring, JavaParserBuilder javaParserBuilder) {
        return new OpenRewriteAnnotation(a, refactoring, javaParserBuilder);
    }

    public static J.Annotation unwrap(Annotation annotation) {
        Assert.isInstanceOf(OpenRewriteAnnotation.class, annotation);
        return ((OpenRewriteAnnotation) annotation).getWrapped();
    }
}
