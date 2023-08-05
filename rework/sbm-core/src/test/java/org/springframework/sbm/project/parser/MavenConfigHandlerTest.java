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
import org.springframework.core.io.Resource;
import org.springframework.sbm.project.TestDummyResource;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MavenConfigHandlerTest {

    private final MavenConfigHandler target = new MavenConfigHandler();

    @Test
    public void shouldNotErrorWhenMavenConfigIsWrong() {

        String contents = """
                -Horror Story:,
                A developer used PHP,
                ** Scary music Intensifies **
                """;

        int propertySizeBefore = System.getProperties().keySet().size();
        List<Resource> mavenConfigResource = List.of(new TestDummyResource(Path.of(".mvn/maven.config"), contents));
        target.injectMavenConfigIntoSystemProperties(mavenConfigResource);
        int propertySizeAfter = System.getProperties().keySet().size();

        assertThat(propertySizeAfter).isEqualTo(propertySizeBefore);
    }

    @Test
    public void parsesMavenConfig() {

        String contents = """
                -Drevision = 1.0.0
                -Dlicense.projectName=projectName
                helloworld=hello
                """;


        target.injectMavenConfigIntoSystemProperties(List.of(new TestDummyResource(Path.of(".mvn/maven.config"), contents)));

        assertThat(System.getProperty("revision")).isEqualTo("1.0.0");
        assertThat(System.getProperty("license.projectName")).isEqualTo("projectName");
        assertThat(System.getProperty("helloworld")).isNull();
    }
}
