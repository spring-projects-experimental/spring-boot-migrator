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
package org.springframework.sbm.openrewrite.maven;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.UpgradeParentVersion;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BumpParentVersionTest {

    @Test
    void bumpedParentPomVersionIsNotReflectedInModelTest() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <parent>\n" +
                        "    <groupId>org.springframework.boot</groupId>\n" +
                        "    <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "    <version>2.7.0</version>\n" +
                        "  </parent>\n" +
                        "  <groupId>com.example</groupId>\n" +
                        "  <artifactId>artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <repositories>\n" +
                        "    <repository>\n" +
                        "      <id>spring-milestone</id>\n" +
                        "      <name>spring-milestone</name>" +
                        "      <url>https://repo.spring.io/milestone</url>\n" +
                        "    </repository>\n" +
                        "    <repository>\n" +
                        "      <id>jcenter</id>\n" +
                        "      <name>jcenter</name>\n" +
                        "      <url>https://jcenter.bintray.com</url>\n" +
                        "    </repository>\n" +
                        "  </repositories>\n" +
                        "</project>";

        List<Xml.Document> parsedPom = MavenParser.builder().build().parse(pomXml);

        List<Result> results = new UpgradeParentVersion("org.springframework.boot", "spring-boot-starter-parent", "3.0.0-M3", null).run(parsedPom);
        assertThat(results.get(0).getAfter().printAll()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <parent>\n" +
                        "    <groupId>org.springframework.boot</groupId>\n" +
                        "    <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "    <version>3.0.0-M3</version>\n" +
                        "  </parent>\n" +
                        "  <groupId>com.example</groupId>\n" +
                        "  <artifactId>artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <repositories>\n" +
                        "    <repository>\n" +
                        "      <id>spring-milestone</id>\n" +
                        "      <name>spring-milestone</name>      <url>https://repo.spring.io/milestone</url>\n" +
                        "    </repository>\n" +
                        "    <repository>\n" +
                        "      <id>jcenter</id>\n" +
                        "      <name>jcenter</name>\n" +
                        "      <url>https://jcenter.bintray.com</url>\n" +
                        "    </repository>\n" +
                        "  </repositories>\n" +
                        "</project>"
        );

        // If this fails because 3.0.0-M3 is reflected in the model the RefreshPomModel could be removed
        assertThat(results.get(0).getAfter().getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getRequested().getParent().getVersion()).isEqualTo("3.0.0-M3");
    }

    @Test
    @Disabled("Fails sometimes (and on GitHub) as 'Unable to download dependency io.micrometer:micrometer-bom:1.10.0-M2'")
    void resolveMilestoneParentVersion() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <parent>\n" +
                        "    <groupId>org.springframework.boot</groupId>\n" +
                        "    <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "    <version>3.0.0-M3</version>\n" +
                        "  </parent>\n" +
                        "  <groupId>com.example</groupId>\n" +
                        "  <artifactId>artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <repositories>\n" +
                        "    <repository>\n" +
                        "      <id>spring-milestone</id>\n" +
                        "      <name>spring-milestone</name>\n" +
                        "      <url>https://repo.spring.io/milestone</url>\n" +
                        "    </repository>\n" +
                        "    <repository>\n" +
                        "      <id>jcenter</id>\n" +
                        "      <name>jcenter</name>\n" +
                        "      <url>https://jcenter.bintray.com</url>\n" +
                        "    </repository>" +
                        "  </repositories>\n" +
                        "</project>";
        List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);
        Optional<MavenResolutionResult> first = parse.get(0).getMarkers().findFirst(MavenResolutionResult.class);
        assertThat(first.get().getPom().getRequested().getParent().getVersion()).isEqualTo("3.0.0-M3");
    }

}
