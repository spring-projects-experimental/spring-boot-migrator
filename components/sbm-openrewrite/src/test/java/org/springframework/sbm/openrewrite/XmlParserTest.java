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
package org.springframework.sbm.openrewrite;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlParserTest {

    @Test
    @Disabled("Fails in Rewrite 7.16.3")
    void parseXhtml() {
        String xml =
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
                "\n" +
                "            <ui:define name=\"content\">\n" +
                "                <h:panelGroup id=\"panel\">\n" +
                "                    <ui:fragment>\n" +
                "                        <ui:include src=\"/#{indexBean.src}.xhtml\"/>\n" +
                "                    </ui:fragment>\n" +
                "                </h:panelGroup>\n" +
                "            </ui:define>\n" +
                "\n" +
                "        </ui:composition>\n" +
                "    </body>\n" +
                "</html>";

        XmlParser xmlParser = new XmlParser();
        Iterable<Parser.Input> inputs = Stream.of(new Parser.Input(Path.of("dummy-dir/index.xhtml").toAbsolutePath(), () -> new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))).collect(Collectors.toList());
        List<Xml.Document> parse = xmlParser.parseInputs(inputs, null, new InMemoryExecutionContext((t) -> t.printStackTrace()));
        System.out.println(parse.get(0).printAll());
    }
}
