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

import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.tree.Xml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.*;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class RewriteParserApplication {
    public static void main(String[] args) {
        Path tmpBasePath = null;
        if(args == null || args.length == 0 || args[0].isEmpty()) {
            throw new IllegalArgumentException("No basePath given.".formatted(tmpBasePath));
        }

        final Path basePath = Path.of(args[0]);
        if(!basePath.toFile().exists()) {
            throw new IllegalArgumentException("Given basePath '%s' does not exist.".formatted(basePath));
        }
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RewriteParserApplication.class, args);
        RewriteMavenProjectParser projectParser = applicationContext.getBean(RewriteMavenProjectParser.class);

        RewriteRecipeDiscovery recipeDiscovery = new RewriteRecipeDiscovery(new ParserSettings(), new MavenProjectFactory());
        RewriteProjectParsingResult parsingResult = projectParser.parse(basePath);
        SourceFile rootPom = parsingResult.sourceFiles().stream().filter(s -> s.getSourcePath().toAbsolutePath().normalize().toString().equals(basePath.resolve("pm.xml").toString())).findFirst().get();
        List<Recipe> recipes = recipeDiscovery.discoverFilteredRecipes(parsingResult.sourceFiles()
                        .stream()
                        .filter(s -> s.getSourcePath().toString().equals("pom.xml"))
                        .filter(Xml.Document.class::isInstance)
                        .map(Xml.Document.class::cast)
                        .findFirst()
                        .get(),
                List.of("")
        );

        recipes.stream()
                        .forEach(r -> r.getRecipeList());

        parsingResult.sourceFiles()
                .stream()
                .forEach(s -> System.out.println("Successfully Parsed " + s.getSourcePath()));
    }
}
