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
package org.springframework.sbm.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.sbm.service.dto.RecipeInfo;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestApiTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    @TempDir
    File tempFolder;
    
    @Test
    public void scanNoParameters() throws Exception {
        ResponseEntity<?> recipes = this.restTemplate.postForEntity("http://localhost:" + port + "/scan", null, Object.class);
        assertThat(recipes.getStatusCode().isError()).isTrue();
    }
    
    @BeforeEach
    void setup() throws IOException {
        FileUtils.cleanDirectory(tempFolder);
    }
    
    @Test
    public void scanProject() throws Exception {
        File testProject = ProjectUtils.initTestProject(tempFolder, "bootify-jaxrs");
        Map<String, String> params = Map.of("projectPath", testProject.toString());
        ResponseEntity<RecipeInfo[]> recipes = this.restTemplate.postForEntity("http://localhost:" + port + "/scan?projectPath={projectPath}", null, RecipeInfo[].class, params);
        assertThat(recipes.getStatusCode().isError()).isFalse();
        assertThat(recipes.getBody().length).isGreaterThan(0);
    }
    
    @Test
    public void applyRecipe() throws Exception {
        File testProject = ProjectUtils.initTestProject(tempFolder, "bootify-jaxrs");
        Path bootAppMainClass = testProject.toPath().resolve("src/main/java/com/example/jee/app/SpringBootApp.java");
        assertThat(Files.exists(bootAppMainClass)).isFalse();
        Map<String, String> params = Map.of("projectPath", testProject.toString(), "recipe", "initialize-spring-boot-migration");
        ResponseEntity<Void> recipes = this.restTemplate.postForEntity("http://localhost:" + port + "/apply?projectPath={projectPath}&recipe={recipe}", null, Void.class, params);
        assertThat(recipes.getStatusCode().isError()).isFalse();
        assertThat(Files.exists(bootAppMainClass)).isTrue();
    }

}
