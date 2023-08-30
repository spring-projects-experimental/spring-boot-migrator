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

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OpenRewriteAnnotationTest {

    // TODO: Cleanup / rework to test one aspect per test method
    // TODO: test printXXX() methods thoroughly

    @Test
    void getAttributes() {
        String value = "for a reason";
        String source =
                "import org.junit.jupiter.api.Disabled;\n" +
                        "@Disabled(\"" + value + "\")\n" +
                        "public class Foo {}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(source)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        Map<String, Expression> attributes = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).getAnnotations().get(0).getAttributes();
        assertThat(attributes).hasSize(1);
        assertThat(attributes.get("value").print()).isEqualTo("\"" + value + "\"");
    }

    @Test
    void getAttribute() {
        String value = "for a reason";
        String source =
                "import org.junit.jupiter.api.Disabled;\n" +
                        "@Disabled(\"" + value + "\")\n" +
                        "public class Foo {}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(source)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        Expression attribute = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).getAnnotations().get(0).getAttribute("value");
        assertThat(attribute.print()).isEqualTo("\"" + value + "\"");
    }

    @Test
    void getFullyQualifiedName() {
        String source =
                "import org.junit.jupiter.api.Disabled;\n" +
                        "@Disabled\n" +
                        "public class Foo {}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(source)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        Annotation annotation = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).getAnnotations().get(0);
        assertThat(annotation.getFullyQualifiedName()).isEqualTo("org.junit.jupiter.api.Disabled");
    }

    @Test
    void getWrapped() {
        String source =
                "import org.junit.jupiter.api.Disabled;\n" +
                        "@Disabled\n" +
                        "public class Foo {}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(source)
                .withBuildFileHavingDependencies("org.junit.jupiter:junit-jupiter-api:5.7.0")
                .build();

        Type type = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0);
        OpenRewriteAnnotation annotation = (OpenRewriteAnnotation) type.getAnnotations().get(0);
        J.Annotation orAnnotation = ((OpenRewriteAnnotation) type.getAnnotations().get(0)).getWrapped();
        Assertions.assertThat(annotation.getWrapped()).isSameAs(orAnnotation);
    }

    @Test
    public void expressionAnnotationValue() throws Exception {
        String sourceCode = "" +
                "import javax.ws.rs.Path;           		\n" +
                "                                   		\n" +
                "class Constants {             				\n" +
                " static final String PATH = \"/hello\"; 	\n" +
                "}                                  		\n" +
                "                                   		\n" +
                "@Path(value=Constants.PATH)            	\n" +
                "class AnnotatedClass {             		\n" +
                "}                                  		\n" +
                "                                   		\n" +
                "@Path(Constants.PATH)                	    \n" +
                "class AnotherClass {               		\n" +
                "}                                  		\n";


        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final")
                .build();

        Type type = projectContext.getProjectJavaSources().list().get(0).getTypes().get(1);
        Annotation annotatedClassAnnotation = type.getAnnotations().get(0);
        assertThat(annotatedClassAnnotation.getFullyQualifiedName()).isEqualTo("javax.ws.rs.Path");
        assertThat(annotatedClassAnnotation.getAttribute("value").printAssignment()).isEqualTo("Constants.PATH");
        assertThat(annotatedClassAnnotation.getAttribute("value").printVariable()).isEqualTo("value");

        Annotation anotherClassAnnotation = type.getAnnotations().get(0);
        assertThat(anotherClassAnnotation.getFullyQualifiedName()).isEqualTo("javax.ws.rs.Path");
        assertThat(anotherClassAnnotation.getAttribute("value").printAssignment()).isEqualTo("Constants.PATH");
        assertThat(anotherClassAnnotation.getAttribute("value").printVariable()).isEqualTo("value");
    }

    @Test
    public void testAnnotationGetValueAttribute() throws Exception {
        String sourceCode = "" +
                "import javax.ejb.Stateless;        \n" +
                "                                   \n" +
                "@Stateless(name=\"blah\")          \n" +
                "class ComponentClass {             \n" +
                "}                                  \n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build();

        Annotation a = projectContext.getProjectJavaSources().list().get(0).getTypes().get(0).getAnnotations().get(0);
        assertThat(a.getAttribute("name").printAssignment()).isEqualTo("\"blah\""); // FIXME... ?!
        assertThat(a.getAttribute("name").print()).isEqualTo("name=\"blah\"");
    }


    @Test
    public void testAnnotationGetStringAttribute() {

        String sourceCode = "" +
                "import javax.ws.rs.Path;           \n" +
                "import javax.ejb.Stateless;        \n" +
                "                                   \n" +
                "@Path(value=\"/hello\")            \n" +
                "class AnnotatedClass {}            \n" +
                "                                   \n" +
                "@Path(\"/another\")                \n" +
                "class AnotherClass {}              \n";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2",
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final"
                )
                .withJavaSources(sourceCode)
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);

        Annotation annotatedClassAnnotation = javaSource.getTypes().get(0).getAnnotations().get(0);

        assertThat(annotatedClassAnnotation.getAttribute("value").printAssignment()).isEqualTo("\"/hello\"");
        assertThat(annotatedClassAnnotation.getAttribute("foo")).isNull();

        Annotation anotherClassAnnotation = javaSource.getTypes().get(1).getAnnotations().get(0);
        assertThat(anotherClassAnnotation.getAttribute("value").printAssignment()).isEqualTo("\"/another\"");
        assertThat(anotherClassAnnotation.getAttribute("foo")).isNull();

    }
}