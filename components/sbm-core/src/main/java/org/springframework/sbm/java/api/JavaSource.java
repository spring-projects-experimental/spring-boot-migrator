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

import org.springframework.sbm.java.impl.StaticFieldAccessTransformer;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.Recipe;
import org.openrewrite.java.tree.J;

import java.nio.file.Path;
import java.util.List;

public interface JavaSource extends ProjectResource {

    // FIXME: should JavaSorce really expose RewriteSourceFileHolder?!
    <T> RewriteSourceFileHolder<J.CompilationUnit> getResource();

    List<? extends Type> getTypes();

    List<String> getReferencedTypes();

    List<? extends Import> getImports();

    boolean hasImportStartingWith(String... importPattern);

    String getPackageName();

    boolean hasAnnotation(String annotation);

    void replaceConstant(StaticFieldAccessTransformer transform);

    List<Annotation> getAnnotations(String fqName, Expression scope);

    <T> void replaceLiteral(Class<T> klass, LiteralTransformer<T> t);

    /**
     * Apply the given {@code Recipe} to the wrapped compilation unit and reflect these changes inside the set of {@code JavaSource}s.
     * <p>
     * Be careful if the given {@code Recipe} affects more than the wrapped compilation unit YOU MUST CALL {@link JavaSourceSet.apply(..)}
     */
    void apply(Recipe recipe);

    /**
     * Retrieve the {@code Type} declared in this {@code JavaSource}.
     *
     * @param fqName the {@code Type}
     * @throws RuntimeException if the given {@code fqName} does not match any {@code Type}.
     */
    Type getType(String fqName);

    Path getSourceFolder();

    void renameMethodCalls(String methodMatchingPattern, String newName);

    String print();

    void removeUnusedImports();

    void replaceImport(String p, String replace);
}
