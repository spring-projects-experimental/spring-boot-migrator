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
package org.springframework.sbm.xml.parser;

import org.openrewrite.SourceFile;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.ExecutionContext;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RewriteXmlParser extends XmlParser {

    private static final List<String> FILE_ENDINGS_PARSED_AS_XML = List.of(".xsl", ".xslt", ".xml", ".xhtml", ".xsd", ".wsdl");

    private final XmlParser delegatingParser;

    public RewriteXmlParser() {
        this.delegatingParser = new XmlParser() {
            @Override
            public boolean accept(Path path) {
                String filename = path.getFileName().toString();
                String fileEnding = filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : "";
                return FILE_ENDINGS_PARSED_AS_XML.contains(fileEnding);
            }
        };
    }

    public List<RewriteSourceFileHolder<Xml.Document>> parse(List<Path> xmlFiles, Path projectDir, ExecutionContext executionContext) {
        return delegatingParser.parse(xmlFiles, projectDir, executionContext)
                .map(Xml.Document.class::cast)
                .map(pt -> wrapRewriteSourceFile(projectDir, pt))
//                .map(plainText -> addMarkers(projectDir, rewriteProjectResources, plainText))
                .collect(Collectors.toList());
    }

    public RewriteSourceFileHolder<Xml.Document> parse(Path projectDir, Path sourcePath, String xml) {
        Xml.Document parse = delegatingParser.parse(xml).toList().get(0).withSourcePath(sourcePath);
        return wrapRewriteSourceFile(projectDir, parse);
    }

    public boolean shouldBeParsedAsXml(Resource resource) {
        return accept(getPath(resource));
    }

    @Override
    public boolean accept(Path path) {
        String pathStr = path.toString();
        String fileEnding = pathStr.contains(".") ? pathStr.substring(pathStr.lastIndexOf(".")) : "";
        return FILE_ENDINGS_PARSED_AS_XML.contains(fileEnding);
    }

    @Override
    public boolean accept(Input input) {
        return accept(input.getPath());
    }

    // FIXME: duplicated in ProjectParserHelper
    private RewriteSourceFileHolder<Xml.Document> wrapRewriteSourceFile(Path absoluteProjectDir, Xml.Document sourceFile) {
        RewriteSourceFileHolder<Xml.Document> rewriteSourceFileHolder = new RewriteSourceFileHolder<Xml.Document>(absoluteProjectDir, sourceFile);
        return rewriteSourceFileHolder;
    }

    // TODO: duplicate, move to helper
    private static Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
