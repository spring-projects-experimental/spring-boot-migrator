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
package org.springframework.sbm.jee.utils;

import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

/**
 * @author Vincent Botteman
 */
public final class AnnotationUtils {
    public static final String VALUE_ATTRIBUTE_NAME = "value";

    private AnnotationUtils() {
    }

    public static Optional<J.Literal> getAttributeValue(J.Annotation annotation, String attributeName) {
        if (CollectionUtils.isEmpty(annotation.getArguments())) {
            return Optional.empty();
        }

        for (Expression argument : annotation.getArguments()) {
            if (argument instanceof J.Assignment as) {
                J.Identifier variable = (J.Identifier) as.getVariable();
                if (variable.getSimpleName().equals(attributeName)) {
                    return Optional.of((J.Literal) as.getAssignment());
                }
            } else if (argument instanceof J.Literal literal && VALUE_ATTRIBUTE_NAME.equals(attributeName)) {
                return Optional.of(literal);
            }
        }

        return Optional.empty();
    }
}
