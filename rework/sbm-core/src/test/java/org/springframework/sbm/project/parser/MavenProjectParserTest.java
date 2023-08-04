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
package org.springframework.sbm.project.parser;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import java.util.List;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.springframework.sbm.build.util.PomBuilder;

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