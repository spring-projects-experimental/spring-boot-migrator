package org.springframework.sbm.project.parser;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.util.PomBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
class MavenProjectParserTest {

    @Test
    void testSort() {
        String parentPom = PomBuilder.buildPom("com.example:parent:0.1")
                .packaging("pom")
                .withModules("moduleA")
                .withProperties(Map.of("some-property", "value1"))
                .build();
        String moduleA = PomBuilder.buildPom("com.example:parent:0.1", "moduleA").build();
        List<Xml.Document> poms = MavenParser.builder().build().parse(parentPom, moduleA);
        List<Xml.Document> sortedPoms = MavenProjectParser.sort(poms);
        assertThat(sortedPoms.get(0).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getArtifactId()).isEqualTo("parent");
        assertThat(sortedPoms.get(1).getMarkers().findFirst(MavenResolutionResult.class).get().getPom().getArtifactId()).isEqualTo("moduleA");
    }

}