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

import org.openrewrite.*;
import org.openrewrite.java.*;
import org.openrewrite.java.search.FindAnnotations;
import org.openrewrite.java.search.FindReferencedTypes;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Marker;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.java.migration.visitor.ReplaceLiteralVisitor;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.search.recipe.CommentJavaSearchResult;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OpenRewriteJavaSource extends RewriteSourceFileHolder<J.CompilationUnit> implements JavaSource {

    private final JavaRefactoring refactoring;
    private final JavaParser javaParser;

    public OpenRewriteJavaSource(Path absoluteProjectPath, J.CompilationUnit compilationUnit, JavaRefactoring refactoring, JavaParser javaParser) {
        super(absoluteProjectPath, compilationUnit);
        this.refactoring = refactoring;
        this.javaParser = javaParser;
    }

    @Deprecated
    public RewriteSourceFileHolder<J.CompilationUnit> getResource() {
        // FIXME: now is a RewriteSourceFileHolder<J.CompilationUnit>
        return this;
    }

    @Deprecated
    public J.CompilationUnit getCompilationUnit() {
        return getSourceFile();
    }

    /**
     * @return all types defined in this <code>JavaSource</code>.
     */
    @Override
    public List<OpenRewriteType> getTypes() {
        return getCompilationUnit().getClasses().stream()
                .map(cd -> new OpenRewriteType(cd, getResource(), refactoring, javaParser))
                .collect(Collectors.toList());
    }

    /**
     * @return list of fully qualified names of all referenced types
     */
    @Override
    public List<String> getReferencedTypes() {
        Set<JavaType.FullyQualified> fullyQualifiedNames = FindReferencedTypes.find(getCompilationUnit());
        return fullyQualifiedNames.stream()
                .map(jt -> jt.getFullyQualifiedName())
                .collect(Collectors.toList());
    }

    /**
     * getImports returns imports as they are.
     * Meaning {@code com.foo.my.*} will be returned as such.
     */
    @Override
    public List<OpenRewriteImport> getImports() {
        return getCompilationUnit().getImports().stream()
                .map(OpenRewriteImport::new)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasImportStartingWith(String... impoort) {
        return getImports().stream()
                .anyMatch(i -> Arrays.stream(impoort)
                        .anyMatch(pattern -> i.matches(pattern))
                );
    }

    @Override
    public String getPackageName() {
        J.Package packageDecl = getCompilationUnit().getPackageDeclaration();
        if (packageDecl == null) {
            return "";
        }
        return packageDecl.getExpression().printTrimmed();
    }

    public Path getSourceFolder() {
        Path absolutePath = getAbsolutePath();
        String packageName = getPackageName();
        if (packageName.isEmpty()) {
            return absolutePath.getParent();
        }

        String absolutePathString = absolutePath.toString();
        String packagePath = packageName.replace(".", File.separator);
        int i = absolutePathString.indexOf(packagePath);
        String sourceFolder = absolutePath.resolve(packagePath).toString();
        if (i > 0) {
            sourceFolder = absolutePathString.substring(0, i);
        }

        return Path.of(sourceFolder);
    }

    @Override
    public void renameMethodCalls(String methodMatchingPattern, String newName) {
        ChangeMethodName changeMethodName = new ChangeMethodName(methodMatchingPattern, newName, true, false);
        refactoring.refactor(getResource(), changeMethodName);
    }

    /**
     * Searches in the source file for the usage of the given annotation.
     */
    @Override
    public boolean hasAnnotation(String annotation) {
        if (!annotation.startsWith("@")) {
            annotation = "@" + annotation;
        }
        FindAnnotations findAnnotation = new FindAnnotations(annotation);
        List<Result> results = findAnnotation.run(List.of(getCompilationUnit())).getResults();
        return !results.isEmpty();
    }

    @Override
    public void replaceConstant(StaticFieldAccessTransformer transform) {
        refactoring.refactor(getResource(), new ReplaceStaticFieldAccessVisitor(transform));
    }

    @Override
    public List<Annotation> getAnnotations(String fqName, Expression scope) {
        return FindAnnotations.find(((OpenRewriteExpression) scope).getWrapped(), fqName).stream()
                .map(e -> Wrappers.wrap(e, refactoring, javaParser))
                .collect(Collectors.toList());
    }

    @Override
    public <T> void replaceLiteral(Class<T> klass, LiteralTransformer<T> t) {
        refactoring.refactor(getResource(), new ReplaceLiteralVisitor<>(klass, t));
    }


    @Override
    public String toString() {
        return "OpenRewriteJavaSource(" + getAbsolutePath() + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Recipe recipe) {
        refactoring.refactor(getResource(), recipe);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType(String fqName) {
        return getTypes().stream()
                .filter(t -> fqName.equals(t.getFullyQualifiedName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Given type '" + fqName + "' is not declared in JavaSource '" + getAbsolutePath() + "'."));
    }

    @Override
    public String print() {

        JavaPrinter<ExecutionContext> javaPrinter = new JavaPrinter<>() {
            @Override
            public <M extends Marker> M visitMarker(Marker marker, PrintOutputCapture<ExecutionContext> p) {
                if (marker instanceof CommentJavaSearchResult) {
                    CommentJavaSearchResult javaSearchResult = CommentJavaSearchResult.class.cast(marker);
                    p.out.append("/* " + javaSearchResult.getComment() + " */ ");
                }
                return (M) marker;
            }
        };

        PrintOutputCapture<Integer> outputCapture = new PrintOutputCapture(new InMemoryExecutionContext());
        ((JavaPrinter) javaPrinter).visit(getSourceFile(), outputCapture);

        return outputCapture.out.toString();
    }

    @Override
    public void removeUnusedImports() {
        apply(new RemoveUnusedImports());
    }

    @Override
    public void replaceImport(String p, String replace) {
        ChangePackage changePackage = new ChangePackage(p, replace, true);
        apply(changePackage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveTo(Path newPath) {
        // TODO; fire event to trigger rebuild
        // TODO: What if moved to e.g. Foo.java1 ? it's not a Java source anymore
        super.moveTo(newPath);
    }
}
