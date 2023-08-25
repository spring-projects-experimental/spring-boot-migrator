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
package org.springframework.sbm.java.api;

import java.util.List;

public interface Member {

    void addAnnotation(String annotation);

    List<Annotation> getAnnotations();

    Annotation getAnnotation(String annotation);

    /**
     * Check if member is annotated with {@code annotationFqName}.
     *
     * @param annotationFqName the fully qualified name of the annotation
     */
    boolean hasAnnotation(String annotationFqName);

    void addAnnotation(String snippet, String annotationImport, String... otherImports);

    String getTypeFqName();

    String getName();

    /**
     * Remove {@code annotation} from the member.
     */
    void removeAnnotation(Annotation annotation);
}
