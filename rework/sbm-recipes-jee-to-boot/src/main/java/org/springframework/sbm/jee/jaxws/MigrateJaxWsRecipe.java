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
package org.springframework.sbm.jee.jaxws;

import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.conditions.NoExactDependencyExist;
import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MigrateJaxWsRecipe {
    @Bean
    public Recipe jaxWs(UserInteractions ui, freemarker.template.Configuration configuration) {
        GenerateWebServices generateWebServices = new GenerateWebServices(ui, configuration);
        generateWebServices.setDescription("Generate WebServices");
        return Recipe.builder()
                .name("migrate-jax-ws")
                .order(60)
                .description("Migrate Jax Web-Service implementation to Spring Boot bases Web-Service")
                .condition(HasImportStartingWith.builder().value("javax.jws.WebService").description("Has jax-ws WebService import").build())
                .actions(List.of(
                        AddDependencies.builder()
                                .dependencies(
                                        List.of(
                                                Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-web-services").version("latest.release").build()
                                        )
                                )
                                .description("Add spring boot web-services starter")
                                .condition(NoExactDependencyExist.builder().dependency(Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-web-services").build()).build())
                                .build(),

                        generateWebServices
                ))
                .build();
    }
}
