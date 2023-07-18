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
package org.springframework.sbm.build.api;

import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.openrewrite.Recipe;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface JavaSourceSet {

    boolean hasImportStartingWith(String... value);

    Optional<? extends JavaSource> findJavaSourceDeclaringType(String fqName);

    void apply(Recipe recipe);

    Stream<JavaSource> stream();

    List<JavaSource> list();

    @Deprecated
    JavaSource addJavaSource(Path projectRootDirectory, Path sourceFolder, String src, String packageName);

    List<JavaSource> addJavaSource(Path projectRoot, Path sourceFolder, String... sourceCodes);

    JavaSource addJavaSource(Path projectRootDirectory, String src, String packageName);

    void replaceType(String type, String withType);

    Optional<Path> getBaseResourcesLocation(Path relativeTo);

    JavaSourceLocation getJavaSourceLocation();

    Path getAbsolutePath();
}