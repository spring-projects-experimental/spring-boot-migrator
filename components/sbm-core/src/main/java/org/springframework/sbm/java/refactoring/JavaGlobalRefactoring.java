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
package org.springframework.sbm.java.refactoring;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.tree.J;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;

import java.util.List;

public interface JavaGlobalRefactoring {
    void refactor(Recipe... recipes);

    /**
     * Run provided {@code Visitor}s in given order against all {@code SourceFile}s of type {@code T}.
     */
    void refactor(JavaVisitor<ExecutionContext>... visitors);

    /**
     * Run provided {@code Recipe}s to search for {@code SourceFile}s of type {@code T}.
     */
    List<RewriteSourceFileHolder<J.CompilationUnit>> find(Recipe recipe);
}
