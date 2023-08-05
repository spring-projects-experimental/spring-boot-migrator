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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatelessAnnotationTemplateMapperTest {

    private final StatelessAnnotationTemplateMapper sut = new StatelessAnnotationTemplateMapper();

    // TODO: deal with something like...
    /*
     * import foo.bar.BeanNames;
     * ...
     * @Stateless(name=BeanNames.ThisBean)
     */

    @Test
    void shouldCreateTemplateStringForStatelessAnnotation_beanName() {

        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        "import javax.ejb.Stateless;\n" +
                                "@Stateless(mappedName=\"theMappedName\")\n" +
                                "public class TheBean {}")
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation statelessAnnotation = openRewriteJavaSource.getTypes().get(0).getAnnotations().get(0);
        String annotationToTemplate = sut.mapToServiceAnnotation(statelessAnnotation);

        assertThat(annotationToTemplate).isEqualTo(
                "@Service"
        );
    }

    @Test
    void shouldCreateTemplateStringForStatelessAnnotation_name() {

        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        "import javax.ejb.Stateless;\n" +
                                "@Stateless(mappedName=\"theMappedName\", name=\"beanName\")\n" +
                                "public class TheBean {}")
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation statelessAnnotation = openRewriteJavaSource.getTypes().get(0).getAnnotations().get(0);
        String annotationToTemplate = sut.mapToServiceAnnotation(statelessAnnotation);

        assertThat(annotationToTemplate).isEqualTo(
                "@Service(\"beanName\")"
        );
    }

    @Test
    void shouldCreateTemplateStringForStatelessAnnotation_description() {

        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        "import javax.ejb.Stateless;\n" +
                                "@Stateless(description=\"a description\")\n" +
                                "public class TheBean {}")
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation statelessAnnotation = openRewriteJavaSource.getTypes().get(0).getAnnotations().get(0);
        String annotationToTemplate = sut.mapToServiceAnnotation(statelessAnnotation);

        assertThat(annotationToTemplate).isEqualTo(
                "/**\n" +
                        "* a description\n" +
                        "*/\n" +
                        "@Service"
        );
    }


    @Test
    void shouldCreateTemplateStringForStatelessAnnotation_allAttributes() {

        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        "import javax.ejb.Stateless;\n" +
                                "@Stateless(description=\"a description\", mappedName=\"theMappedName\", name=\"beanName\")\n" +
                                "public class TheBean {}")
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation statelessAnnotation = openRewriteJavaSource.getTypes().get(0).getAnnotations().get(0);
        String annotationToTemplate = sut.mapToServiceAnnotation(statelessAnnotation);

        assertThat(annotationToTemplate).isEqualTo(
                "/**\n" +
                        "* a description\n" +
                        "*/\n" +
                        "@Service(\"beanName\")"
        );
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidAnnotation() {
        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        "@Deprecated\n" +
                                "public class TheBean {}")
                .build()
                .getProjectJavaSources()
                .list()
                .get(0);

        Annotation statelessAnnotation = openRewriteJavaSource.getTypes().get(0).getAnnotations().get(0);

        assertThrows(IllegalArgumentException.class, () -> sut.mapToServiceAnnotation(statelessAnnotation));
    }
}