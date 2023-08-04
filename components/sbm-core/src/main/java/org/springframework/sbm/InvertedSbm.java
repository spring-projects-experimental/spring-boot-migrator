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

import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.tree.*;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.sbm.parsers.RewriteMavenProjectParser;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class InvertedSbm implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(InvertedSbm.class, args);
    }

    @Autowired
    ProjectContextInitializer projectContextInitializer;
    @Autowired
    ProjectScanner projectScanner;
    @Autowired
    RewriteRecipeDiscovery discovery;

    private ExecutionContext executionContext;

    @Override
    public void run(String... args) throws Exception {
        MavenExecutionContextView.view(executionContext).setResolutionListener(new ResolutionEventListener() {
            @Override
            public void clear() {

            }

            @Override
            public void downloadError(GroupArtifactVersion gav, Pom containing) {
                throw new RuntimeException("Could not download dependency: " + gav);
            }

            @Override
            public void parent(Pom parent, Pom containing) {

            }

            @Override
            public void dependency(Scope scope, ResolvedDependency resolvedDependency, ResolvedPom containing) {

            }

            @Override
            public void bomImport(ResolvedGroupArtifactVersion gav, Pom containing) {

            }

            @Override
            public void property(String key, String value, Pom containing) {

            }

            @Override
            public void dependencyManagement(ManagedDependency dependencyManagement, Pom containing) {

            }
        });

        Path baseDir = Path.of("/Users/fkrueger/projects/sbm-projects/demo-spring-song-app");
        List<Resource> resources = projectScanner.scan(baseDir, Set.of("**/.DS_*","**/.idea/**", "**/.git/**"));
        ProjectContext context = projectContextInitializer.initProjectContext(baseDir, resources);

        String recipeName = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_1";
        List<Recipe> recipes = discovery.discoverRecipes();
        Recipe upgradeBootRecipe = recipes.stream()
                .filter(r -> recipeName.equals(r.getName()))
                .findFirst()
                .get();

        context.apply(upgradeBootRecipe);
        context.getProjectResources()
                .stream()
                .filter(r -> r.hasChanges())
                .forEach(js -> System.out.println("Changed: " + js.getAbsolutePath().toString()));
    }
}
