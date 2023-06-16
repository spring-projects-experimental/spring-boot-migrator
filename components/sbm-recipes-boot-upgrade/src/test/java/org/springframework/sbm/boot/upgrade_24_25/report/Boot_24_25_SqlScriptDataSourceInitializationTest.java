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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class Boot_24_25_SqlScriptDataSourceInitializationTest {

    @Test
    void isApplicable_withContinueOnError_shouldReturnTrue() {
        String applicationPropertiesLines =
                "spring.datasource.continue-on-error=spring.sql.init.continue-on-error";

        ProjectContext projectContext = getProjectContext(applicationPropertiesLines);

        Boot_24_25_SqlScriptDataSourceInitialization sut = new Boot_24_25_SqlScriptDataSourceInitialization();

        boolean isApplicable = sut.isApplicable(projectContext);
        assertThat(isApplicable).isTrue();
    }

    private ProjectContext getProjectContext(String applicationPropertiesLines) {
        Path rootDirectory = Path.of("./dummy");

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(rootDirectory)
                .withProjectResource("src/main/resources/application.properties", applicationPropertiesLines)
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();

        return projectContext;
    }

}