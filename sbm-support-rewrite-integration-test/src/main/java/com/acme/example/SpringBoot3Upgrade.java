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

import org.openrewrite.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.rewrite.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.parsers.events.StartedParsingResourceEvent;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.*;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class SpringBoot3Upgrade implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3Upgrade.class, args);
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
        System.out.println();
    }

    @Override
    public void run(String... args) throws Exception {
        Path baseDir = null;
        if(args.length == 0) {
            throw new IllegalArgumentException("A path must be provided");
        } else {
            String pathStr = args[0];
            baseDir = Path.of(pathStr);
        }

        // scan
        List<Resource> resources = scanner.scan(baseDir);

        // parse
        RewriteProjectParsingResult parsingResult = parser.parse(baseDir);
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();
        ProjectResourceSet projectResourceSet = projectResourceSetFactory.create(baseDir, sourceFiles);

        // discover
        String recipeName = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_1";
        List<Recipe> recipes = discovery.discoverRecipes();
        Optional<Recipe> recipe = recipes.stream().filter(r -> recipeName.equals(r.getName())).findFirst();

        // apply
        recipe.ifPresent((Recipe r) -> {
            projectResourceSet.apply(r);
            serializer.writeChanges(projectResourceSet);
        });
    }
}
