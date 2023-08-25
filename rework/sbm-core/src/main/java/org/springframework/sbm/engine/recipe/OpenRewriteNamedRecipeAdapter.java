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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openrewrite.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenRewriteNamedRecipeAdapter extends AbstractAction {

    @Setter
    private String openRewriteRecipeName;

    @Autowired
    @JsonIgnore
    private RewriteRecipeLoader rewriteRecipeLoader;

    @JsonIgnore
    @Autowired
    private RewriteRecipeRunner rewriteRecipeRunner;

    @Override
    public void apply(ProjectContext context) {
        Recipe recipe = rewriteRecipeLoader.loadRewriteRecipe(openRewriteRecipeName);
        rewriteRecipeRunner.run(context, recipe);
    }
}
