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
package org.springframework.sbm.java.migration.recipes;

import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeMethodReturnTypeRecipeTest {
    @Test
    void recipeTest() {
        String javaSource1 =
                """
                public class Service {
                    public Integer foo() {
                        return (int) 1L;
                    }
                }
                """;
        String javaSource2 =
                "public class Client {\n" +
                "    public void call() {\n" +
                "        Integer foo = new Service().foo();\n" +
                "    }\n" +
                "}";

        ProjectJavaSources projectJavaSources = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource1, javaSource2)
                .build()
                .getProjectJavaSources();

        String returnTypeExpr = "Long";
        String[] imports = {"java.lang.Long"};
        ChangeMethodReturnTypeRecipe sut = new ChangeMethodReturnTypeRecipe(md -> true, returnTypeExpr, imports);
        projectJavaSources.apply(sut);

        assertThat(projectJavaSources.findJavaSourceDeclaringType("Service").get().print()).isEqualTo(
                """
                 public class Service {
                     public Long foo() {
                         return (int) 1L;
                     }
                 }
                 """
        );
        assertThat(projectJavaSources.findJavaSourceDeclaringType("Client").get().print()).isEqualTo(
                "public class Client {\n" +
                "    public Long call() {\n" +
                "        Integer foo = new Service().foo();\n" + // FIXME: Recipe should attempt to modify type on caller side
                "    }\n" +
                "}"
        );
    }

}