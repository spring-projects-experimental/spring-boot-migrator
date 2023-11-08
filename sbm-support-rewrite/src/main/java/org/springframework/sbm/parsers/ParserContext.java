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
package org.springframework.sbm.parsers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ParserContext {

    private final Path baseDir;
    @Getter
    private final List<Resource> resources;
    @Getter
    private final List<MavenProject> sortedProjects;
    @Getter
    private Map<Path, Xml.Document> pathDocumentMap;



    public List<String> getActiveProfiles() {
        // FIXME: Add support for Maven profiles
        return List.of("default");
    }

    public Resource getMatchingBuildFileResource(MavenProject pom) {
        return resources.stream()
                .filter(r -> ResourceUtil.getPath(r).toString().equals(pom.getPomFilePath().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find a resource in the list of resources that matches the path of MavenProject '%s'".formatted(pom.getPomFile().toString())));
    }

    public List<Resource> getBuildFileResources() {
        return sortedProjects.stream()
                .map(p -> p.getPomFile())
                .toList();
    }

    public Xml.Document getXmlDocument(Path path) {
        return pathDocumentMap.get(path);
    }

    public void setParsedBuildFiles(List<Xml.Document> xmlDocuments) {
        this.pathDocumentMap = xmlDocuments.stream()
                .peek(doc -> addSourceFileToModel(baseDir, getSortedProjects(), doc))
                .collect(Collectors.toMap(doc -> baseDir.resolve(doc.getSourcePath()), doc -> doc));
    }

    public List<Xml.Document> getSortedBuildFileDocuments() {
        return getSortedProjects().stream().map(p -> pathDocumentMap.get(p.getFile().toPath())).toList();
    }

    private void addSourceFileToModel(Path baseDir, List<MavenProject> sortedProjectsList, Xml.Document s) {
        sortedProjectsList.stream()
                .filter(p -> ResourceUtil.getPath(p.getPomFile()).toString().equals(baseDir.resolve(s.getSourcePath()).toString()))
                .forEach(p -> p.setSourceFile(s));
    }

}
