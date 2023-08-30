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
package org.springframework.sbm.engine.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.openrewrite.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;

@SuperBuilder
@AllArgsConstructor
public class OpenRewriteDeclarativeRecipeAdapter extends AbstractAction {
    @Setter
    @Getter
    private String openRewriteRecipe;

    @Autowired
    @JsonIgnore
    @Setter
    private RewriteRecipeLoader rewriteRecipeLoader;

    public OpenRewriteDeclarativeRecipeAdapter() {
        super(builder());
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return super.isApplicable(context);
    }

    @Override
    public void apply(ProjectContext context) {
        Recipe recipe = rewriteRecipeLoader.createRecipe(openRewriteRecipe);
        context.apply(recipe);
    }
}
