package org.springframework.sbm.parsers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.recipes.RewriteRecipeDiscovery;
import org.springframework.sbm.test.util.DummyResource;
import org.springframework.sbm.test.util.OpenRewriteDummyRecipeInstaller;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

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
        ParserSettings parserSettings = ParserSettings.builder()
                .runPerSubmodule(false)
                .exclusions(Set.of("testcode"))
                .build();
        MavenProjectFactory mavenProjectFactory = new MavenProjectFactory();
        DummyResource rootPom = new DummyResource("pom.xml",
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.springframework.sbm</groupId>
                    <artifactId>sbm-openrewrite-parser</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                    </properties>
                </project>
                """);


        OpenRewriteDummyRecipeInstaller recipeInstaller = new OpenRewriteDummyRecipeInstaller();
        recipeInstaller.installRecipe();


        RewriteRecipeDiscovery recipeDiscovery = new RewriteRecipeDiscovery(parserSettings, mavenProjectFactory);
//        OpenRewriteProjectParser openRewriteProjectParser = new OpenRewriteProjectParser(new ProvenanceMarkerFactory(new ParserSettings(), new MavenProjectFactory()), new BuildFileParser(new MavenModelReader()), new SourceFileParser(), new StyleDetector(), new ParserSettings());
//        Path baseDir = Path.of(".");
//        RewriteProjectParsingResult parsingResult = openRewriteProjectParser.parse(baseDir, List.of(rootPom), new InMemoryExecutionContext(t -> t.printStackTrace()));
        RewriteMavenProjectParser rewriteMavenProjectParser = new RewriteMavenProjectParser();
        ExecutionContext executionContext = new InMemoryExecutionContext(t -> t.printStackTrace());
        RewriteProjectParsingResult parsingResult = rewriteMavenProjectParser.parse(
                Path.of("/Users/fkrueger/projects/spring-boot-migrator/sbm-rewrite-maven-parser/testcode/openrewrite-dummy-recipe"),
                true,
                "pomCache",
                false,
                Set.of("**/testcode/**"),
                Set.of(),
                -1,
                false,
                executionContext);
        List<Recipe> dummyRecipe = recipeDiscovery.discoverFilteredRecipes((Xml.Document) parsingResult.sourceFiles().get(0), List.of("Dummy Recipe"));
    }

}