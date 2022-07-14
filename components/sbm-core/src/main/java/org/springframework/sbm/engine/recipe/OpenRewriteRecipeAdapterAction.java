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
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.*;
import org.openrewrite.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.common.filter.AbsolutePathResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.nio.file.Path;
import java.util.*;

@Slf4j
//@RequiredArgsConstructor
public class OpenRewriteRecipeAdapterAction extends AbstractAction {

    private final Recipe recipe;

    @JsonIgnore
    @Autowired
    private RewriteMigrationResultMerger resultMerger;

//    private final ModifiableProjectResourceFactory modifiableProjectResourceFactory = new ModifiableProjectResourceFactory();


    @Override
    public boolean isApplicable(ProjectContext context) {
        return true;
        // FIXME: use getApplicableTest and getSingleSourceApplicableTest to calculate
        /*
        Method getApplicableTest = ReflectionUtils.findMethod(Recipe.class, "getApplicableTest");
        ReflectionUtils.makeAccessible(getApplicableTest);
        try {
            TreeVisitor<?, ExecutionContext> visitor = (TreeVisitor<?, ExecutionContext>) getApplicableTest.invoke(recipe);
            if(visitor == null) {
                return true;
            } else {
                List<SourceFile> search = context.search(new OpenRewriteSourceFileFinder());
                return visitor.visit(search, new InMemoryExecutionContext());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
         */
    }

    public OpenRewriteRecipeAdapterAction(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String getDescription() {
        return recipe.getDescription().isEmpty() ? recipe.getDisplayName() : recipe.getDescription();
    }
    
    @Override
    public void apply(ProjectContext context) {
        List<? extends SourceFile> rewriteSourceFiles = context.search(new OpenRewriteSourceFilesFinder());
        List<Result> results = recipe.run(rewriteSourceFiles);
        resultMerger.mergeResults(context, results);
    }


}