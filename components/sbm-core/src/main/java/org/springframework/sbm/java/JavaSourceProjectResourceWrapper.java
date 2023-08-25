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
package org.springframework.sbm.java;

import org.openrewrite.ExecutionContext;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;
import org.openrewrite.java.tree.J;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaSourceProjectResourceWrapper implements ProjectResourceWrapper<OpenRewriteJavaSource> {

    private final JavaRefactoringFactory javaRefactoringFactory;
    private final JavaParserBuilder javaParserBuilder;

    private final ExecutionContext executionContext;

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return J.CompilationUnit.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass());
    }

    @Override
    public OpenRewriteJavaSource wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        J.CompilationUnit compilationUnit = J.CompilationUnit.class.cast(rewriteSourceFileHolder.getSourceFile());
        JavaRefactoring refactoring = javaRefactoringFactory.createRefactoring(compilationUnit);
        return new OpenRewriteJavaSource(rewriteSourceFileHolder.getAbsoluteProjectDir(), compilationUnit, refactoring, javaParserBuilder, executionContext);
    }
}
