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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Recipe;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.RecipeTestSupport;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class UpgradeDepenenciesMigrationTest {
    @Test
    void migrateEhCacheToSpringBoot3() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withSpringBootParentOf("2.7.5")
                .withBuildFileHavingDependencies("org.ehcache:ehcache")
                .build();

        System.out.println(context.getApplicationModules().getRootModule().getBuildFile().print());

        RecipeTestSupport.testRecipe(Path.of("recipes/27_30/migration/sbu30-upgrade-dependencies.yaml"), recipes -> {
            Recipe recipe = recipes.getRecipeByName("sbu30-upgrade-dependencies").get();
            recipe.apply(context);
            String modifiedPom = context.getApplicationModules().getRootModule().getBuildFile().print();
            assertThat(modifiedPom).isEqualTo(
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                        <parent>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-parent</artifactId>
                            <version>2.7.5</version>
                            <relativePath/>
                        </parent>
                        
                        <groupId>com.example</groupId>
                        <artifactId>dummy-root</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                        <packaging>jar</packaging>
                        <dependencies>
                            <dependency>
                                <groupId>org.ehcache</groupId>
                                <artifactId>ehcache</artifactId>
                                <version>3.10.2</version>
                                <classifier>jakarta</classifier>
                            </dependency>
                        </dependencies>
                        
                    </project>
                    """
            );
            SourceFile document = MavenParser.builder().build().parse(modifiedPom).toList().get(0);
            assertThat(document).isNotNull();
        });
    }
}
