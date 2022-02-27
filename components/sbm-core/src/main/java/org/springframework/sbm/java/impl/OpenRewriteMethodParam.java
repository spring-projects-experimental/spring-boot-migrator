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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.MethodParam;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.java.AddAnnotationVisitor;
import org.springframework.sbm.support.openrewrite.java.RemoveAnnotationVisitor;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Statement;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class OpenRewriteMethodParam implements MethodParam {

    private final RewriteSourceFileHolder<J.CompilationUnit> sourceFile;

    private final Statement wrappedMethodParam;

    private final JavaRefactoring refactoring;

    public OpenRewriteMethodParam(RewriteSourceFileHolder<J.CompilationUnit> sourceFile, Statement statement, JavaRefactoring refactoring) {
        wrappedMethodParam = statement;
        this.sourceFile = sourceFile;
        this.refactoring = refactoring;
    }

    @Override
    public List<Annotation> getAnnotations() {
        if (wrappedMethodParam instanceof J.VariableDeclarations) {
            return ((J.VariableDeclarations) wrappedMethodParam).getLeadingAnnotations().stream()
                    .map(a -> new OpenRewriteAnnotation(a, refactoring))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public void removeAnnotation(Annotation annotation) {
        RemoveAnnotationVisitor removeAnnotationRecipe = new RemoveAnnotationVisitor(wrappedMethodParam, annotation.getFullyQualifiedName());
        refactoring.refactor(sourceFile, removeAnnotationRecipe);
    }

    @Override
    public void addAnnotation(String snippet, String annotationImport, String... otherImports) {
        JavaParser javaParser = JavaParserFactory.getCurrentJavaParser();
        AddAnnotationVisitor visitor = new AddAnnotationVisitor(() -> javaParser, wrappedMethodParam, snippet, annotationImport, otherImports);
        refactoring.refactor(sourceFile, visitor);
    }

    @Override
    public boolean containsAnnotation(Pattern annotationPattern) {
        if (wrappedMethodParam instanceof J.VariableDeclarations) {
            return ((J.VariableDeclarations) wrappedMethodParam).getLeadingAnnotations()
                    .stream()
                    .anyMatch(a -> {
                        JavaType.Class type = (JavaType.Class) a.getAnnotationType().getType();
                        if (type == null) {
                            log.warn("Could not resolve Type for annotation: '" + a.getSimpleName() + ".");
                            return false;
                        }
                        return annotationPattern.matcher(type.getFullyQualifiedName()).matches();
                    });
        }
        return false;
    }


}
