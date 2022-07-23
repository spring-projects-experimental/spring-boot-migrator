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
import org.openrewrite.Result;
import org.openrewrite.config.YamlResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        RecipeParser.class,
        YamlObjectMapperConfiguration.class,
        CustomValidator.class,
        ResourceHelper.class,
        ActionDeserializerRegistry.class,
        RewriteMigrationResultMerger.class,
        RewriteSourceFileWrapper.class,
        CustomValidatorBean.class
})
class OpenRewriteDeclarativeRecipeAdapterTest {

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
                        "    - type: org.springframework.sbm.engine.recipe.OpenRewriteDeclarativeRecipeAdapter\n" +
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

        Recipe[] recipes = recipeParser.parseRecipe(yaml);
        assertThat(recipes[0].getActions().get(0)).isInstanceOf(OpenRewriteDeclarativeRecipeAdapter.class);
        OpenRewriteDeclarativeRecipeAdapter recipeAdapter = (OpenRewriteDeclarativeRecipeAdapter) recipes[0].getActions().get(0);

        String javaSource = "@java.lang.Deprecated\n" +
                "public class Foo {}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", javaSource)
                .build();

        recipeAdapter.apply(context);

        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "public class Foo {\n" +
                        "}"
        );
    }
}