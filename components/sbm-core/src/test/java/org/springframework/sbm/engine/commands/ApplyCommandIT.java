package org.springframework.sbm.engine.commands;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.scopeplayground.RecipeRuntimeScope;
import org.springframework.sbm.scopeplayground.RecipeScope;
import org.springframework.sbm.scopeplayground.ScopeConfiguration;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Architectural Fitnesse Function Test for the concept of a 'recipeScope'.
 *
 * The {@link ExecutionContext} has {@link RecipeScope}.
 * The recipe scope starts with the {@link ApplyCommand#execute(ProjectContext, String)} and ends when the method returns.
 * The 'recipeScope' is a {@link org.springframework.context.support.SimpleThreadScope} and two parallel threads calling the
 * {@link ApplyCommand#execute(ProjectContext, String)} method should have two different instances of {@link ExecutionContext}
 * injected. All objects in the same thread with a {@link ExecutionContext} injected should use the exact same instance.
 *
 * The test verifies this by calling the {@link ApplyCommand#execute(ProjectContext, String)} method in two parallel threads.
 * The executed recipe {@link ApplyCommandITDummyRecipe} has one action {@link ApplyCommandITDummyRecipe#testSupportAction()}
 * which generates a UUID and stores it as message in the injected {@link ExecutionContext} under key 'id'.
 *
 * The testLog is populated with
 *
 *
 * @author Fabian Krüger
 */
@Slf4j
@SpringBootTest(classes = {ApplyCommand.class, RecipesBuilder.class, RecipeParser.class, SbmRecipeLoader.class, YAMLMapper.class, CustomValidator.class, ResourceHelper.class, CustomValidatorBean.class, RecipeRuntimeScope.class, ScopeConfiguration.class, ApplyCommandITDummyRecipe.class})
class ApplyCommandIT {
    public static final String RECIPE_NAME = "dummy-recipe";

    @MockBean
    private GitSupport gitSupport;

    @MockBean
    private ProjectContextSerializer contextSerializer;

    @MockBean
    private ProjectSyncVerifier projectSyncVerifier;

    @Autowired
    TestHelper testHelper;

    @Autowired
    TestLog testLog;

    @Test
    void twoParallelRecipeRunsShouldHaveIndependentExecutionContexts() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> r1 = executorService.submit(() -> {
            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(PomBuilder.buildPom("com.acme:a:1.0.0").build())
                    .build();
            String s = testHelper.printId(projectContext, RECIPE_NAME);
            return s;
        });
        Future<String> r2 = executorService.submit(() -> {
            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(PomBuilder.buildPom("com.acme:b:1.0.0").build())
                    .build();
            String s = testHelper.printId(projectContext, RECIPE_NAME);
            return s;
        });

        // wait for the threads to finish
        Object uuid1 = r1.get();
        Object uuid2 = r2.get();

        assertThatFourExecutionContextsWereCreated(testLog);

        List<TestLog.CallToApplyEvent> callsToApply = getCallsToApply(testLog);
        assertThatEveryThreadUsedIndependentExecutionCotext(callsToApply);

        List<TestLog.TestHelperBeforeApplyEvent> testHelperBeforeApplyEvents = testLog
                .getLog()
                .stream()
                .filter(TestLog.TestHelperBeforeApplyEvent.class::isInstance)
                .map(TestLog.TestHelperBeforeApplyEvent.class::cast)
                .collect(Collectors.toList());
        assertThatCodeOutsideRecipeRunUsesNewExecutionContext(testHelperBeforeApplyEvents, callsToApply);
    }

    private void assertThatCodeOutsideRecipeRunUsesNewExecutionContext(List<TestLog.TestHelperBeforeApplyEvent> testHelperBeforeApplyEvents, List<TestLog.CallToApplyEvent> callsToApply) {
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(testHelperBeforeApplyEvents.get(1).contextId());
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(callsToApply.get(0).contextId());
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(callsToApply.get(1).contextId());
        assertThat(callsToApply.get(0).contextId()).isNotEqualTo(callsToApply.get(1).contextId());
    }

    private void assertThatEveryThreadUsedIndependentExecutionCotext(List<TestLog.CallToApplyEvent> callsToApply) {
        // get log about the ExecutionContext in execute()
        assertThat(callsToApply).hasSize(2);
        // The two ExecutionContext used in {@link ApplyCommand#execute(ProjectContext, String)} method
        // have a message 'id'
        assertThat(callsToApply).allMatch(e -> e.id() != null);
        // and the id differs
        assertThat(callsToApply.get(0).id()).isNotEqualTo(callsToApply.get(1).id());
        // Different ProjectContext (buildfile's artifactId) were provided to execute
        assertThat(callsToApply)
                .anyMatch(e -> e.artifactId().equals("a"))
                .anyMatch(e -> e.artifactId().equals("b"));
        // The ExecutionContext instances differ
        assertThat(callsToApply.get(0).contextId()).isNotEqualTo(callsToApply.get(1).contextId());
    }

    @NotNull
    private List<TestLog.CallToApplyEvent> getCallsToApply(TestLog testLog) {
        return testLog
                .getLog()
                .stream()
                .filter(TestLog.CallToApplyEvent.class::isInstance)
                .map(TestLog.CallToApplyEvent.class::cast)
                .collect(Collectors.toList());
    }

    private void assertThatFourExecutionContextsWereCreated(TestLog testLog) {
        List<Object> executionContextCreationEvents = testLog
                .getLog()
                .stream()
                .filter(TestLog.CreatedExecutionContextEvent.class::isInstance)
                .collect(Collectors.toList());
        // Two ExecutionContext were created during recipe execution, one per thread.
        // Another two were created in {@link TestHelper#printId}.
        assertThat(executionContextCreationEvents).hasSize(6);
    }

    @Getter
    @RequiredArgsConstructor
    static class TestHelper {

        private final ApplyCommand applyCommand;
        private final ExecutionContext executionContext;

        private final TestLog testLog;

        public String printId(ProjectContext projectContext, String recipeName) {
//            System.out.println("Context in print: " + executionContext.getMessage("contextId"));
            testLog.testHelperBeforeApplyCommand(executionContext.getMessage("contextId"), projectContext.getBuildFile().getArtifactId(), recipeName);
            applyCommand.execute(projectContext, recipeName);
            testLog.testHelperAfterApplyCommand(executionContext.getMessage("contextId"));
            // executionContext has been removed from scope in execute(), the getMessage(..) will be null
            String id = executionContext.getMessage("id");
            // TODO: keep
            System.out.println(executionContext.getMessage("contextId") + " ID was: " + id);
            return id;
        }
    }


}


@TestConfiguration
class ApplyCommandITDummyRecipe {

    @Primary
    @Bean(name ="executionContext")
    @RecipeScope
    public ExecutionContext executionContext(TestLog testLog) {
        String s = UUID.randomUUID().toString();
        RewriteExecutionContext executionContext = new RewriteExecutionContext();
        executionContext.putMessage("contextId", s);
        // now we can count calls
        testLog.createdExecutionContext(executionContext.getMessage("contextId"));
        return executionContext;
    }

    @Bean
    ApplyCommandIT.TestHelper testHelper(ApplyCommand applyCommand, ExecutionContext executionContext, TestLog testLog) {
        return new ApplyCommandIT.TestHelper(applyCommand, executionContext, testLog);
    }

    @Bean
    TestLog testLog() {
        return new TestLog();
    }

    @Bean
    Action testSupportAction() {
        AbstractAction action = new AbstractAction() {
            @Autowired
            private ExecutionContext executionContext;

            @Autowired
            private TestLog testLog;

            @Override
            public void apply(ProjectContext context) {
                String uuid = UUID.randomUUID().toString();
                testLog.applyBeforePutMessage(executionContext.getMessage("contextId"), executionContext.getMessage("id"));
                executionContext.putMessage("id", uuid);
                testLog.applyCalled(executionContext.getMessage("contextId"), context.getBuildFile().getArtifactId(), executionContext.getMessage("contextId"));
            }
        };
        return action;
    }

    @Bean
    Recipe dummyRecipe(Action testSupportAction) {
        return Recipe.builder().name("dummy-recipe").action(testSupportAction).build();
    }
}

class TestLog {
    @Getter
    private List<Object> log = Collections.synchronizedList(new ArrayList<>());

    public void applyCalled(String contextId, String artifactId, String id) {
        log.add(new CallToApplyEvent(contextId, artifactId, id));
    }

    public static void contextId(String uuid, Object contextId) {
    }

    public void createdExecutionContext(String contextId) {
        log.add(new CreatedExecutionContextEvent(contextId));
    }

    public void testHelperBeforeApplyCommand(String contextId, String artifactId, String recipeName) {
        log.add(new TestHelperBeforeApplyEvent(contextId, artifactId, recipeName));
    }

    public void testHelperAfterApplyCommand(String contextId) {
        log.add(new TestHelperAfterApplyEvent(contextId));
    }

    public void applyBeforePutMessage(String contextId, String id) {
        log.add(new ApplyBeforePutMessageEvent(contextId, id));
    }

    class LogEntry {
        private String contextId;
    }

    record CreatedExecutionContextEvent(String contextId) {}
    record CallToApplyEvent(String contextId, String artifactId, String id) {}
    record TestHelperBeforeApplyEvent(String contextId, String artifactId, String recipeName) {}
    record TestHelperAfterApplyEvent(String contextId) {}
    private record ApplyBeforePutMessageEvent(String contextId, String id) {}
}
