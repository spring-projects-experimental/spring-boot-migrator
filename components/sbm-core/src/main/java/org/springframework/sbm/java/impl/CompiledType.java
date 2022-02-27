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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.api.*;
import org.openrewrite.Recipe;
import org.openrewrite.java.tree.JavaType;

import java.util.List;
import java.util.Optional;
import java.util.Set;


// TODO: fix this class!
public class CompiledType implements Type {
    private JavaType.Class type;

    public CompiledType(JavaType.Class type) {
        this.type = type;
    }

    @Override
    public String getSimpleName() {
        return null;
    }

    @Override
    public String getFullyQualifiedName() {
        return type.getFullyQualifiedName();
    }

    @Override
    public List<? extends Member> getMembers() {
        return null;
    }

    @Override
    public boolean hasAnnotation(String annotation) {
        return false;
    }

    @Override
    public List<Annotation> findAnnotations(String annotation) {
        return null;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return null;
    }

    @Override
    public void addAnnotation(String fqName) {

    }

    @Override
    public void removeAnnotation(String fqName) {

    }

    @Override
    public KindOfType getKind() {
        return null;
    }

    @Override
    public void removeImplements(String... fqNames) {

    }

    @Override
    public List<Method> getMethods() {
        return null;
    }

    @Override
    public void addMethod(String methodTemplate, Set<String> importTypes) {

    }

    @Override
    public boolean isTypeOf(String gqName) {
        return false;
    }

    @Override
    public List<? extends Type> getImplements() {
        return null;
    }

    @Override
    public Optional<? extends Type> getExtends() {
        return Optional.empty();
    }

    @Override
    public void removeAnnotation(Annotation annotation) {

    }

    @Override
    public void addAnnotation(String snippet, String annotationImport, String... otherImports) {

    }

    @Override
    public Annotation getAnnotation(String fqName) {
        return null;
    }

    @Override
    public void apply(Recipe r) {

    }

    @Override
    public boolean hasMethod(String methodPattern) {
        return false;
    }

    @Override
    public Method getMethod(String methodPattern) {
        return null;
    }

    @Override
    public void addMember(Visibility visibility, String type, String name) {

    }
}
