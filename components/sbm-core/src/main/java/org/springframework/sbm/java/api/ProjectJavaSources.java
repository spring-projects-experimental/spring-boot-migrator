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
import org.openrewrite.java.tree.J;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectJavaSources {

    void apply(Recipe recipe);

    List<JavaSource> list();

    @Deprecated
    Stream<JavaSource> asStream();

    Stream<JavaSource> stream();

    List<RewriteSourceFileHolder<J.CompilationUnit>> find(Recipe find);

    void replaceType(String existingType, String withType);

    /**
     * Tries to match regex against imports of all Java sources.
     * <p>
     * {@code .*\\.foo\\.bar\\.TheClass} => matches {@code com.example.foo.bar.TheClass} or {@code a.foo.bar.TheClass}
     *
     * @param regex to match against import statements.
     * @return true if any match was found, false otherwise
     */
    boolean hasImportStartingWith(String regex);

    Optional<? extends JavaSource> findJavaSourceDeclaringType(String fqName);

    List<MethodCall> findMethodCalls(String pattern);

    List<JavaSourceAndType> findTypesImplementing(String interfaceType);

    List<? extends JavaSource> findClassesUsingType(String type);
}
