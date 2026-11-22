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
public class ThymeleafSpring6MigrationTest {

    private final String customPrometheusSource =
            "package org.fabianlee.springmicrowithactuator.actuator;\n" +
            "\n" +
            "import org.thymeleaf.spring5.SpringTemplateEngine;\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.stereotype.Component;\n" +
            "\n" +
            "@Component\n" +
            "public class CustomPrometheusEndpoint {\n" +
            "    @Autowired\n" +
            "    protected SpringTemplateEngine mMessageTemplateEngine;\n" +
            "    private org.thymeleaf.spring5.SpringTemplateEngine directEngine;\n" +
            "}\n";

    @Test
    void shouldMigrateThymeleafDirectly() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/actuator/CustomPrometheusEndpoint.java", customPrometheusSource)
                .build();

        context.apply(new MigrateThymeleafSpring5To6());

        String rewritten = context.getProjectJavaSources().list().get(0).print();
        assertThat(rewritten).contains("import org.thymeleaf.spring6.SpringTemplateEngine;");
        assertThat(rewritten).contains("private org.thymeleaf.spring6.SpringTemplateEngine directEngine;");
        assertThat(rewritten).doesNotContain("org.thymeleaf.spring5");
    }

    @Test
    void shouldMigrateThymeleafViaDeclarativeRecipeAdapter() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/actuator/CustomPrometheusEndpoint.java", customPrometheusSource)
                .build();

        OpenRewriteDeclarativeRecipeAdapter adapter = new OpenRewriteDeclarativeRecipeAdapter();
        adapter.setRewriteRecipeLoader(new RewriteRecipeLoader());
        adapter.setOpenRewriteRecipe(
                "type: specs.openrewrite.org/v1beta/recipe\n" +
                "name: org.springframework.sbm.boot.upgrade_27_30.MigrateThymeleafToSpring6\n" +
                "displayName: Migrate Thymeleaf Spring 5 to Spring 6\n" +
                "description: Migrate Thymeleaf Spring 5 to Spring 6\n" +
                "recipeList:\n" +
                "  - org.springframework.sbm.boot.upgrade_27_30.MigrateThymeleafSpring5To6\n"
        );

        adapter.apply(context);

        String rewritten = context.getProjectJavaSources().list().get(0).print();
        assertThat(rewritten).contains("import org.thymeleaf.spring6.SpringTemplateEngine;");
        assertThat(rewritten).contains("private org.thymeleaf.spring6.SpringTemplateEngine directEngine;");
        assertThat(rewritten).doesNotContain("org.thymeleaf.spring5");
    }

    @Test
    void shouldMigrateThymeleafViaUpgradeDependenciesYaml() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withSpringBootParentOf("3.0.0")
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/actuator/CustomPrometheusEndpoint.java", customPrometheusSource)
                .build();

        RecipeTestSupport.testRecipe(Path.of("recipes/27_30/migration/sbu30-upgrade-dependencies.yaml"), recipes -> {
            Recipe recipe = recipes.getRecipeByName("sbu30-upgrade-dependencies").get();
            recipe.apply(context);

            String rewritten = context.getProjectJavaSources().list().get(0).print();
            assertThat(rewritten).contains("import org.thymeleaf.spring6.SpringTemplateEngine;");
            assertThat(rewritten).contains("private org.thymeleaf.spring6.SpringTemplateEngine directEngine;");
            assertThat(rewritten).doesNotContain("org.thymeleaf.spring5");
        });
    }

    @Test
    void shouldMigrateThymeleafViaDedicatedThymeleafYaml() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withSpringBootParentOf("3.0.0")
                .withJavaSource("src/main/java/org/fabianlee/springmicrowithactuator/actuator/CustomPrometheusEndpoint.java", customPrometheusSource)
                .build();

        RecipeTestSupport.testRecipe(Path.of("recipes/27_30/migration/sbu30-migrate-thymeleaf.yaml"), recipes -> {
            Recipe recipe = recipes.getRecipeByName("sbu30-migrate-thymeleaf").get();
            recipe.apply(context);

            String rewritten = context.getProjectJavaSources().list().get(0).print();
            assertThat(rewritten).contains("import org.thymeleaf.spring6.SpringTemplateEngine;");
            assertThat(rewritten).contains("private org.thymeleaf.spring6.SpringTemplateEngine directEngine;");
            assertThat(rewritten).doesNotContain("org.thymeleaf.spring5");
        });
    }
}
