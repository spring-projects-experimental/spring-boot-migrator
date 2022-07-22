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
package org.springframework.sbm.boot.upgrade.common.actions;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAutoconfigurationActionTest {

    private ProjectContext context;
    private CreateAutoconfigurationAction action;

    private final String newAutoConfigFile = "/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";
    private final String existingSpringFactoriesFile = "/**/src/main/resources/META-INF/spring.factories";

    @BeforeEach
    public void setup() {
        context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                hello.world=something
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ
                                """
                )
                .build();
        action = new CreateAutoconfigurationAction();
    }

    @Test
    public void autoConfigurationImportsIsGenerated() {
        action.apply(context);

        assertThat(getFileAsProjectResource(newAutoConfigFile)).hasSize(1);
    }

    @Test
    public void autoConfigurationImportsContent() {
        action.apply(context);

        String content = getNewAutoConfigFile();

        assertThat(content).isEqualTo("XYZ");
    }

    @Test
    public void itDeletesSourceWhenMovedToNewFile() {
        action.apply(context);

        String content = getSpringFactoryFile();

        assertThat(content).isEqualTo("""
                hello.world=something
                """);
    }

    @Test
    public void shouldMoveMultipleProperties() {
        context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        """
                                hello.world=something
                                org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
                                XYZ,\
                                ABC,\
                                DEF
                                """
                )
                .build();

        action.apply(context);

        String content = getNewAutoConfigFile();

        assertThat(content).isEqualTo("""
                XYZ
                ABC
                DEF"""
        );
    }

    private String getNewAutoConfigFile() {
        return getFileAsProjectResource(
                "/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"
        ).get(0).print();
    }

    private List<ProjectResource> getFileAsProjectResource(String path) {
        return context.search(new PathPatternMatchingProjectResourceFinder(path));
    }

    private String getSpringFactoryFile() {

        return getFileAsProjectResource(existingSpringFactoriesFile).get(0).print();
    }
}
