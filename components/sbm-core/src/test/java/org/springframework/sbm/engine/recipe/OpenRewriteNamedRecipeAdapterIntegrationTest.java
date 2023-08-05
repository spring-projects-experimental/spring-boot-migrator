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
        YamlObjectMapperConfiguration.class,
        CustomValidator.class,
        ResourceHelper.class,
        ActionDeserializerRegistry.class,
        DefaultActionDeserializer.class,
        RewriteMigrationResultMerger.class,
        RewriteSourceFileWrapper.class,
        RewriteRecipeLoader.class,
        CustomValidatorBean.class,
        RewriteExecutionContext.class,
        ScanScope.class,
        ExecutionScope.class,
        ScopeConfiguration.class
})
public class OpenRewriteNamedRecipeAdapterIntegrationTest {

    @Autowired
    RecipeParser recipeParser;

    @Test
    void recipeFromYaml() throws IOException {
        String yaml =
                "- name: test-recipe\n" +
                "  description: Replace deprecated spring.datasource.* properties\n" +
                "  condition:\n" +
                "    type: org.springframework.sbm.common.migration.conditions.TrueCondition\n" +
                "  actions:\n" +
                "    - type: org.springframework.sbm.engine.recipe.OpenRewriteNamedRecipeAdapter\n" +
                "      description: Call a OpenRewrite recipe\n" +
                "      openRewriteRecipeName: org.springframework.sbm.dummy.RemoveDeprecatedAnnotation\n";

        // parse the recipe
        Recipe[] recipes = recipeParser.parseRecipe(yaml);
        assertThat(recipes[0].getActions().get(0)).isInstanceOf(OpenRewriteNamedRecipeAdapter.class);
        // retrieve adapter action
        OpenRewriteNamedRecipeAdapter recipeAdapter = (OpenRewriteNamedRecipeAdapter) recipes[0].getActions().get(0);
        // create context
        String javaSource = "@java.lang.Deprecated\n" +
                "public class Foo {\n" +
                "}\n";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", javaSource)
                .build();
        // and apply the adapter
        recipeAdapter.apply(context);
        // verify the openrewrite recipe ran
        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "public class Foo {\n" +
                        "}\n"
        );
    }

    @Test
    public void propagateExceptionFromOpenRewriteRecipe() throws IOException {

        String actionDescription =
                "- name: test-recipe\n" +
                        "  description: Replace deprecated spring.datasource.* properties\n" +
                        "  condition:\n" +
                        "    type: org.springframework.sbm.common.migration.conditions.TrueCondition\n" +
                        "  actions:\n" +
                        "    - type: org.springframework.sbm.engine.recipe.OpenRewriteNamedRecipeAdapter\n" +
                        "      description: Test recipe producing exception\n" +
                        "      openRewriteRecipeName: org.springframework.sbm.engine.recipe.ErrorClass\n";

        Recipe[] recipes = recipeParser.parseRecipe(actionDescription);
        assertThat(recipes[0].getActions().get(0)).isInstanceOf(OpenRewriteNamedRecipeAdapter.class);
        OpenRewriteNamedRecipeAdapter recipeAdapter = (OpenRewriteNamedRecipeAdapter) recipes[0].getActions().get(0);

        String javaSource = "@java.lang.Deprecated\n" +
                "public class Foo {}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", javaSource)
                .build();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> recipeAdapter.apply(context));

        assertThat(thrown).hasRootCauseMessage("A problem happened whilst visiting");
    }
}
