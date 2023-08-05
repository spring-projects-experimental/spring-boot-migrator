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
package org.springframework.sbm.support.openrewrite.yaml;

import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;
import org.openrewrite.config.YamlResourceLoader;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Properties;

public class YamlParserTest {

    @Test
    void yamlParserTest() {

        String yaml =
                "type: specs.openrewrite.org/v1beta/recipe\n" +
                        "name: org.openrewrite.java.spring.boot2.SpringBoot2JUnit4to5Migration\n" +
                        "displayName: JUnit Jupiter migration from JUnit 4.x for Spring Boot 2.x projects\n" +
                        "description: Migrates Spring Boot 2.x projects having JUnit 4.x tests to JUnit Jupiter\n" +
                        "tags:\n" +
                        "  - testing\n" +
                        "recipeList:\n" +
                        "  - org.openrewrite.java.testing.junit5.JUnit4to5Migration";

        YamlResourceLoader yamlResourceLoader = new YamlResourceLoader(new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)), URI.create("recipe.yaml"), new Properties());
        Collection<Recipe> recipes = yamlResourceLoader.listRecipes();
//        YamlParser yamlParser = new YamlParser();
//        List<Yaml.Documents> documents = yamlParser.parse(yaml);
//        RecipeSerializer recipeSerializer = new RecipeSerializer();
//        Recipe recipe = recipeSerializer.read(documents.get(0).getDocuments().get(0).);
    }
}
