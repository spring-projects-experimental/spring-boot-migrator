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

package org.springframework.sbm.boot.upgrade_27_30.report;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportActionDeserializer;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.*;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
        RecipeParser.class,
        YamlObjectMapperConfiguration.class,
        CustomValidator.class,
        ResourceHelper.class,
        ActionDeserializerRegistry.class,
        DefaultActionDeserializer.class,
        SpringBootUpgradeReportActionDeserializer.class,
        RewriteMigrationResultMerger.class,
        RewriteRecipeRunner.class,
        RewriteSourceFileWrapper.class,
        RewriteRecipeLoader.class,
        CustomValidatorBean.class
})
public class SpringBootUpgradeReportActionDeserializerIntegrationTest {

    @Autowired
    RecipeParser recipeParser;

    @Test
    void recipeFromYaml() throws IOException {
        @Language("yaml")
        String yaml =
                """
                - name: boot-2.7-3.0-upgrade-report2
                  description: Create a report for Spring Boot Upgrade from 2.7.x to 3.0.0-M3
                  condition:
                    type: org.springframework.sbm.boot.upgrade.common.conditions.IsAnyMatchingSpringBootVersion
                    versionPatterns: 2.7.,3.0.
                                
                  actions:
                                
                    - type: org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportAction
                      description: The description
                      file: report.html
                      condition:
                        type: org.springframework.sbm.common.migration.conditions.TrueCondition
                      sections:
                        - title: Changes to Data Properties
                          helper: org.springframework.sbm.boot.upgrade_27_30.report.ChangesToDataProperties
                          info: |
                            The data prefix has been reserved for Spring Data and any properties under the `data` prefix imply that 
                            Spring Data is required on the classpath.
                          reason: |
                            The scan found properties with `spring.data` prefix but no dependency matching `org.springframework.data:.* `.
                            <#list matches as match>
                              file://${match.absolutePath}[`${match.relativePath}`]
                              <#list match.propertiesFound as property>
                              - `${property.key}=${property.value}`
                              </#list>
                            </#list>
                          todos: |
                            Either add `spring-data` dependency, rename the property or remove it in case it's not required anymore.
                          

                #        - title: DatabaseDriver.GAE was deprecated in Spring Boot 2.7
                #          condition:
                #            type: org.springframework.sbm.common.migration.conditions.TrueCondition
                #          dataProvider:  org.springframework.sbm.boot.upgrade_27_30.report.ChangesToDataProperties
                #          info: |-
                #
                #          Support for GAE database driver has been removed in 3.0.0 without replacement following the removal
                #          of AppEngineDriver from version 2.0 of the AppEngine API SDK.
                #
                #          reason: |-
                #          Dependencies containing 'com.google.appengine.api.rdbms.AppEngineDriver' were found in these poms
                #          todos: |-
                #
                #          You need to find a replacement for Google App Engine.                          
                """;

        // parse the recipe
        Recipe[] recipes = recipeParser.parseRecipe(yaml);
        assertThat(recipes[0].getActions().get(0)).isInstanceOf(SpringBootUpgradeReportAction.class);
        // retrieve adapter action
        SpringBootUpgradeReportAction recipeAdapter = (SpringBootUpgradeReportAction) recipes[0].getActions().get(0);
        assertThat(recipeAdapter.getSections().get(0).getChange()).isEqualTo(
            """
            The data prefix has been reserved for Spring Data and any properties under the `data` prefix imply that 
            Spring Data is required on the classpath.
            """
        );
//        assertThat(recipeAdapter.getSections().get(0).getReason()).isEqualTo(
//                """
//                The scan found properties with `spring.data` prefix but no dependency matching `org.springframework.data:.* `.
//                <#list matches as match>
//                  file://${match.absolutePath}[`${match.relativePath}`]
//                  <#list match.propertiesFound as property>
//                  - `${property.key}=${property.value}`
//                  </#list>
//                </#list>
//                """
//        );

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource("src/main/resources/application.properties", "spring.data.foo=bar")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .build();

        recipes[0].getActions().get(0).apply(context);

        // create context
//        String javaSource = "@java.lang.Deprecated\n" +
//                "public class Foo {\n" +
//                "}\n";
//
//        ProjectContext context = TestProjectContext.buildProjectContext()
//                .addJavaSource("src/main/java", javaSource)
//                .build();
//        // and apply the adapter
//        recipeAdapter.apply(context);
//        // verify the openrewrite recipe ran
//        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
//                "public class Foo {\n" +
//                        "}\n"
//        );
    }

}
