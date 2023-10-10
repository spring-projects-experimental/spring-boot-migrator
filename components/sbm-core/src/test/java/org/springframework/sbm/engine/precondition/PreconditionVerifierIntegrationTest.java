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
package org.springframework.sbm.engine.precondition;

import org.eclipse.jgit.api.Git;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.project.resource.SbmApplicationProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        SbmApplicationProperties.class,
        PreconditionVerifier.class,
        GitSupport.class,
        PreconditionVerifierIntegrationTest.TestConfig.class
})
public class PreconditionVerifierIntegrationTest {

    /*
     * Initialize all beans extending {@source PreconditionCheck}.
     * Populate and inject list of Checks ordered by their given @Order.
     */
    @Configuration
    @ComponentScan(includeFilters = {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes = PreconditionCheck.class)})
    public static class TestConfig { }

    @Autowired
    private PreconditionVerifier sut;

    @Autowired
    private SbmApplicationProperties sbmApplicationProperties;

    @Autowired
    private GitSupport gitSupport;

    @Test
    void allChecksFailed(@TempDir Path tmpDir) {
        Path projectRoot = tmpDir.toAbsolutePath().normalize();

        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "9");

        sbmApplicationProperties.setGitSupportEnabled(true);

        PreconditionVerificationResult preconditionVerificationResult = sut.verifyPreconditions(projectRoot, resources);

        assertThat(sbmApplicationProperties.isGitSupportEnabled()).isTrue();
        assertThat(preconditionVerificationResult.getResults()).hasSize(4);
        assertThat(preconditionVerificationResult.hasError()).isTrue();
        assertThat(preconditionVerificationResult.getResults().get(0).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(0).getMessage()).isEqualTo("SBM requires a Maven build file. Please provide a minimal pom.xml.");
        assertThat(preconditionVerificationResult.getResults().get(1).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(1).getMessage()).isEqualTo("'sbm.gitSupportEnabled' is 'true' but no '.git' dir exists in project dir. Either disable git support or initialize git.");
        assertThat(preconditionVerificationResult.getResults().get(2).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(2).getMessage()).isEqualTo("PreconditionCheck check could not find a 'src/main/java' dir. This dir is required.");
        assertThat(preconditionVerificationResult.getResults().get(3).getState()).isEqualTo(PreconditionCheck.ResultState.WARN);
        assertThat(preconditionVerificationResult.getResults().get(3).getMessage()).isEqualTo("Java 11 or 17 is required. Check found Java 9.");
    }

    @Test
    void allChecksSucceed(@TempDir Path tempDir) throws IOException {
        Path projectRoot = tempDir.resolve("./test-dummy").toAbsolutePath().normalize();

        // Add MyClass.java
        Path resolve = projectRoot.resolve("src/main/java/MyClass.java");
        Files.createDirectories(resolve.getParent());
        Path path = Files.writeString(resolve, "", StandardOpenOption.CREATE_NEW);
        Resource javaResource = createResource(path);

        // pom.xml exists
        Resource buildFileResource = mock(Resource.class);
        File buildFile = mock(File.class);
        when(buildFile.toPath()).thenReturn(projectRoot.resolve("pom.xml"));
        when(buildFileResource.getFile()).thenReturn(buildFile);

        // Java version is 11
        System.setProperty("java.specification.version", "11");

        // git enabled
        sbmApplicationProperties.setGitSupportEnabled(true);

        // .git exists
        Path gitDir = projectRoot;
        File repo = gitDir.toFile();
        Git git = gitSupport.initGit(repo);
        gitSupport.add(repo, ".");
        gitSupport.commit(repo, "initial commit");
        Resource gitResource = createResource(gitDir.toAbsolutePath().normalize());

        List<Resource> resources = List.of(javaResource, buildFileResource, gitResource);


        PreconditionVerificationResult preconditionVerificationResult = sut.verifyPreconditions(projectRoot, resources);

        assertThat(sbmApplicationProperties.isGitSupportEnabled()).isTrue();
        assertThat(preconditionVerificationResult.getResults()).hasSize(4);
        assertThat(preconditionVerificationResult.getResults().get(0).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(preconditionVerificationResult.getResults().get(0).getMessage()).isEqualTo("Found pom.xml.");
        assertThat(preconditionVerificationResult.getResults().get(1).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);

        assertThat(preconditionVerificationResult.getResults().get(1).getMessage()).matches("'sbm\\.gitSupportEnabled' is 'true', changes will be committed to branch \\[(master|main)] after each recipe\\.");
        assertThat(preconditionVerificationResult.getResults().get(2).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(preconditionVerificationResult.getResults().get(2).getMessage()).isEqualTo("Found required source dir 'src/main/java'.");
        assertThat(preconditionVerificationResult.getResults().get(3).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(preconditionVerificationResult.getResults().get(3).getMessage()).isEqualTo("Required Java version (11) was found.");
    }

    @NotNull
    private Resource createResource(Path path) {
        return new TestDummyResource(path, "");
    }

}
