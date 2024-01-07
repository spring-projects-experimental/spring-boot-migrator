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
import org.springframework.sbm.project.resource.ProjectResourceSetHolder;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import lombok.RequiredArgsConstructor;
import org.openrewrite.java.tree.J;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaRefactoringFactoryImpl implements JavaRefactoringFactory {

    private final ProjectResourceSetHolder projectResourceSetHolder;
    private final ExecutionContext executionContext;

    @Override
    @Deprecated
    public JavaRefactoring createRefactoring(J.CompilationUnit compilationUnit) {
        JavaRefactoringImpl refactoring = new JavaRefactoringImpl(projectResourceSetHolder.getProjectResourceSet(), compilationUnit,
                                                                  executionContext);
        return refactoring;
    }

    @Override
    public JavaGlobalRefactoring createRefactoring() {
        JavaGlobalRefactoring refactoring = new JavaGlobalRefactoringImpl(projectResourceSetHolder.getProjectResourceSet(),
                                                                          executionContext);
        return refactoring;
    }

    @Override
    public JavaRefactoring createRefactoring(RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder) {
        JavaRefactoringImpl refactoring = new JavaRefactoringImpl(projectResourceSetHolder.getProjectResourceSet(), rewriteSourceFileHolder,
                                                                  executionContext);
        return refactoring;
    }
}
