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


import org.junit.jupiter.api.Test;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAutoconfigurationActionTest {

    @Test
    public void autoConfigurationImportsIsGenerated() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ"
                )
                .build();

        CreateAutoconfigurationAction action = new CreateAutoconfigurationAction();

        action.apply(context);

        assertThat(context
                .search(
                        new PathPatternMatchingProjectResourceFinder("/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
                )
        ).hasSize(1);
    }

    @Test
    public void autoConfigurationImportsContent() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource(
                        "src/main/resources/META-INF/spring.factories",
                        "org.springframework.boot.autoconfigure.EnableAutoConfiguration=XYZ"
                )
                .build();

        CreateAutoconfigurationAction action = new CreateAutoconfigurationAction();

        action.apply(context);

        String content = context.search(
                new PathPatternMatchingProjectResourceFinder("/**/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
        ).get(0).print();

        assertThat(content).isEqualTo("XYZ");
    }
}
