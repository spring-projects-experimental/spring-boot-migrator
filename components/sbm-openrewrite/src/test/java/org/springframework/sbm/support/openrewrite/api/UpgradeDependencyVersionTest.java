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
package org.springframework.sbm.support.openrewrite.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.UpgradeDependencyVersion;
import org.openrewrite.xml.tree.Xml;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class UpgradeDependencyVersionTest {

    public static final String POM_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                    + "    <modelVersion>4.0.0</modelVersion>\n"
//                        + "    <parent>\n"
//                        + "        <groupId>org.springframework.boot</groupId>\n"
//                        + "        <artifactId>spring-boot-starter-parent</artifactId>\n"
//                        + "        <version>2.4.5</version>\n"
//                        + "        <relativePath/> <!-- lookup parent from repository -->\n"
//                        + "    </parent>\n"
                    + "    <groupId>com.example</groupId>\n"
                    + "    <artifactId>boot-23-app</artifactId>\n"
                    + "    <version>0.0.1-SNAPSHOT</version>\n"
                    + "    <name>boot-23-app</name>\n"
                    + "    <description>Demo project for Spring Boot</description>\n"
                    + "    <properties>\n"
                    + "        <java.version>11</java.version>\n"
                    + "    </properties>\n"
                    + "    <dependencies>\n"
                    + "        <dependency>\n"
                    + "            <groupId>org.springframework.boot</groupId>\n"
                    + "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n"
                    + "            <version>2.4.5</version>\n"
                    + "        </dependency>\n"
                    + "        <dependency>\n"
                    + "            <groupId>org.springframework.boot</groupId>\n"
                    + "            <artifactId>spring-boot-starter-test</artifactId>\n"
                    + "            <scope>test</scope>\n"
                    + "            <version>2.4.5</version>\n"
                    + "        </dependency>\n"
                    + "    </dependencies>\n"
                    + "</project>";

    private final List<Xml.Document> mavens = MavenParser.builder().build().parse(POM_XML);

    @Test
    @Disabled
    void testUpgradeDependency() {
        String expectedPomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>boot-23-app</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>boot-23-app</name>\n" +
                        "    <description>Demo project for Spring Boot</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                        "            <version>2.4.5</version>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                        "            <scope>test</scope>\n" +
                        "            <version>2.5.3</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        String groupId = "org.springframework.boot";
        String artifactId = "spring-boot-starter-test";
        String version = "2.5.3";
        UpgradeDependencyVersion sut = new UpgradeDependencyVersion(groupId, artifactId, version, null, false);

        RecipeRun recipeRun = sut.run(mavens);

        assertThat(recipeRun.getResults().get(0).getAfter().printAll()).isEqualTo(expectedPomXml);
    }

    @Test
    @Disabled
    void testUpgradeDependency_trustParent() {
        String expectedPomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "       <groupId>com.example</groupId>\n" +
                        "    <artifactId>boot-23-app</artifactId>\n" +
                        "    </parent>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>boot-23-app</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>boot-23-app</name>\n" +
                        "    <description>Demo project for Spring Boot</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                        "            <version>2.4.5</version>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                        "            <scope>test</scope>\n" +
                        "            <version>2.6.1</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        String groupId = "org.springframework.boot";
        String artifactId = "spring-boot-starter-test";
        String version = "2.5.3";
        UpgradeDependencyVersion sut = new UpgradeDependencyVersion(groupId, artifactId, version, null, true);

        RecipeRun recipeRun = sut.run(mavens);

        assertThat(recipeRun.getResults().get(0).getAfter().printAll()).isEqualTo(expectedPomXml);
    }

    @Test
    @Disabled
    void testUpgradeDependency_latestReleaseVersion() {
        String expectedPomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>boot-23-app</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>boot-23-app</name>\n" +
                        "    <description>Demo project for Spring Boot</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                        "            <version>2.4.5</version>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                        "            <scope>test</scope>\n" +
                        "            <version>2.5.3</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        String groupId = "org.springframework.boot";
        String artifactId = "spring-boot-starter-test";
        String version = "latest.release";
        UpgradeDependencyVersion sut = new UpgradeDependencyVersion(groupId, artifactId, version, null, false);

        RecipeRun results = sut.run(mavens);

        assertThat(results.getResults().get(0).getAfter().printAll()).isEqualTo(expectedPomXml);
    }

    @Test
    void testUpgradeDependency_nullVersion() {
        String groupId = "org.springframework.boot";
        String artifactId = "spring-boot-starter-test";
        String version = null;
        UpgradeDependencyVersion sut = new UpgradeDependencyVersion(groupId, artifactId, version, null, false);

        AtomicBoolean exceptionThrown = new AtomicBoolean(false);
        RecipeRun results = sut.run(mavens, new InMemoryExecutionContext((e) -> {
            e.printStackTrace();
            exceptionThrown.set(true);
        }));
//        assertThat(exceptionThrown).isTrue();

    }
}
