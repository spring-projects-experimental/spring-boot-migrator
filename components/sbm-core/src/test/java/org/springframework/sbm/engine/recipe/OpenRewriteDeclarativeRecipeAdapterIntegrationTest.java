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

package org.springframework.sbm.engine.recipe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.scopes.ExecutionScope;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.sbm.scopes.ScopeConfiguration;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
        RecipeParser.class,
        RewriteRecipeLoader.class,
        YamlObjectMapperConfiguration.class,
        CustomValidator.class,
        ResourceHelper.class,
        ActionDeserializerRegistry.class,
        DefaultActionDeserializer.class,
        RewriteMigrationResultMerger.class,
        RewriteRecipeRunner.class,
        RewriteSourceFileWrapper.class,
        CustomValidatorBean.class,
        RewriteExecutionContext.class,
        ScopeConfiguration.class,
        ExecutionScope.class,
        ScanScope.class
})
class OpenRewriteDeclarativeRecipeAdapterIntegrationTest {

    @Autowired
    RecipeParser recipeParser;

    @Test
    void adapterActionShouldExecuteOpenRewriteYamlRecipe() throws IOException {
        String validSbmRecipeYaml =
                        "- name: test-recipe\n" +
                        "  description: Replace deprecated spring.datasource.* properties\n" +
                        "  condition:\n" +
                        "    type: org.springframework.sbm.common.migration.conditions.TrueCondition\n" +
                        "  actions:\n" +
                        "    - type: org.springframework.sbm.engine.recipe.OpenRewriteDeclarativeRecipeAdapter\n" +
                        "      condition:\n" +
                        "        type: org.springframework.sbm.common.migration.conditions.TrueCondition\n" +
                        "      description: Call a OpenRewrite recipe\n" +
                        "      openRewriteRecipe: |-\n" +
                        "        type: specs.openrewrite.org/v1beta/recipe\n" +
                        "        name: org.openrewrite.java.RemoveAnnotation\n" +
                        "        displayName: Order imports\n" +
                        "        description: Order imports\n" +
                        "        recipeList:\n" +
                        "          - org.openrewrite.java.RemoveAnnotation:\n" +
                        "              annotationPattern: \"@java.lang.Deprecated\"\n" +
                        "          - org.openrewrite.java.format.AutoFormat";

        // parse the recipe
        Recipe[] recipes = recipeParser.parseRecipe(validSbmRecipeYaml);
        assertThat(recipes[0].getActions().get(0)).isInstanceOf(OpenRewriteDeclarativeRecipeAdapter.class);
        // retrieve adapter action
        OpenRewriteDeclarativeRecipeAdapter recipeAdapter = (OpenRewriteDeclarativeRecipeAdapter) recipes[0].getActions().get(0);
        // create a test prokect
        String javaSource = "@java.lang.Deprecated\n" +
                "public class Foo {}";
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", javaSource)
                .build();
        // run the adapter action and thus the declared rewrite recipes
        recipeAdapter.apply(context);
        // verify that the rewrite recipes were executed (reformatted, @Deprecated added)
        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "public class Foo {\n" +
                        "}"
        );
    }
}
