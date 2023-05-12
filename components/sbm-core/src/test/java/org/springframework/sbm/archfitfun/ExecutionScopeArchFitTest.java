package org.springframework.sbm.archfitfun;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.NamedThreadLocal;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.build.impl.RewriteMavenArtifactDownloader;
import org.springframework.sbm.build.impl.RewriteMavenParser;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.context.ProjectContextSerializer;
import org.springframework.sbm.engine.context.ProjectRootPathResolver;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.precondition.PreconditionVerifier;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.parser.*;
import org.springframework.sbm.project.resource.ProjectResourceSetHolder;
import org.springframework.sbm.project.resource.ProjectResourceWrapperRegistry;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.properties.parser.RewritePropertiesParser;
import org.springframework.sbm.scopeplayground.ExecutionRuntimeScope;
import org.springframework.sbm.scopeplayground.ProjectMetadata;
import org.springframework.sbm.scopeplayground.ScanRuntimeScope;
import org.springframework.sbm.scopeplayground.ExecutionScope;
import org.springframework.sbm.xml.parser.RewriteXmlParser;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.sbm.archfitfun.ExecutionScopeArchFitTest.ScopeCacheHelper.getCacheSnapshot;

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
 * should have its own {@link org.openrewrite.ExecutionContext} (or whatever bean is annotated with {@link ExecutionScope}) instance.
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
 * ## Special behaviour for {@link org.openrewrite.ExecutionContext}:
 * - When creating an `ExecutionContext` access to data gathered during scan/parse is required.
 * - It is not an aspect of this test to test the correct creation of `ExecutionContext`.
 *
 * @author Fabian Krüger
 */
@Slf4j
//@SpringBootTest(classes = {ApplyCommand.class, RecipesBuilder.class, RecipeParser.class, SbmRecipeLoader.class, YAMLMapper.class, CustomValidator.class, ResourceHelper.class, CustomValidatorBean.class, ScanRuntimeScope.class, ScopeConfiguration.class, ApplicableRecipeListCommand.class, ProjectRootPathResolver.class, ProjectContextInitializer.class})
@SpringBootTest(classes = {
        TheSpringContext.class,
        ScanRuntimeScope.class,
        ExecutionRuntimeScope.class,
        ScanCommand.class,
        ProjectRootPathResolver.class,
        PathScanner.class,
        SbmApplicationProperties.class,
        ResourceHelper.class,
        PreconditionVerifier.class,
        ProjectContextInitializer.class,
        ProjectContextFactory.class,
        ProjectResourceWrapperRegistry.class,
        ProjectResourceSetHolder.class,
        JavaRefactoringFactoryImpl.class,
        BasePackageCalculator.class,
        RewriteJavaParser.class,
        MavenProjectParser.class,
        ResourceParser.class,
        RewriteJsonParser.class,
        RewriteXmlParser.class,
        RewriteYamlParser.class, RewritePropertiesParser.class,
        RewritePlainTextParser.class, RewriteMavenParser.class,
        MavenSettingsInitializer.class,
        RewriteMavenArtifactDownloader.class,
        JavaProvenanceMarkerFactory.class,
        MavenConfigHandler.class,
        RewriteSourceFileWrapper.class
    }
)
class ExecutionScopeArchFitTest {
    public static final String RECIPE_NAME = "dummy-recipe";

    @MockBean
    private GitSupport gitSupport;

    @MockBean
    private ProjectContextSerializer contextSerializer;

    @MockBean
    private ProjectSyncVerifier projectSyncVerifier;


    //@MockBean
    //private ProjectContextInitializer projectContextInitializer;

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
    ScanCommand scanCommand;

    @Autowired
    ApplyRecipeCommand applyRecipeCommand;
    @Autowired
    EvaluateConditionsCommand evaluateConditionsCommand;
    @Autowired
    ScanProjectCommand scanProjectCommand;
    @Autowired
    ScanRuntimeScope scanRuntimeScope;
    @Autowired
    ExecutionRuntimeScope executionRuntimeScope;
    @Autowired
    private TestRecorder testRecorder;

    @MockBean
    PathScanner pathScanner;
    @MockBean
    private ProjectRootPathResolver projectRootPathResolver;

    @Test
    void scanEvaluateConditionsApplyRecipe() {
        // No beans in scan or execution scope
        assertThat(getCacheSnapshot(scanRuntimeScope)).isEmpty();
        assertThat(getCacheSnapshot(executionRuntimeScope)).isEmpty();

        // scan project
        String s = "target/dummy-path";
        Path projectRoot = Path.of(s);
        when(projectRootPathResolver.getProjectRootOrDefault(s)).thenReturn(projectRoot);
        when(pathScanner.scan(projectRoot)).thenReturn(List.of());

        scanCommand.execute(s);

        // ProjectMetadata was added to scan scope
        assertThat(getCacheSnapshot(scanRuntimeScope)).containsKey("scopedTarget.projectMetadata");
        ProjectMetadata projectMetadataAfterScan = ProjectMetadata.class.cast(getCacheSnapshot(scanRuntimeScope).get("scopedTarget.projectMetadata"));
        // no beans in execution scope
        assertThat(getCacheSnapshot(executionRuntimeScope)).isEmpty();

        // evaluating conditions starts the executionScope
        evaluateConditionsCommand.execute();

        // after scan the scna scope didn't change
        assertThat(getCacheSnapshot(scanRuntimeScope)).hasSize(1);
        assertThat(getCacheSnapshot(scanRuntimeScope)).containsKey("scopedTarget.projectMetadata");
        ProjectMetadata projectMetadataAfterConditions = ProjectMetadata.class.cast(
                getCacheSnapshot(scanRuntimeScope).get("scopedTarget.projectMetadata"));

        // Execution scope now contains an ExecutionContext
        assertThat(getCacheSnapshot(executionRuntimeScope)).hasSize(1);
        assertThat(getCacheSnapshot(executionRuntimeScope)).containsKey("scopedTarget.executionContext");
        String executionContextIdAfterConditions = ExecutionContext.class.cast(getCacheSnapshot(executionRuntimeScope).get("scopedTarget.executionContext")).getMessage("id");

        // apply recipe
        applyRecipeCommand.execute();

        // The ExecutionContext used in apply recipe was the same the in stance created in conditions
        String executionContextInRecipe = ((ExecutionContext)testRecorder.getEventsOfType(TestRecorder.EnteringApplyRecipeSnapshot.class).get(0).executionScopeCache().get("scopedTarget.executionContext")).getMessage("applyRecipe.íd");
        assertThat(executionContextInRecipe).isEqualTo(executionContextIdAfterConditions);
        // Scan runtime scope unchanged
        assertThat(getCacheSnapshot(scanRuntimeScope)).hasSize(1);
        assertThat(getCacheSnapshot(scanRuntimeScope)).containsKey("scopedTarget.projectMetadata");
        ProjectMetadata projectMetadataAfterRecipe = ProjectMetadata.class.cast(getCacheSnapshot(scanRuntimeScope).get("scopedTarget.projectMetadata"));
        // Exdecution scope is empty after applying the recipe
        assertThat(getCacheSnapshot(executionRuntimeScope)).isEmpty();
        // The ProjectMetadata was unchanged
        assertThat(projectMetadataAfterScan).isSameAs(projectMetadataAfterConditions).isSameAs(projectMetadataAfterRecipe);
    }

    static class ScopeCacheHelper {
        public static Map<String, Object> getCacheSnapshot(org.springframework.beans.factory.config.Scope scope) {
            Map<String, Object> threadScope = ((Map<String, Object>)ReflectionTestUtils.getField(
                    scope, "threadScope"));
            return new HashMap(threadScope);
        }
    }


    static class ScanProjectCommand {
        @Autowired
        private ProjectMetadata projectMetadata;
        @Autowired
        private TestRecorder testRecorder;
        public void execute() {
            testRecorder.enteringScanCommmand();
            projectMetadata.setMetadata("the metadata");
        }
    }

    static class EvaluateConditionsCommand {
        @Autowired
        private TestRecorder testRecorder;
        @Autowired
        private ExecutionContext executionContext;
        public void execute() {
            executionContext.putMessage("evaluateConditions.id", UUID.randomUUID().toString());
            testRecorder.exitingEvaluateConditionCommandSnapshot();
        }

    }

    static class ApplyRecipeCommand {

        @Autowired
        private ExecutionContext executionContext;

        @Autowired
        ExecutionRuntimeScope runtimeScope;

        @Autowired
        ConfigurableListableBeanFactory beanFactory;

        @Autowired
        private TestRecorder testRecorder;

        void execute() {
            testRecorder.enteringApplyRecipe();
            executionContext.putMessage("applyRecipe.id", UUID.randomUUID().toString());
            runtimeScope.clear(beanFactory);
        }
    }

    static class TestRecorder {

        @Autowired
        private ExecutionRuntimeScope executionRuntimeScope;

        @Autowired
        private ScanRuntimeScope scanRuntimeScope;

        @Getter
        private List<String> scopedBeanCreations = new ArrayList<>();
        private List<Object> events = new ArrayList<>();
        private List<ProjectMetadata> metadataCreations = new ArrayList<>();

        void executionContextCreated(String uuid) {
            this.scopedBeanCreations.add(uuid);
        }

        private Map<String, Object> getExecutionScopeCache() {
            return (Map<String, Object>) ScopeCacheHelper.getCacheSnapshot(executionRuntimeScope);
        }

        private Map<String, Object> getScanScopeCache() {
            return (Map<String, Object>) ScopeCacheHelper.getCacheSnapshot(scanRuntimeScope);
        }

        public void exitingApplyCommand() {
            this.events.add(new ExitingApplyCommandSnapshot(getScanScopeCache(), getExecutionScopeCache()));
        }

        public void exitingEvaluateConditionCommandSnapshot() {

        }

        public void enteringApplyRecipe() {
            events.add(new EnteringApplyRecipeSnapshot(getScanScopeCache(), getExecutionScopeCache()));
        }

        public <T> List<T> getEventsOfType(Class<T> eventClass) {
            return events.stream().filter(eventClass::isInstance).map(eventClass::cast).collect(Collectors.toList());
        }

        public void enteringScanCommmand() {
            events.add(new EnteringScanCommand(getScanScopeCache(), getExecutionScopeCache()));
        }

        public void projectMetadataCreated(ProjectMetadata projectMetadata) {
            metadataCreations.add(projectMetadata);
        }

        public record ExitingApplyCommandSnapshot(Map<String, Object> scanScopeCache,
                                                  Map<String, Object> executionScopeCache) {
        }

        public record EnteringScanCommand(Map<String, Object> scanScopeCache,
                                          Map<String, Object> executionScopeCache) {
        }

        private record EnteringApplyRecipeSnapshot(Map<String, Object> scanScopeCache,
                                                   Map<String, Object> executionScopeCache) {
        }
    }



}

@TestConfiguration
class TheSpringContext {

    @Bean
    ExecutionScopeArchFitTest.ApplyRecipeCommand applyRecipeCommand() {
        return new ExecutionScopeArchFitTest.ApplyRecipeCommand();
    }

    @Bean
    ExecutionScopeArchFitTest.ScanProjectCommand scanProjectCommand() {
        return new ExecutionScopeArchFitTest.ScanProjectCommand();
    }

    @Bean
    ExecutionScopeArchFitTest.EvaluateConditionsCommand evaluateConditionsCommand() {
        return new ExecutionScopeArchFitTest.EvaluateConditionsCommand();
    }

    @Bean
    @Scope(scopeName = ScanRuntimeScope.SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
    ProjectMetadata projectMetadata() {
        ProjectMetadata projectMetadata = new ProjectMetadata();
        testObserver().projectMetadataCreated(projectMetadata);
        return projectMetadata;
    }

    @Bean
    @ExecutionScope
    ExecutionContext executionContext(ProjectMetadata projectMetadata) {
        RewriteExecutionContext rewriteExecutionContext = new RewriteExecutionContext();
        rewriteExecutionContext.putMessage("executionContextId", UUID.randomUUID().toString());
        return rewriteExecutionContext;
    }

    @Bean
    ExecutionScopeArchFitTest.TestRecorder testObserver() {
        return new ExecutionScopeArchFitTest.TestRecorder();
    }

}