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
package com.example.bootupgrade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.shaded.jgit.api.CreateBranchCommand;
import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.api.ListBranchCommand;
import org.openrewrite.shaded.jgit.api.ResetCommand;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.openrewrite.shaded.jgit.lib.Ref;
import org.openrewrite.shaded.jgit.revwalk.RevCommit;
import org.openrewrite.shaded.jgit.transport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.parsers.events.StartedParsingResourceEvent;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.ProjectResourceSetFactory;
import org.springframework.sbm.project.resource.ProjectResourceSetSerializer;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class IterativeBoot3UpgradeExample implements ApplicationRunner {
    private static final String MAIN_BRANCH = "main";
    public static final String GH_ORG_NAME = "fabapp2";
    public static final String GH_REPO_NAME = "spring-petclinic-detached";
    public static final String GIT_COMMIT_HASH = "baafc5a80c988cffa0c11e88859143308e850f08";
    private String repoUrl;
    private String ghToken;

    public static void main(String[] args) {
        SpringApplication.run(IterativeBoot3UpgradeExample.class, args);
    }

    private static Map<String, List<String>> versionToRecipeMap = new HashMap<>();
    static {
        Map<List<String>, List<String>> map = Map.of(
                List.of("2.3.0", "2.3.1", "2.3.2", "2.3.4", "2.3.5.RELEASE"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_2_4"
                ),

                List.of("2.4.0", "2.4.13"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_2_5"
                ),

                List.of("2.5.0", "2.5.15"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_2_6"
                ),

                List.of("2.6.0", "2.6.15"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_2_7"
                ),

                List.of("2.7.0", "2.7.17"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_3_0"
                ),

                List.of("3.0.0", "3.0.12"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_3_1"
                ),

                List.of("3.1.0", "3.1.5"),
                List.of(
                        "org.openrewrite.java.spring.boot2.UpgradeSpringBoot_3_2"
                )
        );
        map.keySet()
                .stream()
                .forEach(keys -> {
                    keys.stream()
                        .forEach(k -> {
                            versionToRecipeMap.put(k, map.get(keys));
                        });
                });

    }

    @Autowired
    private RewriteProjectParser parser;
    @Autowired
    private ProjectScanner scanner;
    @Autowired
    private RewriteRecipeDiscovery discovery;
    @Autowired
    private ProjectResourceSetSerializer serializer;
    @Autowired
    private ProjectResourceSetFactory projectResourceSetFactory;

    @EventListener(StartedParsingResourceEvent.class)
    public void onStartedParsingResourceEvent(StartedParsingResourceEvent event) {
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repoUrl = getRepoUrl(args);
        ghToken = getGitHubToken(args);
    }

    @Scheduled(fixedDelay = 10_000)
    void loop() throws IOException, GitAPIException {
        log.info("Scheduling...");
        if(repoUrl == null || ghToken == null) {
            return;
        }
        // clone
        Path baseDir = cloneFreshRepo(repoUrl);

        String pomXml = Files.readString(baseDir.resolve("pom.xml"));
        String currentBootVersion = extractSpringBootVersion(pomXml);

        if(versionToRecipeMap.containsKey(currentBootVersion)) {
            String newBootVersion = calculateNextBootVersion(currentBootVersion);
            log.info("Found migration path to Spring Boot %s".formatted(newBootVersion));
            String branchName = calculateBranchName(newBootVersion);
            Git git = Git.open(baseDir.resolve(".git").toFile());
            if(branchExists(git, branchName)) {
                log.info("There might be an open PR? Otherwise please delete this remote branch: " + branchName);
                return;
            }
            // find recipe name
            List<String> recipeNames = versionToRecipeMap.get(currentBootVersion);
            // parse
            ProjectResourceSet projectResourceSet = parseProject(baseDir);
            // discover
            List<Recipe> recipes = discoverRecipes(recipeNames);
            // apply
            applyRecipe(projectResourceSet, recipes);
            // create branch
            Ref branchRef = createBranch(git, branchName);
            checkout(git, branchName);
            // commit
            String message = "Upgrade Spring Boot from %s to %s".formatted(currentBootVersion, newBootVersion);
            RevCommit commit = commit(git, message);
            System.out.println("commit: " + commit);
            push(git, branchRef, ghToken);

            // Create PR
            String mainBranch = "main";
            GHPullRequest pr = createPullRequest(ghToken, message, branchName, mainBranch, message);
        } else {
            log.info("No migration found for Spring Boot version: %s".formatted(currentBootVersion));
        }
    }

    private boolean branchExists(Git git, String branchName) {
        try {
            git.fetch().call();
            // FIXME: This works only in this example. origin could be different
            return git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call()
                    .stream()
                    .anyMatch(b -> "refs/remotes/origin/%s".formatted(branchName).equals(b.getName()));
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private String calculateBranchName(String newBootVersion) {
        return "app-analyzer/boot-upgrade-%s".formatted(newBootVersion.replace(".", ""));
    }

    private String calculateNextBootVersion(String currentBootVersion) {
        String[] split = currentBootVersion.split("\\.");
        return split[0] + "." + (Integer.parseInt(split[1]) + 1) + ".x";
    }

    private String extractSpringBootVersion(String pomXml) {
        try {
            ObjectMapper mm = new XmlMapper();
            String version = mm.readTree(pomXml).get("parent").get("version").textValue();
            return version;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkout(Git git, String branchName) throws GitAPIException {
        git.checkout().setName(branchName).call();
    }

    private String getGitHubToken(ApplicationArguments args) {
        if(args.getNonOptionArgs().size() < 2) {
            throw new IllegalArgumentException("The GitHub token must be provided as second parameter.");
        }
        return args.getNonOptionArgs().get(1);
    }

    private void push(Git git, Ref branch, String ghToken) throws GitAPIException {
        git.push().add(branch).setCredentialsProvider(new UsernamePasswordCredentialsProvider(ghToken, "")).call();
    }

    private static RevCommit commit(Git git, String message) throws GitAPIException {
        git.add().addFilepattern(".").call();
        return git.commit().setMessage(message).call();
    }

    private static Ref createBranch(Git git, String branchName) throws GitAPIException {
        Ref branchRef = git.branchCreate()
                        .setName(branchName)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                        .setForce(true)
                        .call();
        return branchRef;
    }

    private ProjectResourceSet parseProject(Path baseDir) {
        // scan
        List<Resource> resources = scanner.scan(baseDir);
        // parse
        RewriteProjectParsingResult parsingResult = parser.parse(baseDir, resources);
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        ProjectResourceSet projectResourceSet = projectResourceSetFactory.create(baseDir, sourceFiles);
        return projectResourceSet;
    }

    @NotNull
    private Path cloneFreshRepo(String repoUrl) throws IOException {
        Path targetDir = Path.of(System.getProperty("java.io.tmpdir")).resolve(GH_REPO_NAME); //Files.createTempDirectory(GH_REPO_NAME).toAbsolutePath();
        if(targetDir.toFile().exists()) {
            FileSystemUtils.deleteRecursively(targetDir);
        }
        String commitHash = null;// GIT_COMMIT_HASH; // https://github.com/spring-projects/spring-petclinic/commit/a3294f2
        cloneFreshRepo(repoUrl, targetDir, commitHash);
        System.out.println("Cloned to %s".formatted(targetDir));
        return targetDir;
    }

    private static String getRepoUrl(ApplicationArguments args) {
        if(args.getNonOptionArgs().isEmpty()) {
            throw new IllegalArgumentException("A repository URL must be provided.");
        }
        String repoUrl = args.getNonOptionArgs().get(0);
        return repoUrl;
    }

    private void cloneFreshRepo(String repoUrl, Path targetDir, String gitHash) {
        try {
            File directory = targetDir.toFile();
            Git git = Git.cloneRepository()
                    .setDirectory(directory)
                    .setURI(repoUrl)
                    .call();

            if (gitHash != null) {
                git.reset().setMode(ResetCommand.ResetType.HARD).setRef(GIT_COMMIT_HASH);
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private GHPullRequest createPullRequest(String ghToken, String name, String head, String base, String body) {
        try {
            GitHub gitHub = new GitHubBuilder().withOAuthToken(ghToken).build();

            GHRepository repo = gitHub
                    /*.getOrganization(GH_ORG_NAME)*/
                    .getRepository(GH_ORG_NAME + "/" + GH_REPO_NAME);

            GHPullRequest pr = repo.createPullRequest(name, head, base, body);
            log.info("Created PR '%s'".formatted(name));
            return pr;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void applyRecipe(ProjectResourceSet projectResourceSet, List<Recipe> recipes) {
        // FIXME: If recipes is a list they need to be provided through Recipe.getRecipeList() to not require a new parse
        log.info("Applying recipes %s".formatted(recipes.stream().map(Recipe::getDisplayName).toList()));
        if(recipes.size() > 1) throw new UnsupportedOperationException("Can only handle single recipes.");
        recipes.forEach(r -> this.applyRecipe(projectResourceSet, r));
    }

    private void applyRecipe(ProjectResourceSet projectResourceSet, Recipe recipe) {
        projectResourceSet.apply(recipe);
        serializer.writeChanges(projectResourceSet);
        System.out.println("Applied recipe " + recipe.getDisplayName());
    }

    private List<Recipe> discoverRecipes(List<String> recipeNames) {
        List<Recipe> recipes = discovery.discoverRecipes();
        List<Recipe> matchingRecipes = recipes.stream().filter(r -> recipeNames.contains(r.getName())).toList();
        return matchingRecipes;
    }
}
