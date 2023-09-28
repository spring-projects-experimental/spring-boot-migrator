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
package org.springframework.sbm.archfitfun;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.sbm.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.sbm.boot.autoconfigure.ScopeConfiguration;
import org.springframework.sbm.build.impl.MavenSettingsInitializer;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.*;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.git.ProjectSyncVerifier;
import org.springframework.sbm.engine.precondition.PreconditionVerifier;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.parser.*;
import org.springframework.sbm.project.resource.ProjectResourceSetHolder;
import org.springframework.sbm.project.resource.ProjectResourceWrapperRegistry;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.scopes.AbstractBaseScope;
import org.springframework.sbm.scopes.ExecutionScope;
import org.springframework.sbm.scopes.ProjectMetadata;
import org.springframework.sbm.scopes.ScanScope;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.sbm.archfitfun.ExecutionScopeArchFitTest.ScopeCacheHelper.getCacheSnapshot;

/**
 * Architectural Fitnesse Function for the concept of a `scanScope` ({@link org.springframework.sbm.scopes.annotations.ScanScope}) and `executionScope` ({@link org.springframework.sbm.scopes.annotations.ExecutionScope}).
 *
 * ## executionScope
 * Beans annotated with {@link org.springframework.sbm.scopes.annotations.ExecutionScope} will be created on first access and added to the executionScope.
 * Subsequent usages will receive the executionScoped instance from the scope until the scope ends and all scoped beans
 * get removed from the scope.
 *
 * The `executionScope` starts with
 * - the evaluation of conditions in {@link ApplicableRecipeListCommand#execute(ProjectContext)}
 * - or with a recipe-run in {@link ApplicableRecipeListCommand#execute(ProjectContext)}
 *
 * The `executionScope` ends with
 * - the end of recipe-run
 * - or when the application stops.
 *
 * ## scanScope
 * Beans annotated with {@link org.springframework.sbm.scopes.annotations.ScanScope} will be created on first access during scan/parse and added to the scanScope.
 * Subsequent usages will receive instances from the scanScope until the scope ends and all scoped beans get removed
 * from the scope.
 *
 * The `scanScope` starts with
 * - the scan of a given application in {@link ScanCommand}
 *
 * The `scanScope` ends with
 * - a new scan
 * - or when the application stops
 *
 * @author Fabian Krüger
 */
@Slf4j
@Disabled()
@SpringBootTest(classes = {
//                    ScanScope.class,
//                    ExecutionScope.class,
                    ScanCommand.class,
                    ProjectRootPathResolver.class,
//                    PathScanner.class,
                    SbmApplicationProperties.class,
                    ResourceHelper.class,
                    PreconditionVerifier.class,
                    ProjectContextInitializer.class,
                    ProjectContextFactory.class,
                    ProjectResourceWrapperRegistry.class,
                    ProjectResourceSetHolder.class,
                    ProjectContextHolder.class,
                    JavaRefactoringFactoryImpl.class,
                    BasePackageCalculator.class,
                    JavaParserBuilder.class,
//                    RewriteParserConfiguration.class,
//                    RewriteProjectParser.class,
//                    ResourceParser.class,
//                    RewriteJsonParser.class,
//                    RewriteXmlParser.class,
//                    RewriteYamlParser.class,
//                    RewritePropertiesParser.class,
//                    RewritePlainTextParser.class,
//                    RewriteMavenParser.class,
                    MavenSettingsInitializer.class,
                    MigrationResultProjectContextMerger.class,
                    JavaProvenanceMarkerFactory.class,
                    MavenConfigHandler.class,
                    RewriteSourceFileWrapper.class,
                    ApplyCommand.class,
                    RecipesBuilder.class,
                    RecipeParser.class,
                    YAMLMapper.class,
                    CustomValidator.class,
                    CustomValidatorBean.class,
                    ScopeConfiguration.class,
                    ApplicableRecipeListCommand.class,
                    ApplicableRecipesListHolder.class,
                    SbmRecipeLoader.class,
                    ExecutionScopeArchFitTestContext.class,
                    SbmSupportRewriteConfiguration.class
            },
            properties = {
                    "spring.main.allow-bean-definition-overriding=true", // required to provide custom ProjectMetadata
                    "debug=true"}
)
public class ExecutionScopeArchFitTest {
    public static final String TEST_RECIPE_NAME = "dummy-recipe";

    @Autowired
    ScanCommand scanCommand;
    @Autowired
    ApplicableRecipeListCommand applicableRecipeListCommand;
    @Autowired
    ApplyCommand applyCommand;
    @Autowired
    ScanScope scanScope;
    @Autowired
    ExecutionScope executionScope;
    @Autowired
    private TestRecorder testRecorder;
    // Having this bean mocked turns off GIT repo creation
    @MockBean
    private GitSupport gitSupport;
    // Having this bean mocked prevents writing to disk
    @MockBean
    private ProjectContextSerializer contextSerializer;
    @MockBean
    private ProjectSyncVerifier projectSyncVerifier;
    @MockBean
    PathScanner pathScanner;
    @MockBean
    private ProjectRootPathResolver projectRootPathResolver;


    /**
     * Test 'classic' flow of scan/evaluate conditions/run applicable recipe and changes in scanScope and executionScope.
     */
    @Test
    void scanEvaluateConditionsApplyRecipe() {
        // --- APPLICATION STARTUP
        // All scopes empty before first scan command
        assertThat(getCacheSnapshot(scanScope)).isEmpty();
        assertThat(getCacheSnapshot(executionScope)).isEmpty();

        // ---- SCAN ----
        // The scan/parse is the first command
        // Settings like MavenSettings are initialized during this phase and stored in the ExecutionContext
        // The data (like Maven settings) can be kept until next scan - scanScope
        // An ExecutionContext instance is created during this step and will be added to - executionScope

        // fixture
        String s = "testcode/scope-test";
        Path projectRoot = Path.of(s).toAbsolutePath().normalize();
        when(projectRootPathResolver.getProjectRootOrDefault(s)).thenReturn(projectRoot);
        when(pathScanner.scan(projectRoot)).thenReturn(List.of(new FileSystemResource(projectRoot.resolve("pom.xml"))));

        // execute command
        ProjectContext projectContext = scanCommand.execute(s);

        // assertions
        // One ExecutionContext was created...
        assertThat(testRecorder.getExecutionContextCreations()).hasSize(1);
        String executionContextIdAfterScan = testRecorder.getExecutionContextCreations().get(0);
        // and is now in executionScope
        assertThat(getCacheSnapshot(executionScope)).hasSize(1);
        ExecutionContext executionContextInExecutionScope = (ExecutionContext) getCacheSnapshot(
                executionScope).get("scopedTarget.executionContext");
        String executionContextIdInExecutionScope = executionContextInExecutionScope.getMessage("executionContextId");
        String executionContextIdRecorded = testRecorder.getExecutionContextCreations().get(0);
        // they are the same instance
        assertThat(executionContextIdInExecutionScope).isEqualTo(executionContextIdRecorded);
        // One ProjectMetadata creation recorded
        assertThat(testRecorder.getMetadataCreations()).hasSize(1);
        // get created Metadata
        ProjectMetadata metadataCreationIdRecorded = testRecorder.getMetadataCreations().get(0);
        // and Metadata from scanScope
        ProjectMetadata metadataInScanScope = ProjectMetadata.class.cast(getCacheSnapshot(
                scanScope).get("scopedTarget.projectMetadata"));
        assertThat(metadataInScanScope).isSameAs(metadataCreationIdRecorded);
        // metadata was added to ExecutionContext...
        MavenSettings projectMetadataInExecutionContext = MavenSettings.class.cast(executionContextInExecutionScope.getMessage("org.openrewrite.maven.settings"));
        // and is same as in scanScope
        assertThat(projectMetadataInExecutionContext).isSameAs(metadataInScanScope.getMavenSettings());


        // ---- CONDITIONS ----
        // the ApplicableRecipeListCommand evaluates conditions which starts a new
        // executionScope. All existing beans are removed from scope before conditions get evaluated.
        // The recipeCondition bean is evaluated and uses ExecutionContext
        // this creates a new instance which was added to executionScope

        // execute command
        List<Recipe> applicableRecipes = applicableRecipeListCommand.execute(projectContext);

        // assertions
        // No new ExecutionContext was created
        assertThat(testRecorder.getExecutionContextCreations()).hasSize(1);
        // The ExecutionContext available in condition is the one created during scan
        String executionContextIdInCondition = testRecorder.getExecutionContextIdInCondition();
        assertThat(executionContextIdInCondition).isEqualTo(executionContextIdInExecutionScope);
        // And is still in scope after scan
        String executionContextIdAfterConditions = ExecutionContext.class.cast(getCacheSnapshot(executionScope).get("scopedTarget.executionContext")).getMessage("executionContextId");
        assertThat(executionContextIdInCondition).isEqualTo(executionContextIdAfterConditions);
        // scan runtime scope didn't change
        assertThat(getCacheSnapshot(scanScope)).hasSize(2);
        assertThat(getCacheSnapshot(scanScope)).containsKey("scopedTarget.projectMetadata");
        assertThat(getCacheSnapshot(scanScope)).containsKey("scopedTarget.javaParserBuilder");
        // and no new ProjectMetadata was created
        assertThat(testRecorder.getMetadataCreations()).hasSize(1);
        // ProjectMetadata unchanged
        ProjectMetadata projectMetadataAfterConditions = ProjectMetadata.class.cast(getCacheSnapshot(
                scanScope).get("scopedTarget.projectMetadata"));
        assertThat(testRecorder.getMetadataCreations().get(0)).isSameAs(projectMetadataAfterConditions);

        // ---- APPLY RECIPE ----
        // execute command
        applyCommand.execute(projectContext, TEST_RECIPE_NAME);

        // assertions
        // executionScope is empty after applying the recipe
        assertThat(getCacheSnapshot(executionScope)).isEmpty();
        // No new ExecutionContext was created
        assertThat(testRecorder.getExecutionContextCreations()).hasSize(1);
        // the ExecutionContext that was injected into ApplyCommand is the same that was injected in ApplicableRecipeListCommand
        String executionContextIdInAction = testRecorder.getExecutionContextIdInAction();
        assertThat(executionContextIdInAction).isEqualTo(executionContextIdAfterScan);
        assertThat(executionContextIdInAction).isEqualTo(executionContextIdInCondition);
        assertThat(executionContextIdInAction).isEqualTo(executionContextIdAfterConditions);
        // scanScope unchanged
        assertThat(getCacheSnapshot(scanScope)).hasSize(2);
        assertThat(getCacheSnapshot(scanScope)).containsKey("scopedTarget.projectMetadata");
        assertThat(getCacheSnapshot(scanScope)).containsKey("scopedTarget.javaParserBuilder");
        ProjectMetadata projectMetadataAfterRecipe = ProjectMetadata.class.cast(getCacheSnapshot(
                scanScope).get("scopedTarget.projectMetadata"));
        // ProjectMetadata unchanged
        assertThat(testRecorder.getMetadataCreations().get(0)).isSameAs(projectMetadataAfterRecipe);
    }

    /**
     * Test helper to access the scoped beans of given scope
     */
    static class ScopeCacheHelper {
        public static Map<String, Object> getCacheSnapshot(AbstractBaseScope scope) {
            Map<String, Object> threadScope = ((Map<String, Object>) ReflectionTestUtils.getField(scope, "scopedBeans"));
            return new HashMap(threadScope);
        }
    }

    /**
     * Helper class recording state during test execution to allow observing state changes where state would otherwise
     * not be accessible.
     */
    public static class TestRecorder {
        @Getter
        private List<String> executionContextCreations = new ArrayList<>();
        private List<Object> events = new ArrayList<>();
        @Getter
        private List<ProjectMetadata> metadataCreations = new ArrayList<>();
        @Getter
        private ExecutionContext executionContextInAction;
        @Getter
        private ExecutionContext executionContextInCondition;
        @Getter
        private String executionContextIdInCondition;
        @Getter
        private String executionContextIdInAction;

        public void projectMetadataCreated(ProjectMetadata projectMetadata) {
            metadataCreations.add(projectMetadata);
        }

        public void executionContextInAction(ExecutionContext executionContext) {
            this.executionContextInAction = executionContext;
        }

        public void executionContextInCondition(ExecutionContext executionContext) {
            this.executionContextInCondition = executionContext;
        }

        public void executionContextCreated(String id) {
            this.executionContextCreations.add(id);
        }

        public void executionContextIdInCondition(String executionContextId) {
            this.executionContextIdInCondition = executionContextId;
        }

        public void executionContextIdInAction(String executionContextId) {
            this.executionContextIdInAction = executionContextId;
        }
    }

}

