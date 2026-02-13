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

import org.junit.jupiter.api.Test;

class ScanProjectWithPersistenceXmlInTestResourcesIssue1010Test
extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "project-with-persistence-xml-in-test-resources";
    }

    @Test
    void scanAndMigrateJpaDoesNotFail() {

        intializeTestProject();

        executeMavenGoals(getTestDir(), "clean", "package");

        // This step previously crashed with ResourceFilterException (#1010)
        scanProject();

        assertRecipeApplicable("migrate-jpa-to-spring-boot");

        applyRecipe("migrate-jpa-to-spring-boot");
    }
}
