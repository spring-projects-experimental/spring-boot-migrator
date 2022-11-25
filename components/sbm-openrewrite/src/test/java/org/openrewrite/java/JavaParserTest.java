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
package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.java.search.FindAnnotations;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class JavaParserTest {
    @Test
    void test_renameMe() {

        List<Path> classpathFiles = OpenRewriteTestSupport.getClasspathFiles("javax.validation:validation-api:2.0.1.Final");

        JavaParser javaParser = JavaParser
                .fromJavaVersion()
                .classpath(classpathFiles)
                .build();


        J.CompilationUnit compilationUnit = javaParser
                .parse("""
                       import javax.validation.constraints.Email;
                       public class Cat {
                           @Email
                           private String email;
                       }
                       """)
                .get(0);

        FindAnnotations findAnnotation = new FindAnnotations("@javax.validation.constraints.Email", true);
        List<Result> results = findAnnotation.run(List.of(compilationUnit)).getResults();
        assertThat(results).hasSize(1);
    }
}
