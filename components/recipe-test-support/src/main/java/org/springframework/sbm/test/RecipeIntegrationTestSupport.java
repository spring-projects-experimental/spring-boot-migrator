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
package org.springframework.sbm.test;

import org.springframework.sbm.engine.commands.ApplicableRecipeListCommand;
import org.springframework.sbm.engine.commands.ApplyCommand;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import freemarker.template.Configuration;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Helper class for integration testing recipes.
 */
public class RecipeIntegrationTestSupport {

    public static Path getResultDir(String toDirName) {
        return Path.of(TARGET_DIR).resolve(toDirName);
    }

    private static final String TARGET_DIR = "./target/testcode";
    private static final String TEMPLATES = "./src/main/resources/templates";
    @Setter(AccessLevel.PRIVATE)
    private Path rootDir;
    @Setter(AccessLevel.PRIVATE)
    private Path sourceDir;


    /**
     * Change the default target dir from {@code ./target/testcode} to {@code targetDir}.
     */
    public static RecipeIntegrationTestSupport withTargetDir(Path targetDir) {
        RecipeIntegrationTestSupport recipeIntegrationTestSupport = new RecipeIntegrationTestSupport();
        recipeIntegrationTestSupport.setRootDir(targetDir);
        return recipeIntegrationTestSupport;
    }

    public static RecipeIntegrationTestSupport initializeProject(Path sourceDir, String targetDirName) {
        try {
            Resource classPathResource = new FileSystemResource(sourceDir.toString());
            File to = getResultDir(targetDirName).toFile().getCanonicalFile();
            FileUtils.deleteDirectory(to);
            FileUtils.forceMkdir(to);
            FileUtils.copyDirectory(classPathResource.getFile().getCanonicalFile(), to);
            RecipeIntegrationTestSupport recipeIntegrationTestSupport = new RecipeIntegrationTestSupport();
            recipeIntegrationTestSupport.setRootDir(to.toPath());
            recipeIntegrationTestSupport.setSourceDir(sourceDir);
            return recipeIntegrationTestSupport;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Expects a test project at {@code 'testcode/${targetDirName}/given'} as source.
     * The project is copied to {@code './target/${targetDirName} and the recipe is then applied to this project.
     */
    public static RecipeIntegrationTestSupport initializeProject(String targetDirName) {
        return initializeProject(Path.of("./testcode").resolve(targetDirName).resolve("given"), targetDirName);
    }

    public void andApplyRecipe(String recipeName) {
        SpringBeanProvider.run(ctx -> {
                    Path templatesPath = Path.of(TEMPLATES).normalize().toAbsolutePath();
                    if (Files.exists(templatesPath)) {
                        // FIXME: two freemarker configurations exist
                        Configuration configuration = ctx.getBean("configuration", Configuration.class);
                        configuration.setDirectoryForTemplateLoading(templatesPath.toFile().getCanonicalFile());
                    }

                    ScanCommand scanCommand = ctx.getBean(ScanCommand.class);
                    
                    SbmApplicationProperties sbmApplicationProperties = ctx.getBean(SbmApplicationProperties.class);
                    sbmApplicationProperties.setDefaultBasePackage("org.springframework.sbm");
                    
                    ApplicableRecipeListCommand applicableRecipeListCommand = ctx.getBean(ApplicableRecipeListCommand.class);
                    ProjectContext projectContext = scanCommand.execute(rootDir.toString());
                    ctx.getBean(ProjectContextHolder.class).setProjectContext(projectContext);
                    List<Recipe> recipesFound = applicableRecipeListCommand.execute(projectContext);
                    Recipe recipe = recipesFound.stream()
                            .filter(r -> r.getName().equals(recipeName))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Could not find recipe '%s' in list of recipes found: %s".formatted(recipeName, recipesFound)));
                    ApplyCommand applyCommand = ctx.getBean(ApplyCommand.class);
                    applyCommand.execute(projectContext, recipe.getName());
                },
                SpringBeanProvider.ComponentScanConfiguration.class,
                SbmApplicationProperties.class,
                Configuration.class,
                UserInteractionsDummy.class
        );
    }

    /**
     * Run the given recipe and verify that all files in the resulting project equal the files in the expected project.
     * <p>
     * When the test has been initialized with {@code initializeProject(Path.of("testcode/my-app/given"), "my-app")},
     * this method verifies that the migrated project in {@code './target/testcode/my-app'} equals {@code 'testcode/my-app/expected'}.
     */
    public void andApplyRecipeComparingWithExpected(String recipeName) {
        try {
            andApplyRecipe(recipeName);
            Path migratedProject = rootDir;
            Path expectedProject = sourceDir.getParent().resolve("expected");

            List<Path> result;
            try (Stream<Path> walk = Files.walk(expectedProject)) {
                result = walk.filter(Files::isRegularFile)
                        .map(expectedProject::relativize)
                        .toList();
            }

            result.forEach(r -> assertThat(expectedProject.resolve(r).normalize().toAbsolutePath())
                    .hasSameTextualContentAs(migratedProject.resolve(r).normalize().toAbsolutePath()));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public RecipeIntegrationTestSupport addGitSupport() {
        GitSupport.initGit(this.rootDir.toFile());
        return this;
    }
}
