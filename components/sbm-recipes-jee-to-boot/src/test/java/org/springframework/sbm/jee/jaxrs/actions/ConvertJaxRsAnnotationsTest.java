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
package org.springframework.sbm.jee.jaxrs.actions;

import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConvertJaxRsAnnotationsTest {

    @Test
    void replaceMethodAnnotationsWithOneAnnotation() throws Exception {

        String sourceCode = "" +
                "import javax.ws.rs.Path;\n" +
                "import javax.ws.rs.Consumes;\n" +
                "import javax.ws.rs.POST;\n" +
                "import javax.ws.rs.Path;\n" +
                "import javax.ws.rs.PathParam;\n" +
                "import javax.ws.rs.Produces;\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "\n" +
                "\n" +
                "@Path(\"/hello\")\n" +
                "class ControllerClass {\n" +
                "    @POST\n" +
                "    @Path(\"/json/{name}\")\n" +
                "    @Produces({\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML})\n" +
                "    @Consumes(\"application/json\")\n" +
                "    public String getHelloWorldJSON(" +
                "        @PathParam(\"name\") " +
                "        String name) {\n" +
                "        return \"Hello\";\n" +
                "    }\n" +
                "}";

        String expected = "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "import javax.ws.rs.PathParam;\n" +
                "import javax.ws.rs.core.MediaType;\n" +
                "\n" +
                "\n" +
                "@RestController\n" +
                "@RequestMapping(value = \"/hello\")\n" +
                "class ControllerClass {\n" +
                "    @RequestMapping(value = \"/json/{name}\", produces = {\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML}, consumes = \"application/json\", method = RequestMethod.POST)\n" +
                "    public String getHelloWorldJSON(" +
                "        @PathParam(\"name\") " +
                "        String name) {\n" +
                "        return \"Hello\";\n" +
                "    }\n" +
                "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final", "org.springframework.boot:spring-boot-starter-web:2.4.2")
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);

        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();
        sut.apply(projectContext);

        assertThat(javaSource.print()).isEqualTo(expected);
    }


    @Test
    void replaceMethodAnnotations() throws Exception {
        String sourceCode =
                "import javax.ws.rs.Path;\n" +
                        "import javax.ws.rs.Consumes;\n" +
                        "import javax.ws.rs.*;\n" +
                        "import javax.ws.rs.Path;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "import javax.ws.rs.Produces;\n" +
                        "import javax.ws.rs.core.MediaType;\n" +
                        "        \n" +
                        "                                   \n" +
                        "@Path(\"/hello\")                                   \n" +
                        "class ControllerClass {             \n" +
                        "    @POST\n" +
                        "    @GET\n" +
                        "    @PUT\n" +
                        "    @DELETE\n" +
                        "    @Path(\"/json/{name}\")\n" +
                        "    @Produces({\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML})\n" +
                        "    @Consumes(\"application/json\")\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "    public String notAnEndpoint(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";

        String expected =
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                        "import org.springframework.web.bind.annotation.RestController;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "import javax.ws.rs.core.MediaType;\n" +
                        "\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(value = \"/hello\")\n" +
                        "class ControllerClass {\n" +
                        "    @RequestMapping(value = \"/json/{name}\", produces = {\"image/jpeg\", \"image/gif\", \"image/png\", MediaType.APPLICATION_XML}, consumes = \"application/json\"," +
                        " method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "    public String notAnEndpoint(@PathParam(\"name\") String name) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final", "org.springframework:spring-web:5.3.8")
                .build();

        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();

        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }

    @Test
    void migrateToController() {
        String sourceCode =
                "package com.example.jee.app;\n" +
                        "\n" +
                        "import javax.inject.Inject;\n" +
                        "import javax.ws.rs.*;\n" +
                        "import javax.ws.rs.core.MediaType;\n" +
                        "import java.util.stream.Collectors;\n" +

                        "\n" +
                        "@Path(\"/\")\n" +
                        "public class PersonController {\n" +
                        "\n" +
                        "    @POST\n" +
                        "    @Path(\"/json/{name}\")\n" +
                        "    @Produces(\"application/json\")\n" +
                        "    @Consumes(\"application/json\")\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "\n" +
                        "    @GET\n" +
                        "    @Path(\"/json\")\n" +
                        "    @Produces(MediaType.APPLICATION_JSON)\n" +
                        "    @Consumes(MediaType.APPLICATION_JSON)\n" +
                        "    public String getAllPersons() throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "\n" +
                        "    @POST\n" +
                        "    @Path(\"/xml/{name}\")\n" +
                        "    @Produces(MediaType.APPLICATION_XML)\n" +
                        "    @Consumes(MediaType.APPLICATION_XML)\n" +
                        "    public String getHelloWorldXML(@PathParam(\"name\") String name) throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "}";

        String expected =
                "package com.example.jee.app;\n" +
                        "\n" +
                        "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                        "import org.springframework.web.bind.annotation.RestController;\n" +
                        "\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "import javax.ws.rs.core.MediaType;\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(value = \"/\")\n" +
                        "public class PersonController {\n" +
                        "\n" +
                        "    @RequestMapping(value = \"/json/{name}\", produces = \"application/json\", consumes = \"application/json\", method = RequestMethod.POST)\n" +
                        "    public String getHelloWorldJSON(@PathParam(\"name\") String name) throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "\n" +
                        "    @RequestMapping(value = \"/json\", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.GET)\n" +
                        "    public String getAllPersons() throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "\n" +
                        "    @RequestMapping(value = \"/xml/{name}\", produces = MediaType.APPLICATION_XML, consumes = MediaType.APPLICATION_XML, method = RequestMethod.POST)\n" +
                        "    public String getHelloWorldXML(@PathParam(\"name\") String name) throws Exception {\n" +
                        "        return \"\";\n" +
                        "    }\n" +
                        "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final", "org.springframework:spring-web:5.3.8")
                .build();

        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();

        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }

    @Test
    void addRequestBodyOverParameter() throws Exception {
        String sourceCode =
                "import javax.ws.rs.Path;\n" +
                        "import javax.ws.rs.POST;\n" +
                        "import javax.ws.rs.Path;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "\n" +
                        "@Path(\"/hello\")\n" +
                        "class ControllerClass {\n" +
                        "    @POST\n" +
                        "    @Path(\"/json/{name}\")\n" +
                        "    public String create(@PathParam(\"name\") String name, String data) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";

        String expected =
                "import org.springframework.web.bind.annotation.RequestBody;\n" +
                        "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                        "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                        "import org.springframework.web.bind.annotation.RestController;\n" +
                        "import javax.ws.rs.PathParam;\n" +
                        "\n" +
                        "@RestController\n" +
                        "@RequestMapping(value = \"/hello\")\n" +
                        "class ControllerClass {\n" +
                        "    @RequestMapping(value = \"/json/{name}\", method = RequestMethod.POST)\n" +
                        "    public String create(@PathParam(\"name\") String name, @RequestBody String data) {\n" +
                        "        return \"Hello\";\n" +
                        "    }\n" +
                        "}";


        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies(
                        "org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final",
                        "org.springframework:spring-web:4.1.7.RELEASE",
                        "org.springframework:spring-webmvc:4.1.7.RELEASE")
                .build();

        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();
        sut.apply(projectContext);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }
}
