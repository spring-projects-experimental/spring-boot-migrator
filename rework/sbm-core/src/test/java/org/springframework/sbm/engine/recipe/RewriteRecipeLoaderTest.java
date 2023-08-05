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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RewriteRecipeLoaderTest {

    @Test
    void shouldCreateRecipeFromValidYaml() {
        String rewriteRecipeDeclaration =
                """
                type: specs.openrewrite.org/v1beta/recipe
                name: org.openrewrite.java.spring.boot3.data.UpgradeSpringData30
                displayName: Upgrade to Spring Data 3.0
                description: 'Upgrade to Spring Data to 3.0 from any prior version.'
                recipeList:
                  - org.springframework.sbm.engine.recipe.ErrorClass
                """;

        RewriteRecipeLoader sut = new RewriteRecipeLoader();

        Recipe recipe = sut.createRecipe(rewriteRecipeDeclaration);
        assertThat(recipe.getDescription()).isEqualTo("Upgrade to Spring Data to 3.0 from any prior version.");
        assertThat(recipe.getDisplayName()).isEqualTo("Upgrade to Spring Data 3.0");
        assertThat(recipe.getName()).isEqualTo("org.openrewrite.java.spring.boot3.data.UpgradeSpringData30");
        assertThat(recipe.getRecipeList()).hasSize(1);
        assertThat(recipe.getRecipeList().get(0).getName()).isEqualTo("org.springframework.sbm.engine.recipe.ErrorClass");
    }

}