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
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.*;
import org.openrewrite.java.format.WrappingAndBraces;
import org.openrewrite.java.tree.*;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.java.migration.visitor.RemoveImplementsVisitor;
import org.springframework.sbm.java.refactoring.JavaRefactoring;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.java.search.DeclaresMethod;
import org.openrewrite.java.tree.J.ClassDeclaration;
import org.openrewrite.java.tree.JavaType.Class;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.support.openrewrite.java.AddAnnotationVisitor;
import org.springframework.sbm.support.openrewrite.java.FindCompilationUnitContainingType;
import org.springframework.sbm.support.openrewrite.java.RemoveAnnotationVisitor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class OpenRewriteType implements Type {

    private final UUID classDeclId;

    private final RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder;

    private final JavaRefactoring refactoring;
    private final ClassDeclaration classDeclaration;
    private final ExecutionContext executionContext;
    private JavaParser.Builder javaParserBuilder;

    public OpenRewriteType(ClassDeclaration classDeclaration, RewriteSourceFileHolder<J.CompilationUnit> rewriteSourceFileHolder, JavaRefactoring refactoring, ExecutionContext executionContext, JavaParser.Builder javaParserBuilder) {
        this.classDeclId = classDeclaration.getId();
        this.classDeclaration = classDeclaration;
        this.rewriteSourceFileHolder = rewriteSourceFileHolder;
        this.refactoring = refactoring;
        this.executionContext = executionContext;
        this.javaParserBuilder = javaParserBuilder;
    }

    public List<OpenRewriteMember> getMembers() {
        return Utils.getFields(getClassDeclaration()).stream()
                .flatMap(variableDecls -> createMembers(refactoring, variableDecls))
                .collect(Collectors.toList());
    }

    private Stream<OpenRewriteMember> createMembers(JavaRefactoring refactoring, J.VariableDeclarations variableDecls) {
        return variableDecls.getVariables().stream()
                .map(namedVar -> new OpenRewriteMember(variableDecls, namedVar, rewriteSourceFileHolder, refactoring, javaParserBuilder));
    }

    @Override
    public String getSimpleName() {
        return getClassDeclaration().getSimpleName();
    }

    @Override
    public String getFullyQualifiedName() {
        return TypeUtils.asFullyQualified(getClassDeclaration().getType()).getFullyQualifiedName();
    }

    @Override
    public boolean hasAnnotation(String annotation) {
        return !findORAnnotations(annotation).isEmpty();
    }

    @Override
    public List<Annotation> findAnnotations(String annotation) {
        return findORAnnotations(annotation).stream()
                .map(a -> Wrappers.wrap(a, refactoring, javaParserBuilder)).collect(Collectors.toList());
    }

    @Override
    public List<Annotation> getAnnotations() {
        return getClassDeclaration().getLeadingAnnotations().stream()
                .map(a -> new OpenRewriteAnnotation(a, refactoring, javaParserBuilder))
                .collect(Collectors.toList());
    }

    @Override
    public void addAnnotation(String fqName) {
        // FIXME: Hack, JavaParser should have latest classpath
        Supplier<JavaParser> javaParserSupplier = () -> javaParserBuilder;
        String snippet = "@" + fqName.substring(fqName.lastIndexOf('.') + 1);
        AddAnnotationVisitor addAnnotationVisitor = new AddAnnotationVisitor(javaParserSupplier, getClassDeclaration(), snippet, fqName);
        refactoring.refactor(rewriteSourceFileHolder, addAnnotationVisitor);


        List<SourceFile> ast = List.of();
        ExecutionContext ctx = new InMemoryExecutionContext(t -> t.printStackTrace());
        RecipeRun runResult = new RemoveAnnotation("").run(new InMemoryLargeSourceSet(ast), ctx);
        List<SourceFile> result = runResult.getChangeset().getAllResults().stream()
                .map(r -> r.getAfter())
                .toList();
        ast = merge(ast, result);
    }

    @Override
    public void addAnnotation(String snippet, String annotationImport, String... otherImports) {
        // FIXME: #7 JavaParser does not update typesInUse
        Supplier<JavaParser> javaParserSupplier = () -> JavaParser.fromJavaVersion().classpath(ClasspathRegistry.getInstance().getCurrentDependencies()).build();
        AddAnnotationVisitor addAnnotationVisitor = new AddAnnotationVisitor(javaParserSupplier, getClassDeclaration(), snippet, annotationImport, otherImports);
        Recipe recipe = new GenericOpenRewriteRecipe<>(() -> addAnnotationVisitor);
        refactoring.refactor(rewriteSourceFileHolder, recipe);
    }

    @Override
    public Annotation getAnnotation(String fqName) {
        return getAnnotations().stream()
                .filter(a -> a.getFullyQualifiedName().equals(fqName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Type '" + getFullyQualifiedName() + "' has no annotation '" + fqName + "'."));
    }

    @Override
    // FIXME: reuse
    public void removeAnnotation(String fqName) {
        // TODO: See if RemoveAnnotationVisitor can be replaced with OpenRewrite's version
        Recipe removeAnnotationRecipe = new GenericOpenRewriteRecipe<>(() -> new RemoveAnnotationVisitor(getClassDeclaration(), fqName))
                .doNext(new RemoveUnusedImports());
        refactoring.refactor(rewriteSourceFileHolder, removeAnnotationRecipe);
    }

    @Override
    public void removeAnnotation(Annotation annotation) {
        Recipe removeAnnotationRecipe = new GenericOpenRewriteRecipe<>(() -> new RemoveAnnotationVisitor(getClassDeclaration(), annotation.getFullyQualifiedName()))
                .doNext(new RemoveUnusedImports());
        refactoring.refactor(rewriteSourceFileHolder, removeAnnotationRecipe);
    }

    @Override
    public List<Method> getMethods() {
        return Utils.getMethods(getClassDeclaration()).stream()
                .map(m -> new OpenRewriteMethod(rewriteSourceFileHolder, m, refactoring, javaParserBuilder, executionContext))
                .collect(Collectors.toList());
    }

    @Override
    public void addMethod(String methodTemplate, Set<String> requiredImports) {
        this.apply(new GenericOpenRewriteRecipe<>(() -> new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                // FIXME: #7 hack, get JavaParser as SpringBean with access to classpath
                // TODO: 786
                javaParserBuilder = new RewriteJavaParser(new SbmApplicationProperties(), executionContext);
                javaParserBuilder.setClasspath(ClasspathRegistry.getInstance().getCurrentDependencies());

                J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
                JavaTemplate template = JavaTemplate
                        .builder(() -> getCursor().getParent(), methodTemplate)
                        .javaParser(() -> javaParserBuilder)
                        .imports(requiredImports.toArray(new String[0]))
                        .build();
                requiredImports.forEach(this::maybeAddImport);
                cd = cd.withTemplate(template, cd.getBody().getCoordinates().lastStatement());
                return cd;
            }
        }).doNext(new WrappingAndBraces()));
    }


    private List<J.Annotation> findORAnnotations(String annotation) {
        return getClassDeclaration().getLeadingAnnotations()
                .stream()
                .filter(a -> {
                    Object typeObject = a.getAnnotationType().getType();
                    String simpleName = a.getSimpleName();
                    if (JavaType.Unknown.class.isInstance(typeObject)) {
                        log.warn("Could not resolve Type for annotation: '" + simpleName + "' while comparing with '" + annotation + "'.");
                        return false;
                    } else if (JavaType.Class.class.isInstance(typeObject)) {
                        Class type = Class.class.cast(typeObject);
                        return annotation.equals(type.getFullyQualifiedName());
                    } else {
                        log.warn("Could not resolve Type for annotation: '" + simpleName + "' (" + typeObject + ") while comparing with '" + annotation + "'.");
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTypeOf(String fqName) {
        return classDeclaration.getType().isAssignableTo(fqName);
    }

    @Override
    public List<? extends Type> getImplements() {
        ClassDeclaration classDeclaration = getClassDeclaration();
        if (classDeclaration.getType() == null) {
            throw new RuntimeException("Could not resolve type for classDeclaration '" + classDeclaration.getName() + "'");
        }

        List<? extends Type> types = classDeclaration.getType().getInterfaces().stream()
                .filter(jt -> JavaType.FullyQualified.class.isAssignableFrom(jt.getClass()))
                .map(JavaType.FullyQualified.class::cast)
                .map(this::extracted)
                .collect(Collectors.toList());

        return types;
    }

    private Type extracted(JavaType.FullyQualified javaType) {
        // TODO: maybe split Type into TypeInfo and Type (extending TypeInfo) to split Type in mutable and immutable api ?
        if (JavaType.FullyQualified.class.isAssignableFrom(javaType.getClass())) {
            JavaType.FullyQualified fullyQualified = JavaType.FullyQualified.class.cast(javaType);
            Optional<OpenRewriteType> openRewriteTypeOptional = buildForJavaType(fullyQualified);
            if (openRewriteTypeOptional.isPresent()) {
                return openRewriteTypeOptional.get();
            } else {
                if (JavaType.Class.class.isAssignableFrom(javaType.getClass())) {
                    JavaType.Class javaTypeClass = JavaType.Class.class.cast(javaType);
                    return new CompiledType(javaTypeClass);
                }
            }
        }
        throw new RuntimeException(String.format("Could not retrieve type for '%s'", javaType));
    }

    @Override
    public Optional<? extends Type> getExtends() {
        ClassDeclaration classDeclaration = getClassDeclaration();
        if (classDeclaration.getType() == null) {
            throw new RuntimeException("Could not resolve type for classDeclaration '" + classDeclaration.getName() + "'");
        }
        JavaType.FullyQualified extendings = classDeclaration.getType().getSupertype();
        if (extendings == null) {
            return Optional.empty();
        }
        return buildForJavaType(extendings);
    }

    private Optional<OpenRewriteType> buildForJavaType(JavaType.FullyQualified jt) {
        List<RewriteSourceFileHolder<J.CompilationUnit>> compilationUnits = refactoring.find(new FindCompilationUnitContainingType(jt));
        if (compilationUnits.isEmpty()) {
            return Optional.empty();
        }
        RewriteSourceFileHolder<J.CompilationUnit> modifiableCompilationUnit = compilationUnits.get(0);
        J.ClassDeclaration classDeclaration = modifiableCompilationUnit.getSourceFile().getClasses().stream()
                .peek(c -> {
                    if (c.getType() == null)
                        log.warn("Could not resolve type for class declaration '" + c.getName() + "'.");
                })
                .filter(c -> c.getType() != null)
                .filter(c -> c.getType().getFullyQualifiedName().equals(jt.getFullyQualifiedName().trim()))
                .findFirst()
                .orElseThrow();
        return Optional.of(new OpenRewriteType(classDeclaration, modifiableCompilationUnit, refactoring,
                                               executionContext, javaParserBuilder));
    }

    @Override
    public KindOfType getKind() {
        return KindOfType.valueOf(this.getClassDeclaration().getKind().name().toUpperCase());
    }

    @Override
    public void removeImplements(String... fqNames) {
        RemoveImplementsVisitor visitor = new RemoveImplementsVisitor(getClassDeclaration(), fqNames);
        refactoring.refactor(rewriteSourceFileHolder, visitor);
    }

    public J.ClassDeclaration getClassDeclaration() {
        return rewriteSourceFileHolder.getSourceFile().getClasses().stream()
                .filter(cd -> cd.getId().equals(classDeclId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not get class declaration for type in '" + rewriteSourceFileHolder.getSourceFile().getSourcePath() + "' with ID '" + classDeclId + "'."));
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    @Override
    public void apply(Recipe r) {
        refactoring.refactor(rewriteSourceFileHolder, r);
    }

    @Override
    public boolean hasMethod(String methodPattern) {
        // TODO: parse and validate methodPattern
        methodPattern = this.getFullyQualifiedName() + " " + methodPattern;
        DeclaresMethod<ExecutionContext> declaresMethod = new DeclaresMethod(new MethodMatcher(methodPattern, true));
        List<RewriteSourceFileHolder<J.CompilationUnit>> matches = refactoring.find(rewriteSourceFileHolder, new GenericOpenRewriteRecipe<>(() -> declaresMethod));
        // TODO: searches in all classes, either filter result list or provide findInCurrent() or similar
        return !matches.isEmpty();
    }

    @Override
    public Method getMethod(String methodPattern) {
        final String fullMethodPattern = this.getFullyQualifiedName() + " " + methodPattern;
        if (!hasMethod(methodPattern)) {
            throw new IllegalArgumentException(String.format("Type '%s' has no method matching pattern '%s'.", getFullyQualifiedName(), fullMethodPattern));
        }
        return getMethods().stream()
                // TODO: can getMethodDecl be made package private ?
                .filter(m -> new MethodMatcher(fullMethodPattern, true).matches(m.getMethodDecl(), this.classDeclaration))
                .findFirst().get();
    }

    /**
     * Add a non static member to this type
     *
     * [source, java]
     * .....
     *  import <type>;
     *  class MyClass {
     *      <visibilit> <type> <name>;
     *  }
     * .....
     *
     * @param visibility of the member
     * @param type       the fully qualified type of the member
     * @param name       of the member
     */
    @Override
    public void addMember(Visibility visibility, String type, String name) {
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new JavaIsoVisitor<>() {
            @Override
            public ClassDeclaration visitClassDeclaration(ClassDeclaration classDecl, ExecutionContext executionContext) {
                ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
                JavaType javaType = JavaType.buildType(type);
                String className = ((JavaType.FullyQualified) javaType).getClassName();

                JavaTemplate javaTemplate = JavaTemplate.builder("@Autowired\n" + visibility.getVisibilityName() + " " + className + " " + name + ";")
                        .imports(type, "org.springframework.beans.factory.annotation.Autowired")
                        .javaParser(javaParserBuilder)
                        .build();

                maybeAddImport(type);
                maybeAddImport("org.springframework.beans.factory.annotation.Autowired");


                cd = javaTemplate.apply(getCursor().getParent(), cd.getBody().getCoordinates().firstStatement());
                return cd;
            }
        };
        apply(new GenericOpenRewriteRecipe<JavaIsoVisitor<ExecutionContext>>(() -> javaIsoVisitor));
    }

    public boolean isImplementing(String fqClassName){
        return getClassDeclaration()
                .getType()
                .getInterfaces()
                .stream()
                .map(JavaType.FullyQualified::getFullyQualifiedName)
                .anyMatch(fqClassName::equals);
    }

}
