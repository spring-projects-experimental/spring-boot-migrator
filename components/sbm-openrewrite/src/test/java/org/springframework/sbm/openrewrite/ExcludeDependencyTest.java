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
package org.springframework.sbm.openrewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.RecipeRun;
import org.openrewrite.maven.ExcludeDependency;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("#7")
public class ExcludeDependencyTest {

    @Test
    void test() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "         xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies>\n" +
                        "    <dependency>\n" +
                        "      <groupId>org.junit.jupiter</groupId>\n" +
                        "      <artifactId>junit-jupiter-engine</artifactId>\n" +
                        "      <version>5.7.0</version>\n" +
                        "      <scope>test</scope>" +
                        "    </dependency>\n" +
                        "  </dependencies>\n" +
                        "</project>\n";

        Xml.Document maven = MavenParser.builder().build().parse(pomXml).get(0);
        ExcludeDependency excludeDependency = new ExcludeDependency("org.junit.jupiter", "junit-jupiter-api", "test");
        RecipeRun run = excludeDependency.run(List.of(maven));
        assertThat(run.getResults().get(0).getAfter().printAll()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "         xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies>\n" +
                        "    <dependency>\n" +
                        "      <groupId>org.junit.jupiter</groupId>\n" +
                        "      <artifactId>junit-jupiter-engine</artifactId>\n" +
                        "      <version>5.7.0</version>\n" +
                        "      <scope>test</scope>\n" +
                        "      <exclusions>\n" +
                        "        <exclusion>\n" +
                        "          <groupId>org.junit.jupiter</groupId>\n" +
                        "          <artifactId>junit-jupiter-api</artifactId>\n" +
                        "        </exclusion>\n" +
                        "      </exclusions>\n" +
                        "</dependency>\n" + // TODO: #7 formatting broken
                        "  </dependencies>\n" +
                        "</project>\n"
        );

    }
}
