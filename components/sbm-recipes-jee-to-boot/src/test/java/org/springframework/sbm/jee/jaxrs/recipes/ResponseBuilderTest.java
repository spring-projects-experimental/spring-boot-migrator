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

import org.openrewrite.java.tree.J;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBuilderTest {

    private final static String SPRING_VERSION = "5.3.13";

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    ReplaceResponseEntityBuilder r = new ReplaceResponseEntityBuilder();
                    context.getProjectJavaSources().apply(r);
                }
            };

    @Test
    void allow() {

        String javaSource = ""
                + "import java.util.Set;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.allow(\"POST\", \"PUT\");\n"
                + "        b.allow(Set.of(\"GET\"));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import java.util.Set;\n"
                + "\n"
                + "import org.springframework.http.HttpMethod;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        b.allow(HttpMethod.resolve(\"POST\"), HttpMethod.resolve(\"PUT\"));\n"
                + "        b.allow(Set.of(\"GET\").stream().map(HttpMethod::resolve).toArray(String[]::new));\n"
                + "        return b;\n"
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
    void expires() {

        String javaSource = ""
                + "import java.util.Date;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.expires(new Date(100000));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import java.util.Date;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        b.headers(h -> h.setExpires(new Date(100000).toInstant()));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:8.0")
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void language() {

        String javaSource = ""
                + "import java.util.Locale;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.language(\"ua\");\n"
                + "        b.language(Locale.ITALY);\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import java.util.Locale;\n"
                + "\n"
                + "import org.springframework.http.HttpHeaders;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        b.headers(h -> h.set(HttpHeaders.CONTENT_LANGUAGE, \"ua\"));\n"
                + "        b.headers(h -> h.setContentLanguage(Locale.ITALY));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "javax:javaee-api:8.0",
                        "org.springframework:spring-web:5.3.18")
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void lastModified() {

        String javaSource = ""
                + "import java.util.Date;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.lastModified(new Date(100000));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import java.util.Date;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        b.lastModified(new Date(100000).toInstant());\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:8.0")
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void replaceAll() {

        String javaSource = ""
                + "import javax.ws.rs.core.MultivaluedMap;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        MultivaluedMap m;\n"
                + "        b.replaceAll(m);\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import javax.ws.rs.core.MultivaluedMap;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        MultivaluedMap m;\n"
                + "        b.headers(h -> {\n"
                + "            h.clear();\n"
                + "            h.addAll(m);\n"
                + "        });\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "javax:javaee-api:8.0",
                        "org.springframework:spring-core:"+SPRING_VERSION)
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();
        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

    @Test
    void tag() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "       ResponseBuilder b;\n"
                + "       b.tag(\"foo\");\n"
                + "       return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "       ResponseEntity.BodyBuilder b;\n"
                + "       b.eTag(\"foo\");\n"
                + "       return b;\n"
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
    void type() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder test() {\n"
                + "        ResponseBuilder b;\n"
                + "        b.type(MediaType.APPLICATION_JSON_TYPE);\n"
                + "        b.type(\"application/json\");\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity.BodyBuilder test() {\n"
                + "        ResponseEntity.BodyBuilder b;\n"
                + "        b.contentType(MediaType.APPLICATION_JSON_TYPE);\n"
                + "        b.headers(h -> h.set(HttpHeaders.CONTENT_TYPE, \"application/json\"));\n"
                + "        return b;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax:javaee-api:8.0")
                .withJavaSources(javaSource)
                .build();

        action.apply(projectContext);

        String actual = projectContext.getProjectJavaSources().list().get(0).print();

        // verify it compiles
        List<J.CompilationUnit> parse = new RewriteJavaParser(new SbmApplicationProperties(),
                                                              executionContext).parse(actual);

        assertThat(actual)
                .as(TestDiff.of(actual, expected))
                .isEqualTo(expected);
    }

}
