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
package org.springframework.sbm.jee.jaxrs.actions;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertJaxRsAnnotationsTest {

    @Test
    void convertJaxRsMethodWithoutPathToSpringMvc() {
        @Language("java")
        String restControllerCode = """
                package com.example.jeerest.rest;
                                
                import com.example.jeerest.Movie;
                import com.example.jeerest.MoviesBean;
                import org.springframework.beans.factory.annotation.Autowired;
                                
                import javax.ws.rs.DELETE;
                import javax.ws.rs.GET;
                import javax.ws.rs.PUT;
                import javax.ws.rs.Path;
                import javax.ws.rs.PathParam;
                import javax.ws.rs.Produces;
                import javax.ws.rs.QueryParam;
                import javax.ws.rs.core.MediaType;
                import java.util.List;
                                
                @Path("movies")
                @Produces({"application/json"})
                public class MoviesRest {
                    @GET
                    public List<Movie> getMovies(@QueryParam("first") Integer first, @QueryParam("max") Integer max,
                                                 @QueryParam("field") String field, @QueryParam("searchTerm") String searchTerm) {
                        return service.getMovies(first, max, field, searchTerm);
                    }
                }
                """;

        @Language("java")
        String expected = """
                package com.example.jeerest.rest;
                                
                import com.example.jeerest.Movie;
                import com.example.jeerest.MoviesBean;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestMethod;
                import org.springframework.web.bind.annotation.RestController;
                                
                import javax.ws.rs.DELETE;
                import javax.ws.rs.PUT;
                import javax.ws.rs.PathParam;
                import javax.ws.rs.QueryParam;
                import javax.ws.rs.core.MediaType;
                import java.util.List;
                                
                                
                @RestController
                @RequestMapping(value = "movies", produces = {"application/json"})
                public class MoviesRest {
                    @RequestMapping(method = RequestMethod.GET)
                    public List<Movie> getMovies(@QueryParam("first") Integer first, @QueryParam("max") Integer max,
                                                 @QueryParam("field") String field, @QueryParam("searchTerm") String searchTerm) {
                        return service.getMovies(first, max, field, searchTerm);
                    }
                }
                """;


        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(restControllerCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final", "org.springframework.boot:spring-boot-starter-web:2.4.2")
                .build();
        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();
        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }

    @Test
    void replaceMethodAnnotationsWithOneAnnotation() {

        String sourceCode = """
                import javax.ws.rs.Path;
                import javax.ws.rs.Consumes;
                import javax.ws.rs.POST;
                import javax.ws.rs.Path;
                import javax.ws.rs.PathParam;
                import javax.ws.rs.Produces;
                import javax.ws.rs.core.MediaType;


                @Path("/hello")
                class ControllerClass {
                    @POST
                    @Path("/json/{name}")
                    @Produces({"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML})
                    @Consumes("application/json")
                    public String getHelloWorldJSON(        @PathParam("name")         String name) {
                        return "Hello";
                    }
                }""";

        String expected = """
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestMethod;
                import org.springframework.web.bind.annotation.RestController;
                import javax.ws.rs.PathParam;
                import javax.ws.rs.core.MediaType;


                @RestController
                @RequestMapping(value = "/hello")
                class ControllerClass {
                    @RequestMapping(value = "/json/{name}", produces = {"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML}, consumes = "application/json", method = RequestMethod.POST)
                    public String getHelloWorldJSON(        @PathParam("name")         String name) {
                        return "Hello";
                    }
                }""";

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
    void replaceMethodAnnotations() {
        String sourceCode = """
                import javax.ws.rs.Path;
                import javax.ws.rs.Consumes;
                import javax.ws.rs.*;
                import javax.ws.rs.Path;
                import javax.ws.rs.PathParam;
                import javax.ws.rs.Produces;
                import javax.ws.rs.core.MediaType;


                @Path("/hello")
                class ControllerClass {
                    @POST
                    @GET
                    @PUT
                    @DELETE
                    @Path("/json/{name}")
                    @Produces({"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML})
                    @Consumes("application/json")
                    public String getHelloWorldJSON(@PathParam("name") String name) {
                        return "Hello";
                    }
                    public String notAnEndpoint(@PathParam("name") String name) {
                        return "Hello";
                    }
                }""";

        @Language("java")
        String expected = """
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestMethod;
                import org.springframework.web.bind.annotation.RestController;
                
                import javax.ws.rs.PathParam;
                import javax.ws.rs.core.MediaType;
                                
                                
                @RestController
                @RequestMapping(value = "/hello")
                class ControllerClass {
                    @RequestMapping(value = "/json/{name}", produces = {"image/jpeg", "image/gif", "image/png", MediaType.APPLICATION_XML}, consumes = "application/json", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
                    public String getHelloWorldJSON(@PathParam("name") String name) {
                        return "Hello";
                    }
                    public String notAnEndpoint(@PathParam("name") String name) {
                        return "Hello";
                    }
                }""";

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
        String sourceCode = """
                package com.example.jee.app;

                import javax.inject.Inject;
                import javax.ws.rs.*;
                import javax.ws.rs.core.MediaType;
                import java.util.stream.Collectors;

                @Path("/")
                public class PersonController {

                    @POST
                    @Path("/json/{name}")
                    @Produces("application/json")
                    @Consumes("application/json")
                    public String getHelloWorldJSON(@PathParam("name") String name) throws Exception {
                        return "";
                    }

                    @GET
                    @Path("/json")
                    @Produces(MediaType.APPLICATION_JSON)
                    @Consumes(MediaType.APPLICATION_JSON)
                    public String getAllPersons() throws Exception {
                        return "";
                    }

                    @POST
                    @Path("/xml/{name}")
                    @Produces(MediaType.APPLICATION_XML)
                    @Consumes(MediaType.APPLICATION_XML)
                    public String getHelloWorldXML(@PathParam("name") String name) throws Exception {
                        return "";
                    }
                }""";

        String expected = """
                package com.example.jee.app;

                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestMethod;
                import org.springframework.web.bind.annotation.RestController;

                import javax.ws.rs.PathParam;
                import javax.ws.rs.core.MediaType;

                @RestController
                @RequestMapping(value = "/")
                public class PersonController {

                    @RequestMapping(value = "/json/{name}", produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
                    public String getHelloWorldJSON(@PathParam("name") String name) throws Exception {
                        return "";
                    }

                    @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.GET)
                    public String getAllPersons() throws Exception {
                        return "";
                    }

                    @RequestMapping(value = "/xml/{name}", produces = MediaType.APPLICATION_XML, consumes = MediaType.APPLICATION_XML, method = RequestMethod.POST)
                    public String getHelloWorldXML(@PathParam("name") String name) throws Exception {
                        return "";
                    }
                }""";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(sourceCode)
                .withBuildFileHavingDependencies("org.jboss.spec.javax.ws.rs:jboss-jaxrs-api_2.1_spec:1.0.1.Final", "org.springframework:spring-web:5.3.8")
                .build();

        ConvertJaxRsAnnotations sut = new ConvertJaxRsAnnotations();

        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }

    @Test
    void addRequestBodyOverParameter() {
        String sourceCode = """
                import javax.ws.rs.Path;
                import javax.ws.rs.POST;
                import javax.ws.rs.Path;
                import javax.ws.rs.PathParam;

                @Path("/hello")
                class ControllerClass {
                    @POST
                    @Path("/json/{name}")
                    public String create(@PathParam("name") String name, String data) {
                        return "Hello";
                    }
                }""";

        String expected = """
                import org.springframework.web.bind.annotation.RequestBody;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RequestMethod;
                import org.springframework.web.bind.annotation.RestController;
                import javax.ws.rs.PathParam;

                @RestController
                @RequestMapping(value = "/hello")
                class ControllerClass {
                    @RequestMapping(value = "/json/{name}", method = RequestMethod.POST)
                    public String create(@PathParam("name") String name, @RequestBody String data) {
                        return "Hello";
                    }
                }""";


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
