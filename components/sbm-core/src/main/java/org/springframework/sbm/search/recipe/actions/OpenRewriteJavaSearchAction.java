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
package org.springframework.sbm.search.recipe.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openrewrite.java.JavaParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.recipe.DisplayDescription;
import org.springframework.sbm.engine.recipe.FrameworkSupportAction;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.impl.OpenRewriteRecipeJavaSearch;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openrewrite.Recipe;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenRewriteJavaSearchAction extends FrameworkSupportAction {

    @JsonIgnore
    private String rewriteRecipeDefinition;
    private Recipe rewriteRecipe;
    private String commentText;
    private String description;
    private Condition condition = Condition.TRUE;
    @JsonIgnore
    @Autowired
    private JavaParser javaParser;


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public String getDetailedDescription() {
        final String conditionDescription =
                (getCondition().getDescription().isBlank())
                        ? ""
                        : getCondition().getDescription() + " then\n\t  ";
        return conditionDescription + getDescription();
    }

    @Override
    public boolean isAutomated() {
        return !this.getClass().isAssignableFrom(DisplayDescription.class);
    }


    public void apply(ProjectContext context) {
        OpenRewriteRecipeJavaSearch recipeJavaSearch = new OpenRewriteRecipeJavaSearch((compilationUnits -> rewriteRecipe.run(compilationUnits).getResults()), javaParser);
        recipeJavaSearch.commentFindings(context.getProjectJavaSources().list(), commentText);
    }

}
