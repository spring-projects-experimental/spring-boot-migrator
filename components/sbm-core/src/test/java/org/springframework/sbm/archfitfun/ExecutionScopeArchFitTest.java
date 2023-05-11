package org.springframework.sbm.archfitfun;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.scopeplayground.ScanRuntimeScope;
import org.springframework.sbm.scopeplayground.ExecutionScope;
import org.springframework.sbm.scopeplayground.ScopeConfiguration;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Architectural Fitnesse Function for the concept of a `executionScope`.
 *
 * The `executionScope` starts either with the evaluation of conditions or with a recipe-run and it ends with the
 * recipe-run or when the application stops.
 * Beans annotated with {@link ExecutionScope} will be created with every start of an `executionScope` and put into the
 * scope. They will be removed on every end of a recipe-run.
 *
 * ## Aspects to test:
 * - ``executionScope``d beans are removed at the beginning of every execution of {@link org.springframework.sbm.engine.commands.ApplicableRecipeListCommand#execute(ProjectContext)}
 * - ``executionScope``d beans are added to the scope at the beginning of every execution of {@link org.springframework.sbm.engine.commands.ApplicableRecipeListCommand#execute(ProjectContext)}
 * - ``executionScope``d beans are added at the beginning of every execution of {@link org.springframework.sbm.engine.commands.ApplyCommand#execute(ProjectContext, String)} when no bean exists in the scope
 * - ``executionScope``d beans are retrieved from the scope at the beginning of every execution of {@link org.springframework.sbm.engine.commands.ApplyCommand#execute(ProjectContext, String)} when found in scope.
 * - The described behaviour works for all beans annotated with {@link ExecutionScope} without modifying production code.
 *
 * The 'executionScope' is a {@link org.springframework.context.support.SimpleThreadScope} and each thread accessing beans in this scope
 * should have its own {@link ExecutionContext} (or whatever bean is annotated with {@link ExecutionScope}) instance.
 * All objects in the same thread with such a bean injected will then use the exact same instance.
 *
 * The test verifies this behaviour by
 *
 * - Observing ExecutionContext creations and putting a UUID into the messages
 * - Starting two independent threads
 * - Where each thread executes a recipe
 * - The recipe has one action
 * - The action puts a UUIDs under a key 'id' into their `ExecutionContext}
 * - A last action copies the messages into a test observer
 *
 * ## Special behaviour for {@link ExecutionContext}:
 * - When creating an `ExecutionContext` access to data gathered during scan/parse is required.
 * - It is not an aspect of this test to test the correct creation of `ExecutionContext`.
 *
 *
 * @author Fabian Krüger
 */
@Slf4j
@SpringBootTest(classes = {ApplyCommand.class, RecipesBuilder.class, RecipeParser.class, SbmRecipeLoader.class, YAMLMapper.class, CustomValidator.class, ResourceHelper.class, CustomValidatorBean.class, ScanRuntimeScope.class, ScopeConfiguration.class, ApplicableRecipeListCommand.class, ProjectRootPathResolver.class, ProjectContextInitializer.class})
class ExecutionScopeArchFitTest {
    public static final String RECIPE_NAME = "dummy-recipe";

    @MockBean
    private GitSupport gitSupport;

    @MockBean
    private ProjectContextSerializer contextSerializer;

    @MockBean
    private ProjectSyncVerifier projectSyncVerifier;

    @MockBean
    private ProjectContextInitializer projectContextInitializer;

//    @Autowired
//    TestHelper testHelper;
//
//    @Autowired
//    TestObserver testObserver;


    // test: verify ApplicableRecipeListCommand removes beans from scope when started
    // test: verify ApplicableRecipeListCommand adds beans to scope when no exist (start the scope)
    // test: verify ApplyCommand uses existing scope when ApplicableRecipeListCommand was called before
    // test: verify ApplyCommand adds beans to scope when no exist (start the scope)
    // test: verify ApplyCommand removes all beans from scope when it ends
    // test: verify two parallel scopes have dedicated instances


    @Autowired
    private ApplicableRecipeListCommand applicableRecipeListCommand;

    @MockBean
    private ScanRuntimeScope runtimeScope;
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Test
    void applicableRecipeListCommandRemovesBeansFromScopeWhenStarted() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext().build();
        applicableRecipeListCommand.execute(projectContext);
        verify(runtimeScope).clear(beanFactory);
    }
/*
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

        assertThatFourExecutionContextsWereCreated(testObserver);

        List<TestObserver.CallToApplyEvent> callsToApply = getCallsToApply(testObserver);
        assertThatEveryThreadUsedIndependentExecutionCotext(callsToApply);

        List<TestObserver.TestHelperBeforeApplyEvent> testHelperBeforeApplyEvents = testObserver
                .getEvents()
                .stream()
                .filter(TestObserver.TestHelperBeforeApplyEvent.class::isInstance)
                .map(TestObserver.TestHelperBeforeApplyEvent.class::cast)
                .collect(Collectors.toList());
        assertThatCodeOutsideRecipeRunUsesNewExecutionContext(testHelperBeforeApplyEvents, callsToApply);
    }

    private void assertThatCodeOutsideRecipeRunUsesNewExecutionContext(List<TestObserver.TestHelperBeforeApplyEvent> testHelperBeforeApplyEvents, List<TestObserver.CallToApplyEvent> callsToApply) {
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(testHelperBeforeApplyEvents.get(1).contextId());
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(callsToApply.get(0).contextId());
        assertThat(testHelperBeforeApplyEvents.get(0).contextId()).isNotEqualTo(callsToApply.get(1).contextId());
        assertThat(callsToApply.get(0).contextId()).isNotEqualTo(callsToApply.get(1).contextId());
    }

    private void assertThatEveryThreadUsedIndependentExecutionCotext(List<TestObserver.CallToApplyEvent> callsToApply) {
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
    private List<TestObserver.CallToApplyEvent> getCallsToApply(TestObserver testObserver) {
        return testObserver
                .getEvents()
                .stream()
                .filter(TestObserver.CallToApplyEvent.class::isInstance)
                .map(TestObserver.CallToApplyEvent.class::cast)
                .collect(Collectors.toList());
    }

    private void assertThatFourExecutionContextsWereCreated(TestObserver testObserver) {
        List<Object> executionContextCreationEvents = testObserver
                .getEvents()
                .stream()
                .filter(TestObserver.ExecutionContextCreatedEvent.class::isInstance)
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

        private final TestObserver testObserver;

        public String printId(ProjectContext projectContext, String recipeName) {
//            System.out.println("Context in print: " + executionContext.getMessage("contextId"));
            testObserver.testHelperBeforeApplyCommand(executionContext.getMessage("contextId"), projectContext.getBuildFile().getArtifactId(), recipeName);
            applyCommand.execute(projectContext, recipeName);
            testObserver.testHelperAfterApplyCommand(executionContext.getMessage("contextId"));
            // executionContext has been removed from scope in execute(), the getMessage(..) will be null
            String id = executionContext.getMessage("id");
            // TODO: keep
            System.out.println(executionContext.getMessage("contextId") + " ID was: " + id);
            return id;
        }
    }
*/

}


//@TestConfiguration
//class ApplyCommandITDummyRecipe {
//
//    private String threadName;
//
//    @Primary
//    @Bean(name ="dummyBwean")
//    @ExecutionScope
//    public ExecutionContext executionContext(TestObserver testObserver) {
//        String s = UUID.randomUUID().toString();
//        RewriteExecutionContext executionContext = new RewriteExecutionContext();
//        executionContext.putMessage("contextId", s);
//        // track creation of ExecutionContexts
//        testObserver.createdExecutionContext(executionContext.getMessage("contextId"));
//        return executionContext;
//    }
//
//    @Bean
//    ExecutionScopeArchFitTest.TestHelper testHelper(ApplyCommand applyCommand, ExecutionContext executionContext, TestObserver testObserver) {
//        return new ExecutionScopeArchFitTest.TestHelper(applyCommand, executionContext, testObserver);
//    }
//
//    @Bean
//    TestObserver testLog() {
//        return new TestObserver();
//    }
//
//    @Bean
//    Action actionOne() {
//        AbstractAction action = new AbstractAction() {
//
//            @Autowired
//            ExecutionContextProvider executionContextProvider;
//
//            @Autowired
//            private ExecutionContext executionContext;
//
//            @Autowired
//            private TestObserver testLog;
//
//            @Override
//            public void apply(ProjectContext context) {
//
//                String uuid = UUID.randomUUID().toString();
//                testLog.applyBeforePutMessage(executionContext.getMessage("contextId"), executionContext.getMessage("id"));
//                executionContext.putMessage("action1", uuid);
//                testLog.applyCalled(executionContext.getMessage("contextId"), context.getBuildFile().getArtifactId(), executionContext.getMessage("contextId"));
//            }
//        };
//        return action;
//    }
//
//    @Bean
//    Action actionTwo() {
//        AbstractAction action = new AbstractAction() {
//            @Autowired
//            private ExecutionContext executionContext;
//
//            @Autowired
//            private TestObserver testLog;
//
//            @Override
//            public void apply(ProjectContext context) {
//                testLog.firstActionFirstRecipeStarted(executionContext.);
//
//                String uuid = UUID.randomUUID().toString();
//                testLog.applyBeforePutMessage(executionContext.getMessage("contextId"), executionContext.getMessage("id"));
//                executionContext.putMessage("id", threadName);
//                testLog.applyCalled(executionContext.getMessage("contextId"), context.getBuildFile().getArtifactId(), executionContext.getMessage("contextId"));
//            }
//        };
//        return action;
//    }
//
//    @Bean
//    Recipe threadOneRecipe(Action actionOne) {
//        return Recipe.builder().name("threadOneRecipe").action(actionOne).build();
//    }
//}
//
//class TestObserver {
//    @Getter
//    private List<Object> events = Collections.synchronizedList(new ArrayList<>());
//    private Map<Object, Object> clonedMessages;
//
//    public void createdExecutionContext(String contextId) {
//        events.add(new ExecutionContextCreatedEvent(contextId));
//    }
//
//    public void firstActionFirstRecipeStarted(Map<Object, Object> messages) {
//        clonedMessages = new HashMap<>(messages);
//    }
//
//    public void applyCalled(String contextId, String artifactId, String id) {
//        events.add(new CallToApplyEvent(contextId, artifactId, id));
//    }
//
//    public static void contextId(String uuid, Object contextId) {
//    }
//
//    public void testHelperBeforeApplyCommand(String contextId, String artifactId, String recipeName) {
//        events.add(new TestHelperBeforeApplyEvent(contextId, artifactId, recipeName));
//    }
//
//    public void testHelperAfterApplyCommand(String contextId) {
//        events.add(new TestHelperAfterApplyEvent(contextId));
//    }
//
//    public void applyBeforePutMessage(String contextId, String id) {
//        events.add(new ApplyBeforePutMessageEvent(contextId, id));
//    }
//
//    class LogEntry {
//        private String contextId;
//    }
//
//    record ExecutionContextCreatedEvent(String contextId) {}
//    record CallToApplyEvent(String contextId, String artifactId, String id) {}
//    record TestHelperBeforeApplyEvent(String contextId, String artifactId, String recipeName) {}
//    record TestHelperAfterApplyEvent(String contextId) {}
//    private record ApplyBeforePutMessageEvent(String contextId, String id) {}
//}
//*/