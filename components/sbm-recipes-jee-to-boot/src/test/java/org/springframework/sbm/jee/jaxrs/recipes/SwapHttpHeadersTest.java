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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SwapHttpHeadersTest {

    private final static String SPRING_VERSION = "5.3.13";

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    SwapHttHeaders r = new SwapHttHeaders();
                    context.getProjectJavaSources().apply(r);
                }
            };

    @Test
    void constants() {

        String javaSource = ""
                + "import javax.ws.rs.core.HttpHeaders;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       String s1 = HttpHeaders.COOKIE;\n"
                + "       String s2 = HttpHeaders.CONTENT_ID;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpHeaders;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       String s1 = HttpHeaders.COOKIE;\n"
                + "       String s2 = HttpHeaders.CONTENT_ID;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "javax:javaee-api:8.0",
                        "org.springframework:spring-core:"+SPRING_VERSION
                )
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void instanceMethods() {

        String javaSource = ""
                + "import javax.ws.rs.core.HttpHeaders;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "        HttpHeaders h;\n"
                + "        h.getAcceptableLanguages();\n"
                + "        h.getDate();\n"
                + "        h.getHeaderString(\"Accept\");\n"
                + "        h.getLanguage();\n"
                + "        h.getLength();\n"
                + "        h.getMediaType();\n"
                + "        h.getRequestHeader(\"Accept\");\n"
                + "        var all = h.getRequestHeaders();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpHeaders;\n"
                + "\n"
                + "import java.util.Date;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "        HttpHeaders h;\n"
                + "        h.getAcceptLanguageAsLocales();\n"
                + "        new Date(h.getDate());\n"
                + "        String.join(\", \", h.get(\"Accept\"));\n"
                + "        h.getContentLanguage();\n"
                + "        h.getContentLength();\n"
                + "        h.getContentType();\n"
                + "        h.get(\"Accept\");\n"
                + "        var all = h;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "javax:javaee-api:8.0",
                        "org.springframework:spring-web:"+SPRING_VERSION
                )
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }
}
