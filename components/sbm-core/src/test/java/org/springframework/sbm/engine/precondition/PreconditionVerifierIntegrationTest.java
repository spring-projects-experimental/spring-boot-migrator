package org.springframework.sbm.engine.precondition;

import org.eclipse.jgit.api.Git;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.sbm.project.resource.ApplicationProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        ApplicationProperties.class,
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
    private ApplicationProperties applicationProperties;

    @Autowired
    private GitSupport gitSupport;

    @Test
    void allChecksFailed() {
        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();

        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "9");

        applicationProperties.setGitSupportEnabled(true);

        PreconditionVerificationResult preconditionVerificationResult = sut.verifyPreconditions(projectRoot, resources);

        assertThat(applicationProperties.isGitSupportEnabled()).isTrue();
        assertThat(preconditionVerificationResult.getResults()).hasSize(4);
        assertThat(preconditionVerificationResult.hasError()).isTrue();
        assertThat(preconditionVerificationResult.getResults().get(0).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(0).getMessage()).isEqualTo("SBM requires a Maven build file. Please provide a minimal pom.xml.");
        assertThat(preconditionVerificationResult.getResults().get(1).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(1).getMessage()).isEqualTo("'sbm.gitSupportEnabled' is 'true' but no '.git' dir exists in project dir. Either disable git support or initialize git.");
        assertThat(preconditionVerificationResult.getResults().get(2).getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(preconditionVerificationResult.getResults().get(2).getMessage()).isEqualTo("PreconditionCheck check could not find a 'src/main/java' dir. This dir is required.");
        assertThat(preconditionVerificationResult.getResults().get(3).getState()).isEqualTo(PreconditionCheck.ResultState.WARN);
        assertThat(preconditionVerificationResult.getResults().get(3).getMessage()).isEqualTo("Java 11 is required. Check found Java 9.");
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
        applicationProperties.setGitSupportEnabled(true);

        // .git exists
//        Path git = Files.createDirectories(projectRoot.resolve(".git"));
//        Resource gitResource = createResource(git);
        Path gitDir = projectRoot.resolve(".git");
        File repo = gitDir.toFile();
        Git git = gitSupport.initGit(repo);
        gitSupport.add(repo, "*");
        gitSupport.commit(repo, "initial commit");
        Resource gitResource = createResource(gitDir);

        List<Resource> resources = List.of(javaResource, buildFileResource, gitResource);


        PreconditionVerificationResult preconditionVerificationResult = sut.verifyPreconditions(projectRoot, resources);

        assertThat(applicationProperties.isGitSupportEnabled()).isTrue();
        assertThat(preconditionVerificationResult.getResults()).hasSize(4);
        assertThat(preconditionVerificationResult.getResults().get(0).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(preconditionVerificationResult.getResults().get(0).getMessage()).isEqualTo("Found pom.xml.");
        assertThat(preconditionVerificationResult.getResults().get(1).getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(preconditionVerificationResult.getResults().get(1).getMessage()).isEqualTo("'sbm.gitSupportEnabled' is 'true', changes will be committed to branch [master] after each recipe.");
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
