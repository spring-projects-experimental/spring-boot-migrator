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

package org.springframework.sbm.example;

import org.apache.commons.io.FileUtils;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.ProjectResourceSetFactory;
import org.springframework.sbm.project.resource.ProjectResourceSetSerializer;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;
import org.springframework.sbm.test.util.GitTestHelper;

import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.joining;

@SpringBootApplication
public class BootUpgradeExample implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootUpgradeExample.class, args);
    }

    @Autowired
    ProjectScanner scanner;
    @Autowired
    RewriteProjectParser parser;
    @Autowired
    RewriteRecipeDiscovery discovery;
    @Autowired
    ProjectResourceSetSerializer serializer;
    @Autowired
    ProjectResourceSetFactory factory;

    @Override
    public void run(String... args) throws Exception {

        // Clone Spring PetClinic using Boot 2.7
        String tmpDir = System.getProperty("java.io.tmpdir");
        String target = tmpDir + "/cloned";
        FileUtils.forceDelete(Path.of(target).toFile());
        Path baseDir = GitTestHelper.cloneProjectCommit("https://github.com/spring-projects/spring-petclinic.git", target, "9ecdc1111e3da388a750ace41a125287d9620534");

        // parse
        RewriteProjectParsingResult parsingResult = parser.parse(baseDir);
        List<SourceFile> sourceFiles = parsingResult.sourceFiles();

        // create ProjectResourceSet
        ProjectResourceSet projectResourceSet = factory.create(baseDir, sourceFiles);

        // find recipe
        String recipeName = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_1";
        List<Recipe> recipes = discovery.discoverRecipes();
        System.out.println("Discovered %d recipes.".formatted(recipes.size()));
        Recipe recipe = recipes.stream().filter(r -> r.getName().equals(recipeName)).findFirst().get();

        // apply recipe
        System.out.println("Apply recipe '%s'".formatted(recipe.getName()));
        projectResourceSet.apply(recipe);

        // write changes to fs
        serializer.writeChanges(projectResourceSet);
    }

}