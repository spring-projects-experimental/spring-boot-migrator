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
package org.springframework.sbm.boot.web.api;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.stringtemplate.v4.ST;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class RestControllerBeanTest {
    @Test
    void restController_simpleGET() {
        @Language("java")
        String restController =
                """
                package com.example;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.RestController;
                                
                @RestController
                public class AdditionRestController {
                                
                    @GetMapping({"/info", "/otherInfo"})
                    public String getInfo() {
                        return "info";
                    }
                }
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.3")
                .withJavaSource("src/main/java", restController)
                .build();

        JavaSource js = context.getProjectJavaSources().list().get(0);
        Type type = js.getTypes().get(0);
        RestControllerBean restControllerBean = new RestControllerBean(js, type);
        Type restControllerType = restControllerBean.restControllerType();
        RestMethod restMethod = restControllerBean.getRestMethods().get(0);

        assertThat(restControllerType.getFullyQualifiedName()).isEqualTo("com.example.AdditionRestController");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.GET);
        assertThat(restMethod.consumes()).containsExactly(MediaType.APPLICATION_JSON_VALUE);
        assertThat(restMethod.produces()).containsExactly(MediaType.APPLICATION_JSON_VALUE);
        assertThat(restMethod.path()).containsExactlyInAnyOrder("/info", "/otherInfo");
        assertThat(restMethod.methodReference().getName()).isEqualTo("getInfo");
    }

    @Test
    void restController_complexGET() {
        @Language("java")
        String restController =
                """
                package com.example;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.RestController;
                import org.springframework.http.MediaType;
                                
                @RestController
                public class AdditionRestController {
                                
                    @GetMapping(
                        name = "getInfoEndpoint",
                        path={"/info", "/otherInfo"},
                        params = {"p1", "p2"},
                        headers = {"a=b", "c=d"},
                        consumes = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_NDJSON_VALUE}, 
                        produces = {MediaType.APPLICATION_CBOR_VALUE, MediaType.APPLICATION_PDF_VALUE}
                    )
                    public String getInfo() {
                        return "info";
                    }
                }
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.3")
                .withJavaSource("src/main/java", restController)
                .build();

        JavaSource js = context.getProjectJavaSources().list().get(0);
        Type type = js.getTypes().get(0);
        RestControllerBean restControllerBean = new RestControllerBean(js, type);
        Type restControllerType = restControllerBean.restControllerType();
        RestMethod restMethod = restControllerBean.getRestMethods().get(0);

        assertThat(restControllerType.getFullyQualifiedName()).isEqualTo("com.example.AdditionRestController");
        assertThat(restMethod.name()).isEqualTo("getInfoEndpoint");
        assertThat(restMethod.path()).containsExactlyInAnyOrder("/info", "/otherInfo");
        assertThat(restMethod.params()).containsExactlyInAnyOrder("p1", "p2");
        assertThat(restMethod.headers()).containsExactlyInAnyOrder("a=b", "c=d");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.GET);
        assertThat(restMethod.consumes()).containsExactly(MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_NDJSON_VALUE);
        assertThat(restMethod.produces()).containsExactly(MediaType.APPLICATION_CBOR_VALUE, MediaType.APPLICATION_PDF_VALUE);
        assertThat(restMethod.methodReference().getName()).isEqualTo("getInfo");
    }



    @Test
    void RequestMapping_POST() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.POST)", "org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.POST);
    }

    @Test
    void RequestMapping_GET() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.GET)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.GET);
    }

    @Test
    void RequestMapping_DELETE() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.DELETE)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.DELETE);
    }

    @Test
    void RequestMapping_HEAD() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.HEAD)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.HEAD);
    }

    @Test
    void RequestMapping_OPTIONS() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.OPTIONS)", "org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.OPTIONS);
    }

    @Test
    void RequestMapping_PUT() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.PUT)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.PUT);
    }

    @Test
    void RequestMapping_PATCH() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.PATCH)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.PATCH);
    }

    @Test
    void RequestMapping_TRACE() {
        RestMethod restMethod = createRestControllerWithAnnotatedMethod("@RequestMapping(method=RequestMethod.TRACE)","org.springframework.web.bind.annotation.RequestMapping", "org.springframework.web.bind.annotation.RequestMethod");
        assertThat(restMethod.method().get(0)).isEqualTo(RequestMethod.TRACE);
    }

    private RestMethod createRestControllerWithAnnotatedMethod(String requestMapping, String... imports) {
        @Language("java")
        String restController =
                """
                package com.example;
                import org.springframework.web.bind.annotation.RestController;
                <imports>
                                
                @RestController
                public class AdditionRestController {
                                
                    <requestMapping>
                    public String getInfo() {
                        return "info";
                    }
                }
                """;

        ST st = new ST(restController);
        st.add("requestMapping", requestMapping);
        st.add("imports", Arrays.stream(imports).map(i -> "import " + i + ";").collect(Collectors.joining("\n")));

        String restControllerCode = st.render();

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.3")
                .withJavaSource("src/main/java", restControllerCode)
                .build();
        JavaSource js = context.getProjectJavaSources().list().get(0);
        Type type = js.getTypes().get(0);
        RestControllerBean restControllerBean = new RestControllerBean(js, type);
        RestMethod restMethod = restControllerBean.getRestMethods().get(0);

        return restMethod;
    }
}