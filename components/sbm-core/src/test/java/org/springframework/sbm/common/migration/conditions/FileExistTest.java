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
package org.springframework.sbm.common.migration.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FileExistTest {

    @Test
    void shouldReturnTrueWhenFileExist() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withEmptyProjectResource("src/main/resources/some.file")
                .build();

        FileExist sut = FileExist.builder().fileName("some.file").build();
        Assertions.assertThat(sut.evaluate(context)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenFileDoesntExist() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .build();
        FileExist sut = FileExist.builder().fileName("unknown.java").build();
        Assertions.assertThat(sut.evaluate(context)).isFalse();
    }
}
