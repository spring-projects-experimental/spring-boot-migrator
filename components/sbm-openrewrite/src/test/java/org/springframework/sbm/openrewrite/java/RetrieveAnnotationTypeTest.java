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
package org.springframework.sbm.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RetrieveAnnotationTypeTest {
    @Test
    void retrieveAnnotation() {
        String javaSource =
                "import javax.ejb.Stateless;\n" +
                "@Stateless\n" +
                "public class MyClass {" +
                "}";

//        String mavenRepo = System.getProperty("user.home") + "/.m2/repository";
//        List<Path> paths = JavaParser.dependenciesFromClasspath("ejb-api");
//        List<Path> paths = JavaParser.dependenciesFromClasspath("javax/ejb/javax.ejb-api/3.2/javax.ejb-api-3.2.jar");

        List<Path> classpathFiles = OpenRewriteTestSupport.getClasspathFiles("javax.ejb:javax.ejb-api:3.2");

        JavaParser javaParser = JavaParser
                .fromJavaVersion()
                .classpath(classpathFiles)
                .build();

        List<J.Annotation> leadingAnnotations = javaParser.parse(javaSource).get(0).getClasses().get(0).getLeadingAnnotations();
        JavaType.Class type = JavaType.Class.class.cast(leadingAnnotations.get(0).getType());
        assertThat(type.getFullyQualifiedName()).isEqualTo("javax.ejb.Stateless");
    }

}
