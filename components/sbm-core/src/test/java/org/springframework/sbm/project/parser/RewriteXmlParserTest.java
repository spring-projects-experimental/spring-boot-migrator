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

import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.xml.parser.RewriteXmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RewriteXmlParserTest {

    private RewriteXmlParser sut;

    @BeforeEach
    void beforeEach() {
        sut = new RewriteXmlParser();
    }

    @Test
    void parse() {
        String xml = "<foo>content</foo>";
        RewriteSourceFileHolder<Xml.Document> parse = sut.parse(Path.of(".").toAbsolutePath(), Path.of("some.xml"), xml);
        assertThat(parse.getSourceFile().getRoot().getName()).isEqualTo("foo");
        assertThat(parse.getSourceFile().getRoot().getValue().get()).isEqualTo("content");
    }

    @Test
    void testParse() {
        Path file = Path.of("testcode/module1/src/main/resources/some.xml").toAbsolutePath();
        List<RewriteSourceFileHolder<Xml.Document>> rewriteSourceFileHolders = sut.parse(List.of(file), Path.of("./testcode").toAbsolutePath().normalize(), new RewriteExecutionContext());
        RewriteSourceFileHolder<Xml.Document> sourceFileHolder = rewriteSourceFileHolders.get(0);
        assertThat(sourceFileHolder.getSourceFile().getRoot().getName()).isEqualTo("shiporder");
        assertThat(sourceFileHolder.getSourceFile().getRoot().getChild("orderperson").get().getValue().get()).isEqualTo("John Smith");
    }

    @Test
    void shouldBeParsedAsXml() {
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.xml"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.xsl"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.xslt"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.xhtml"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.xsd"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.wsdl"), ""))).isTrue();
        assertThat(sut.shouldBeParsedAsXml(new TestDummyResource(Path.of("file.txt"), ""))).isFalse();
    }
}