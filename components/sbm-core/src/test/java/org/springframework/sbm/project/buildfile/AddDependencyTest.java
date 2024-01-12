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
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.openrewrite.ExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ResourceLoader;
import org.springframework.rewrite.boot.autoconfigure.ScopeConfiguration;
import org.springframework.rewrite.boot.autoconfigure.SpringRewriteCommonsConfiguration;
import org.springframework.rewrite.parsers.JavaParserBuilder;
import org.springframework.rewrite.parsers.RewriteParserConfiguration;
import org.springframework.sbm.SbmCoreConfig;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.build.migration.MavenPomCacheProvider;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.GitSupportTest;
import org.springframework.sbm.engine.precondition.PreconditionVerifier;
import org.springframework.sbm.engine.recipe.MigrationResultProjectContextMerger;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.java.impl.DependenciesChangedEventHandler;
import org.springframework.sbm.java.impl.DependencyChangeHandler;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.project.parser.DependencyHelper;
import org.springframework.sbm.project.parser.JavaProvenanceMarkerFactory;
import org.springframework.sbm.project.parser.MavenConfigHandler;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.project.resource.*;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.test.SpringBeanProvider;
import org.springframework.sbm.test.TestProjectContextInfo;
import org.springframework.sbm.xml.parser.RewriteXmlParser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the inner workings when adding a dependency
 *
 * @author Fabian Krüger
 */
@SpringBootTest(classes = {
        SpringBeanProvider.ComponentScanConfiguration.class,
        ProjectContextInitializer.class,
        JavaProvenanceMarkerFactory.class,
        BasePackageCalculator.class,
        BasePackageCalculator.class,
        ProjectRootPathResolver.class,
        PreconditionVerifier.class,
        ProjectContextFactory.class,
        MavenPomCacheProvider.class,
        SbmApplicationProperties.class,
        MigrationResultProjectContextMerger.class,
        RewritePropertiesParser.class,
        RewriteMavenParser.class,
        RewriteXmlParser.class,
        ResourceHelper.class,
        ResourceLoader.class,
        GitSupport.class,
        ScanCommand.class,
        ProjectResourceSetHolder.class,
        JavaRefactoringFactoryImpl.class,
        ProjectResourceWrapperRegistry.class,
        MavenConfigHandler.class,
        ScopeConfiguration.class,
        SpringRewriteCommonsConfiguration.class,
        DependenciesChangedEventHandler.class,
        DependencyChangeHandler.class,
        LocalValidatorFactoryBean.class,
        ProjectContextHolder.class

//        DependencyChangeHandler.class,
//        ProjectContextHolder.class,
//        SbmApplicationProperties.class,
//        ProjectContextInitializer.class,
//        ProjectContextFactory.class,
//        ProjectResourceSetHolder.class,
//        GitSupport.class,
//        JavaRefactoringFactoryImpl.class,
//        BasePackageCalculator.class,
//        SpringRewriteCommonsConfiguration.class,
//        ProjectResourceWrapperRegistry.class,
//        RewriteParserConfiguration.class,
//        SpringBeanProvider.ComponentScanConfiguration.class

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
                .buildProjectContext()
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
                .map(t -> ((JavaType.Class) t).getFullyQualifiedName())
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


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class UsingTypeFromExternalDependency {

        private static final String JAVA_SOURCE_CODE = """
                package com.acme;
                class SomeClass {
                    String someMember;
                }
                """;

        private ProjectContext context = createProjectContext();

        /*
         * Given a type (annotation) from an external dependency
         * When using the annotation
         * Then the code is correct but the type can't be resolved
         */
        @Test
        @Order(1)
        @DisplayName("Adding an annotation of unknown type is possible")
        void addingAnAnnotationOfUnkownTypeIsPossible() {
            addAnnotation();
            String fullyQualifiedName = getFullyQualifiedName();
            assertCodeTransformation();
            assertThat(fullyQualifiedName).isNull();
        }

        /*
         * Given the type is used but the dependency is missing
         * When the dependency gets added
         * Then the type can be resolved
         */
        @Test
        @Order(2)
        @DisplayName("Adding the dependency resolves the missing type")
        void addingTheDependencyResolvesTheMissingType() {
            addDependency();
            String fullyQualifiedName = getFullyQualifiedName();
            assertThat(fullyQualifiedName).isEqualTo("javax.validation.constraints.Email");
        }

        /**
         * Given a project without dependencies
         * When a new dependency gets added
         * and then a type from the dependency is used
         * Then the type can be resolved
         */
        @Test
        @DisplayName("Adding a new dependency makes types available")
        void addingANewDependencyMakesTypesAvailable() {
            context = createProjectContext();
            // Adding the dependency makes the type resolvable
            addDependency();
            addAnnotation();
            String fullyQualifiedName = getFullyQualifiedName();
            assertCodeTransformation();
            assertThat(fullyQualifiedName).isEqualTo("javax.validation.constraints.Email");
        }

        private ProjectContext createProjectContext() {
            return TestProjectContext
                    .buildProjectContext(beanFactory)
                    .withJavaSource("src/main/java", JAVA_SOURCE_CODE)
                    .build();
        }

        private void addAnnotation() {
            context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get()
                    .getTypes().get(0)
                    .getMembers().get(0)
                    .addAnnotation("@Email", "javax.validation.constraints.Email");
        }

        private String getFullyQualifiedName() {
            return context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get()
                    .getTypes().get(0)
                    .getMembers().get(0)
                    .getAnnotations().get(0)
                    .getFullyQualifiedName();
        }

        private void addDependency() {
            context.getApplicationModules().getRootModule().getBuildFile().addDependency(
                    Dependency.builder()
                            .groupId("javax.validation")
                            .artifactId("validation-api")
                            .version("2.0.1.Final")
                            .build()
            );
        }

        private void assertCodeTransformation() {
            assertThat(context.getProjectJavaSources().findJavaSourceDeclaringType("com.acme.SomeClass").get().print())
                    .isEqualTo(
                            """
                                    package com.acme;
                                                                
                                    import javax.validation.constraints.Email;
                                                                
                                    class SomeClass {
                                        @Email  
                                        String someMember;
                                    }
                                    """);
        }
    }

    /*
     *
     *
     */
    @Test
    @DisplayName("flow")
    @ExpectedToFail("https://github.com/spring-projects/spring-rewrite-commons/issues/38")
    void flow() {
        // create project with four modules
        /*
                    parent
               /      |      \
        moduleA -> moduleB -> moduleC

         */
        String parent = PomBuilder.buildPom("com.example:parent:1.0")
                .withModules("moduleA", "moduleB", "moduleC")
                .build();

        String moduleC = PomBuilder.buildParentPom("com.example:parent:1.0", "com.example:moduleC:1.0").build();
        String moduleB = PomBuilder.buildParentPom("com.example:parent:1.0", "com.example:moduleB:1.0").unscopedDependencies("com.example:moduleC:1.0").build();
        String moduleA = PomBuilder.buildParentPom("com.example:parent:1.0", "com.example:moduleA:1.0").unscopedDependencies("com.example:moduleB:1.0").build();

        System.out.println(parent);
        System.out.println(moduleA);
        System.out.println(moduleB);
        System.out.println(moduleC);

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("pom.xml", parent)
                .withProjectResource("moduleA/pom.xml", moduleA)
                .withJavaSource("moduleA/src/main/java",
                        """
                                package com.example.a;
                                import com.example.b.B;
                                public class A {
                                    B b;
                                }
                                """
                )
                .withProjectResource("moduleB/pom.xml", moduleB)
                .withJavaSource("moduleB/src/main/java",
                        """
                                package com.example.b;
                                import com.example.c.C;
                                public class B {
                                    C c;
                                }
                                """
                )
                .withProjectResource("moduleC/pom.xml", moduleC)
                .withJavaSource("moduleC/src/main/java",
                        """
                                package com.example.c;
                                public class C {
                                }
                                """
                )
                .build();

        List<JavaSource> list = projectContext.getProjectJavaSources().list();
        List<String> classpathA = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.a.A").get().getResource().getSourceFile().getMarkers().findFirst(JavaSourceSet.class).get().getClasspath().stream().map(fq -> fq.getFullyQualifiedName()).toList();
        assertThat(classpathA).contains("com.example.b.B", "com.example.c.C");
        List<String> classpathB = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.b.B").get().getResource().getSourceFile().getMarkers().findFirst(JavaSourceSet.class).get().getClasspath().stream().map(fq -> fq.getFullyQualifiedName()).toList();
        assertThat(classpathB).contains("com.example.c.C");
        String typeFqNameA = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.a.A").get().getTypes().get(0).getMembers().get(0).getTypeFqName();
        assertThat(typeFqNameA).isEqualTo("com.example.b.B");

        // ok, cool
        // let's add validation-api to moduleC and see if types are available in dependant modules
        projectContext.getApplicationModules().getModule("moduleC").getBuildFile().addDependency(
                Dependency.builder()
                        .groupId("javax.validation")
                        .artifactId("validation-api")
                        .version("2.0.1.Final")
                        .build()
        );

        list = projectContext.getProjectJavaSources().list();
        classpathA = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.a.A").get().getResource().getSourceFile().getMarkers().findFirst(JavaSourceSet.class).get().getClasspath().stream().map(fq -> fq.getFullyQualifiedName()).toList();
        assertThat(classpathA).contains("com.example.b.B", "com.example.c.C", "javax.validation.constraints.Email");
        classpathB = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.b.B").get().getResource().getSourceFile().getMarkers().findFirst(JavaSourceSet.class).get().getClasspath().stream().map(fq -> fq.getFullyQualifiedName()).toList();
        assertThat(classpathB).contains("com.example.c.C");
        typeFqNameA = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.example.a.A").get().getTypes().get(0).getMembers().get(0).getTypeFqName();
        assertThat(typeFqNameA).isEqualTo("com.example.b.B");

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
