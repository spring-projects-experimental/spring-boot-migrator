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
package org.springframework.sbm.jee.jaxrs;

import lombok.RequiredArgsConstructor;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.migration.actions.AddDependencies;
import org.springframework.sbm.build.migration.conditions.NoExactDependencyExist;
import org.springframework.sbm.engine.recipe.OpenRewriteDeclarativeRecipeAdapter;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.engine.recipe.RewriteRecipeLoader;
import org.springframework.sbm.engine.recipe.RewriteRecipeRunner;
import org.springframework.sbm.java.JavaRecipeAction;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.java.migration.actions.ReplaceTypeAction;
import org.springframework.sbm.java.migration.conditions.HasAnnotation;
import org.springframework.sbm.java.migration.conditions.HasImportStartingWith;
import org.springframework.sbm.java.migration.conditions.HasTypeAnnotation;
import org.springframework.sbm.jee.jaxrs.actions.ConvertJaxRsAnnotations;
import org.springframework.sbm.jee.jaxrs.recipes.ReplaceMediaType;
import org.springframework.sbm.jee.jaxrs.recipes.SwapCacheControl;
import org.springframework.sbm.jee.jaxrs.recipes.SwapHttHeaders;
import org.springframework.sbm.jee.jaxrs.recipes.SwapResponseWithResponseEntity;
import org.springframework.sbm.support.openrewrite.java.AddOrReplaceAnnotationAttribute;

import java.util.List;
import java.util.function.Supplier;

@Configuration
//@RequiredArgsConstructor
public class MigrateJaxRsRecipe {


    private final Supplier<JavaParser> javaParserSupplier = () -> JavaParser.fromJavaVersion().classpath(ClasspathRegistry.getInstance().getCurrentDependencies()).build();

    @Bean
    public Recipe jaxRs(RewriteRecipeLoader rewriteRecipeLoader, RewriteRecipeRunner rewriteRecipeRunner) {
        return Recipe.builder()
                .name("migrate-jax-rs")
                .order(60)
                .description("Any class has import starting with javax.ws.rs")
                .condition(HasImportStartingWith.builder().value("javax.ws.rs").description("Any class has import starting with javax.ws.rs").build())
                .actions(List.of(

                                AddDependencies.builder()
                                        .dependencies(
                                                List.of(
                                                        Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-web").version("2.3.4.RELEASE").build()
                                                )
                                        )
                                        .description("Add spring-boot-starter-web dependency to build file.")
                                        .condition(NoExactDependencyExist.builder().dependency(Dependency.builder().groupId("org.springframework.boot").artifactId("spring-boot-starter-web").build()).build())
                                        .build(),

                                ConvertJaxRsAnnotations.builder()
                                        .condition(HasTypeAnnotation.builder().annotation("javax.ws.rs.Path").build())
                                        .description("Convert JAX-RS annotations into Spring Boot annotations.")
                                        .build(),

                                // Important! Replace method parameter annotations after ConvertJaxRsAnnotations action
                                // ConvertJaxRsAnnotations examines Jax-Rs annotations on the parameters to determine the request body param

                                ReplaceTypeAction.builder()
                                        .condition(HasAnnotation.builder().annotation("javax.ws.rs.PathParam").build())
                                        .description("Replace JAX-RS @PathParam with Spring Boot @PathVariable annotation.")
                                        .annotation("javax.ws.rs.PathParam")
                                        .withAnnotation("org.springframework.web.bind.annotation.PathVariable")
                                        .build(),

                                ReplaceTypeAction.builder()
                                        .condition(HasAnnotation.builder().annotation("javax.ws.rs.QueryParam").build())
                                        .description("Replace JAX-RS @QueryParam with Spring Boot @RequestParam annotation.")
                                        .annotation("javax.ws.rs.QueryParam")
                                        .withAnnotation("org.springframework.web.bind.annotation.RequestParam")
                                        .build(),

                                ReplaceTypeAction.builder()
                                        .condition(HasAnnotation.builder().annotation("javax.ws.rs.FormParam").build())
                                        .description("Replace JAX-RS @FormParam with Spring Boot @RequestParam annotation.")
                                        .annotation("javax.ws.rs.QueryParam")
                                        .withAnnotation("org.springframework.web.bind.annotation.RequestParam")
                                        .build(),

                                JavaRecipeAction.builder()
                                        .condition(HasImportStartingWith.builder().value("javax.ws.rs.core.MediaType").build())
                                        .description("Replace JaxRs MediaType with it's Spring equivalent.")
                                        .recipe(new ReplaceMediaType(javaParserSupplier))
                                        .build(),

                                JavaRecipeAction.builder()
                                        .condition(HasImportStartingWith.builder().value("javax.ws.rs.core.HttpHeaders").build())
                                        .description("Replace JaxRs HttpHeaders with it's Spring equivalent.")
                                        .recipe(new SwapHttHeaders())
                                        .build(),

                                JavaRecipeAction.builder()
                                        .condition(HasImportStartingWith.builder().value("javax.ws.rs.core.MultivaluedMap").build())
                                        .description("Replace JaxRs MultivaluedMap with it's Spring equivalent.")
                                        .recipe(new ChangeType("javax.ws.rs.core.MultivaluedMap", "org.springframework.util.MultiValueMap", false))
                                        .build(),

                                JavaRecipeAction.builder()
                                        .condition(HasImportStartingWith.builder().value("javax.ws.rs.core.CacheControl").build())
                                        .description("Replace JaxRs CacheControl with it's Spring equivalent.")
                                        .recipe(new SwapCacheControl())
                                        .build(),

                                JavaRecipeAction.builder()
                                        .condition(HasImportStartingWith.builder().value("javax.ws.rs.core.Response").build())
                                        .description("Replace JaxRs Response and ResponseBuilder with it's Spring equivalent.")
                                        .recipe(new SwapResponseWithResponseEntity(javaParserSupplier))
                                        .build(),

                                OpenRewriteDeclarativeRecipeAdapter.builder()
                                        .condition(HasAnnotation.builder().annotation("org.springframework.web.bind.annotation.RequestParam").build())
                                        .description("Adds required=false to all @RequestParam annotations")
                                        .rewriteRecipeLoader(rewriteRecipeLoader)
                                        .rewriteRecipeRunner(rewriteRecipeRunner)
                                        .openRewriteRecipe(
                                                """
                                                type: specs.openrewrite.org/v1beta/recipe
                                                name: org.springframework.sbm.jee.MakeRequestParamsOptional
                                                displayName: Set required=false for @RequestParam without 'required'
                                                description: Set required=false for @RequestParam without 'required'
                                                causesAnotherCycle: true
                                                recipeList:
                                                  - org.openrewrite.java.AddOrUpdateAnnotationAttribute:
                                                      annotationType: "org.springframework.web.bind.annotation.RequestParam"
                                                      attributeName: "required"
                                                      attributeValue: "false"
                                                      addOnly: true
                                                """)
                                        .build()
                        )
                )
                .build();
    }

}
