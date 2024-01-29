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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.ExecutionContext;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.VariableDeclarations.NamedVariable;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeTree;
import org.openrewrite.marker.Markers;
import org.springframework.rewrite.parser.JavaParserBuilder;
import org.springframework.rewrite.parser.maven.ClasspathDependencies;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;
import org.springframework.rewrite.recipes.GenericOpenRewriteRecipe;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.support.openrewrite.java.RemoveAnnotationVisitor;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Markers markers = getRewriteSourceFileHolder().getSourceFile().getMarkers();
        ClasspathDependencies compileClasspath = markers.findFirst(ClasspathDependencies.class).get();
        JavaParser.Builder parserBuilder = javaParserBuilder
                .classpath(compileClasspath.getDependencies())
                .clone();
        GenericOpenRewriteRecipe<JavaIsoVisitor<ExecutionContext>> addAnnotation = new GenericOpenRewriteRecipe<>(() -> new JavaIsoVisitor<>() {
            @Override
            public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, ExecutionContext executionContext) {
                J.VariableDeclarations vd = super.visitVariableDeclarations(multiVariable, executionContext);
                if (vd == getVariableDeclarations()) {
                    // TODO: Reminder: Use JavaTemplate to add the annotation here...
                    JavaTemplate javaTemplate = JavaTemplate.builder(snippet).imports(fqName).javaParser(parserBuilder).build();
                    maybeAddImport(fqName);
                    vd = javaTemplate.apply(getCursor(), vd.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
                }
                return vd;
            }
        });
        refactoring.refactor(rewriteSourceFileHolder, addAnnotation);
    }

    @Override
    public void addAnnotation(String snippet, String annotationImport, String... otherImports) {
        GenericOpenRewriteRecipe<JavaIsoVisitor<ExecutionContext>> recipe = new GenericOpenRewriteRecipe<>(() -> new JavaIsoVisitor<>() {

//            private JavaSourceSet main;
//            private List<Path> classpath;
//            private Set<String> imports;
//            private List<JavaType.FullyQualified> newCLasspathFqn;
//            private JavaTypeCache typeCache;
//            private boolean visitCu = false;

//            @Override
//            public J.CompilationUnit visitCompilationUnit(J.CompilationUnit compilationUnit, ExecutionContext executionContext) {
//                J.CompilationUnit cu = super.visitCompilationUnit(compilationUnit, executionContext);
//                if(visitCu) {
//                    // TODO: De ganze Zirkus hier nochmal
//                    JavaSourceSet javaSourceSet = cu.getMarkers().findFirst(JavaSourceSet.class).get();
//                    Field field = ReflectionUtils.findField(JavaSourceSet.class, "classpath");
//                    ReflectionUtils.makeAccessible(field);
//                    ReflectionUtils.setField(field, javaSourceSet, newCLasspathFqn);
//                    imports.forEach(i -> maybeAddImport(i));
//                    visitCu = false;
//                    final J.CompilationUnit tmpCu = cu;
//                    J.CompilationUnit cu1 = (J.CompilationUnit) JavaParser.fromJavaVersion().typeCache(typeCache).classpath(classpath).clone().build().parseInputs(List.of(new JavaParser.Input(cu.getSourcePath(), () -> new ByteArrayInputStream(tmpCu.printAll().getBytes()))), null, executionContext).toList().get(0);
//                    cu = cu1.withMarkers(Markers.build(List.of(main)));
//                }
//                return cu;
//            }

            @Override
            public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, ExecutionContext executionContext) {
                J.VariableDeclarations vd = super.visitVariableDeclarations(multiVariable, executionContext);
                if (vd == getVariableDeclarations()) {
                    J.CompilationUnit cu = getCursor().dropParentUntil(J.CompilationUnit.class::isInstance).getValue();
                    List<Path> classpathJars = cu.getMarkers().findFirst(ClasspathDependencies.class).get().getDependencies();
//                    JavaSourceSet javaSourceSet = cu.getMarkers().findFirst(JavaSourceSet.class).get();
//                    JavaTypeCache typeCache = new JavaTypeCache();

                    Set<String> imports = Stream.concat(Stream.of(annotationImport), Stream.of(annotationImport)).collect(Collectors.toSet());
                    JavaTypeCache typeCache = new JavaTypeCache();



                    JavaParser.Builder<? extends JavaParser, ?> javaParser = JavaParser.fromJavaVersion().typeCache(typeCache.clone()).classpath(classpathJars);

                    vd = JavaTemplate.builder(snippet)
                            .imports(new ArrayList<>(imports).toArray(String[]::new))
                            .javaParser(javaParser)
                            .build()
                            .apply(getCursor(), vd.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)));
                    imports.forEach(i -> maybeAddImport(i, false));

//                    final J.CompilationUnit tmpCu = cu;
//                    J.CompilationUnit cu1 = (J.CompilationUnit) JavaParser.fromJavaVersion().typeCache(typeCache).classpath(classpath).build().parseInputs(List.of(new JavaParser.Input(cu.getSourcePath(), () -> new ByteArrayInputStream(tmpCu.printAll().getBytes()))), null, executionContext).toList().get(0);

/*
                    visitCu = false;
                    final J.CompilationUnit tmpCu = cu;
                    J.CompilationUnit cu1 = (J.CompilationUnit) JavaParser.fromJavaVersion().typeCache(typeCache).classpath(classpath).build().parseInputs(List.of(new JavaParser.Input(cu.getSourcePath(), () -> new ByteArrayInputStream(tmpCu.printAll().getBytes()))), null, executionContext).toList().get(0);
                    JavaSourceSet newJavaSourceSet = JavaSourceSet.build(javaSourceSet.getName(), cu.getMarkers().findFirst(ClasspathDependencies.class).get().getDependencies(), typeCache, true);
                    cu = cu1.withMarkers(cu1.getMarkers().removeByType(JavaSourceSet.class).addIfAbsent(newJavaSourceSet));
//                    cu = cu1.withMarkers(Markers.build(List.of(main)));


//                    J.CompilationUnit cu = getRewriteSourceFileHolder().getSourceFile();
//                    classpath = cu.getMarkers().findFirst(ClasspathDependencies.class).get().getDependencies();
                    imports = Stream.concat(Stream.of(annotationImport), Stream.of(annotationImport)).collect(Collectors.toSet());
//                    // FIXME: main is not always correct
//                    typeCache = new JavaTypeCache();
//                    main = JavaSourceSet.build("main", classpath, typeCache, true);

                    newCLasspathFqn = main.getClasspath();

                    visitCu = true;
                    imports.forEach(i -> maybeAddImport(i, false));

 */
                }
                return vd;
            }
        });
        refactoring.refactor(rewriteSourceFileHolder, recipe);
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
