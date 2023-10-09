/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.boot.properties.api;


import org.junit.jupiter.api.Test;
import org.openrewrite.SourceFile;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.springframework.sbm.parsers.RewriteExecutionContext;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootApplicationPropertiesTest {

    @Test
    void createNewProperties_Add_Print() {
        SpringBootApplicationProperties sut = SpringBootApplicationProperties.newApplicationProperties(Path.of("./projectDir").toAbsolutePath(), Path.of("./fake2.properties"), new RewriteExecutionContext());
        sut.setProperty("some", "property");
        sut.setProperty("another", "foo");
        assertThat(sut.print()).isEqualTo("some=property\n" +
                "another=foo");
    }

    @Test
    void parseExistingPropertiesTest() {
        List<SourceFile> parse = new PropertiesParser()
                .parse(
                    """
                    foo=bar
                    bob=bill
                    """
                )
                .toList();
        Path absolutePath = Path.of("./projectDir").toAbsolutePath();
        Properties.File propertiesFile = (Properties.File) parse.get(0);
        SpringBootApplicationProperties sut = new SpringBootApplicationProperties(absolutePath, propertiesFile, new RewriteExecutionContext());
        assertThat(sut.getProperty("foo").get()).isEqualTo("bar");
        assertThat(sut.getProperty("bob").get()).isEqualTo("bill");
        assertThat(sut.getProperty("jane")).isEmpty();
    }

}