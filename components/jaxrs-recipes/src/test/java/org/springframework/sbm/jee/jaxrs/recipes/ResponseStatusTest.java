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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseStatusTest {

    private final static String SPRING_VERSION = "5.3.5";

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    SwapStatusForHttpStatus r = new SwapStatusForHttpStatus();
                    context.getProjectJavaSources().apply(r);
                }
            };


    @Test
    void testHttpStatusOK() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Status respond() {\n"
                + "       return Status.OK;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public HttpStatus respond() {\n"
                + "       return HttpStatus.OK;\n"
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
    void testOkWithTopLevelType() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Response.Status respond() {\n"
                + "       return Response.Status.OK;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public HttpStatus respond() {\n"
                + "       return HttpStatus.OK;\n"
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

    @Test
    void testResponseStatusAsParameter() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void respond() {\n"
                + "       System.out.println(Response.Status.NOT_FOUND);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void respond() {\n"
                + "       System.out.println(HttpStatus.NOT_FOUND);\n"
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
    void testHttpStatusEntityTooLarge() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Status respond() {\n"
                + "       Status s =  Status.REQUEST_ENTITY_TOO_LARGE;\n"
                + "       return s;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public HttpStatus respond() {\n"
                + "       HttpStatus s =  HttpStatus.PAYLOAD_TOO_LARGE;\n"
                + "       return s;\n"
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

    @Test
    void testHttpStatusCode() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public int respond() {\n"
                + "       Status s =  Status.REQUEST_ENTITY_TOO_LARGE;\n"
                + "       return s.getStatusCode();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public int respond() {\n"
                + "       HttpStatus s =  HttpStatus.PAYLOAD_TOO_LARGE;\n"
                + "       return s.getValue();\n"
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
    void testHttpStatusToEnum() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status;\n"
                + "import javax.ws.rs.core.Response.StatusType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Status respond() {\n"
                + "       StatusType s =  Status.OK;\n"
                + "       return s.toEnum();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public HttpStatus respond() {\n"
                + "       HttpStatus s =  HttpStatus.OK;\n"
                + "       return s;\n"
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
    void testStatusGetFamily() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status;\n"
                + "import javax.ws.rs.core.Response.Status.Family;\n"
                + "import javax.ws.rs.core.Response.StatusType;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public Family respond() {\n"
                + "       StatusType s =  Status.OK;\n"
                + "       return s.getFamily();\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public HttpStatus.Series respond() {\n"
                + "       HttpStatus s =  HttpStatus.OK;\n"
                + "       return s.series();\n"
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
    void mini_integration_test_1() {
        String javaSource = "package com.hotjoe.services.exception;\n"
                + "\n"
                + "import javax.ws.rs.core.Response;\n"
                + "\n"
                + "public class NotFoundServiceException extends ServiceException {\n"
                + "	private static final long serialVersionUID = 1736707486401545199L;\n"
                + "\n"
                + "	public NotFoundServiceException() {\n"
                + "		super();\n"
                + "	}\n"
                + "\n"
                + "	public NotFoundServiceException(String message) {\n"
                + "		super(message);\n"
                + "	}\n"
                + "\n"
                + "	public NotFoundServiceException(Throwable cause) {\n"
                + "		super(cause);\n"
                + "	}\n"
                + "\n"
                + "\n"
                + "	public NotFoundServiceException(String message, Throwable cause) {\n"
                + "		super(message, cause);\n"
                + "	}\n"
                + "\n"
                + "	@Override\n"
                + "	public Response.Status getStatus() {\n"
                + "		return Response.Status.NOT_FOUND;\n"
                + "	}\n"
                + "}";

        String expected = "package com.hotjoe.services.exception;\n"
                + "\n"
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class NotFoundServiceException extends ServiceException {\n"
                + "	private static final long serialVersionUID = 1736707486401545199L;\n"
                + "\n"
                + "	public NotFoundServiceException() {\n"
                + "		super();\n"
                + "	}\n"
                + "\n"
                + "	public NotFoundServiceException(String message) {\n"
                + "		super(message);\n"
                + "	}\n"
                + "\n"
                + "	public NotFoundServiceException(Throwable cause) {\n"
                + "		super(cause);\n"
                + "	}\n"
                + "\n"
                + "\n"
                + "	public NotFoundServiceException(String message, Throwable cause) {\n"
                + "		super(message, cause);\n"
                + "	}\n"
                + "\n"
                + "	@Override\n"
                + "	public HttpStatus getStatus() {\n"
                + "		return HttpStatus.NOT_FOUND;\n"
                + "	}\n"
                + "}";

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
