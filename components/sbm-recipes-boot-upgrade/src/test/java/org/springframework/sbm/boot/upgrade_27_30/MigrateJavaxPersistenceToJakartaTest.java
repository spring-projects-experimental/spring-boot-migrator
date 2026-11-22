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
package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.OpenRewriteDeclarativeRecipeAdapter;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RewriteRecipeLoader;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.RecipeTestSupport;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kishore S
 */
public class MigrateJavaxPersistenceToJakartaTest {

    private final String productSource =
            "package org.fabianlee.springmicrowithactuator.product;\n" +
            "\n" +
            "import javax.persistence.Column;\n" +
            "import javax.persistence.Entity;\n" +
            "import javax.persistence.Id;\n" +
            "import javax.persistence.Table;\n" +
            "\n" +
            "@Entity\n" +
            "@Table(name=\"products\")\n" +
            "public class Product {\n" +
            "    @Id\n" +
            "    private long id;\n" +
            "\n" +
            "    @Column(length=120)\n" +
            "    private String name;\n" +
            "\n" +
            "    private int count;\n" +
            "    private Double price;\n" +
            "    private javax.persistence.EntityManager em;\n" +
            "}\n";

    @Test
    void shouldMigrateJavaxPersistenceDirectly() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/product/Product.java", productSource)
                .build();

        context.apply(new MigrateJavaxPersistenceToJakarta());

        String rewritten = context.getProjectJavaSources().list().get(0).print();
        assertThat(rewritten).contains("import jakarta.persistence.Column;");
        assertThat(rewritten).contains("import jakarta.persistence.Entity;");
        assertThat(rewritten).contains("import jakarta.persistence.Id;");
        assertThat(rewritten).contains("import jakarta.persistence.Table;");
        assertThat(rewritten).contains("private jakarta.persistence.EntityManager em;");
        assertThat(rewritten).doesNotContain("javax.persistence");
    }

    @Test
    void shouldMigrateJavaxPersistenceViaDeclarativeRecipeAdapter() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/product/Product.java", productSource)
                .build();

        OpenRewriteDeclarativeRecipeAdapter adapter = new OpenRewriteDeclarativeRecipeAdapter();
        adapter.setRewriteRecipeLoader(new RewriteRecipeLoader());
        adapter.setOpenRewriteRecipe(
                "type: specs.openrewrite.org/v1beta/recipe\n" +
                "name: org.springframework.sbm.boot.upgrade_27_30.MigrateJavaxPersistence\n" +
                "displayName: Migrate javax.persistence to jakarta.persistence\n" +
                "description: Migrate javax.persistence to jakarta.persistence\n" +
                "recipeList:\n" +
                "  - org.springframework.sbm.boot.upgrade_27_30.MigrateJavaxPersistenceToJakarta\n"
        );

        adapter.apply(context);

        String rewritten = context.getProjectJavaSources().list().get(0).print();
        assertThat(rewritten).contains("import jakarta.persistence.Column;");
        assertThat(rewritten).contains("import jakarta.persistence.Entity;");
        assertThat(rewritten).contains("import jakarta.persistence.Id;");
        assertThat(rewritten).contains("import jakarta.persistence.Table;");
        assertThat(rewritten).contains("private jakarta.persistence.EntityManager em;");
        assertThat(rewritten).doesNotContain("javax.persistence");
    }

    @Test
    void shouldMigrateJavaxPersistenceViaYamlRecipe() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withSpringBootParentOf("3.0.0")
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/product/Product.java", productSource)
                .build();

        RecipeTestSupport.testRecipe(Path.of("recipes/27_30/migration/sbu30-migrate-to-jakarta-packages.yaml"), recipes -> {
            Recipe recipe = recipes.getRecipeByName("sbu30-migrate-to-jakarta-packages").get();
            recipe.apply(context);

            String rewritten = context.getProjectJavaSources().list().get(0).print();
            assertThat(rewritten).contains("import jakarta.persistence.Column;");
            assertThat(rewritten).contains("import jakarta.persistence.Entity;");
            assertThat(rewritten).contains("import jakarta.persistence.Id;");
            assertThat(rewritten).contains("import jakarta.persistence.Table;");
            assertThat(rewritten).contains("private jakarta.persistence.EntityManager em;");
            assertThat(rewritten).doesNotContain("javax.persistence");
        });
    }
}
