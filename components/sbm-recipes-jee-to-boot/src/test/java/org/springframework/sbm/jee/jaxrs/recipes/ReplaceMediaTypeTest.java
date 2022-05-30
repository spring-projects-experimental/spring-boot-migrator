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

import org.openrewrite.java.JavaParser;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReplaceMediaTypeTest {

    private final static String SPRING_VERSION = "5.3.13";

    private Supplier<JavaParser> javaParserSupplier = () -> new RewriteJavaParser(new ApplicationProperties());

    final private AbstractAction action = new AbstractAction() {
        @Override
        public void apply(ProjectContext context) {
            ReplaceMediaType r = new ReplaceMediaType(javaParserSupplier);
            context.getProjectJavaSources().apply(r);
        }
    };

    @Test
    void replaceMediaTypeConstant_with_removed_import() {
        String sourceCode =
                "import javax.ws.rs.core.MediaType;\n" +
                        "\n" +
                        "class ControllerClass {\n" +
                        "    public String getHelloWorldJSON(String name) {\n" +
                        "        return MediaType.APPLICATION_XML;\n" +
                        "    }\n" +
                        "\n" +
                        "}";

        String expected = "import org.springframework.http.MediaType;\n" +
                "\n" +
                "class ControllerClass {\n" +
                "    public String getHelloWorldJSON(String name) {\n" +
                "        return MediaType.APPLICATION_XML_VALUE;\n" +
                "    }\n" +
                "\n" +
                "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
                        "org.springframework:spring-core:"+SPRING_VERSION
                )
                .build();

        ReplaceMediaType sut = new ReplaceMediaType(javaParserSupplier);
        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        javaSource.apply(sut);

        assertThat(javaSource.print()).isEqualTo(expected);
    }

    @Test
    void constants() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;\n"
                + "       String s1 = MediaType.APPLICATION_ATOM_XML;\n"
                + "       \n"
                + "       MediaType m2 = MediaType.APPLICATION_FORM_URLENCODED_TYPE;\n"
                + "       String s2 = MediaType.APPLICATION_FORM_URLENCODED;\n"
                + "       \n"
                + "       MediaType m3 = MediaType.APPLICATION_JSON_TYPE;\n"
                + "       String s3 = MediaType.APPLICATION_JSON;\n"
                + "       \n"
                + "       MediaType m4 = MediaType.APPLICATION_JSON_PATCH_JSON_TYPE;\n"
                + "       String s4 = MediaType.APPLICATION_JSON_PATCH_JSON;\n"
                + "       \n"
                + "       MediaType m5 = MediaType.APPLICATION_OCTET_STREAM_TYPE;\n"
                + "       String s5 = MediaType.APPLICATION_OCTET_STREAM;\n"
                + "       \n"
                + "       MediaType m7 = MediaType.APPLICATION_SVG_XML_TYPE;\n"
                + "       String s7 = MediaType.APPLICATION_SVG_XML;\n"
                + "       \n"
                + "       MediaType m8 = MediaType.APPLICATION_XHTML_XML_TYPE;\n"
                + "       String s8 = MediaType.APPLICATION_XHTML_XML;\n"
                + "       \n"
                + "       MediaType m9 = MediaType.APPLICATION_XML_TYPE;\n"
                + "       String s9 = MediaType.APPLICATION_XML;\n"
                + "       \n"
                + "       MediaType m10 = MediaType.MULTIPART_FORM_DATA_TYPE;\n"
                + "       String s10 = MediaType.MULTIPART_FORM_DATA;\n"
                + "       \n"
                + "       MediaType m11 = MediaType.SERVER_SENT_EVENTS_TYPE;\n"
                + "       String s11 = MediaType.SERVER_SENT_EVENTS;\n"
                + "       \n"
                + "       MediaType m12 = MediaType.TEXT_HTML_TYPE;\n"
                + "       String s12 = MediaType.TEXT_HTML;\n"
                + "       \n"
                + "       MediaType m13 = MediaType.TEXT_PLAIN_TYPE;\n"
                + "       String s13 = MediaType.TEXT_PLAIN;\n"
                + "       \n"
                + "       MediaType m14 = MediaType.TEXT_XML_TYPE;\n"
                + "       String s14 = MediaType.TEXT_XML;\n"
                + "       \n"
                + "       MediaType m15 = MediaType.WILDCARD_TYPE;\n"
                + "       String s15 = MediaType.WILDCARD;\n"
                + "       \n"
                + "       String s16 = MediaType.CHARSET_PARAMETER;\n"
                + "       \n"
                + "       String s17 = MediaType.MEDIA_TYPE_WILDCARD;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.util.MimeType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML;\n"
                + "       String s1 = MediaType.APPLICATION_ATOM_XML_VALUE;\n"
                + "       \n"
                + "       MediaType m2 = MediaType.APPLICATION_FORM_URLENCODED;\n"
                + "       String s2 = MediaType.APPLICATION_FORM_URLENCODED_VALUE;\n"
                + "       \n"
                + "       MediaType m3 = MediaType.APPLICATION_JSON;\n"
                + "       String s3 = MediaType.APPLICATION_JSON_VALUE;\n"
                + "       \n"
                + "       MediaType m4 = MediaType.APPLICATION_JSON_PATCH_JSON;\n"
                + "       String s4 = MediaType.APPLICATION_JSON_PATCH_JSON_VALUE;\n"
                + "       \n"
                + "       MediaType m5 = MediaType.APPLICATION_OCTET_STREAM;\n"
                + "       String s5 = MediaType.APPLICATION_OCTET_STREAM_VALUE;\n"
                + "       \n"
                + "       MediaType m7 = MediaType.APPLICATION_SVG_XML;\n"
                + "       String s7 = MediaType.APPLICATION_SVG_XML_VALUE;\n"
                + "       \n"
                + "       MediaType m8 = MediaType.APPLICATION_XHTML_XML;\n"
                + "       String s8 = MediaType.APPLICATION_XHTML_XML_VALUE;\n"
                + "       \n"
                + "       MediaType m9 = MediaType.APPLICATION_XML;\n"
                + "       String s9 = MediaType.APPLICATION_XML_VALUE;\n"
                + "       \n"
                + "       MediaType m10 = MediaType.MULTIPART_FORM_DATA;\n"
                + "       String s10 = MediaType.MULTIPART_FORM_DATA_VALUE;\n"
                + "       \n"
                + "       MediaType m11 = MediaType.TEXT_EVENT_STREAM;\n"
                + "       String s11 = MediaType.TEXT_EVENT_STREAM_VALUE;\n"
                + "       \n"
                + "       MediaType m12 = MediaType.TEXT_HTML;\n"
                + "       String s12 = MediaType.TEXT_HTML_VALUE;\n"
                + "       \n"
                + "       MediaType m13 = MediaType.TEXT_PLAIN;\n"
                + "       String s13 = MediaType.TEXT_PLAIN_VALUE;\n"
                + "       \n"
                + "       MediaType m14 = MediaType.TEXT_XML;\n"
                + "       String s14 = MediaType.TEXT_XML_VALUE;\n"
                + "       \n"
                + "       MediaType m15 = MediaType.ALL;\n"
                + "       String s15 = MediaType.ALL_VALUE;\n"
                + "       \n"
                + "       String s16 = MimeType.PARAM_CHARSET;\n"
                + "       \n"
                + "       String s17 = MimeType.WILDCARD_TYPE;\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void instanceMethodIsCompatible() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public boolean respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;\n"
                + "       return MediaType.APPLICATION_XML_TYPE.isCompatible(m1);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public boolean respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML;\n"
                + "       return MediaType.APPLICATION_XML.isCompatibleWith(m1);\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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

    @Test
    void instanceMethodWithCharset() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;\n"
                + "       return m1.withCharset(\"UTF-8\");\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "import java.nio.charset.Charset;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       MediaType m1 = MediaType.APPLICATION_ATOM_XML;\n"
                + "       return new MediaType(m1, Charset.forName(\"UTF-8\"));\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void instanceMethodWithCharset2() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return MediaType.APPLICATION_ATOM_XML_TYPE.withCharset(\"UTF-8\");\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "import java.nio.charset.Charset;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(MediaType.APPLICATION_ATOM_XML, Charset.forName(\"UTF-8\"));\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void constructor1() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(\"foo\", \"bar\");\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(\"foo\", \"bar\");\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void constructor2() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       String type = \"foo\";\n"
                + "       return new MediaType(type, \"bar\", \"UTF-8\");\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "import java.nio.charset.Charset;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       String type = \"foo\";\n"
                + "       return new MediaType(type, \"bar\", Charset.forName(\"UTF-8\"));\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void constructor3() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.util.MimeType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE);\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void constructor4() {

        String javaSource = ""
                + "import javax.ws.rs.core.MediaType;\n"
                + "import java.util.Map;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(\"blah\", \"UTF-8\", Map.of(\"foo\", \"bar\"));\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "\n"
                + "import java.util.Map;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public MediaType respond() {\n"
                + "       return new MediaType(\"blah\", \"UTF-8\", Map.of(\"foo\", \"bar\"));\n"
                + "    }\n"
                + "}\n"
                + "";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
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
    void replaceMediaTypeConstant() {

        String sourceCode =
                "import javax.ws.rs.Path;\n" +
                        "import javax.ws.rs.Consumes;\n" +
                        "import javax.ws.rs.POST;\n" +
                        "import javax.ws.rs.PUT;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "import javax.ws.rs.Produces;\n" +
                        "import javax.ws.rs.core.MediaType;\n" +
                        "\n" +
                        "\n" +
                        "@Path(\"/hello\")\n" +
                        "class ControllerClass {\n" +
                        "    @POST\n" +
                        "    @PUT\n" +
                        "    @Path(\"/json/{name}\")\n" +
                        "    @Produces({\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML})\n" +
                        "    @Consumes(\"application/json\")\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";

        String expected =
                "import javax.ws.rs.Path;\n" +
                        "\n" +
                        "import javax.ws.rs.Consumes;\n" +
                        "\n" +
                        "import org.springframework.http.MediaType;\n" +
                        "import javax.ws.rs.POST;\n" +
                        "import javax.ws.rs.PUT;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "import javax.ws.rs.Produces;\n" +
                        "\n" +
                        "\n" +
                        "@Path(\"/hello\")\n" +
                        "class ControllerClass {\n" +
                        "    @POST\n" +
                        "    @PUT\n" +
                        "    @Path(\"/json/{name}\")\n" +
                        "    @Produces({\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML_VALUE})\n" +
                        "    @Consumes(\"application/json\")\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
                        "org.springframework:spring-core:"+SPRING_VERSION
                )
                .build();

        ReplaceMediaType r = new ReplaceMediaType(javaParserSupplier);
        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        javaSource.apply(r);

        assertThat(javaSource.print()).isEqualTo(expected);
    }
}
