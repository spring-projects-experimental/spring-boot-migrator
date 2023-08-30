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
package org.springframework.sbm.support.openrewrite.java;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaCoordinates;
import org.openrewrite.template.SourceTemplate;

import java.util.List;
import java.util.function.Supplier;

public class AddOrReplaceAnnotationAttribute extends JavaIsoVisitor<ExecutionContext> {

    private final J.Annotation targetAnnotation;
    private final String attribute;
    private final Object value;
    private final Class valueType;
    private final Supplier<JavaParser> javaParserSupplier;

    public AddOrReplaceAnnotationAttribute(Supplier<JavaParser> javaParserSupplier, J.Annotation targetAnnotation, String attribute, Object value, Class valueType) {
        this.targetAnnotation = targetAnnotation;
        this.attribute = attribute.trim();
        this.value = value;
        this.valueType = valueType;
        this.javaParserSupplier = javaParserSupplier;
    }

    @Deprecated(forRemoval = true)
    public AddOrReplaceAnnotationAttribute(J.Annotation targetAnnotation, String attribute, Object value, Class valueType) {
        this.targetAnnotation = targetAnnotation;
        this.attribute = attribute.trim();
        this.value = value;
        this.valueType = valueType;
        javaParserSupplier = () -> JavaParser.fromJavaVersion().build();
    }

    @Override
    public J.Annotation visitAnnotation(J.Annotation annotation, ExecutionContext executionContext) {
        if (!targetAnnotation.getId().equals(annotation.getId())) {
            return super.visitAnnotation(annotation, executionContext);
        }

        String templateString = renderTemplateString(annotation);

        SourceTemplate<J, JavaCoordinates> template = JavaTemplate.builder(() -> getCursor(), templateString).javaParser(javaParserSupplier).build();
        return annotation.withTemplate(template, annotation.getCoordinates().replace());
    }

    private String renderTemplateString(J.Annotation annotation) {
        boolean attributeHandled = false;
        List<Expression> annotationArguments = annotation.getArguments();

        StringBuilder templateString = new StringBuilder("@" + annotation.getSimpleName());
        templateString.append("(");
        if (hasArguments(annotationArguments)) {
            for (Expression exp : annotationArguments) {
                if (exp.getClass().isAssignableFrom(J.Assignment.class)) {
                    J.Assignment assignment = (J.Assignment) exp;
                    if (assignment.getVariable().print().equals(attribute)) {
                        attributeHandled = true;
                        renderAttribute(templateString);
                    } else {
                        templateString.append(assignment.printTrimmed());
                    }
                    if (hasMoreElements(annotationArguments, exp)) {
                        templateString.append(", ");
                    }
                }
            }
            if (!attributeHandled) {
                templateString.append(", ");
            }
        }
        if (!attributeHandled) {
            renderAttribute(templateString);
        }
        templateString.append(")");
        return templateString.toString();
    }

    private boolean hasArguments(List<Expression> annotationArguments) {
        return annotationArguments != null && !annotationArguments.isEmpty();
    }

    private void renderAttribute(StringBuilder templateString) {
        templateString.append(attribute.trim());
        templateString.append(" = ");
        templateString.append(renderValue(value, valueType));
    }

    private boolean hasMoreElements(List<Expression> annotationArguments, Expression exp) {
        return annotationArguments.indexOf(exp) < annotationArguments.size() - 1;
    }

    private String renderValue(Object value, Class valueType) {
        if (valueType == String.class) {
            return "\"" + value.toString().trim() + "\"";
        } else if (value == Character.class) {
            return "'" + value + "'";
        }
        return value.toString();
    }
}