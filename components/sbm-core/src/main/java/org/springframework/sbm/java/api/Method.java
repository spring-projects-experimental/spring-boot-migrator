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

import org.openrewrite.java.tree.J;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public interface Method {

    List<MethodParam> getParams();

    List<Annotation> getAnnotations();

    String getName();

    boolean containsAnnotation(Pattern annotationPattern);

    void removeAnnotation(Annotation annotation);

    void addAnnotation(String snippet, String annotationImport, String... otherImports);

    /**
     * Checks if method is annotated with annotation with {@code annotationFqName}.
     *
     * @param annotationFqName the fully qualified name of the annotation
     */
    boolean hasAnnotation(String annotationFqName);

    /**
     * Get the first annotation found by given fully qualified name.
     *
     * @param annotationFqName the fully qualified name of the annotation
     */
    Optional<Annotation> getAnnotation(String annotationFqName);

    Visibility getVisibility();

    String getReturnValue();

    void rename(String methodPattern, String newName);

    J.MethodDeclaration getMethodDecl();
}
