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
package org.springframework.sbm.boot.upgrade_24_25.recipes;

import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_24_25_SpringDataJpaRecipeTest {

    @Disabled("FIXME: flacky test, messed up concurrency maybe?")
    @Test
    void testRecipe() throws IOException {
        String applicationDir = "spring-boot-2.4-to-2.5-example";
        Path from = Path.of("./testcode").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("boot-2.4-2.5-spring-data-jpa");

        Path repository = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/java/com/example/springboot24to25example/TaskRepository.java");
        Path tagRepositoryCaller = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/java/com/example/springboot24to25example/TagService.java");
        Path taskRepositoryCaller = RecipeIntegrationTestSupport.getResultDir(applicationDir).resolve("src/main/java/com/example/springboot24to25example/TaskService.java");

        String expectedRepositoryCode =
                "package com.example.springboot24to25example;\n" +
                "\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.data.jpa.repository.Query;\n" +
                "import org.springframework.data.repository.query.Param;\n" +
                "\n" +
                "public interface TaskRepository extends JpaRepository<Task, Long> {\n" +
                "\n" +
                "    @Query(\"from Task t where t.id=:id\")\n" +
                "    Task getTaskById(@Param(\"id\") Long id);\n" +
                "\n" +
                "}";

        String tagRepositoryCallerCode =
                "package com.example.springboot24to25example;\n" +
                "\n" +
                "import lombok.RequiredArgsConstructor;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "@Service\n" +
                "@Transactional\n" +
                "@RequiredArgsConstructor\n" +
                "public class TagService {\n" +
                "\n" +
                "    private final TagRepository tagRepository;\n" +
                "\n" +
                "    public Tag getTag(Long id) {\n" +
                "        return tagRepository.getById(id);\n" +
                "    }\n" +
                "}\n";

        String taskRepositoryCallerCode =
                "package com.example.springboot24to25example;\n" +
                "\n" +
                "import lombok.RequiredArgsConstructor;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "@Service\n" +
                "@Transactional\n" +
                "@RequiredArgsConstructor\n" +
                "public class TaskService {\n" +
                "\n" +
                "    private final TaskRepository taskRepository;\n" +
                "\n" +
                "    public Task getTask(Long id) {\n" +
                "        return taskRepository.getTaskById(id);\n" +
                "    }\n" +
                "}";

        assertThat(repository).hasContent(expectedRepositoryCode);
        assertThat(tagRepositoryCaller).hasContent(tagRepositoryCallerCode);
        assertThat(taskRepositoryCaller).hasContent(taskRepositoryCallerCode);
    }
}
