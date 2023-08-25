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
package org.springframework.sbm.build.migration.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fabian Krüger
 */
class AnyEffectiveDependencyExistMatchingRegexTest {

    @Test
    void shouldReturnTrueWhenDeclaredDependencyExists() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.batch:spring-batch-core:5.0.0")
                .build();

        boolean exists = new AnyEffectiveDependencyExistMatchingRegex(
                List.of(
                    "org\\.springframework\\.boot\\:.*",
                    "org\\.springframework\\.batch\\:.*"
                )
        ).evaluate(context);

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnTrueWhenTransitiveDependencyExists() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-batch:3.0.0")
                .build();

        boolean exists = new AnyEffectiveDependencyExistMatchingRegex(
                List.of(
                        "org\\.springframework\\.batch\\:.*"
                )
        ).evaluate(context);

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenNoEffectiveDependencyExists() {
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework.batch:spring-batch-core:5.0.0")
                .build();

        boolean exists = new AnyEffectiveDependencyExistMatchingRegex(
                List.of("org\\.springframework\\.boot\\:.*")).evaluate(context);

        assertThat(exists).isFalse();
    }
}