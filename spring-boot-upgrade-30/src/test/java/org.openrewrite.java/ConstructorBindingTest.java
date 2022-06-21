package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.cache.LocalMavenArtifactCache;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Brain dump
 * TODO:
 *  * Source can be 11 and destination must be 17 separate test for that ?
 */
public class ConstructorBindingTest {

    @Test
    void newGeneratedClassRemovesConstructorBinding() {

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

        String pom2_7 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.example</groupId>\n" +
                "    <artifactId>demo</artifactId>\n" +
                "    <version>0.0.1-SNAPSHOT</version>\n" +
                "    <name>demo</name>\n" +
                "    <description>Demo project for Spring Boot</description>\n" +
                "    <properties>\n" +
                "        <java.version>11</java.version>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot</artifactId>\n" +
                "            <version>2.7.0</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>";

        String source = "" +
                "package com.example.config.demo;\n" +
                "\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "import org.springframework.boot.context.properties.ConstructorBinding;\n" +

                "@ConfigurationProperties(prefix = \"mail\")\n" +
                "@ConstructorBinding\n" +
                "public class ConfigProperties {\n" +
                "    private String hostName;\n" +
                "    private int port;\n" +
                "    private String from;\n" +
                "\n" +
                "    public ConfigProperties(String hostName, int port, String from) {\n" +
                "        this.hostName = hostName;\n" +
                "        this.port = port;\n" +
                "        this.from = from;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getHostName() {\n" +
                "        return hostName;\n" +
                "    }\n" +
                "\n" +
                "    public int getPort() {\n" +
                "        return port;\n" +
                "    }\n" +
                "\n" +
                "    public String getFrom() {\n" +
                "        return from;\n" +
                "    }\n" +
                "}";


        Xml.Document document = MavenParser.builder().build().parse(pom2_7).get(0);
        Optional<MavenResolutionResult> mavenResolutionResult =
                document.getMarkers().findFirst(MavenResolutionResult.class);

        List<ResolvedDependency> resolvedDependencies = mavenResolutionResult.get()
                .getDependencies().get(Scope.Provided);

        MavenArtifactDownloader mavenArtifactDownloader = new MavenArtifactDownloader(
                new LocalMavenArtifactCache(
                        Path.of(System.getProperty("user.home")).resolve(".m2/repository")
                ),
                null,
                Throwable::printStackTrace
        );

        List<Path> listOfDependencies = resolvedDependencies
                .stream()
                .map(mavenArtifactDownloader::downloadArtifact)
                .collect(Collectors.toList());


        List<J.CompilationUnit> parse = Java17Parser.builder()
                .classpath(listOfDependencies)
                .build().parse(ctx, source);


        String recipeName = "org.openrewrite.java.spring.boot3.data.java.constructorbinding";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(parse, ctx);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo("package com.example.config.demo;\n" +
                "\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "@ConfigurationProperties(prefix = \"mail\")\n" +
                "public class ConfigProperties {\n" +
                "    private String hostName;\n" +
                "    private int port;\n" +
                "    private String from;\n" +
                "\n" +
                "    public ConfigProperties(String hostName, int port, String from) {\n" +
                "        this.hostName = hostName;\n" +
                "        this.port = port;\n" +
                "        this.from = from;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getHostName() {\n" +
                "        return hostName;\n" +
                "    }\n" +
                "\n" +
                "    public int getPort() {\n" +
                "        return port;\n" +
                "    }\n" +
                "\n" +
                "    public String getFrom() {\n" +
                "        return from;\n" +
                "    }\n" +
                "}");
    }
}
