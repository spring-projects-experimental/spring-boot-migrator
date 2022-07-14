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
package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.UpgradeDependencyVersion;
import org.openrewrite.maven.UpgradeParentVersion;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UpgradeSpringBoot3_0Test {

    @Test
    void shouldAddRepositoryAndUpdateParentVersionUsingYaml() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.7.0</version>\n" +
                        "    </parent>\n" +
                        "    <groupId>org.springframework.sbm</groupId>\n" +
                        "    <artifactId>spring-boot-upgrades-30</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "</project>";

        String expectedPom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>2.7.0</version>\n" +
                "    </parent>\n" +
                "    <groupId>org.springframework.sbm</groupId>\n" +
                "    <artifactId>spring-boot-upgrades-30</artifactId>\n" +
                "    <version>0.1.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>repository.spring.milestone</id>\n" +
                "            <name>repository.spring.milestone</name>\n" +
                "            <url>https://repo.spring.io/milestone</url>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "</project>";

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        List<Xml.Document> parse = MavenParser.builder().build().parse(ctx, pomXml);

        String recipeName = "org.openrewrite.java.spring.boot3.data.TEST";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(parse, ctx);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(expectedPom);
    }

    @Test
    public void canAddRepository() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.7.0</version>\n" +
                        "    </parent>\n" +
                        "    <groupId>org.springframework.sbm</groupId>\n" +
                        "    <artifactId>spring-boot-upgrades-30</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "</project>";

        String expectedPom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>2.7.0</version>\n" +
                "    </parent>\n" +
                "    <groupId>org.springframework.sbm</groupId>\n" +
                "    <artifactId>spring-boot-upgrades-30</artifactId>\n" +
                "    <version>0.1.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>repository.spring.milestone</id>\n" +
                "            <name>repository.spring.milestone</name>\n" +
                "            <url>https://repo.spring.io/milestone</url>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "</project>";

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        List<Xml.Document> parse = MavenParser.builder().build().parse(ctx, pomXml);

        String recipeName = "org.openrewrite.java.spring.boot3.data.TEST2";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(parse, ctx);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(expectedPom);
    }
    @Test
    void testBumpingParentPomVersion() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.7.0</version>\n" +
                        "    </parent>\n" +
                        "    <artifactId>test-project</artifactId>\n" +
                        "    <repositories>\n" +
                        "        <repository>\n" +
                        "            <id>repository.spring.milestone</id>\n" +
                        "            <name>Spring Milestone Repository</name>\n" +
                        "            <url>http://repo.spring.io/milestone</url>\n" +
                        "        </repository>\n" +
                        "    </repositories>\n" +
                        "</project>";

        List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);

        UpgradeParentVersion target = new UpgradeParentVersion("org.springframework.boot",
                "spring-boot-starter-parent", "3.0.0-M3", null);

        List<Result> output = target.run(parse);

        assertThat(output).isNotEmpty();
        assertThat(output.get(0).getAfter().printAll()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>3.0.0-M3</version>\n" +
                "    </parent>\n" +
                "    <artifactId>test-project</artifactId>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>repository.spring.milestone</id>\n" +
                "            <name>Spring Milestone Repository</name>\n" +
                "            <url>http://repo.spring.io/milestone</url>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "</project>");
    }

    @Test
    void testUpgradeDependencyVersion() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <artifactId>test-project</artifactId>\n" +
                        "    <groupId>org.my.test</groupId>\n" +
                        "    <version>1.0.0-SNAPSHOT</version>\n" +
                        "    <repositories>\n" +
                        "        <repository>\n" +
                        "            <id>repository.spring.milestone</id>\n" +
                        "            <name>Spring Milestone Repository</name>\n" +
                        "            <url>http://repo.spring.io/milestone</url>\n" +
                        "        </repository>\n" +
                        "    </repositories>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter</artifactId>\n" +
                        "            <version>2.7.0</version>\n" +
                        "        </dependency>" +
                        "    </dependencies>" +
                        "</project>";

        List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);

        UpgradeDependencyVersion target = new UpgradeDependencyVersion("org.springframework.boot",
                "*", "3.0.0-M3", null, false);

        List<Result> output = target.run(parse);

        assertThat(output).isNotEmpty();
        assertThat(output.get(0).getAfter().printAll()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <artifactId>test-project</artifactId>\n" +
                "    <groupId>org.my.test</groupId>\n" +
                "    <version>1.0.0-SNAPSHOT</version>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>repository.spring.milestone</id>\n" +
                "            <name>Spring Milestone Repository</name>\n" +
                "            <url>http://repo.spring.io/milestone</url>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter</artifactId>\n" +
                "            <version>3.0.0-M3</version>\n" +
                "        </dependency>    </dependencies></project>");
    }
}
