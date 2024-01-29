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
package org.springframework.sbm.build.api;

import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.springframework.rewrite.parser.RewriteExecutionContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringManagedDependenciesTest {

    @Test
    public void pullBootStarter274Dependencies_expectJakartaAnnotationDependency(){
        String jakartaCoordinates = "jakarta.annotation:jakarta.annotation-api:1.3.5";

        ExecutionContext executionContext = new RewriteExecutionContext();
        assertThat(SpringManagedDependencies.by("org.springframework.boot", "spring-boot-starter", "2.7.4",
                                                executionContext)
                                     .stream()
                                     .map(Dependency::getGav)
                                     .anyMatch(jakartaCoordinates::equals)
        ).isTrue();
    }
}
