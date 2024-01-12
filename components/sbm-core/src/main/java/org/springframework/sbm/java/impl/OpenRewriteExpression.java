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

import org.springframework.sbm.java.refactoring.JavaRefactoring;
import lombok.Getter;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.springframework.rewrite.parsers.JavaParserBuilder;

@Getter
public class OpenRewriteExpression implements org.springframework.sbm.java.api.Expression {

    private final Expression wrapped;
    private final JavaRefactoring refactoring;
    private final JavaParserBuilder javaParserBuilder;
    public OpenRewriteExpression(Expression e, JavaRefactoring refactoring, JavaParserBuilder javaParserBuilder) {
        this.wrapped = e;
        this.refactoring = refactoring;
        this.javaParserBuilder = javaParserBuilder;
    }

    @Override
    public String toString() {
        return "OpenRewriteExpression(" + wrapped.print() + ")";
    }

    // TODO: provide method for getting variable, assignment and expression, valueSource and value
    @Override
    public String print() {
        if (wrapped.getClass().isAssignableFrom(J.Literal.class)) {
            if (((J.Literal) wrapped).getValue().getClass().isAssignableFrom(String.class)) {
                return ((J.Literal) wrapped).getValueSource();
            }
        }
        if (wrapped.getClass().isAssignableFrom(J.Assignment.class)) {
            return wrapped.print();
        }
        return wrapped.print();
    }

    @Override
    public String printAssignment() {
        if (wrapped.getClass().isAssignableFrom(J.Literal.class)) {
            if (((J.Literal) wrapped).getValue().getClass().isAssignableFrom(String.class)) {
                return ((J.Literal) wrapped).getValueSource();
            }
        }
        if (wrapped.getClass().isAssignableFrom(J.Assignment.class)) {
            J.Assignment assign = (J.Assignment) wrapped;
            Expression elem = assign.getAssignment();
            if (elem.getClass().isAssignableFrom(J.Literal.class)) {
                J.Literal literal = (J.Literal) elem;
                return literal.getValueSource();
            }
            return ((J.Assignment) wrapped).getAssignment().print();
        }
        return wrapped.print();
    }

    @Override
    public String printAssignmentValue() {
        if (wrapped.getClass().isAssignableFrom(J.Literal.class)) {
            if (((J.Literal) wrapped).getValue().getClass().isAssignableFrom(String.class)) {
                return ((J.Literal) wrapped).getValue().toString();
            }
        }
        if (wrapped.getClass().isAssignableFrom(J.Assignment.class)) {
            J.Assignment assign = (J.Assignment) wrapped;
            Expression elem = assign.getAssignment();
            if (elem.getClass().isAssignableFrom(J.Literal.class)) {
                J.Literal literal = (J.Literal) elem;
                return literal.getValue().toString();
            }
            return ((J.Assignment) wrapped).getAssignment().print();
        }
        return wrapped.print();
    }

    @Override
    public String printVariable() {
        if (wrapped.getClass().isAssignableFrom(J.Literal.class)) {
            if (((J.Literal) wrapped).getValue().getClass().isAssignableFrom(String.class)) {
                return ((J.Literal) wrapped).getValue().toString();
            }
        }
        if (wrapped.getClass().isAssignableFrom(J.Assignment.class)) {
            return ((J.Assignment) wrapped).getVariable().print();
        }
        return wrapped.print();
    }

    public org.springframework.sbm.java.api.Expression getAssignmentRightSide() {
        if (wrapped.getClass().isAssignableFrom(J.Assignment.class)) {
            J.Assignment assign = (J.Assignment) wrapped;
            Expression elem = assign.getAssignment();
            return Wrappers.wrap(elem, refactoring, javaParserBuilder);
        }
        // TODO: throw Exception or return Optional.empty()
        return null;
    }
}