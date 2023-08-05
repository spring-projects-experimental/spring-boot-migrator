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
package org.springframework.sbm;

import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.build.migration.conditions.AnyDeclaredDependencyExistMatchingRegex;
import org.springframework.sbm.engine.recipe.Action;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.spring.migration.actions.InitDataSourceAfterJpaInitAction;
import org.openrewrite.maven.UpgradeDependencyVersion;
import org.openrewrite.maven.UpgradeParentVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MigrateBoot23To24Recipe {

    @Bean("migrateBoot23To24")
    public Recipe migrateBoot23To24(UserInteractions ui) {
        return createSpringBootUpgradeRecipe("2.3", "2.4", ui);
    }
    
    private Recipe createSpringBootUpgradeRecipe(String fromVersion, String toVersion, UserInteractions ui) {

        return Recipe.builder()
                .name("migrate-boot-" + fromVersion + "-" + toVersion)
                .description("Migrate from Spring Boot " + fromVersion + " to " + toVersion)
                .condition(AnyDeclaredDependencyExistMatchingRegex.builder()
                        .dependencies(List.of("org\\.springframework\\.boot:.*:" + fromVersion.replace(".", "\\.") + ".*"))
                        .build()
                )
                .actions(upgradeDependencyVersion(toVersion))
                .action(new InitDataSourceAfterJpaInitAction(ui))
                .build();
    }
    
    private List<Action> upgradeDependencyVersion(String toVersion) {
        UpgradeDependencyVersion upgradeDependencyVersion = new UpgradeDependencyVersion("org.springframework.boot", null, toVersion + ".x", null, false);
        UpgradeParentVersion upgradeParentVersion = new UpgradeParentVersion("org.springframework.boot", "spring-boot-starter-parent", toVersion + ".x", null);
        return List.of(
                OrRecipesConfig.createAction(upgradeDependencyVersion),
                OrRecipesConfig.createAction(upgradeParentVersion)
        );
    }
    
}
