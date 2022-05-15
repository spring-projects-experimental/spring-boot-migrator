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
package org.springframework.sbm.java.api;

import org.openrewrite.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Type {

    String getSimpleName();

    String getFullyQualifiedName();

    List<? extends Member> getMembers();

    boolean hasAnnotation(String annotation);

    List<Annotation> findAnnotations(String annotation);

    List<Annotation> getAnnotations();

    void addAnnotation(String fqName);

    void removeAnnotation(String fqName);

    KindOfType getKind();

    void removeImplements(String... fqNames);

    List<Method> getMethods();

    void addMethod(String methodTemplate, Set<String> importTypes);

    boolean isTypeOf(String gqName);

    List<? extends Type> getImplements();

    Optional<? extends Type> getExtends();

    void removeAnnotation(Annotation annotation);

    void addAnnotation(String snippet, String annotationImport, String... otherImports);

    Annotation getAnnotation(String fqName);

    void apply(Recipe r);

    boolean hasMethod(String methodPattern);

    Method getMethod(String methodPattern);

    void addMember(Visibility visibility, String type, String name);
}
