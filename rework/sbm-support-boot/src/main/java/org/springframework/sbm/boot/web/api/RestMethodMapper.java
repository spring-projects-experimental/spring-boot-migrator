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

package org.springframework.sbm.boot.web.api;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.springframework.http.MediaType;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;
import org.springframework.sbm.java.api.Method;
import org.springframework.sbm.java.impl.OpenRewriteExpression;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.springframework.sbm.boot.web.api.SpringRestMethodAnnotation.*;

class RestMethodMapper {

    public static final List<String> SPRING_REST_METHOD_ANNOTATIONS = SpringRestMethodAnnotation.getAll().stream().map(SpringRestMethodAnnotation::getFullyQualifiedName)
            .collect(Collectors.toList());

    public RestMethod map(Method method) {
        return mapToRestMethod(method);
    }

    private RestMethod mapToRestMethod(Method method) {
        RestMethod.RestMethodBuilder builder = RestMethod.builder();
        Annotation annotation = getRestMethodAnnotation(method);
        builder.methodReference(method);
        buildRestMethod(method, annotation, builder);
        buildRequestBodyType(method, builder);
        return builder.build();
    }

    private void buildRequestBodyType(Method method, RestMethod.RestMethodBuilder builder) {
        builder.requestBodyParameterType(method.getParams().stream()
                                                 .filter(p -> p.containsAnnotation(Pattern.compile(".*\\.RequestBody")))
                                                 .findFirst());
    }

    private Annotation getRestMethodAnnotation(Method method) {
        return method.getAnnotations().stream()
                .filter(methodAnnotations -> SPRING_REST_METHOD_ANNOTATIONS.stream().anyMatch(fqName -> methodAnnotations.getFullyQualifiedName().equals(fqName)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("The provided method '%s' has no Spring Request Mapping annpotation.", method.getName())));
    }

    private void buildRestMethod(Method method, Annotation annotation, RestMethod.RestMethodBuilder builder) {
        String name = "name";
        if(annotation.hasAttribute(name)) {
            mapNameAttribute(annotation, builder, name);
        }
        extractExistingAnnotationAttribute(annotation, "value", values -> builder.path(values));
        extractExistingAnnotationAttribute(annotation, "path", paths -> builder.path(paths));
        extractExistingAnnotationAttribute(annotation, "params", params -> builder.params(params));
        extractExistingAnnotationAttribute(annotation, "consumes", consumes -> builder.consumes(consumes));
        extractExistingAnnotationAttribute(annotation, "produces", produces -> builder.produces(produces));
        extractExistingAnnotationAttribute(annotation, "headers", headers -> builder.headers(headers));
        mapMethodAttribute(annotation, builder);

        if(!annotation.hasAttribute("consumes")) {
            builder.consumes(List.of(MediaType.APPLICATION_JSON_VALUE));
        }
        if(!annotation.hasAttribute("produces")) {
            builder.produces(List.of(MediaType.APPLICATION_JSON_VALUE));
        }

        String fullyQualifiedReturnType = ((JavaType.Class) method
                .getMethodDecl()
                .getReturnTypeExpression()
                .getType()).getFullyQualifiedName();

        builder.returnType(fullyQualifiedReturnType);
    }

    private void extractExistingAnnotationAttribute(Annotation annotation, String attribute, Consumer<List<String>> valueConsumer) {
        if(annotation.hasAttribute(attribute)) {
            extractAnnotationAttribute(annotation, attribute, valueConsumer);
        }
    }

    private void extractAnnotationAttribute(Annotation annotation, String attribute, Consumer<List<String>> consumer) {
        Expression expression = annotation.getAttributes().get(attribute);
        List<String> values = handleExpression(expression);
        consumer.accept(values);
    }

    private void mapNameAttribute(Annotation annotation, RestMethod.RestMethodBuilder builder, String name) {
        Expression value = annotation.getAttributes().get(name);
        String s = value.getAssignmentRightSide().printVariable();
        builder.name(s);
    }

    private void mapMethodAttribute(Annotation annotation, RestMethod.RestMethodBuilder builder) {
        if(annotation.hasAttribute("method")) {
            List<String> methods = handleExpression(annotation.getAttribute("method"));
            builder.method(methods.stream().map(m -> RequestMethod.valueOf(m)).collect(Collectors.toList()));
        }
        findAndMapMethodAttribute(annotation, builder, GET_MAPPING.getFullyQualifiedName(), RequestMethod.GET);
        findAndMapMethodAttribute(annotation, builder, POST_MAPPING.getFullyQualifiedName(), RequestMethod.POST);
        findAndMapMethodAttribute(annotation, builder, PUT_MAPPING.getFullyQualifiedName(), RequestMethod.PUT);
        findAndMapMethodAttribute(annotation, builder, DELETE_MAPPING.getFullyQualifiedName(), RequestMethod.DELETE);
        findAndMapMethodAttribute(annotation, builder, PATCH_MAPPING.getFullyQualifiedName(), RequestMethod.PATCH);
    }

    private void findAndMapMethodAttribute(Annotation annotation, RestMethod.RestMethodBuilder builder, String requestNapping, RequestMethod requestMethod) {
        if(annotation.getFullyQualifiedName().equals(requestNapping)) {
            builder.method(List.of(requestMethod));
        }
    }

    private List<String> handleLiteral(org.openrewrite.java.tree.Expression wrapped) {
        J.Literal literal = J.Literal.class.cast(wrapped);
        return  List.of((String)literal.getValue());
    }

    @NotNull
    private List<String> handleArray(org.openrewrite.java.tree.Expression expression) {
        J.NewArray array = J.NewArray.class.cast(expression);
        List<String> elements = array
                .getInitializer()
                .stream()
                .map(e -> this.handleExpression(e))
                .filter(not(List::isEmpty))
                .map(s -> s.get(0))
                .collect(Collectors.toList());
        return elements;
    }

    private List<String> handleExpression(Expression value) {
        OpenRewriteExpression openRewriteExpression = OpenRewriteExpression.class.cast(value);
        org.openrewrite.java.tree.Expression wrapped = openRewriteExpression.getWrapped();
        return handleExpression(wrapped);
    }

    private List<String> handleExpression(org.openrewrite.java.tree.Expression expression) {
        List<String> elements = new ArrayList<>();
        if(J.Literal.class.isInstance(expression)) {
            elements = handleLiteral(expression);
        }
        else if(J.NewArray.class.isInstance(expression)) {
            elements = handleArray(expression);
        }
        else if(J.Assignment.class.isInstance(expression)) {
            J.Assignment assignment = J.Assignment.class.cast(expression);
            org.openrewrite.java.tree.Expression assignment1 = assignment.getAssignment();
            if(J.NewArray.class.isInstance(assignment1)) {
                elements = handleArray(assignment1);
            }
            else if(J.FieldAccess.class.isInstance(assignment.getAssignment())) {
                elements = List.of(staticFieldReference(assignment.getAssignment()));
            }
        } else if(J.FieldAccess.class.isInstance(expression)) {
            elements = List.of(staticFieldReference(expression));
        }
        return elements;
    }

    private String staticFieldReference(org.openrewrite.java.tree.Expression expression) {
        J.FieldAccess fieldAccess = J.FieldAccess.class.cast(expression);
        String fqName = ((JavaType.Class) fieldAccess.getTarget().getType()).getFullyQualifiedName();
        Class<?> aClass = null;
        String methodName = fieldAccess.getName().getSimpleName();
        try {
            aClass = Class.forName(fqName);
            Object o = aClass.getDeclaredField(methodName).get(null);
            return o.toString();
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Exception while attempting to resolve the value for constant '%s.%s'", aClass, methodName));
        }
    }
}
