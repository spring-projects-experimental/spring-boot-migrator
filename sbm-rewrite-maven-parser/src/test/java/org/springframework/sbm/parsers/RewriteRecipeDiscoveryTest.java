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
package org.springframework.sbm.parsers;

import com.example.recipes.DummyRecipe;
import io.example.recipes.AnotherDummyRecipe;
import io.github.classgraph.ClassGraph;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;
import org.openrewrite.config.ClasspathScanningLoader;
import org.openrewrite.config.DeclarativeRecipe;
import org.openrewrite.config.Environment;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;
import org.springframework.sbm.test.util.OpenRewriteDummyRecipeInstaller;

import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class RewriteRecipeDiscoveryTest {

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

    @Test
    @DisplayName("Should Discover Dummy Recipes")
    void shouldDiscoverDummyRecipes() {
//        ParserSettings parserSettings = ParserSettings.builder()
//                .runPerSubmodule(false)
//                .exclusions(Set.of("testcode"))
//                .build();
//        MavenProjectFactory mavenProjectFactory = new MavenProjectFactory();
//        DummyResource rootPom = new DummyResource("pom.xml",
//                """
//                <?xml version="1.0" encoding="UTF-8"?>
//                <project xmlns="http://maven.apache.org/POM/4.0.0"
//                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
//                    <modelVersion>4.0.0</modelVersion>
//                    <groupId>org.springframework.sbm</groupId>
//                    <artifactId>sbm-openrewrite-parser</artifactId>
//                    <version>1.0-SNAPSHOT</version>
//                    <properties>
//                        <maven.compiler.source>17</maven.compiler.source>
//                        <maven.compiler.target>17</maven.compiler.target>
//                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
//                    </properties>
//                </project>
//                """);
//
//
        OpenRewriteDummyRecipeInstaller recipeInstaller = new OpenRewriteDummyRecipeInstaller();
        recipeInstaller.installRecipe();


        RewriteRecipeDiscovery recipeDiscovery = new RewriteRecipeDiscovery();
//        OpenRewriteProjectParser openRewriteProjectParser = new OpenRewriteProjectParser(new ProvenanceMarkerFactory(new ParserSettings(), new MavenProjectFactory()), new BuildFileParser(new MavenModelReader()), new SourceFileParser(), new StyleDetector(), new ParserSettings());
//        Path baseDir = Path.of(".");
//        RewriteProjectParsingResult parsingResult = openRewriteProjectParser.parse(baseDir, List.of(rootPom), new InMemoryExecutionContext(t -> t.printStackTrace()));
//        RewriteMavenProjectParser rewriteMavenProjectParser = new RewriteMavenProjectParser(new MavenPlexusContainerFactory());
//        ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
//        RewriteProjectParsingResult parsingResult = rewriteMavenProjectParser.parse(
//                Path.of("/Users/fkrueger/projects/spring-boot-migrator/sbm-rewrite-maven-parser/testcode/openrewrite-dummy-recipe"),
//                true,
//                "pomCache",
//                false,
//                Set.of("**/testcode/**"),
//                Set.of(),
//                -1,
//                false,
//                executionContext);

        String[] acceptPackages = {"org.springframework.sbm"};
        Path jarPath = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository/org/springframework/sbm/openrewrite-dummy-recipe/1.0-SNAPSHOT/openrewrite-dummy-recipe-1.0-SNAPSHOT.jar");
        ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(jarPath, new Properties(), Set.of(new ClasspathScanningLoader(new Properties(), acceptPackages)), getClass().getClassLoader());
        List<Recipe> dummyRecipe = recipeDiscovery.discoverFilteredRecipes(List.of("DummyRecipe"), new Properties(), acceptPackages, classpathScanningLoader);
    }


    @Test
    @DisplayName("ProvidingAcceptedPackagesShouldOnlyShowRecipesWithMatchingPackage")
    void providingAcceptedPackagesShouldOnlyShowRecipesWithMatchingPackage() {
        ClasspathScanningLoader resourceLoader1 = new ClasspathScanningLoader(new Properties(), new String[]{"com.example"});
        Collection<Recipe> recipes = resourceLoader1.listRecipes();
        assertThat(recipes).anyMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
        assertThat(recipes).noneMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());

        ClasspathScanningLoader resourceLoader2 = new ClasspathScanningLoader(new Properties(), new String[]{"io.example"});
        Collection<Recipe> recipes2 = resourceLoader2.listRecipes();
        assertThat(recipes2).noneMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
        assertThat(recipes2).anyMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());

        ClasspathScanningLoader resourceLoader3 = new ClasspathScanningLoader(new Properties(), new String[]{"io.example", "com.example"});
        Collection<Recipe> recipes3 = resourceLoader3.listRecipes();
        assertThat(recipes3).anyMatch(r -> com.example.recipes.DummyRecipe.class == r.getClass());
        assertThat(recipes3).anyMatch(r -> io.example.recipes.AnotherDummyRecipe.class == r.getClass());
    }

    @Test
    @DisplayName("Discover all available recipes by default")
    void discoverAllAvailableRecipesByDefault() {
        RewriteRecipeDiscovery sut = new RewriteRecipeDiscovery();
        List<Recipe> recipes = sut.discoverRecipes();
        assertThat(recipes).anyMatch(r -> r.getClass() == DummyRecipe.class);
        assertThat(recipes).anyMatch(r -> r.getClass() == AnotherDummyRecipe.class);
        assertThat(recipes).anyMatch(r -> "com.example.SomeDummyRecipeInYaml".equals(r.getName()));
    }
    
    @Test
    @DisplayName("Load OpenRewrite Recipes")
    void loadOpenRewriteRecipes() {
        ClasspathScanningLoader resourceLoader = new ClasspathScanningLoader(new Properties(), new String[]{"com.example"});
        Collection<Recipe> recipes1 = resourceLoader.listRecipes();
        assertThat(recipes1)
                .map(Object::getClass)
                .map(Class::getName)
                .contains(DummyRecipe.class.getName());

        List<Recipe> recipes = Environment.builder()
//                .scanJar()
                .load(resourceLoader)
                .build()
                .listRecipes();

        assertThat(recipes).isNotEmpty();

        assertThat(recipes)
//                .map(Object::getClass)
//                .map(Class::getName)
                .map(Recipe::getName)
                .contains(DummyRecipe.class.getName());

        assertThat(recipes)
                .map(Recipe::getName)
                .contains("com.example.SomeDummyRecipeInYaml");
    }

    @Test
    @DisplayName("Load Recipe From Classpath")
    void loadRecipeFromClasspath2() {
        String[] acceptPackages = {}; // "com.example"
        ClasspathScanningLoader loader = new ClasspathScanningLoader(new Properties(), acceptPackages);
        Path jarPath = Path.of("/Users/fkrueger/.m2/repository/org/openrewrite/recipe/rewrite-spring/4.36.0/rewrite-spring-4.36.0.jar");// Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository/org/springframework/sbm/openrewrite-dummy-recipe/1.0-SNAPSHOT/openrewrite-dummy-recipe-1.0-SNAPSHOT.jar");



        String jarName = jarPath.toFile().getName();
        List<String> strings = new ClassGraph()
                .acceptJars(jarName)
                .ignoreParentClassLoaders()
                .overrideClassLoaders(getClass().getClassLoader()).enableClassInfo().scan().getAllClassesAsMap()
                .keySet()
                .stream()
                .filter(k -> k.startsWith("org.springframework"))
                .toList();


        List<Recipe> recipes1 = Environment.builder()
                .scanJar(jarPath, Set.of(), getClass().getClassLoader().getParent())
                .build()
                .listRecipes();


        ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(jarPath, new Properties(), Set.of(loader), getClass().getClassLoader());
        Environment environment = Environment.builder()
                .load(loader, Set.of(classpathScanningLoader))
                .build();
        List<Recipe> recipes = environment.listRecipes();
        assertThat(recipes).hasSizeGreaterThan(1);
        // found the Java recipe under src/test/java/com/example/recipes/DummyRecipe.jav
        Optional<Recipe> recipe = getRecipeByDisplayName(recipes, "DummyRecipe");
        assertThat(recipe).isNotEmpty();
        Optional<Recipe> customJavaFromYaml = getRecipeByName(recipes, "com.example.SomeDummyRecipeInYaml");
        assertThat(customJavaFromYaml).isNotEmpty();
        assertThat(customJavaFromYaml.get()).isInstanceOf(DeclarativeRecipe.class);
//        scanClasses(new ClassGraph()
//                .ignoreParentClassLoaders()
//                .overrideClassLoaders(getClass().getClassLoader()), classpathScanningLoader);
    }

    @Test
    @DisplayName("Load Recipe From Classpath")
//    @Disabled("Still fiddling")
    void loadRecipeFromJar() {
        OpenRewriteDummyRecipeInstaller recipeInstaller = new OpenRewriteDummyRecipeInstaller();
        recipeInstaller.installRecipe();

        String[] acceptPackages = {"com", "org.springframework"};

        Path jarPath = Path.of(System.getProperty("user.home")).resolve(".m2").resolve("repository/org/springframework/sbm/openrewrite-dummy-recipe/1.0-SNAPSHOT/openrewrite-dummy-recipe-1.0-SNAPSHOT.jar");
        assertThat(jarPath.toFile()).exists();
        ClasspathScanningLoader scanningLoader = new ClasspathScanningLoader(new Properties(), acceptPackages);
        ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(jarPath, new Properties(), Set.of(scanningLoader), getClass().getClassLoader());

//        ClasspathScanningLoader classpathScanningLoader = new ClasspathScanningLoader(new Properties(), new String[]{});

        Environment environment = Environment.builder()
                .load(classpathScanningLoader)
                .build();
        List<Recipe> recipes = environment.listRecipes();
        assertThat(recipes).hasSizeGreaterThan(2);

        // found the Java recipe under src/test/java/com/example/recipes/DummyRecipe.java
        Optional<Recipe> recipe = getRecipeByDisplayName(recipes, "Dummy Recipe in Java");
        assertThat(recipe).isNotEmpty();

        // found the declarative recipe under src/test/resources/META-INF/rewrite/dummy-recipe-in-yaml.yaml
        Optional<Recipe> customJavaFromYaml = getRecipeByName(recipes, "com.example.SomeDummyRecipeInYaml");
        assertThat(customJavaFromYaml).isNotEmpty();
        assertThat(customJavaFromYaml.get()).isInstanceOf(DeclarativeRecipe.class);
    }

    private Optional<Recipe> getRecipeByName(List<Recipe> recipes, String s) {
        return recipes.stream()
                .filter(r -> r.getName().equals(s))
                .findFirst();
    }

//    @Test
//    @DisplayName("Load YAML Recipes from META-INF/recipes")
//    void loadYamlRecipesFromMetaInfRecipes() {
//        YamlResourceLoader resourceLoader = new YamlResourceLoader();
//    }

    @NotNull
    private static Optional<Recipe> getRecipeByDisplayName(List<Recipe> recipes, String recipeDisplayName) {
        return recipes.stream().filter(r -> {
            return r.getDisplayName().equals(recipeDisplayName);
        }).findFirst();
    }

}