package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.java.tree.J;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

public class SomeTest {

    @Test
    void test() {
        JavaParser javaParser = JavaParser.fromJavaVersion().build();
        String javaCode = "public class Banana {}";
        List<J.CompilationUnit> parse = javaParser.parse(javaCode);
        String recipeName = "";
        Result result = RewriteTest.fromRuntimeClasspath(recipeName)
                .run(parse)
                .get(0);


    }

    @Test
    void testPom() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.springframework.sbm</groupId>\n" +
                "    <artifactId>spring-boot-upgrades-30</artifactId>\n" +
                "    <version>0.1.0-SNAPSHOT</version>\n" +
                "    <packaging>jar</packaging>\n" +
                "</project>";
        List<Xml.Document> parse = MavenParser.builder().build().parse(pomXml);
        List<Result> run = new AddMavenRepository(RepositoryDefinition.builder()
                .id("spring-milestone")
                .url("some.url")
                .name("spring-milestone")
                .build())
                .run(parse);

        System.out.println(run.get(0).getAfter().printAll());
    }

}
