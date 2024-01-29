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
package org.springframework.sbm.build.impl;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.java.tree.J;
import org.springframework.rewrite.resource.ProjectResource;
import org.springframework.rewrite.resource.ProjectResourceSet;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceLocation;
import org.springframework.sbm.java.impl.OpenRewriteJavaSource;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.rewrite.parser.JavaParserBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaSourceSetImpl implements JavaSourceSet {
    private final ProjectResourceSet projectResourceSet;
    private final Predicate<ProjectResource> filter;
    private final Path sourceSetRoot;
    private final JavaRefactoringFactory javaRefactoringFactory;
    private final BasePackageCalculator basePackageCalculator;
    private final JavaParserBuilder javaParserBuilder;
    private ExecutionContext executionContext;

    public JavaSourceSetImpl(ProjectResourceSet projectResourceSet, Path projectRootDir, Path modulePath, Path mainJavaPath, JavaRefactoringFactory javaRefactoringFactory, BasePackageCalculator basePackageCalculator, JavaParserBuilder javaParser, ExecutionContext executionContext) {
        this.projectResourceSet = projectResourceSet;
        this.basePackageCalculator = basePackageCalculator;
        this.javaParserBuilder = javaParser;
        this.executionContext = executionContext;
        this.sourceSetRoot = projectRootDir.resolve(modulePath).resolve(mainJavaPath);
        this.filter = (r) -> {
            return r.getAbsolutePath().getParent().normalize().toString().startsWith(sourceSetRoot.toString());
        };
        this.javaRefactoringFactory = javaRefactoringFactory;
    }

    /**
     * @deprecated Use {@code addJavaSource(Path, String, packageName)} instead
     */
    @Override
    @Deprecated(forRemoval = true)
    public JavaSource addJavaSource(Path projectRoot, Path sourceFolder, String sourceCode, String packageName) {
        Stream<SourceFile> compilationUnits = javaParserBuilder.build().parse(sourceCode);
        J.CompilationUnit parsedCompilationUnit = (J.CompilationUnit) compilationUnits.toList().get(0);
        String sourceFileName = parsedCompilationUnit.getSourcePath().toString();
        Path sourceFilePath = sourceFolder.resolve(sourceFileName);
        if (Files.exists(sourceFilePath)) {
            throw new RuntimeException("The Java class you tried to add already lives here: '" + sourceFilePath + "'.");
        } else {
            J.CompilationUnit compilationUnit = parsedCompilationUnit.withSourcePath(sourceFilePath);
            OpenRewriteJavaSource addedSource = new OpenRewriteJavaSource(projectRoot, compilationUnit, javaRefactoringFactory.createRefactoring(compilationUnit), javaParserBuilder, executionContext);
            addedSource.markChanged();
            projectResourceSet.add(addedSource);
            return addedSource;
        }
    }

    @Override
    public List<JavaSource> addJavaSource(Path projectRoot, Path sourceFolder, String... sourceCodes) {
        // FIXME: #7 JavaParser
        javaParserBuilder.build().reset();

        Stream<SourceFile> compilationUnits = javaParserBuilder.build().parse(sourceCodes);

        List<JavaSource> addedSources = new ArrayList<>();

        for (SourceFile sf : compilationUnits.toList()) {
            J.CompilationUnit cu = (J.CompilationUnit) sf;
            String sourceFileName = cu.getSourcePath().toString();
            Path sourceFilePath = sourceFolder.resolve(sourceFileName);
            if(!Files.exists(sourceFilePath)) {
                J.CompilationUnit compilationUnit = cu.withSourcePath(sourceFilePath);
                OpenRewriteJavaSource addedSource = new OpenRewriteJavaSource(projectRoot, compilationUnit, javaRefactoringFactory.createRefactoring(compilationUnit), javaParserBuilder, executionContext);
                addedSource.markChanged();
                projectResourceSet.add(addedSource);
                addedSources.add(addedSource);
            }
        }
        return addedSources;
    }

    @Override
    public JavaSource addJavaSource(Path projectRootDirectory, String src, String packageName) {
        Path sourceFolder = this.getJavaSourceLocation().getSourceFolder();
        return addJavaSource(projectRootDirectory, sourceFolder, src, packageName);
    }

    @Override
    public void replaceType(String type, String withType) {

    }

    @Override
    public Optional<Path> getBaseResourcesLocation(Path relativeTo) {
        return Optional.empty();
    }

    @Override
    public JavaSourceLocation getJavaSourceLocation() {
        List<JavaSource> list = new ArrayList<>(list());
        String basePackage = basePackageCalculator.calculateBasePackage(list);
        return new JavaSourceLocation(sourceSetRoot, basePackage);
    }

    @Override
    public Path getAbsolutePath() {
        return sourceSetRoot;
    }

    @Override
    public boolean hasImportStartingWith(String... value) {
        return false;
    }

    @Override
    public Optional<? extends JavaSource> findJavaSourceDeclaringType(String fqName) {
        return Optional.empty();
    }

    @Override
    public void apply(Recipe recipe) {

    }

    @Override
    public Stream<JavaSource> stream() {
        return projectResourceSet.stream().filter(filter).map(JavaSource.class::cast);
    }

    @Override
    public List<JavaSource> list() {
        return stream().collect(Collectors.toUnmodifiableList());
    }
}
