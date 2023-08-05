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
package org.springframework.sbm.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.migration.actions.RemoveManagedDependencies;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.function.Consumer;

/**
 * Test helper to test {@link Action} implementations that require injected Spring beans like e.g. {@link org.openrewrite.ExecutionContext}.
 *
 * Spring managed beans created during {@link ProjectContext} initialization will be injected into fields in {@link Action}s
 * when annotated with
 *
 * [source,java]
 * --
 * public class MyAction {
 *   {@literal @}Autowired
 *   {@literal @}JsonIgnore
 *   private ExecutionContext executionContext;
 * }
 * --
 *
 * @author Fabian Krüger
 */
public class ActionTest {
    private TestProjectContext.Builder projectContextBuilder;
    private Action actionUnderTest;

    public static ActionTest withProjectContext(TestProjectContext.Builder projectContextBuilder) {
        ActionTest actionTest = new ActionTest(projectContextBuilder);
        return actionTest;
    }

    /**
     * @param projectContextBuilder Builder for the {@link ProjectContext} that will be provided to the Action under test
     */
    private ActionTest(TestProjectContext.Builder projectContextBuilder) {
        this.projectContextBuilder = projectContextBuilder;
    }

    /**
     * Spring beans will be injected into Members annotated with @{@link Autowired} and @{@link JsonIgnore}.
     *
     * @param actionUnderTest the tested {@link Action} instance.
     */
    public ActionTest actionUnderTest(Action actionUnderTest) {
        this.actionUnderTest = actionUnderTest;
        return this;
    }

    /**
     * @param projectContextConsumer a {@link Consumer} taking the resulting {@link ProjectContext} to verify migrations.
     */
    public void verify(Consumer<ProjectContext> projectContextConsumer) {
        TestProjectContextInfo projectContext = projectContextBuilder.buildProjectContextInfo();
        projectContext.beanFactory().autowireBean(actionUnderTest);
        actionUnderTest.apply(projectContext.projectContext());
        projectContextConsumer.accept(projectContext.projectContext());
    }
}
