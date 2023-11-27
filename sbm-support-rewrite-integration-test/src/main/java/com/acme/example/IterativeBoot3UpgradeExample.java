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
package com.acme.example;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.shaded.jgit.api.CreateBranchCommand;
import org.openrewrite.shaded.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class IterativeBoot3UpgradeExample implements CommandLineRunner {
    private static final String MAIN_BRANCH = "main";

    public static void main(String[] args) {
        SpringApplication.run(IterativeBoot3UpgradeExample.class, args);
    }

    @Autowired
    RewriteProjectParser parser;
    @Autowired
    ProjectScanner scanner;
    @Autowired
    RewriteRecipeDiscovery discovery;
    @Autowired
    ExecutionContext executionContext;
    @Autowired
    ProjectResourceSetSerializer serializer;
    @Autowired
    ProjectResourceSetFactory projectResourceSetFactory;

    @EventListener(StartedParsingResourceEvent.class)
    public void onStartedParsingResourceEvent(StartedParsingResourceEvent event) {
    }

    @Override
    public void run(String... args) throws Exception {
        Path baseDir = null;
        String token = null;
        if(args.length != 2) {
            throw new IllegalArgumentException("A path must be provided");
        } else {
            String pathStr = args[0];
            baseDir = Path.of(pathStr);
            token = args[1];
        }
        // clone
        // TODO
        // scan
        List<Resource> resources = scanner.scan(baseDir);
        // parse
        RewriteProjectParsingResult parsingResult = parser.parse(baseDir, resources);
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        ProjectResourceSet projectResourceSet = projectResourceSetFactory.create(baseDir, sourceFiles);
        // discover
        String recipeName = " org.openrewrite.java.spring.boot2.UpgradeSpringBoot_2_7";
        Optional<Recipe> recipe = discoverRecipe(recipeName);
        // apply
        applyIfPresent(projectResourceSet, recipe);
        // create branch
        Git git = Git.open(new File(".git"));
        String branchName = "app-analyzer/boot-upgrade-27";
        git.branchCreate().setName(branchName).call();
        // commit
        git.add().call();
        String message = "Upgrade Spring Boot from 2.6 to 2.7";
        git.commit().setMessage(message).call();
        // Create PR
        GHRepository repo = createPullRequest(token, message, branchName, MAIN_BRANCH, message);
    }

    private GHRepository createPullRequest(String token, String name, String head, String base, String body) {
        try {
            GitHub gitHub = new GitHubBuilder().withOAuthToken(token).build();
            GHRepository repo = getRepository(gitHub);
            GHPullRequest p = repo.createPullRequest(name, head, base, body);
            return gitHub.getOrganization("hub4j-test-org").getRepository("github-api");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected GHRepository getRepository(GitHub gitHub) throws IOException {
        return gitHub.getOrganization("hub4j-test-org").getRepository("github-api");
    }

    private GHRepository getRepository(String name, GitHub gitHub, String head, String base, String body) throws IOException {
        GHRepository repo = getRepository(gitHub);
        GHPullRequest p = repo.createPullRequest(name, head, base, body);
        return gitHub.getOrganization("hub4j-test-org").getRepository("github-api");
    }

    private void applyIfPresent(ProjectResourceSet projectResourceSet, Optional<Recipe> recipe) {
        recipe.ifPresent((Recipe r) -> {
            projectResourceSet.apply(r);
            serializer.writeChanges(projectResourceSet);
        });
    }

    @NotNull
    private Optional<Recipe> discoverRecipe(String recipeName) {
        List<Recipe> recipes = discovery.discoverRecipes();
        Optional<Recipe> recipe = recipes.stream().filter(r -> recipeName.equals(r.getName())).findFirst();
        return recipe;
    }
}
