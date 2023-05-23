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
package org.springframework.sbm.boot.properties.finder;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootDefaultPropertiesFinderTest {

    @Test
    public void givenAProjectWithDefaultSpringBootProperties_applyFinder_expectPropertyFile(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .addProjectResource(Path.of("src","main", "resources", "application.properties"), "foo=bar")
                .build();

        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();
        assertThat(springBootDefaultPropertiesFinder.apply(projectContext.getProjectResources()).isPresent()).isTrue();
    }

    @Test
    public void givenAProjectWithoutDefaultSpringBootProperties_applyFinder_expectPropertyFile(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .build();

        SpringBootDefaultPropertiesFinder springBootDefaultPropertiesFinder = new SpringBootDefaultPropertiesFinder();
        assertThat(springBootDefaultPropertiesFinder.apply(projectContext.getProjectResources()).isEmpty()).isTrue();
    }
}
