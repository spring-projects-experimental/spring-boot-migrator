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

import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.search.FindMethods;
import org.openrewrite.java.search.UsesType;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.java.exceptions.UnresolvedTypeException;
import org.springframework.sbm.java.filter.JavaSourceListFinder;
import org.springframework.sbm.java.refactoring.JavaGlobalRefactoring;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ProjectJavaSourcesImpl implements ProjectJavaSources {
    private ProjectResourceSet projectResourceSet;
    private JavaGlobalRefactoring globalRefactoring;

    public ProjectJavaSourcesImpl(ProjectResourceSet filteredResources, JavaGlobalRefactoring globalRefactoring) {
        super();
        projectResourceSet = filteredResources;
        this.globalRefactoring = globalRefactoring;
    }

    @Override
    public void apply(Recipe recipe) {
        globalRefactoring.refactor(recipe);
    }

    @Override
    public List<JavaSource> list() {
        return new JavaSourceListFinder().apply(projectResourceSet);
    }

    @Override
    @Deprecated
    public Stream<JavaSource> asStream() {
        return stream();
    }

    @Override
    public Stream<JavaSource> stream() {
        return new JavaSourceListFinder().apply(projectResourceSet).stream();
    }

    @Override
    public List<RewriteSourceFileHolder<J.CompilationUnit>> find(Recipe findCompilationUnitContainingType) {
        return globalRefactoring.find(findCompilationUnitContainingType);
    }

    @Override
    public void replaceType(String existingType, String withType) {
        ChangeType visitor = new ChangeType(existingType, withType, false);
        globalRefactoring.refactor(visitor);
    }

    @Override
    public boolean hasImportStartingWith(String regex) {
        return list().stream()
                .anyMatch(js -> js.hasImportStartingWith(regex));
    }

    @Override
    public Optional<? extends JavaSource> findJavaSourceDeclaringType(String fqName) {
        return list().stream()
                .filter(js -> {
                    return js.getTypes().stream()
                            .anyMatch(t -> fqName.equals(t.getFullyQualifiedName()));
                })
                .findFirst();
    }

    /**
     * Find method calls matching given {@code methodPattern}.
     *
     * @param methodPattern the pattern for the method like {@code com.example.TheType theMethod(com.example.Arg1, com.example.Arg2)}
     */
    @Override
    public List<MethodCall> findMethodCalls(String methodPattern) {
        List<MethodCall> matches = new ArrayList<>();
        FindMethods findMethods = new FindMethods(methodPattern,true);
        MethodMatcher methodMatcher = new MethodMatcher(methodPattern);
        find(findMethods).stream()
                .map(m -> list().stream().filter(js -> js.getResource().getSourceFile().getId().equals(m.getSourceFile().getId())).findFirst().get())
                .map(m -> new MethodCall(m, methodMatcher))
                .forEach(matches::add);
        return matches;
    }

    @Override
    public List<JavaSourceAndType> findTypesImplementing(String type) {
        List<JavaSourceAndType> matches = new ArrayList<>();
        this.list().forEach(js -> {
            js.getResource().getSourceFile().getClasses().stream()
                    .filter(c -> hasTypeImplementing(c, type))
                    .map(c -> {
                        Type matchingType = getTypeForClassDecl(c);
                        return new JavaSourceAndType(js, matchingType);
                    })
                    .forEach(matches::add);
        });
        return matches;
    }

    @Override
    public List<? extends JavaSource> findClassesUsingType(String type) {
        UsesType<ExecutionContext> usesType = new UsesType<>(type, true);
        GenericOpenRewriteRecipe<UsesType<ExecutionContext>> recipe = new GenericOpenRewriteRecipe<>(() -> usesType);
        return find(recipe).stream()
                .filter(RewriteSourceFileHolder.class::isInstance)
                .map(RewriteSourceFileHolder.class::cast)
                .filter(r -> r.getType().isAssignableFrom(J.CompilationUnit.class))
                .map(r -> (OpenRewriteJavaSource)r)
                .collect(Collectors.toList());
    }

    private boolean hasTypeImplementing(J.ClassDeclaration c, String type) {
        return c.getImplements() != null &&
                c.getImplements()
                        .stream()
                        .anyMatch(intaface -> {
                                    JavaType.FullyQualified fullyQualified = TypeUtils.asFullyQualified(
                                            intaface.getType()
                                    );
                                    if (fullyQualified == null) {

                                        throw new UnresolvedTypeException(
                                                String.format("Could not calculate if class '%s' implements an " +
                                                        "interface compatible to '%s'. Type of interface '%s' could not" +
                                                        " be resolved and was '%s'", c.getType(), type, intaface,
                                                        intaface.getType().toString())
                                        );

                                    }
                                    return fullyQualified
                                            .getFullyQualifiedName()
                                            .equals(type);
                                }
                        );

    }

    private Type getTypeForClassDecl(J.ClassDeclaration c) {
        return list().stream()
                .flatMap(js -> js.getTypes().stream())
                .filter(t -> t.getFullyQualifiedName().equals(c.getType().getFullyQualifiedName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Could not find type for '" + c.getType().getFullyQualifiedName() + "'"));
    }

}
