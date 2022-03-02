package org.springframework.sbm.test;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import static org.assertj.core.api.Assertions.assertThat;

public class MavenParentPomTest {

    @Test
    void getParentPom() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.4.12</version>\n" +
                        "        <relativePath/> <!-- lookup parent from repository -->\n" +
                        "    </parent>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                        "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "</project>\n";

        Xml.Document maven = MavenParser.builder().build().parse(pomXml).get(0);
        MavenResolutionResult resolutionResult = maven.getMarkers().findFirst(MavenResolutionResult.class).get();

        assertThat(resolutionResult.getPom().getRequested().getParent()).isNotNull();
        assertThat(resolutionResult.getPom().getRequested().getParent().getGroupId()).isEqualTo("org.springframework.boot");
        assertThat(resolutionResult.getPom().getRequested().getParent().getArtifactId()).isEqualTo("spring-boot-starter-parent");
        assertThat(resolutionResult.getPom().getRequested().getParent().getVersion()).isEqualTo("2.4.12");
    }
}
