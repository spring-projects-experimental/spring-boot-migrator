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
package org.springframework.sbm.boot.properties.actions;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.sbm.test.ActionTest;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AddSpringBootApplicationPropertiesActionTest {

    @InjectMocks
    private final AddSpringBootApplicationPropertiesAction sut = new AddSpringBootApplicationPropertiesAction();
    private TestProjectContext.Builder projectContextBuilder;

    @BeforeEach
    void beforeEach() {
        projectContextBuilder = TestProjectContext.buildProjectContext()
                .withProjectRoot(Path.of("."));
    }

    @Test
    void apply() {
        ActionTest.withProjectContext(projectContextBuilder)
                .actionUnderTest(sut)
                .verify(projectContext -> {
                    SpringBootApplicationProperties springBootApplicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFinder()).get(0);
                    assertThat(springBootApplicationProperties).isNotNull();
                    assertThat(springBootApplicationProperties.hasChanges()).isTrue();
                });
    }

    @Test
    void isApplicableShouldReturnTrueWhenNoApplicationPropertiesFileExist() {
        boolean isApplicable = sut.isApplicable(projectContextBuilder.build());
        assertThat(isApplicable).isTrue();
    }

    @Test
    void isApplicableShouldReturnFalseWhenApplicationPropertiesFileExist() {
        ProjectContext projectContext = this.projectContextBuilder.build();
        projectContext.getProjectResources().add(SpringBootApplicationProperties.newApplicationProperties(projectContext.getProjectRootDirectory(), Path.of("./src/main/resources/application.properties"), new RewriteExecutionContext()));
        boolean isApplicable = sut.isApplicable(projectContext);
        assertThat(isApplicable).isFalse();
    }

}