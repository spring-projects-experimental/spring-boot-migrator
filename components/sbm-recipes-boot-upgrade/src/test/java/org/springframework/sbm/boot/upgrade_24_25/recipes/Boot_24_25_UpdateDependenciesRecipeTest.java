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
package org.springframework.sbm.boot.upgrade_24_25.recipes;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.test.RecipeIntegrationTestSupport;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class Boot_24_25_UpdateDependenciesRecipeTest {

    @Test
    void updateWithParentPom() {
        // TODO: Move to a more generic Action to be reused, e.g. 'UpgradeParentVersion'
        String applicationDir = "spring-boot-2.4-to-2.5-example";
        Path from = Path.of("./testcode").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("boot-2.4-2.5-dependency-version-update");

        Path resultDir = RecipeIntegrationTestSupport.getResultDir(applicationDir);

        assertThat(contentOf(resultDir.resolve("pom.xml").toFile())).contains(
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>2.5.6</version>\n" +
                "        <relativePath/> <!-- lookup parent from repository -->\n" +
                "    </parent>"
        );
    }
}
