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
package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportActionDeserializer;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.RecipeTestSupport;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class RemoveImageBannerTest {
    @Test
    void testGlobExpression() {
        String pattern = "{**/src,src}/main/resources/banner.{gif,png,jpg}";
        assertThat(FileSystems.getDefault().getPathMatcher("glob:"+pattern).matches(Path.of("src/main/resources/banner.gif"))).isTrue();
        assertThat(FileSystems.getDefault().getPathMatcher("glob:"+pattern).matches(Path.of("/src/main/resources/banner.gif"))).isTrue();
        assertThat(FileSystems.getDefault().getPathMatcher("glob:"+pattern).matches(Path.of("some/path/above/src/main/resources/banner.gif"))).isTrue();
    }

    @Test
    void applyRemoveImageBannerRecipeShouldRemoveAllImageBannerAtDefaultLocation() {
        String parentPom = PomBuilder
                .buildPom("com.example:parent:1.0")
                .withModules("moduleA", "moduleB", "moduleC")
                .build();
        String moduleA = PomBuilder.buildPom("com.example:parent:1.0", "moduleA").build();
        String moduleB = PomBuilder.buildPom("com.example:parent:1.0", "moduleB").build();
        String moduleC = PomBuilder.buildPom("com.example:parent:1.0", "moduleC").build();

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("pom.xml", parentPom)
                .withMavenBuildFileSource("moduleA/pom.xml", moduleA)
                .addProjectResource("moduleA/src/main/resources/banner.jpg", "")
                .withMavenBuildFileSource("moduleB/pom.xml", moduleB)
                .addProjectResource("moduleB/src/main/resources/banner.png", "")
                .withMavenBuildFileSource("moduleC/pom.xml", moduleC)
                .addProjectResource("moduleC/src/main/resources/banner.gif", "")
                .build();

        assertThat(context.getProjectResources().list()).hasSize(7);

        RecipeTestSupport.testRecipe(Path.of("recipes/27_30/migration/sbu30-remove-image-banner.yaml"), recipes -> {
            Recipe recipe = recipes.getRecipeByName("sbu30-remove-image-banner").get();
            recipe.apply(context);

            assertThat(context.getProjectResources().list()).hasSize(4); // only pom.xml left
            assertThat(context.getProjectResources().stream().allMatch(res -> res.getAbsolutePath().toString().endsWith("pom.xml"))).isTrue();
        }, SpringBootUpgradeReportActionDeserializer.class);
    }
}
