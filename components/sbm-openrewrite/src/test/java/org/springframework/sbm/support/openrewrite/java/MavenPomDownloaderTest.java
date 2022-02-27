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
package org.springframework.sbm.support.openrewrite.java;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.maven.internal.MavenPomDownloader;
import org.openrewrite.maven.internal.RawMaven;
import org.openrewrite.maven.tree.MavenRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenPomDownloaderTest {
    @SneakyThrows
    @Test
    void downloadDbcp2_issue130() throws IOException {

        String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.springframework.sbm.examples</groupId>\n" +
                "    <artifactId>jee-app</artifactId>\n" +
                "    <packaging>jar</packaging>\n" +
                "    <version>8.0.5-SNAPSHOT</version>\n" +
                "    <name>TomEE :: Examples :: JPA with Hibernate</name>\n" +
                "    <properties>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "    </properties>\n" +
                "    <repositories>\n" +
                "        <repository>\n" +
                "            <id>jcenter</id>\n" +
                "            <name>jcenter</name>\n" +
                "            <url>https://jcenter.bintray.com</url>\n" +
                "        </repository>\n" +
                "        <repository>\n" +
                "            <id>mavencentral</id>\n" +
                "            <name>mavencentral</name>\n" +
                "            <url>https://repo.maven.apache.org/maven2</url>\n" +
                "        </repository>\n" +
                "    </repositories>\n" +
                "    <build>\n" +
                "        <defaultGoal>install</defaultGoal>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.5.1</version>\n" +
                "                <configuration>\n" +
                "                    <source>1.8</source>\n" +
                "                    <target>1.8</target>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.tomitribe.transformer</groupId>\n" +
                "                <artifactId>org.eclipse.transformer.maven</artifactId>\n" +
                "                <version>0.1.1a</version>\n" +
                "                <configuration>\n" +
                "                    <classifier>jakartaee9</classifier>\n" +
                "                </configuration>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <goals>\n" +
                "                            <goal>run</goal>\n" +
                "                        </goals>\n" +
                "                        <phase>package</phase>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <artifactId>maven-war-plugin</artifactId>\n" +
                "                <version>3.2.3</version>\n" +
                "                <configuration>\n" +
                "                    <failOnMissingWebXml>false</failOnMissingWebXml>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.tomee.maven</groupId>\n" +
                "                <artifactId>tomee-maven-plugin</artifactId>\n" +
                "                <version>8.0.1</version>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.apache.tomee</groupId>\n" +
                "            <artifactId>javaee-api</artifactId>\n" +
                "            <version>[8.0,)</version>\n" +
                "            <scope>provided</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>junit</groupId>\n" +
                "            <artifactId>junit</artifactId>\n" +
                "            <version>4.12</version>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "        <!-- openejb (with hibernate) container for running tests -->\n" +
                "        <!--            TODO: reactivate, see https://rewriteoss.slack.com/archives/D01BT0964EN/p1614080476004100-->\n" +
//                "        <dependency>\n" +
//                "            <groupId>org.apache.tomee</groupId>\n" +
//                "            <artifactId>openejb-core-hibernate</artifactId>\n" +
//                "            <version>8.0.5</version>\n" +
//                "            <type>pom</type>\n" +
//                "        </dependency\n" +
                "        <dependency>\n" +
                "            <groupId>org.hibernate</groupId>\n" +
                "            <artifactId>hibernate-core</artifactId>\n" +
                "            <version>5.0.5.Final</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.ejb</groupId>\n" +
                "            <artifactId>javax.ejb-api</artifactId>\n" +
                "            <version>3.2</version>\n" +
                "            <!--            <scope>provided</scope>-->\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.enterprise</groupId>\n" +
                "            <artifactId>cdi-api</artifactId>\n" +
                "            <version>1.2</version>\n" +
                "            <scope>provided</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.enterprise.concurrent</groupId>\n" +
                "            <artifactId>javax.enterprise.concurrent-api</artifactId>\n" +
                "            <version>1.0</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.jms</groupId>\n" +
                "            <artifactId>javax.jms-api</artifactId>\n" +
                "            <version>2.0</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.json</groupId>\n" +
                "            <artifactId>javax.json-api</artifactId>\n" +
                "            <version>1.0</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.servlet</groupId>\n" +
                "            <artifactId>javax.servlet-api</artifactId>\n" +
                "            <version>4.0.1</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.validation</groupId>\n" +
                "            <artifactId>validation-api</artifactId>\n" +
                "            <version>1.1.0.Final</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.ws.rs</groupId>\n" +
                "            <artifactId>javax.ws.rs-api</artifactId>\n" +
                "            <version>2.1.1</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.hibernate.javax.persistence</groupId>\n" +
                "            <artifactId>hibernate-jpa-2.1-api</artifactId>\n" +
                "            <version>1.0.0.Final</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.jboss.spec.javax.annotation</groupId>\n" +
                "            <artifactId>jboss-annotations-api_1.3_spec</artifactId>\n" +
                "            <version>1.0.1.Final</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.jboss.spec.javax.ws.rs</groupId>\n" +
                "            <artifactId>jboss-jaxrs-api_2.1_spec</artifactId>\n" +
                "            <version>1.0.1.Final</version>\n" +
                "            <scope>runtime</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>\n";

        ExceptionHolder exceptionHolder = new ExceptionHolder();
        Consumer<Throwable> onError = (e) -> {
            e.printStackTrace();
            exceptionHolder.setException(e);
        };
        ExecutionContext executionContext = new InMemoryExecutionContext(onError);
        MavenPomCache cache = new InMemoryMavenPomCache();
        Parser.Input input = Parser.Input.fromString(pom);
        RawMaven rawMaven = RawMaven.parse(input, null, null, executionContext);
        Map<Path, RawMaven> projectPoms = new HashMap<>();
        Path pomPath = Path.of("./pom.xml");
        projectPoms.put(pomPath, rawMaven);


        MavenPomDownloader mavenPomDownloader = new MavenPomDownloader(cache, projectPoms, executionContext);
        String groupId = "org.apache.commons";
        String artifactId = "commons-dbcp2";
        String version = "2.8.0";
        @Nullable String relativePath = "";
        @Nullable RawMaven containingPom = projectPoms.get(pomPath);
        Collection<MavenRepository> repositories = List.of();
        mavenPomDownloader.download(groupId, artifactId, version, relativePath, containingPom, repositories, executionContext);

        // verify resource exists
        URL url = new URL("https://repo.maven.apache.org/maven2/org/apache/commons/commons-dbcp2/2.8.0/commons-dbcp2-2.8.0.jar");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        int responseCode = huc.getResponseCode();
        assertThat(HttpURLConnection.HTTP_OK).isEqualTo(responseCode);
    }

    @Getter
    @Setter
    class ExceptionHolder {
        Throwable exception;
    }
}
