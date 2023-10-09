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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.VariableDeclarations.NamedVariable;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeTree;
import org.springframework.sbm.support.openrewrite.java.AddAnnotationVisitor;
import org.springframework.sbm.support.openrewrite.java.RemoveAnnotationVisitor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class OpenRewriteMember implements Member {

    boolean isOutdated;

    private final UUID variableDeclId;

    private final RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder;

    private final NamedVariable namedVar;

    private final JavaRefactoring refactoring;
    private final JavaParserBuilder javaParserBuilder;

    public OpenRewriteMember(
            J.VariableDeclarations variableDecls, NamedVariable namedVar,
            RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder, JavaRefactoring refactoring, JavaParserBuilder javaParser) {
        this.variableDeclId = variableDecls.getId();
        this.namedVar = namedVar;
        this.rewriteSourceFileHolder = rewriteSourceFileHolder;
        this.refactoring = refactoring;
        this.javaParserBuilder = javaParser;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return getVariableDeclarations().getLeadingAnnotations()
                .stream()
                .map(la -> new OpenRewriteAnnotation(la, refactoring, javaParserBuilder))
                .collect(Collectors.toList());
    }

    @Override
    public Annotation getAnnotation(String annotation) {
        if (annotation == null || annotation.isBlank())
            throw new IllegalArgumentException("Given annotation was empty.");
        return getVariableDeclarations().getLeadingAnnotations()
                .stream()
                .filter(a -> {

                    JavaType type1 = a.getType();
                    if (type1.getClass().isAssignableFrom(JavaType.Unknown.class)) {
                        log.error("Could not get Type for annotation: '" + annotation + "'.");
                        return false;
                    } else if (type1.getClass().isAssignableFrom(JavaType.Class.class)) {
                        JavaType.Class type = (JavaType.Class) a.getType();
                        if (type == null) {
                            String simpleName = a.getSimpleName();
                            log.error("Could not get Type for annotation: '" + simpleName + "' while comparing with '" + annotation + "'.");
                            return false;
                        }
                        String fullyQualifiedName = type.getFullyQualifiedName();
                        return annotation.equals(fullyQualifiedName);
                    } else {
                        log.error("Unknown JavaType type '" + type1.getClass() + "'");
                        return false;
                    }

                })
                .findFirst()
                .map(a -> Wrappers.wrap(a, refactoring, javaParserBuilder))
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAnnotation(String annotationFqName) {
        return getAnnotation(annotationFqName) != null;
    }

    @Override
    public void addAnnotation(String fqName) {
        String snippet = "@" + fqName.substring(fqName.lastIndexOf('.') + 1);
        AddAnnotationVisitor addAnnotationVisitor = new AddAnnotationVisitor(() -> javaParserBuilder.getBuilder(), getVariableDeclarations(), snippet, fqName);
        refactoring.refactor(rewriteSourceFileHolder, addAnnotationVisitor);
    }

    @Override
    public void addAnnotation(String snippet, String annotationImport, String... otherImports) {
        AddAnnotationVisitor visitor = new AddAnnotationVisitor(() -> javaParserBuilder, getVariableDeclarations(), snippet, annotationImport, otherImports);
        refactoring.refactor(rewriteSourceFileHolder, visitor);
    }

    @Override
    public String getTypeFqName() {
        J.VariableDeclarations variableDeclarations = getVariableDeclarations();
        JavaType.@Nullable FullyQualified typeAsClass = variableDeclarations.getTypeAsFullyQualified();
        if (typeAsClass != null) {
            return typeAsClass.getFullyQualifiedName();
        }
        TypeTree typeExpr = variableDeclarations.getTypeExpression();
        JavaType type = typeExpr.getType();
        if (type != null) {
            if (type instanceof JavaType.FullyQualified) {
                return ((JavaType.FullyQualified) type).getFullyQualifiedName();
            }
        }
        return typeExpr.printTrimmed();
    }

    @Override
    public String getName() {
        return namedVar.getSimpleName();
    }

    @Override
    public void removeAnnotation(Annotation annotation) {
        // TODO: Maybe replace RemoveAnnotationVisitor with OpenRewrite's recipe
        RemoveAnnotationVisitor removeAnnotationRecipe = new RemoveAnnotationVisitor(getVariableDeclarations(), annotation.getFullyQualifiedName());
        refactoring.refactor(rewriteSourceFileHolder, removeAnnotationRecipe);
    }

    private J.VariableDeclarations getVariableDeclarations() {
        return rewriteSourceFileHolder.getSourceFile().getClasses().stream()
                .flatMap(cd -> Utils.getFields(cd).stream())
                .filter(vd -> vd.getId().equals(variableDeclId))
                .findAny()
                .orElseThrow();
    }
}
