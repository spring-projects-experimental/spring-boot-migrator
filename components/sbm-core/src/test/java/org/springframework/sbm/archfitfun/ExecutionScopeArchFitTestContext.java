package org.springframework.sbm.archfitfun;

import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.scopeplayground.ProjectMetadata;

import java.util.UUID;

/**
 * Bean definitions required for the test
 */
@TestConfiguration
public class ExecutionScopeArchFitTestContext {

    /**
     * Recipe for test.
     * It contains a condition and an action which allows observing scope behaviour during conditon evaluation and running recipes.
     */
    @Bean
    Recipe testRecipe() {
        return Recipe
                .builder()
                .name(ExecutionScopeArchFitTest.TEST_RECIPE_NAME)
                .condition(recipeCondition())
                .action(recipeAction())
                .build();
    }

    /**
     *
     */
    @Bean
    Action recipeAction() {
        return new AbstractAction() {
            @Autowired
            private ExecutionContext executionContext;
            @Autowired
            private ExecutionScopeArchFitTest.TestRecorder testRecorder;

            @Override
            public void apply(ProjectContext context) {
                String executionContextId = (String) executionContext.getMessage("executionContextId");
                testRecorder.executionContextInAction(executionContext);
                testRecorder.executionContextIdInAction(executionContextId);
            }
        };
    }

    @Bean
    Condition recipeCondition() {
        return new Condition() {
            @Autowired
            private ExecutionContext executionContext;
            @Autowired
            private ExecutionScopeArchFitTest.TestRecorder testRecorder;

            @Override
            public String getDescription() {
                return "Dummy test condition";
            }

            @Override
            public boolean evaluate(ProjectContext context) {
                String executionContextId = (String) executionContext.getMessage("executionContextId");
                testRecorder.executionContextInCondition(executionContext);
                testRecorder.executionContextIdInCondition(executionContextId);
                return true;
            }
        };
    }

    @Bean
    @org.springframework.sbm.scopeplayground.annotations.ScanScope
    ProjectMetadata projectMetadata() {
        ProjectMetadata projectMetadata = new ProjectMetadata();
        testRecorder().projectMetadataCreated(projectMetadata);
        return projectMetadata;
    }

    @Bean
    @org.springframework.sbm.scopeplayground.annotations.ExecutionScope
    ExecutionContext executionContext(ProjectMetadata projectMetadata) {
        String id = UUID.randomUUID().toString();
        RewriteExecutionContext rewriteExecutionContext = new RewriteExecutionContext();
        rewriteExecutionContext.putMessage("executionContextId", id);
        testRecorder().executionContextCreated(id);
        rewriteExecutionContext.putMessage("org.openrewrite.maven.settings", projectMetadata.getMavenSettings());
        return rewriteExecutionContext;
    }

    @Bean
    ExecutionScopeArchFitTest.TestRecorder testRecorder() {
        return new ExecutionScopeArchFitTest.TestRecorder();
    }

}