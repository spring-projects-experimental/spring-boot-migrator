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
package org.springframework.sbm.test;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.testhelper.common.utils.TestDiff;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JavaMigrationActionTestSupport {

    /**
     * Verifies that applying action against a <code>JavaSource</code> results in expected <code>JavaSource</code>.
     *
     * @param given the given Java source code
     * @param expected the expected Java source code
     * @param action the action to apply
     * @param classpath entries in <code>artifact:groupId:version</code> format.
     */
    public static void verify(String given, String expected, AbstractAction action, String... classpath) {
        TestProjectContext.Builder builder = TestProjectContext.buildProjectContext()
                .withJavaSources(given);

        if(classpath.length > 0) {
            builder.withBuildFileHavingDependencies(classpath);
        }
        ProjectContext projectContext = builder.build();
        action.apply(projectContext);
        assertResultMatchesExpected(expected, projectContext);
    }

    /**
     * Verifies that applying an action against a list of <code>JavaSource</code>s results in list of <code>JavaSource</code>s.
     *
     * @param given the given Java source code
     * @param expected the expected Java source code
     * @param action the action to apply
     * @param classpath entries in <code>artifact:groupId:version</code> format.
     */
    public static void verify(List<String> given, List<String> expected, AbstractAction action, String... classpath) {
        TestProjectContext.Builder builder = TestProjectContext.buildProjectContext()
                .withJavaSources(given.toArray(new String[]{}));

        if(classpath.length > 0) {
            builder.withBuildFileHavingDependencies(classpath);
        }
        ProjectContext projectContext = builder.build();
        action.apply(projectContext);
        assertResultMatchesExpected(expected, projectContext);
    }

    private static void assertResultMatchesExpected(List<String> expected, ProjectContext projectContext) {
        for(int i=0; i<expected.size(); i++) {
            assertResultMatchesExpected(i, expected.get(i), projectContext);
        }
    }

    private static void assertResultMatchesExpected(String expected, ProjectContext projectContext) {
        assertResultMatchesExpected(0, expected, projectContext);
    }

    private static void assertResultMatchesExpected(int index, String expected, ProjectContext projectContext) {
        String result = projectContext.getProjectJavaSources().list().get(index).getResource().print();

        assertThat(result)
                .as(TestDiff.of(result, expected))
                .isEqualTo(expected);
    }


}
