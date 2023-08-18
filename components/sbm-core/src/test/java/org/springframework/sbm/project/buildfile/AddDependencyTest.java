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
package org.springframework.sbm.project.buildfile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.LargeSourceSet;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.TypesInUse;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.maven.tree.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.RewriteParserConfig;
import org.springframework.sbm.SbmCoreConfig;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.java.impl.DependenciesChangedEventHandler;
import org.springframework.sbm.java.impl.DependencyChangeHandler;
import org.springframework.sbm.java.impl.OpenRewriteAnnotation;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.project.resource.ProjectResourceWrapperRegistry;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.scopes.ExecutionScope;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.sbm.scopes.ScopeConfiguration;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.support.openrewrite.java.AddAnnotationVisitor;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the inner workings when adding a dependency
 *
 * @author Fabian Krüger
 */
@SpringBootTest(classes ={
        SbmCoreConfig.class,
        RewriteParserConfig.class,
        ScopeConfiguration.class,
        ExecutionScope.class,
        ScanScope.class,
        DependenciesChangedEventHandler.class,
        DependencyChangeHandler.class,
        ProjectContextHolder.class,
        JavaParserBuilder.class,
        SbmApplicationProperties.class,
        ProjectContextInitializer.class,
        ProjectContextFactory.class,
        ProjectResourceWrapperRegistry.class
})
public class AddDependencyTest {

    @Autowired
    DependenciesChangedEventHandler handler;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    ProjectContextHolder contextHolder;

    @Autowired
    JavaParserBuilder javaParserBuilder;

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @Autowired
    ExecutionContext executionContext;

    /**
     *
     */
    @Test
    @DisplayName("type from dependency is accessible")
    void typeFromDependencyIsAccessible() {
        String javacode =
                """
                package com.acme;
                import javax.validation.constraints.Email;
                class SomeClass {
                    @Email
                    String someMember;
                }
                """;
        ProjectContext context = TestProjectContext
                .buildProjectContext(beanFactory)
                .withBuildFileHavingDependencies("javax.validation:validation-api:2.0.1.Final")
                .withJavaSource("src/main/java", javacode)
                .build();

        String fullyQualifiedName = context.getProjectJavaSources().list().get(0).getTypes().get(0).getMembers().get(0).getAnnotations()
                .get(0)
                .getFullyQualifiedName();

        assertThat(fullyQualifiedName).isEqualTo("javax.validation.constraints.Email");
    }

    @Test
    @DisplayName("Compile should make types available")
    void compileShouldMakeTypesAvailable() {
        Path localRepo = Path.of(System.getProperty("user.home")).resolve(".m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar");
        List<Path> classpath = List.of(localRepo);
        JavaParser javaParser = javaParserBuilder
                .classpath(classpath)
                .build();
        SourceFile sourceFile = javaParser
                .parse(
                        """
                                import javax.validation.constraints.Email;
                                public class SomeClass {
                                    @Email
                                    private String email;
                                }
                                """
                )
                .toList()
                .get(0);
        J.CompilationUnit cu = (J.CompilationUnit) sourceFile;
        List<String> types = cu.getTypesInUse().getTypesInUse().stream().map(t -> ((JavaType.Class) t).getFullyQualifiedName()).toList();
        assertThat(types).containsExactlyInAnyOrder("java.lang.String", "javax.validation.constraints.Email");
    }

    @Test
    @DisplayName("Compile should make types available")
    void compileShouldMakeTypesAvailable2() {
        Path localRepo = Path.of(System.getProperty("user.home")).resolve(".m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar");
        List<Path> classpath = List.of(localRepo);
        JavaParser javaParser = javaParserBuilder
                .classpath(classpath)
                .build();
        SourceFile sourceFile = javaParser
                .parse(
                        """
                        public class SomeClass {
                            @Deprecated
                            private String email;
                        }
                        """
                )
                .toList()
                .get(0);
        J.CompilationUnit cu = (J.CompilationUnit) sourceFile;
        List<String> types = cu.getTypesInUse().getTypesInUse().stream().map(t -> ((JavaType.Class) t).getFullyQualifiedName()).toList();
        assertThat(types).containsExactlyInAnyOrder("java.lang.String", "java.lang.Deprecated");


        RecipeRun recipeRun = new ChangeType(
                "java.lang.Deprecated",
                "javax.validation.constraints.Email",
                true
        ).run(new InMemoryLargeSourceSet(List.of(cu)), executionContext);

        Set<String> typesInUse = ((J.CompilationUnit) recipeRun.getChangeset().getAllResults().get(0).getAfter()).getTypesInUse().getTypesInUse().stream()
                .map(t -> ((JavaType.Class)t).getFullyQualifiedName())
                .collect(Collectors.toSet());
        assertThat(typesInUse).containsExactlyInAnyOrder("java.lang.String", "javax.validation.constraints.Email");

        SourceFile sourceFile2 = javaParser
                .parse(
                        """
                        import javax.validation.constraints.Email;
                        public class SomeClass2 {
                            @Email
                            private String email;
                        }
                        """
                )
                .toList()
                .get(0);
        J.CompilationUnit cu2 = (J.CompilationUnit) sourceFile2;
        List<String> types2 = cu2.getTypesInUse().getTypesInUse().stream().map(t -> ((JavaType.Class) t).getFullyQualifiedName()).toList();
        assertThat(types2).containsExactlyInAnyOrder("java.lang.String", "javax.validation.constraints.Email");
    }

    /**
     * Given a single module Maven project without dependencies
     * When a new dependency gets added
     * Then the types are available and resolvable
     *
     * Also see {@link DependenciesChangedEventHandler} and {@link DependencyChangeHandler}
     */
    @Test
    @DisplayName("addingANewDependencyMakesTypesAvailable")
    void addingANewDependencyMakesTypesAvailable() {
        String javaSourceCode =
                """
                package com.acme;
                class SomeClass {
                    String someMember;
                }
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext(beanFactory)
                .withJavaSource("src/main/java", javaSourceCode)
                .build();

        context.getApplicationModules().getRootModule().getBuildFile().addDependency(
                Dependency.builder()
                        .groupId("javax.validation")
                        .artifactId("validation-api")
                        .version("2.0.1.Final")
                        .build()
        );

        context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get()
                .getTypes().get(0)
                .getMembers().get(0)
                .addAnnotation("@Email", "javax.validation.constraints.Email");

        String string = ((OpenRewriteAnnotation) context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get()
                .getTypes().get(0)
                .getMembers().get(0)
                .getAnnotations().get(0)).getWrapped()
                .getAnnotationType()
                .getType()
                .toString();

        String fullyQualifiedName = context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get()
                .getTypes().get(0)
                .getMembers().get(0)
                .getAnnotations().get(0)
                .getFullyQualifiedName();

        assertThat(fullyQualifiedName).isEqualTo("javax.validation.constraints.Email");
    }
    
    
    @Test
    void whenDependencyIsAdded_thenJavaParserTypeCacheGetsUpdated() {
        // simple ProjectContext
        String javaSourceCode =
                    """
                    import javax.validation.constraints.Email; 
                    class Y {
                        @Email String email;
                    }
                    """;
        
        JavaParser javaParser = javaParserBuilder.build();

        ProjectContext context = TestProjectContext
                .buildProjectContext(beanFactory)
                .withBuildFileHavingDependencies("javax.validation:validation-api:2.0.1.Final")
//                .buildProjectContext(eventPublisher, javaParser)
                .withJavaSource("src/main/java", javaSourceCode)
                .build();
        // provide ProjectContext to Spring beans
        contextHolder.setProjectContext(context);

        String javaxValidationEmail = "javax.validation.constraints.Email";
        HashMap<Object, JavaType> typeCache0 = retrieveTypeCache(javaParser);
        assertThat(typeCache0).isEmpty();

        // Parse the java source to fill the type cache
        javaParser.parse(javaSourceCode);
        HashMap<Object, JavaType> typeCache = retrieveTypeCache(javaParser);
        assertThat(typeCache).hasSize(17_590);

        // javax.validation.Email not in type cache
        Optional<String> s = findInTypeCache(typeCache, javaxValidationEmail);
        assertThat(s).isNotPresent();

        // Classpath empty
        assertThat(ClasspathRegistry.getInstance().getCurrentDependencies()).isEmpty();


        // add dependency
        BuildFile buildFile = context.getApplicationModules().getRootModule().getBuildFile();
        buildFile.addDependency(Dependency.builder()
                                        .groupId("javax.validation")
                                        .artifactId("validation-api")
                                        .version("2.0.1.Final")
                                        .build());

        System.out.println(buildFile.print());
        assertThat(buildFile.getDeclaredDependencies(Scope.Compile).get(0).getArtifactId()).isEqualTo("validation-api");

        // validation-api added to Classpath
        assertThat(ClasspathRegistry.getInstance().getCurrentDependencies()).hasSize(1);
        assertThat(ClasspathRegistry.getInstance().getCurrentDependencies().iterator().next().toString()).contains("validation-api");

        // type cache contains the new types as classes were recompiled in DependenciesChangeEventListener
        //javaParser.getJavaParser().parse(javaSourceCode);
        context.getApplicationModules().getRootModule().getMainJavaSourceSet().addJavaSource(TestProjectContext.getDefaultProjectRoot(),
                                                                                                  Path.of("src/main/java"),
                                                                                                  "import jakarta.validation.constraints.Email; class X {@Email String email;}");

        // The Email annotation can now be resolved
        HashMap<Object, JavaType> typeCacheAfter = retrieveTypeCache(javaParser);
        Optional<String> emailType = findInTypeCache(typeCacheAfter, javaxValidationEmail);
        assertThat(emailType).isPresent(); // currently failing
        assertThat(typeCacheAfter).hasSize(17697);
    }

    @NotNull
    private Optional<String> findInTypeCache(HashMap<Object, JavaType> typeCache1, String emailFqName) {
        Optional<String> s = typeCache1
                .values()
                .stream()
                .filter(JavaType.Class.class::isInstance)
                .map(JavaType.Class.class::cast)
                .map(jt -> jt.getFullyQualifiedName())
                .filter(emailFqName::equals)
                .findAny();
        return s;
    }

    @Nullable
    private HashMap<Object, JavaType> retrieveTypeCache(JavaParser javaParser) {
        HashMap<Object, JavaType> typeCache1 = (HashMap) ReflectionTestUtils.getField(ReflectionTestUtils.getField(ReflectionTestUtils.getField(
                javaParser, "delegate"), "typeCache"), "typeCache");
        return typeCache1;
    }
}
