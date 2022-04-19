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
package org.springframework.sbm.jee.ejb.recipes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.common.migration.conditions.FileMatchingPatternExist;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.jee.ejb.actions.MigrateEjbDeploymentDescriptor;

@Configuration
public class MigrateEjbJarDeploymentDescriptorRecipe {
    @Bean
    public Recipe ejbJarDeploymentDescriptor() {
        return Recipe.builder()
                .name("migrate-ejb-jar-deployment-descriptor")
                .order(90)
                .description("Add or overrides @Stateless annotation as defined in ejb deployment descriptor")
                .condition(new FileMatchingPatternExist("/**/ejb-jar.xml")) // Recipe condition is True thus it'll be applicable based on applicability of actions
                .action(
                        MigrateEjbDeploymentDescriptor.builder()
                                .condition(
                                        FileMatchingPatternExist.builder()
                                                .pattern("/**/ejb-jar.xml")
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
