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
package org.springframework.sbm.conditions.xml;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class XmlFileContainingTest {

    @Test
    void shouldReturnTrueIfAnyXmlFileContainsValue() {

        String xmlFile1 = "<xml>theValue</xml>";
        String someText = "I am the text...";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("src/main/resources/to/xmlFile.xml", xmlFile1)
                .withProjectResource("src/main/resources/path/to/sometextFile.txt", someText)
                .build();

        XmlFileContaining sut = new XmlFileContaining();
        sut.setValue("theVal");

        boolean foundXmlFileContainingValue = sut.evaluate(projectContext);

        assertThat(foundXmlFileContainingValue).isTrue();
    }

    @Test
    void shouldReturnFalseIfNoXmlFileExists() {

        String someText = "I am the text...";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("abosolute/path/to/sometextFile.txt", someText)
                .build();

        XmlFileContaining sut = new XmlFileContaining();
        sut.setValue("theVal");

        assertThat(sut.evaluate(projectContext)).isFalse();
    }

    @Test
    void shouldReturnTrueIfNoXmlFileContainsValue() {

        String xmlFile1 = "<xml>theValue</xml>";
        String someText = "I am the text...";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("src/main/resources/path/to/xmlFile.xml", xmlFile1)
                .withProjectResource("src/main/resources/path/to/sometextFile.txt", someText)
                .build();

        assertThat(projectContext.getProjectResources().list()).hasSize(3); // pom.xml was also added

        XmlFileContaining sut = new XmlFileContaining();
        sut.setValue("I am the text...");

        assertThat(sut.evaluate(projectContext)).isFalse();
    }
}