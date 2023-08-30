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
package org.springframework.sbm.support.openrewrite.recipes;

import org.openrewrite.RecipeRun;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.OpenRewriteApiTest;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangeTypeTest {

    @OpenRewriteApiTest
    void testReplaceTypeWithInnerClass() {
        String javaSource =
                "import static javax.ws.rs.core.Response.Status.Family.familyOf;\n" +
                "\n" +
                "import javax.ws.rs.core.Response.Status.Family;\n" +
                "\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "\n" +
                "public class TestController {\n" +
                "\n" +
                "    public void test() {\n" +
                "       int code = 201;\n" +
                "       Family custom = HttpStatus.Series.resolve(code);\n" +
                "    }\n" +
                "}";

        ChangeType changeType = new ChangeType("javax.ws.rs.core.Response$Status$Family", "org.springframework.http.HttpStatus$Series", false);

        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(javaSource, "javax:javaee-api:8.0", "org.springframework:spring-web:5.3.7", "com.google.code.findbugs:jsr305:3.0.2");

        RecipeRun recipeRun = changeType.run(List.of(compilationUnit));
        assertThat(recipeRun.getResults().get(0).getAfter().printAll()).isEqualTo(
                "import org.springframework.http.HttpStatus;\n" +
                "\n" +
                "public class TestController {\n" +
                "\n" +
                "    public void test() {\n" +
                "       int code = 201;\n" +
                "       HttpStatus.Series custom = HttpStatus.Series.resolve(code);\n" +
                "    }\n" +
                "}"
        );
    }

}