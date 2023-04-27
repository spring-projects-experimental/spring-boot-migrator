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
package org.springframework.sbm;

import com.ibm.icu.impl.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.apache.maven.shared.utils.cli.CommandLineException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.shell.ApplyShellCommand;
import org.springframework.sbm.shell.ScanShellCommand;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.SocketUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base class to be extended by integrationTests.
 * <p>
 * Provides helper methods to initialize example projects to migrate in integration tests.
 * MAVEN_HOME must be set on the system running the tests!
 * <p>
 * Projects can either be copied from <code>TESTCODE_DIR</code> or written from inline
 * code.
 * <p>
 * See also: {@link #intializeTestProject()}, {@link #writeFile} and
 * {@link #writeJavaFile(String)}
 */
@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false",
        "sbm.gitSupportEnabled=false"
})
@DirtiesContext // paralel runs
public abstract class IntegrationTestBaseClass {

    /**
     * Points to the source root directory where example projects are expected.
     *
     * See {@link #getTestSubDir()}.
     */
    public static final String TESTCODE_DIR = "src/test/resources/testcode/";

    /**
     * Points to the target root directory where example projects will be copied to.
     *
     * See {@link #getTestSubDir()}.
     */
    public static final String INTEGRATION_TEST_DIR = "./target/sbm-integration-test/";
    @Autowired
    protected ApplicableRecipeListCommand applicableRecipeListCommand;
    @Autowired
    ScanShellCommand scanShellCommand;
    @Autowired
    ApplyShellCommand applyShellCommand;
    @Autowired
    ProjectContextHolder projectContextHolder;
    @Autowired
    private SbmApplicationProperties sbmApplicationProperties;
    private Path testDir;

    private String output;

    @BeforeAll
    public static void beforeAll() {
        String mvnHome = System.getenv("MAVEN_HOME");

        if (mvnHome == null) {
            mvnHome = System.getenv("M2_HOME");
        }

        if (mvnHome == null) {
            System.err.println("You must set $MAVEN_HOME on your system for the integration test to run.");
            throw new RuntimeException();
        }

        System.setProperty("maven.home", mvnHome);
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        testDir = Path.of(INTEGRATION_TEST_DIR).resolve(getTestSubDir());
        clearTestDir();
        testDir.resolve(this.getClass().getName());
        FileUtils.forceMkdir(testDir.toFile());
        testDir = testDir.toRealPath();
    }

    protected void enableGitSupport() {
        sbmApplicationProperties.setGitSupportEnabled(true);
        System.out.println("Programmatically enabled git support!");
    }

    /**
     * Copies example project used for integrationTest.
     */
    protected void intializeTestProject() {
        try {
            Path s = Path.of(TESTCODE_DIR).resolve(getTestSubDir());
            FileUtils.copyDirectory(s.toFile(), getTestDir().toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected int springBootStart() {
        int port = SocketUtils.findAvailableTcpPort();
        executeMavenGoals(getTestDir(), "package",
                "spring-boot:start  -Dspring-boot.run.jmxPort=9999 -Drun.arguments=\"--server.port=" + port + "\"");
        return port;
    }

    protected void springBootStop() {
        executeMavenGoals(getTestDir(), "-Dspring-boot.run.jmxPort=9999 -Dspring-boot.stop.fork=true spring-boot:stop");
    }

    /**
     * defines the subdirectory of {@link #INTEGRATION_TEST_DIR} for this test.
     * <p>
     * All resources for this test will either be copied to or created in this directory.
     */
    protected abstract String getTestSubDir();

    protected Path getTestDir() {
        return testDir;
    }

    /**
     * Assert that exactly the given recipes are applicable and shown.
     *
     * @param applicableRecipes
     */
    protected void assertApplicableRecipesContain(String... applicableRecipes) {
        List<String> recipeNames = getRecipeNames();
        assertThat(recipeNames).contains(applicableRecipes);
    }

    @NotNull
    private List<String> getRecipeNames() {
        return applicableRecipeListCommand.execute(projectContextHolder.getProjectContext()).stream()
                .map(r -> r.getName()).collect(Collectors.toList());
    }

    protected void assertRecipeApplicable(String recipeName) {
        List<String> recipeNames = getRecipeNames();
        assertThat(recipeNames).contains(recipeName);
    }

    protected void assertRecipeNotApplicable(String recipeName) {
        List<String> recipeNames = getRecipeNames();
        assertThat(recipeNames).doesNotContain(recipeName);
    }

    /**
     * Applies the <code>Recipe</code>s matching the given names.
     */
    protected void applyRecipe(String... recipeNames) {
        for (String recipeName : recipeNames) {
            applyShellCommand.apply(recipeName);
        }
    }

    protected void scanProject() {
        try {
            output = scanShellCommand.scan(getTestDir().toRealPath().toString());
            System.out.println(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a list of Maven goals in directory.
     *
     * @param executionDir to run the 'mvn' command from
     * @param goals        to run in given order
     */
    protected void executeMavenGoals(Path executionDir, String... goals) {
        Invoker invoker = new DefaultInvoker();
        InvocationRequest request = new DefaultInvocationRequest();
        request.setErrorHandler(new SystemOutHandler());
        request.setInputStream(InputStream.nullInputStream());
        File pomXml = executionDir.resolve("pom.xml").toFile();
        request.setPomFile(pomXml);
        Arrays.stream(goals).forEach(g -> {
            try {
                request.setGoals(List.of(g));
                InvocationResult invocationResult = invoker.execute(request);
                CommandLineException executionException = invocationResult.getExecutionException();
                int exitCode = invocationResult.getExitCode();
                if (executionException != null) {
                    fail("Maven build 'mvn " + g
                            + " " + pomXml + " "
                            + "' failed with Exception: " + executionException.getMessage());
                }
                if (exitCode != 0) {
                    fail("Maven build 'mvn " + g
                            + "' failed with exitCode: " + exitCode);
                }
            } catch (MavenInvocationException e) {
                fail("Maven build 'mvn " + g
                        + " " + pomXml + " "
                        + "' failed with Exception: " + e.getMessage());
                e.printStackTrace();
                fail("Maven build 'mvn " + g + "' failed");
            }
        });
    }

    protected Path writeFile(String content, String fileName) {
        try {
            return Files.writeString(getTestDir().resolve(fileName), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void afterEach() throws IOException {
        clearTestDir();
    }

    private void clearTestDir() throws IOException {
        if (getTestDir().toFile().exists()) {
            FileUtils.forceDelete(getTestDir().toFile());
        }
    }

    protected void writeJavaFile(String code) {
        try {
            Pattern packagePattern = Pattern.compile("package ([\\w\\d\\.]*);");
            Pattern classPattern = Pattern.compile("(class|interface|enum)\\s([\\w]+).*");
            Matcher packageMatcher = packagePattern.matcher(code);
            Matcher classMatcher = classPattern.matcher(code);

            if (!packageMatcher.find())
                throw new RuntimeException("Could not extract package from code.");
            String packageName = packageMatcher.group(1);

            if (!classMatcher.find())
                throw new RuntimeException("Could not extract classname from code.");
            String className = classMatcher.group(2);
            Path classPath = testDir.resolve("src/main/java").resolve(packageName.replace(".", "/"));

            FileUtils.forceMkdir(classPath.toFile());

            Path classFile = classPath.resolve(className + ".java");
            classFile.toFile().createNewFile();
            Files.writeString(classFile, code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void replaceInFile(Path file, String search, String replace) {
        try {
            String content = new ResourceHelper(new DefaultResourceLoader())
                    .getResourceAsString(new FileSystemResource(file.toString()));
            String replaced = content.replace(search, replace);
            Files.writeString(file, replaced);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void replaceFile(Path target, Path source) {
        try {
            Files.move(source, target, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String loadJavaFileFromSubmodule(String submodulePath, String packageName, String className) {
        return loadJavaFileFromModule(submodulePath, packageName, className, "src/main/java");
    }

    protected String loadTestJavaFileFromSubmodule(String submodulePath, String packageName, String className) {
        return loadJavaFileFromModule(submodulePath, packageName, className, "src/test/java");
    }

    private String loadJavaFileFromModule(String submodulePath, String packageName, String className, String sourceDir) {
        try {
            Path classPath = testDir.resolve(submodulePath + sourceDir).resolve(packageName.replace(".", "/"));
            Path classFile = classPath.resolve(className + ".java");
            return Files.readString(classFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected String loadJavaFile(String packageName, String className) {
        return loadJavaFileFromSubmodule("", packageName, className);
    }

    protected String loadTestJavaFile(String packageName, String className) {
        return loadTestJavaFileFromSubmodule("", packageName, className);
    }

    /**
     * Starts {@code image} as Docker container exposing {@code ports} and waiting for {@code httpEndpoint} to be available.
     */
    protected Integer startDockerContainer(String image, String httpEndpoint, Integer... ports) {
        GenericContainer genericContainer = new GenericContainer(DockerImageName.parse(image))
                .withExposedPorts(ports)
                .waitingFor(Wait.forHttp(httpEndpoint));
        genericContainer.start();
        return genericContainer.getFirstMappedPort();
    }

    protected static RunningNetworkedContainer startDockerContainer(NetworkedContainer networkedContainer,
                                                                    Network attachNetwork, Map<String, String> envMap) {

        Network network = attachNetwork == null ? Network.newNetwork() : attachNetwork;

        GenericContainer genericContainer = new GenericContainer(networkedContainer.image)
                .withExposedPorts(networkedContainer.exposedPorts.toArray(new Integer[0]))
                .withNetwork(network)
                .withNetworkMode("host")
                .withNetworkAliases(networkedContainer.networkAlias)
                .withStartupTimeout(Duration.of(180, ChronoUnit.SECONDS))
                .withEnv(envMap);
        genericContainer.start();

        return new RunningNetworkedContainer(network, genericContainer);
    }

    protected String loadFile(Path of) {
        try {
            return Files.readString(getTestDir().resolve(of));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NetworkedContainer {

        private String image;

        private List<Integer> exposedPorts;

        private String networkAlias;

    }

    @Getter
    @AllArgsConstructor
    public static class RunningNetworkedContainer {

        private Network network;

        private GenericContainer container;

    }

}
