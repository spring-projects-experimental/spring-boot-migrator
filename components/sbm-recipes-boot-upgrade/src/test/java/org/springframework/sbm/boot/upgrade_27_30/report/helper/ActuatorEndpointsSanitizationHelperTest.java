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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class ActuatorEndpointsSanitizationHelperTest {

    @Test
    void withSingleModuleApplication() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withSpringBootParentOf("3.0.0")
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-actuator")
                .build();

        ActuatorEndpointsSanitizationHelper sut = new ActuatorEndpointsSanitizationHelper();
        assertThat(sut.evaluate(context)).isTrue();
        assertThat(sut.getData()).hasSize(1);
        assertThat(sut.getData().get("matchingBuildFiles")).containsExactly(context.getApplicationModules().getRootModule().getBuildFile());
    }

    @Test
    void withMultiModuleApplication() {
        String parentPom = PomBuilder
                .buildParentPom("org.springframework.boot:spring-boot-starter-parent:3.0.0", "com.example:parent:1.0")
                .withModules("moduleA", "moduleB", "moduleC")
                .build();
        String moduleA = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleA")
                .compileScopeDependencies("com.example:moduleC:1.0")
                .build();
        String moduleB = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleB")
                .compileScopeDependencies("com.example:moduleC:1.0")
                .build();
        String moduleC = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleC")
                .compileScopeDependencies("org.springframework.boot:spring-boot-starter-actuator")
                .build();

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(parentPom)
                .withMavenBuildFileSource("moduleA", moduleA)
                .withMavenBuildFileSource("moduleB", moduleB)
                .withMavenBuildFileSource("moduleC", moduleC)
                .build();

        ActuatorEndpointsSanitizationHelper sut = new ActuatorEndpointsSanitizationHelper();
        assertThat(sut.evaluate(context)).isTrue();
        assertThat(sut.getData().get("matchingBuildFiles")).hasSize(3);
        assertThat(sut.getData().get("matchingBuildFiles")).containsExactly(
                context.getApplicationModules().getModule("moduleA").getBuildFile(),
                context.getApplicationModules().getModule("moduleB").getBuildFile(),
                context.getApplicationModules().getModule("moduleC").getBuildFile()
        );
    }

}