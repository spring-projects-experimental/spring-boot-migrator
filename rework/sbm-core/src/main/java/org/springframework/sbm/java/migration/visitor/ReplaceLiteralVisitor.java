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
package org.springframework.sbm.java.migration.visitor;

import org.springframework.sbm.java.api.LiteralTransformer;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J.Literal;

import java.util.Objects;

public class ReplaceLiteralVisitor<T> extends JavaIsoVisitor<ExecutionContext> {

    private final LiteralTransformer<T> t;
    private final Class<T> klass;

    public ReplaceLiteralVisitor(Class<T> klass, LiteralTransformer<T> t) {
        this.klass = klass;
        this.t = t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Literal visitLiteral(Literal literal, ExecutionContext p) {
        if (literal.getValue() != null && klass.isAssignableFrom(literal.getValue().getClass())) {
            T oldValue = (T) literal.getValue();
            T newValue = t.transform(oldValue);
            if (!Objects.equals(oldValue, newValue)) {
                return literal.withValue(newValue).withValueSource(createValueSource(newValue));
            }
        }
        return literal;
    }

    private String createValueSource(T value) {
        if (klass == String.class) {
            return "\"" + value.toString() + "\"";
        } else if (klass == Character.class || klass == char.class) {
            return "'" + value.toString() + "'";
        } else {
            return value.toString();
        }
    }

}
