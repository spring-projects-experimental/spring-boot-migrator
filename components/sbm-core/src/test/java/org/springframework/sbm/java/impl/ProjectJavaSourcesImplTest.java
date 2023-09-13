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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Fabian Krüger
 */
class ProjectJavaSourcesImplTest {

    @Test
    void findTypesImplementing() {
        ProjectContext context = TestProjectContext.buildProjectContext().withJavaSource("src/main/java", """
                package com.example;
                                        
                public interface TheInterface {}
                """).withJavaSource("src/main/java", """
                package com.example;
                                        
                public class TheClass implements TheInterface {}
                """).build();

        List<JavaSourceAndType> typesImplementingInterface = context
                .getProjectJavaSources()
                .findTypesImplementing("com.example.TheInterface");

        assertThat(typesImplementingInterface).isNotEmpty();
        assertThat(typesImplementingInterface.get(0).getType().getFullyQualifiedName()).isEqualTo("com.example.TheClass");
    }

    @Test
    void findTypesImplementingInterfaceFromJdk() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", """
                package com.example;
                
                import java.io.Serializable;
                                        
                public class TheClass implements Serializable {}
                """).build();

        List<JavaSourceAndType> typesImplementingInterface = context
                .getProjectJavaSources()
                .findTypesImplementing("java.io.Serializable");

        assertThat(typesImplementingInterface).isNotEmpty();
        assertThat(typesImplementingInterface.get(0).getType().getFullyQualifiedName()).isEqualTo("com.example.TheClass");
    }

    @Test
    void findTypesImplementingWithInterfaceFromJar() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.23")
                .withJavaSource("src/main/java", """
                        package com.example;
                        
                        import org.springframework.beans.BeansException;
                        import org.springframework.context.ApplicationContext;
                        import org.springframework.context.ApplicationContextAware;
                                        
                        public class TheClass implements ApplicationContextAware {                                                                      
                            @Override
                            public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                            }
                        }
                        """).build();

        List<JavaSourceAndType> typesImplementingInterface = context
                .getProjectJavaSources()
                .findTypesImplementing("org.springframework.context.ApplicationContextAware");

        assertThat(typesImplementingInterface).isNotEmpty();
        assertThat(typesImplementingInterface.get(0).getType().getFullyQualifiedName()).isEqualTo("com.example.TheClass");
    }

    @Test
    void whenClassInheritsParameterizedInterfaceButNoResolvedType() {
        @Language("java") String sourceCode =
                """
                package a.b.c;
                import a.b.c.K;
                
                interface SomeClass extends K<String, String> {         
                }
                """;


        assertThrows(UnsatisfiedDependencyException.class, () ->
            TestProjectContext.buildProjectContext().withJavaSources(sourceCode).build()
        ).getMessage().contains("src/main/java/a/b/c/SomeClass.java:[2,13] cannot find symbol");

    }
}
