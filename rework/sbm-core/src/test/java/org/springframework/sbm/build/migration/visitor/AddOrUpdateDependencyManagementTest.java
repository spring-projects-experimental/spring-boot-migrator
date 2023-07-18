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
package org.springframework.sbm.build.migration.visitor;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.openrewrite.MavenRefactoringTestHelper;

@Tag("integration")
public class AddOrUpdateDependencyManagementTest {
    @Test
    public void shouldCreateDependencyManagementWithDependencyWhenNoneExists() {

        String before =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        Dependency dependency = Dependency.builder()
                .scope("test")
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("5.6.2")
                .build();
        AddOrUpdateDependencyManagement adm = new AddOrUpdateDependencyManagement(dependency);

        MavenRefactoringTestHelper.verifyChange(before, expected, adm);
    }

    @Test
    public void shouldAddDependencyWhenDependencyManagementAlreadyExists() {

        String before =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>";

        String expected =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.projectlombok</groupId>\n" +
                        "                <artifactId>lombok</artifactId>\n" +
                        "                <version>1.18.12</version>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>";

        Dependency dependency = Dependency.builder().groupId("org.projectlombok").artifactId("lombok").version("1.18.12").build();

        AddOrUpdateDependencyManagement adm = new AddOrUpdateDependencyManagement(dependency);

        MavenRefactoringTestHelper.verifyChange(before, expected, adm);
    }

    @Test
    public void shouldUpdateVersionIfDifferent() {

        String before =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        AddOrUpdateDependencyManagement adm = new AddOrUpdateDependencyManagement(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope("test")
                .build()
        );
        MavenRefactoringTestHelper.verifyChange(before, expected, adm);
    }

    @Test
    public void shouldUpdateScopeIfDifferent() {

        String before =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>compile</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        AddOrUpdateDependencyManagement adm = new AddOrUpdateDependencyManagement(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope("test")
                .build()
        );
        MavenRefactoringTestHelper.verifyChange(before, expected, adm);
    }


    @Test
    public void shouldRemoveScopeIfRemoved() {

        String before =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>compile</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        AddOrUpdateDependencyManagement adm = new AddOrUpdateDependencyManagement(Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope(null)
                .build()
        );
        MavenRefactoringTestHelper.verifyChange(before, expected, adm);
    }
}
