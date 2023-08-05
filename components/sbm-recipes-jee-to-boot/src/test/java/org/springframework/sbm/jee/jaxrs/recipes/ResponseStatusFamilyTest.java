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

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseStatusFamilyTest {

    private final static String SPRING_VERSION = "5.3.13";

    final private AbstractAction action =
            new AbstractAction() {
                @Override
                public void apply(ProjectContext context) {
                    SwapFamilyForSeries r = new SwapFamilyForSeries();
                    context.getProjectJavaSources().apply(r);
                }
            };

    @Test
    void enumConstantsTest() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status.Family;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       Family f1 = Family.INFORMATIONAL;\n"
                + "       Family f2 = Family.SUCCESSFUL;\n"
                + "       Family f3 = Family.REDIRECTION;\n"
                + "       Family f4 = Family.CLIENT_ERROR;\n"
                + "       Family f5 = Family.SERVER_ERROR;\n"
                + "       \n"
                + "       int code = 201;\n"
                + "       Family custom = Family.familyOf(code);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.HttpStatus.Series;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       HttpStatus.Series f1 = Series.INFORMATIONAL;\n"
                + "       HttpStatus.Series f2 = Series.SUCCESSFUL;\n"
                + "       HttpStatus.Series f3 = Series.REDIRECTION;\n"
                + "       HttpStatus.Series f4 = Series.CLIENT_ERROR;\n"
                + "       HttpStatus.Series f5 = Series.SERVER_ERROR;\n"
                + "       \n"
                + "       int code = 201;\n"
                + "       HttpStatus.Series custom = HttpStatus.Series.resolve(code);\n"
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
    void otherEnumConstantsTest() {

        String javaSource = ""
                + "import javax.ws.rs.core.Response.Status.Family;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       Family f = Family.OTHER;\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.HttpStatus.Series;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       HttpStatus.Series f = HttpStatus.Series.OTHER;\n"
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
    void staticImportMethod() {

        String javaSource = ""
                + "import static javax.ws.rs.core.Response.Status.Family.familyOf;\n"
                + "import javax.ws.rs.core.Response.Status.Family;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       int code = 201;\n"
                + "       Family custom = familyOf(code);\n"
                + "    }\n"
                + "}\n"
                + "";

        String expected = ""
                + "import org.springframework.http.HttpStatus;\n"
                + "\n"
                + "public class TestController {\n"
                + "\n"
                + "    public void test() {\n"
                + "       int code = 201;\n"
                + "       HttpStatus.Series custom = HttpStatus.Series.resolve(code);\n"
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

}
