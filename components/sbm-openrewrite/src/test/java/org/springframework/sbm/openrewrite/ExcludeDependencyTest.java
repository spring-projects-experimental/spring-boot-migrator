package org.springframework.sbm.openrewrite;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.maven.ExcludeDependency;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;

import java.util.List;

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
//                        "    <dependency>\n" +
//                        "      <groupId>org.junit.jupiter</groupId>\n" +
//                        "      <artifactId>junit-jupiter-api</artifactId>\n" +
//                        "      <version>5.7.0</version>" +
//                        "      <scope>test</scope>\n" +
//                        "    </dependency>\n" +
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
        List<Result> run = excludeDependency.run(List.of(maven));
        System.out.println(run.get(0).getAfter().printAll());

    }
}
