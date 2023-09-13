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
package org.springframework.sbm.support.openrewrite.xml;

import org.openrewrite.SourceFile;
import org.springframework.sbm.GitHubIssue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlParserTest {

    @GitHubIssue("https://github.com/openrewrite/rewrite/issues/1259")
    @Test
    @Disabled("Experienced in 7.16.3")
    void parseXhtml() {
        String xhtml =
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                        "<!DOCTYPE html>\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                        "      xmlns:h=\"http://xmlns.jcp.org/jsf/html\"\n" +
                        "      xmlns:ui=\"http://xmlns.jcp.org/jsf/facelets\">\n" +
                        "    <head>\n" +
                        "        <title>Index</title>\n" +
                        "    </head>\n" +
                        "    <body>\n" +
                        "        <ui:composition template=\"./WEB-INF/templates/template.xhtml\">\n" +
                        "            <ui:define name=\"title\">JSF 2.2 Reference Implementation (Mojarra) Examples</ui:define>\n" +
                        "            <ui:define name=\"content\">\n" +
                        "                <h:panelGroup id=\"panel\">\n" +
                        "                    <ui:fragment>\n" +
                        "                        <ui:include src=\"/#{indexBean.src}.xhtml\"/>\n" +
                        "                    </ui:fragment>\n" +
                        "                </h:panelGroup>\n" +
                        "            </ui:define>\n" +
                        "        </ui:composition>\n" +
                        "    </body>\n" +
                        "</html>\n";

        Stream<SourceFile> documents = new XmlParser().parseInputs(
                List.of(new Parser.Input(Path.of("./foo.xhtml").toAbsolutePath(), () -> new ByteArrayInputStream(xhtml.getBytes(StandardCharsets.UTF_8)))),
                Path.of(".").toAbsolutePath(),
                new InMemoryExecutionContext((e) -> e.printStackTrace()));

        assertThat(documents).hasSize(1);
    }

}
