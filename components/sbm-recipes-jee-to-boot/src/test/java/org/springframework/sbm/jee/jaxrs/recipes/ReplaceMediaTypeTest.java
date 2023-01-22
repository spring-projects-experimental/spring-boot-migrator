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
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class ReplaceMediaTypeTest {

    private final static String SPRING_VERSION = "5.3.13";

    private final Supplier<JavaParser> javaParserSupplier = () -> new RewriteJavaParser(new SbmApplicationProperties());

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
                """
                import javax.ws.rs.core.MediaType;
                class ControllerClass {
                    public String getHelloWorldJSON(String name) {
                        return MediaType.APPLICATION_XML;
                    }

                }""";

        String expected = """
                import org.springframework.http.MediaType;

                class ControllerClass {
                    public String getHelloWorldJSON(String name) {
                        return MediaType.APPLICATION_XML_VALUE;
                    }

                }""";

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public void respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;
                       String s1 = MediaType.APPLICATION_ATOM_XML;

                       MediaType m2 = MediaType.APPLICATION_FORM_URLENCODED_TYPE;
                       String s2 = MediaType.APPLICATION_FORM_URLENCODED;

                       MediaType m3 = MediaType.APPLICATION_JSON_TYPE;
                       String s3 = MediaType.APPLICATION_JSON;

                       MediaType m4 = MediaType.APPLICATION_JSON_PATCH_JSON_TYPE;
                       String s4 = MediaType.APPLICATION_JSON_PATCH_JSON;

                       MediaType m5 = MediaType.APPLICATION_OCTET_STREAM_TYPE;
                       String s5 = MediaType.APPLICATION_OCTET_STREAM;

                       MediaType m7 = MediaType.APPLICATION_SVG_XML_TYPE;
                       String s7 = MediaType.APPLICATION_SVG_XML;

                       MediaType m8 = MediaType.APPLICATION_XHTML_XML_TYPE;
                       String s8 = MediaType.APPLICATION_XHTML_XML;

                       MediaType m9 = MediaType.APPLICATION_XML_TYPE;
                       String s9 = MediaType.APPLICATION_XML;

                       MediaType m10 = MediaType.MULTIPART_FORM_DATA_TYPE;
                       String s10 = MediaType.MULTIPART_FORM_DATA;

                       MediaType m11 = MediaType.SERVER_SENT_EVENTS_TYPE;
                       String s11 = MediaType.SERVER_SENT_EVENTS;

                       MediaType m12 = MediaType.TEXT_HTML_TYPE;
                       String s12 = MediaType.TEXT_HTML;

                       MediaType m13 = MediaType.TEXT_PLAIN_TYPE;
                       String s13 = MediaType.TEXT_PLAIN;

                       MediaType m14 = MediaType.TEXT_XML_TYPE;
                       String s14 = MediaType.TEXT_XML;

                       MediaType m15 = MediaType.WILDCARD_TYPE;
                       String s15 = MediaType.WILDCARD;

                       String s16 = MediaType.CHARSET_PARAMETER;

                       String s17 = MediaType.MEDIA_TYPE_WILDCARD;
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;
                import org.springframework.util.MimeType;

                public class TestController {

                    public void respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML;
                       String s1 = MediaType.APPLICATION_ATOM_XML_VALUE;

                       MediaType m2 = MediaType.APPLICATION_FORM_URLENCODED;
                       String s2 = MediaType.APPLICATION_FORM_URLENCODED_VALUE;

                       MediaType m3 = MediaType.APPLICATION_JSON;
                       String s3 = MediaType.APPLICATION_JSON_VALUE;

                       MediaType m4 = MediaType.APPLICATION_JSON_PATCH_JSON;
                       String s4 = MediaType.APPLICATION_JSON_PATCH_JSON_VALUE;

                       MediaType m5 = MediaType.APPLICATION_OCTET_STREAM;
                       String s5 = MediaType.APPLICATION_OCTET_STREAM_VALUE;

                       MediaType m7 = MediaType.APPLICATION_SVG_XML;
                       String s7 = MediaType.APPLICATION_SVG_XML_VALUE;

                       MediaType m8 = MediaType.APPLICATION_XHTML_XML;
                       String s8 = MediaType.APPLICATION_XHTML_XML_VALUE;

                       MediaType m9 = MediaType.APPLICATION_XML;
                       String s9 = MediaType.APPLICATION_XML_VALUE;

                       MediaType m10 = MediaType.MULTIPART_FORM_DATA;
                       String s10 = MediaType.MULTIPART_FORM_DATA_VALUE;

                       MediaType m11 = MediaType.TEXT_EVENT_STREAM;
                       String s11 = MediaType.TEXT_EVENT_STREAM_VALUE;

                       MediaType m12 = MediaType.TEXT_HTML;
                       String s12 = MediaType.TEXT_HTML_VALUE;

                       MediaType m13 = MediaType.TEXT_PLAIN;
                       String s13 = MediaType.TEXT_PLAIN_VALUE;

                       MediaType m14 = MediaType.TEXT_XML;
                       String s14 = MediaType.TEXT_XML_VALUE;

                       MediaType m15 = MediaType.ALL;
                       String s15 = MediaType.ALL_VALUE;

                       String s16 = MimeType.PARAM_CHARSET;

                       String s17 = MimeType.WILDCARD_TYPE;
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public boolean respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;
                       return MediaType.APPLICATION_XML_TYPE.isCompatible(m1);
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                public class TestController {

                    public boolean respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML;
                       return MediaType.APPLICATION_XML.isCompatibleWith(m1);
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public MediaType respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML_TYPE;
                       return m1.withCharset("UTF-8");
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                import java.nio.charset.Charset;

                public class TestController {

                    public MediaType respond() {
                       MediaType m1 = MediaType.APPLICATION_ATOM_XML;
                       return new MediaType(m1, Charset.forName("UTF-8"));
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public MediaType respond() {
                       return MediaType.APPLICATION_ATOM_XML_TYPE.withCharset("UTF-8");
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                import java.nio.charset.Charset;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType(MediaType.APPLICATION_ATOM_XML, Charset.forName("UTF-8"));
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType("foo", "bar");
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType("foo", "bar");
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public MediaType respond() {
                       String type = "foo";
                       return new MediaType(type, "bar", "UTF-8");
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                import java.nio.charset.Charset;

                public class TestController {

                    public MediaType respond() {
                       String type = "foo";
                       return new MediaType(type, "bar", Charset.forName("UTF-8"));
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType();
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;
                import org.springframework.util.MimeType;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE);
                    }
                }
                """;

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

        String javaSource = """
                import javax.ws.rs.core.MediaType;
                import java.util.Map;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType("blah", "UTF-8", Map.of("foo", "bar"));
                    }
                }
                """;

        String expected = """
                import org.springframework.http.MediaType;

                import java.util.Map;

                public class TestController {

                    public MediaType respond() {
                       return new MediaType("blah", "UTF-8", Map.of("foo", "bar"));
                    }
                }
                """;

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
                """
                        import javax.ws.rs.Path;
                        import javax.ws.rs.Consumes;
                        import javax.ws.rs.POST;
                        import javax.ws.rs.PUT;
                        import javax.ws.rs.PathParam;
                        import javax.ws.rs.Produces;
                        import javax.ws.rs.core.MediaType;


                        @Path("/hello")
                        class ControllerClass {
                            @POST
                            @PUT
                            @Path("/json/{name}")
                            @Produces({"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML})
                            @Consumes("application/json")
                            public String getHelloWorldJSON(@PathParam("name") String name) {
                                return "Hello";
                            }
                        }""";

        String expected =
                """
                        import javax.ws.rs.Path;

                        import javax.ws.rs.Consumes;

                        import org.springframework.http.MediaType;
                        import javax.ws.rs.POST;
                        import javax.ws.rs.PUT;
                        import javax.ws.rs.PathParam;
                        import javax.ws.rs.Produces;


                        @Path("/hello")
                        class ControllerClass {
                            @POST
                            @PUT
                            @Path("/json/{name}")
                            @Produces({"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML_VALUE})
                            @Consumes("application/json")
                            public String getHelloWorldJSON(@PathParam("name") String name) {
                                return "Hello";
                            }
                        }""";

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
