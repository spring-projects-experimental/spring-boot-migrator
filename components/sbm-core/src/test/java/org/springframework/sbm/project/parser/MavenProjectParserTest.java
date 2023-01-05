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

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.Parser;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
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


        List<Xml.Document> parsed = MavenParser.builder().build().parseInputs(parserInputs, null, new RewriteExecutionContext());

        List<Xml.Document> sort = MavenProjectParser.sort(parsed);
    }
}