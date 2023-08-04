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
<<<<<<<< HEAD:sbm-support-rewrite/src/main/java/org/springframework/sbm/parsers/JavaParserBuilder.java
package org.springframework.sbm.parsers;

import lombok.Getter;
import lombok.Setter;
import org.openrewrite.java.JavaParser;
import org.springframework.sbm.scopes.annotations.ScanScope;
import org.springframework.stereotype.Component;
========
package org.springframework.sbm.jee.jpa.recipes;

import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
>>>>>>>> ee0cf46c (sbm-core):rework/sbm-recipes-jee-to-boot/src/test/java/org/springframework/sbm/jee/jpa/recipes/MigrateEclipseLinkToSpringBootRecipeTest.java

import java.util.function.Supplier;

<<<<<<<< HEAD:sbm-support-rewrite/src/main/java/org/springframework/sbm/parsers/JavaParserBuilder.java
/**
 * @author Fabian Krüger
 */
@Component
@ScanScope
public class JavaParserBuilder extends JavaParser.Builder{

    @Getter
    @Setter
    private JavaParser.Builder builder;

    public Supplier<JavaParser.Builder> getSupplier() {
        return () -> builder;
    }

    @Override
    public JavaParser build() {
        return builder.build();
    }
========
public class MigrateEclipseLinkToSpringBootRecipeTest {

    @Test
    @Disabled("TODO: define assertions")
    void test() {
        String applicationDir = "eclipselink-jpa";
        Path from = Path.of("./testcode/jee/jpa").resolve(applicationDir).resolve("given");
        RecipeIntegrationTestSupport.initializeProject(from, applicationDir)
                .andApplyRecipe("migrate-jpa-to-spring-boot");


    }

>>>>>>>> ee0cf46c (sbm-core):rework/sbm-recipes-jee-to-boot/src/test/java/org/springframework/sbm/jee/jpa/recipes/MigrateEclipseLinkToSpringBootRecipeTest.java
}
