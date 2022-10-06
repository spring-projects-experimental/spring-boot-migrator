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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MavenConfigParserTest {

    private final MavenConfigParser target = new MavenConfigParser();

    @Test
    public void doesNotErrorOnNullOrEmpty() {

        assertThat(target.parse(null)).isEmpty();
        assertThat(target.parse(new ArrayList<>())).isEmpty();
    }

    @Test
    public void parsesSingleMavenConfig() {

        List<String> mavenConfigFile = List.of("""
                -Drevision=1.0.0
                -Dlicense.projectName=projectName
                 
                 helloworld=hello
                 
                """);

        Map<String, String> output = target.parse(mavenConfigFile);

        assertThat(output).hasSize(2);
        assertThat(output.get("revision")).isEqualTo("1.0.0");
        assertThat(output.get("license.projectName")).isEqualTo("projectName");
    }

    @Test
    public void shouldParseMultipleFiles() {
        List<String> mavenConfigFile = List.of("""
                -Drevision=1.0.0
                -Dlicense.projectName=projectName
                """, """
                -Dtest=123
                """);

        Map<String, String> output = target.parse(mavenConfigFile);

        assertThat(output).hasSize(3);
        assertThat(output.get("revision")).isEqualTo("1.0.0");
        assertThat(output.get("license.projectName")).isEqualTo("projectName");
        assertThat(output.get("test")).isEqualTo("123");
    }

    @Test
    public void shouldNotErrorWhenMavenConfigIsWrong()  {

        List<String> mavenConfigFile = List.of("""
                -Once upon a time there was a handsome prince
                who used java and died
                -Dhello
                """);

        Map<String, String> output = target.parse(mavenConfigFile);

        assertThat(output).hasSize(0);
    }
}
