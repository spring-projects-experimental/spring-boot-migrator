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
package org.springframework.sbm.project.buildfile;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.maven.tree.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.java.impl.DependenciesChangedEventHandler;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the inner workings when adding a dependency
 *
 * @author Fabian Krüger
 */
@SpringBootTest(classes ={
        DependenciesChangedEventHandler.class,
        ProjectContextHolder.class,
        RewriteJavaParser.class,
        SbmApplicationProperties.class
})
@Disabled("See comment in test")
public class AddDependencyTest {

    @Autowired
    DependenciesChangedEventHandler handler;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    ProjectContextHolder contextHolder;

    @Autowired
    RewriteJavaParser rewriteJavaParser;

    /**
     * Test that after adding a dependency...
     *
     * * The ClasspathRegistry was updated
     * * The types are in the type cache of encapsulated JavaParser
     * * The new types can be resolved
     *
     * Also see {@link DependenciesChangedEventHandler}
     */
    @Test
    void whenDependencyIsAdded_thenJavaParserTypeCacheGetsUpdated() {
        // simple ProjectContext
        String javaSourceCode = "import javax.validation.constraints.Email; class Y {@Email String email;}";
        ProjectContext context = TestProjectContext
                .buildProjectContext(eventPublisher, rewriteJavaParser)
                .addJavaSource("src/main/java", javaSourceCode)
                .build();
        // provide ProjectContext to Spring beans
        contextHolder.setProjectContext(context);

        String javaxValidationEmail = "javax.validation.constraints.Email";
        HashMap<Object, JavaType> typeCache0 = retrieveTypeCache(rewriteJavaParser.getJavaParser());
        assertThat(typeCache0).isEmpty();

        // Parse the java source to fill the type cache
        rewriteJavaParser.parse(javaSourceCode);
        HashMap<Object, JavaType> typeCache = retrieveTypeCache(rewriteJavaParser.getJavaParser());
        assertThat(typeCache).hasSize(17_590);

        // javax.validation.Email not in type cache
        Optional<String> s = findInTypeCache(typeCache, javaxValidationEmail);
        assertThat(s).isNotPresent();

        // Classpath empty
        assertThat(ClasspathRegistry.getInstance().getCurrentDependencies()).isEmpty();


        // add dependency
        BuildFile buildFile = context.getBuildFile();
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
        //rewriteJavaParser.getJavaParser().parse(javaSourceCode);
        context.getApplicationModules().getRootModule().getMainJavaSourceSet().addJavaSource(TestProjectContext.getDefaultProjectRoot(),
                                                                                                  Path.of("src/main/java"),
                                                                                                  "import javax.validation.constraints.Email; class X {@Email String email;}");

        // The Email annotation can now be resolved
        HashMap<Object, JavaType> typeCacheAfter = retrieveTypeCache(rewriteJavaParser.getJavaParser());
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
