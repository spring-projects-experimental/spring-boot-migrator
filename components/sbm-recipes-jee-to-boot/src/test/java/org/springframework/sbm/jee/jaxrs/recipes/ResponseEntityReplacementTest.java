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
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseEntityReplacementTest {

    private final static String SPRING_VERSION = "5.3.13";

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    Supplier<JavaParser> javaParserSupplier = () -> new RewriteJavaParser(new ApplicationProperties());
                    Recipe r = new SwapResponseWithResponseEntity(javaParserSupplier).doNext(new ReplaceMediaType(javaParserSupplier));
                    context.getProjectJavaSources().apply(r);
                }
            };


    @Test
    void testUnsupportedStaticCall() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.status(200, \"All good\").build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return /* SBM FIXME: Couldn't find exact replacement for status(int, java.lang.String) - dropped java.lang.String argument */ ResponseEntity.status(200).build();\n"
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
    void testUnsupportedBuilderCall() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.status(200).tag(\"My Tag\").build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.status(200).eTag(\"My Tag\").build();\n"
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
    void testUnsupportedBuilder() {

        String javaSource = ""
                + "import java.util.stream.LongStream;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public LongStream respond() {\n"
                + "       return LongStream.builder().add(1).add(2).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = javaSource;

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
    void testOnlyReturnStatementBuilder() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       Response r = Response.status(200).build();\n"
                + "       return r;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       ResponseEntity r = ResponseEntity.status(200).build();\n"
                + "       return r;\n"
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
    void testSimplestCase() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.status(200).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.status(200).build();\n"
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
    void testReplaceBuildWithBody() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.ok(\"All good!\").build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.ok().body(\"All good!\");\n"
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
    void testReplaceOkWithBody() {
        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       Response r = Response.ok(\"great!\").build();\n"
                + "       return r;\n"
                + "    }\n"
                + "}\n"
                + "";


        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       ResponseEntity r = ResponseEntity.ok().body(\"great!\");\n"
                + "       return r;\n"
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
    void testReplaceOkWithMediaTypeAndBody() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.ok(\"All good!\", MediaType.APPLICATION_JSON_TYPE).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(\"All good!\");\n"
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
    void testReplaceOkWithMediaTypeStringAndBody() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.ok(\"All good!\", \"application/json\").build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.ok().contentType(MediaType.parseMediaType(\"application/json\")).body(\"All good!\");\n"
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
    void accepted_1() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.accepted().build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.accepted().build();\n"
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
    void accepted_2() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.accepted(\"Correct\").build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.accepted().body(\"Correct\");\n"
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
    void created() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return Response.created(uri).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return ResponseEntity.created(uri).build();\n"
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
    void fromResponse() {
        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       Response r = Response.ok(\"great!\").build();\n"
                + "       return Response.fromResponse(r).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       ResponseEntity r = ResponseEntity.ok().body(\"great!\");\n"
                + "       return ResponseEntity.status(r.getStatusCode()).headers(r.getHeaders()).body(r.getBody());\n"
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
    void notModified() {
        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "        Response.notModified(\"great!\");\n"
                + "        return Response.notModified().build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "        ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(\"great!\");\n"
                + "        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();\n"
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
    void seeOther() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return Response.seeOther(uri).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return ResponseEntity.status(HttpStatus.SEE_OTHER).location(uri).build();\n"
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
    void serverError() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.serverError().build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.status(HttpStatus.SERVER_ERROR).build();\n"
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
    void temporaryRedirect() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return Response.temporaryRedirect(uri).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "import java.net.URI;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       URI uri = URI.create(\"https://spring.io\");\n"
                + "       return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(uri).build();\n"
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
                + "import javax.ws.rs.core.Response;\n"
                + "import javax.ws.rs.core.GenericType;\n"
                + "import java.lang.annotation.Annotation;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public String respond() {\n"
                + "        Response r =  Response.ok().build();\n"
                + "        r.getAllowedMethods();\n"
                + "        r.bufferEntity();\n"
                + "        r.close();\n"
                + "        r.getCookies();\n"
                + "        r.getDate();\n"
                + "        r.getEntity();\n"
                + "        r.bufferEntity();\n"
                + "        r.getEntityTag();\n"
                + "        r.getHeaders();\n"
                + "        r.getHeaderString(\"Accept\");\n"
                + "        r.getLanguage();\n"
                + "        r.getLastModified();\n"
                + "        r.getLength();\n"
                + "        r.getLink(\"Something\");\n"
                + "        r.getLinkBuilder(\"Something\");\n"
                + "        r.getLinks();\n"
                + "        r.getLocation();\n"
                + "        r.getMediaType();\n"
                + "        r.getMetadata();\n"
                + "        r.getStatus();\n"
                + "        r.getStatusInfo();\n"
                + "        r.getStringHeaders();\n"
                + "        r.hasEntity();\n"
                + "        r.hasLink(\"Something\");\n"
                + "        r.readEntity(String.class, new Annotation[0]);\n"
                + "        r.readEntity(GenericType.forInstance(\"Something\"));\n"
                + "        r.readEntity(GenericType.forInstance(\"Something\"), new Annotation[0]);\n"
                + "        return r.readEntity(String.class);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.ResponseEntity;\n"
                + "import java.util.Date;\n"
                + "import java.util.stream.Collectors;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public String respond() {\n"
                + "        ResponseEntity r =  ResponseEntity.ok().build();\n"
                + "        r.getHeaders().getAllow().stream().map(m -> m.toString()).collect(Collectors.toList());\n"
                + "        r.bufferEntity();\n"
                + "        r.close();\n"
                + "        r.getCookies();\n"
                + "        new Date(r.getHeaders().getDate());\n"
                + "        r.getBody();\n"
                + "        r.bufferEntity();\n"
                + "        r.getHeaders().getETag();\n"
                + "        r.getHeaders();\n"
                + "        r.getHeaders().get(\"Accept\").stream().collect(Collectors.joining(\", \"));\n"
                + "        r.getHeaders().getContentLanguage();\n"
                + "        new Date(r.getHeaders().getLastModified());\n"
                + "        r.getHeaders().getContentLength();\n"
                + "        r.getLink(\"Something\");\n"
                + "        r.getLinkBuilder(\"Something\");\n"
                + "        r.getLinks();\n"
                + "        r.getHeaders().getLocation();\n"
                + "        r.getHeaders().getContentType();\n"
                + "        r.getHeaders();\n"
                + "        r.getStatusCodeValue();\n"
                + "        r.getStatusCode();\n"
                + "        r.getHeaders();\n"
                + "        r.hasBody();\n"
                + "        r.hasLink(\"Something\");\n"
                + "        r.getBody();\n"
                + "        r.getBody();\n"
                + "        r.getBody();\n"
                + "        return r.getBody();\n"
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
    void chain_1() {
        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response respond() {\n"
                + "       return Response.status(200).entity(\"Hello\").tag(\"My Tag\").type(MediaType.TEXT_PLAIN_TYPE).build();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.status(200).eTag(\"My Tag\").contentType(MediaType.TEXT_PLAIN).body(\"Hello\");\n"
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
    void chain_2() {
        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "import javax.ws.rs.core.Response.ResponseBuilder;\n"
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseBuilder respond() {\n"
                + "       return Response.status(200).entity(\"Hello\").tag(\"My Tag\").type(MediaType.TEXT_PLAIN_TYPE);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.MediaType;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public ResponseEntity respond() {\n"
                + "       return ResponseEntity.status(200).eTag(\"My Tag\").contentType(MediaType.TEXT_PLAIN).body(\"Hello\");\n"
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
}
