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
package org.openrewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.internal.TypesInUse;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class JavaParserTest {
    @Test
    @DisplayName("shouldHaveTypeInUse")
    @Disabled("Examination test")
    void shouldHaveTypeInUse() {

        String localM2Repo = Path.of(System.getProperty("user.home")).resolve(".m2/repository").toString();
        List<Path> classpath = List.of(
                Path.of(localM2Repo + "/org/springframework/boot/spring-boot-starter/3.1.1/spring-boot-starter-3.1.1.jar"),
                Path.of(localM2Repo + "/org/springframework/boot/spring-boot/3.1.1/spring-boot-3.1.1.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-context/6.0.10/spring-context-6.0.10.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-aop/6.0.10/spring-aop-6.0.10.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-beans/6.0.10/spring-beans-6.0.10.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-expression/6.0.10/spring-expression-6.0.10.jar"),
                Path.of(localM2Repo + "/org/springframework/boot/spring-boot-autoconfigure/3.1.1/spring-boot-autoconfigure-3.1.1.jar"),
                Path.of(localM2Repo + "/org/springframework/boot/spring-boot-starter-logging/3.1.1/spring-boot-starter-logging-3.1.1.jar"),
                Path.of(localM2Repo + "/ch/qos/logback/logback-classic/1.4.8/logback-classic-1.4.8.jar"),
                Path.of(localM2Repo + "/ch/qos/logback/logback-core/1.4.8/logback-core-1.4.8.jar"),
                Path.of(localM2Repo + "/org/slf4j/slf4j-api/2.0.7/slf4j-api-2.0.7.jar"),
                Path.of(localM2Repo + "/org/apache/logging/log4j/log4j-to-slf4j/2.20.0/log4j-to-slf4j-2.20.0.jar"),
                Path.of(localM2Repo + "/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar"),
                Path.of(localM2Repo + "/org/slf4j/jul-to-slf4j/2.0.7/jul-to-slf4j-2.0.7.jar"),
                Path.of(localM2Repo + "/jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-core/6.0.10/spring-core-6.0.10.jar"),
                Path.of(localM2Repo + "/org/springframework/spring-jcl/6.0.10/spring-jcl-6.0.10.jar"),
                Path.of(localM2Repo + "/org/yaml/snakeyaml/1.33/snakeyaml-1.33.jar")
        );
        JavaTypeCache javaTypeCache = new JavaTypeCache();
        SourceFile sourceFile = JavaParser.fromJavaVersion().classpath(classpath).typeCache(javaTypeCache)
                .build()
                .parse("""
                package com.example;
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                                            
                @SpringBootApplication
                public class MyMain {
                    public static void main(String[] args){
                        SpringApplication.run(MyMain.class, args);
                    }
                }
                """)
                .toList()
                .get(0);

        J.CompilationUnit compilationUnit = (J.CompilationUnit) sourceFile;
        List<String> typesInUse = compilationUnit.getTypesInUse().getTypesInUse().stream().map(s -> s.toString()).toList();
        assertThat(typesInUse).contains("org.springframework.boot.SpringApplication", "org.springframework.boot.SpringApplication", "com.example.MyMain");
        JavaSourceSet main = JavaSourceSet.build("main", classpath, javaTypeCache, true);
        List<String> typesOnClasspath = main.getClasspath().stream().map(JavaType.FullyQualified::getFullyQualifiedName).toList();
        assertThat(typesOnClasspath).doesNotContain("com.example.MyMain"); // By design

        javaTypeCache.put("com.example.MyMain", sourceFile);

        main = JavaSourceSet.build("main", classpath, javaTypeCache, true);
        List<JavaType.FullyQualified> mainCp = main.getClasspath();
        TypesInUse typesInUseBefore = compilationUnit.getTypesInUse();

        typesInUse = typesInUseBefore.getTypesInUse().stream().map(s -> s.toString()).toList();
        assertThat(typesInUse).contains("org.springframework.boot.SpringApplication", "org.springframework.boot.SpringApplication", "com.example.MyMain");
        compilationUnit.getClasses().stream().map(c -> c.getType()).forEach(mainCp::add);

        main = main.withClasspath(mainCp);
        assertThat(main.getClasspath().stream().map(JavaType.FullyQualified::getFullyQualifiedName).toList()).contains("org.springframework.boot.SpringApplication");
        assertThat(main.getClasspath().stream().map(JavaType.FullyQualified::getFullyQualifiedName).toList()).contains("com.example.MyMain");
    }
}
