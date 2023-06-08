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
package org.springframework.sbm.java.migration.actions;

import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReplaceTypeActionTest {

    @Mock
    private ProjectContext projectContext;

    @Mock
    private ProjectJavaSources projectJavaSources;



    @Test
    void test() {
        String annotation = "annotationToBeReplaced";
        String withAnnotation = "annotationReplacing";
        ReplaceTypeAction sut = new ReplaceTypeAction();
        sut.setExistingType(annotation);
        sut.setWithType(withAnnotation);

        Mockito.when(projectContext.getProjectJavaSources()).thenReturn(projectJavaSources);

        sut.apply(projectContext);

        Mockito.verify(projectJavaSources).replaceType(annotation, withAnnotation);
    }

    @Test
    void replaceTypeOfMethodParameter() {
        String given =
                "import javax.ws.rs.core.MediaType;\n" +
                "import javax.ws.rs.PathParam;\n" +
                "\n" +
                "class ControllerClass {             \n" +
                "    public String getHelloWorldJSON(@PathParam(\"name\") String name) {\n" +
                "        return MediaType.APPLICATION_XML;\n" +
                "    }\n" +
                "}";

        String expected =
                "import org.springframework.web.bind.annotation.PathVariable;\n" +
                "\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "\n" +
                "class ControllerClass {             \n" +
                "    public String getHelloWorldJSON(@PathVariable(\"name\") String name) {\n" +
                "        return MediaType.APPLICATION_XML;\n" +
                "    }\n" +
                "}";

        ReplaceTypeAction sut = new ReplaceTypeAction();
        sut.setExistingType("javax.ws.rs.PathParam");
        sut.setWithType("org.springframework.web.bind.annotation.PathVariable");

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(given)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
                        "org.springframework.boot:spring-boot-starter-web:2.4.2")
                .build();

        sut.apply(projectContext);
        Assertions.assertThat(expected).isEqualTo(projectContext.getProjectJavaSources().list().get(0).print());
    }

}
