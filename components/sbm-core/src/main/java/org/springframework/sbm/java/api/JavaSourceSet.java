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

import org.openrewrite.Recipe;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Deprecated
public interface JavaSourceSet {

    JavaSource addJavaSource(Path projectRoot, Path sourceFolder, String sourceCode, String packageName);

    void replaceType(String type, String withType);

    Optional<Path> getBaseResourcesLocation(Path relativeTo);

    JavaSourceLocation getBaseJavaSourceLocation();

    JavaSourceLocation getBaseTestJavaSourceLocation();

    void removeUnusedImports();

    boolean hasImportStartingWith(String... value);

    List<? extends JavaSource> asList();

    /**
     * Find the JavaSource containing the given type declaration in all source folders.
     *
     * @param fqName the fully qualified name of the type.
     * @return the {@code JavaSource} or Optional.empty()
     */
    Optional<? extends JavaSource> findJavaSourceDeclaringType(String fqName);

    /**
     * Apply the recipe to all java sources in the source set. If the recipe is to be applied to only one java source use {@link JavaSource#apply(Recipe)}
     *
     * @param recipe the recipe to apply
     */
    void apply(Recipe recipe);

    List<JavaSourceAndType> findTypesImplementing(String jpaRepositoryInterface);

}
