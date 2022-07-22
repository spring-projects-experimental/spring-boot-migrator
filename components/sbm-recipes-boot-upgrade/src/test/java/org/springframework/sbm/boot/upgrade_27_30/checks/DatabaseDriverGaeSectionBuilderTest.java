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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheckResult;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.precondition.PreconditionCheck;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseDriverGaeSectionBuilderTest {

    @Test
    void checkShouldFailWhenAppEngineDriverIsFoundOnClasspath() {

        String javaClass =
                "import org.springframework.boot.jdbc.DatabaseDriver;\n" +
                        "\n" +
                        "public class Foo {\n" +
                        "    public Foo() {\n" +
                        "        DatabaseDriver driver = DatabaseDriver.GAE;\n" +
                        "        com.google.appengine.api.rdbms.AppEngineDriver d;" +
                        "    }\n" +
                        "}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot:2.7.1"/*, "com.google.appengine:appengine-api-1.0-sdk:1.9.17"*/)
                .addJavaSource("src/main/java", javaClass)
                .build();

        Sbu30_PreconditionCheckResult result = new DatabaseDriverGaeSectionBuilder(new DatabaseDriverGaeFinder()).run(context);

        assertThat(result.getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(result.getMessage()).isEqualTo("Dependencies containing 'com.google.appengine.api.rdbms.AppEngineDriver' were found in these modules: 'com.example:dummy-root:0.1.0-SNAPSHOT'");

    }
}