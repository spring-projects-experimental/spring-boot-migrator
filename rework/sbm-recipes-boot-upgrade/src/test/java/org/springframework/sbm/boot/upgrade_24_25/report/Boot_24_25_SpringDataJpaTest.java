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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class Boot_24_25_SpringDataJpaTest {

    @Test
    @Disabled("FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/205")
    void isApplicable_withCallsToGetOne_shouldReturnTrue() {
        String model =
                "package com.example;\n" +
                "public class Tag {}";
        String repo =
                "package com.example;\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "\n" +
                "public interface TagRepository extends JpaRepository<Tag, Long> {}\n";
        String caller =
                "package com.example;\n" +
                "public class Caller {\n" +
                "    TagRepository repo;" +
                "    public void call() {\n" +
                "        repo.getOne(1L);\n" +
                "    }\n" +
                "}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withJavaSource("src/main/java", model)
                .withJavaSource("src/main/java", repo)
                .withJavaSource("src/main/java", caller)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-data-jpa:2.4.12")
                .build();

        Boot_24_25_SpringDataJpa sut = new Boot_24_25_SpringDataJpa();
        assertThat(sut.isApplicable(context)).isTrue();

    }

}