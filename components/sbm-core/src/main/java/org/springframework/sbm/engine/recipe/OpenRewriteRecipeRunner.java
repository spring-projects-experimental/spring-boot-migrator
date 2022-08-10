package org.springframework.sbm.engine.recipe;

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenRewriteRecipeRunner {
    @Autowired
    private RewriteMigrationResultMerger resultMerger;

    public void run(ProjectContext context, Recipe recipe) {
        List<? extends SourceFile> rewriteSourceFiles = context.search(new OpenRewriteSourceFilesFinder());
        List<Result> results = recipe.run(rewriteSourceFiles, new InMemoryExecutionContext(
                (t) -> {
                    throw new RuntimeException(t);
                }
        ));
        resultMerger.mergeResults(context, results);
    }

}
