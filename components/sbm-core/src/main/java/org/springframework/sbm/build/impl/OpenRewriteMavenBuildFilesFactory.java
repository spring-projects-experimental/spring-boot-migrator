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
package org.springframework.sbm.build.impl;

import lombok.RequiredArgsConstructor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.BuildFilesFactory;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Deprecated
public class OpenRewriteMavenBuildFilesFactory implements BuildFilesFactory {

    public static final String BUILD_FILE = "pom.xml";

    private final ApplicationEventPublisher eventPublisher;
    private final JavaParser javaParser;
    // FIXME: wrap parsers? -> RewriteMavenParser
    private final MavenParser parser = MavenParser.builder().build();

    @Override
    public List<OpenRewriteMavenBuildFile> applyProjections(Path absoluteProjectDir, ProjectResourceSet projectResources) {
        RewriteExecutionContext executionContext = new RewriteExecutionContext(eventPublisher);
        List<OpenRewriteMavenBuildFile> result = new ArrayList<>();
        for (int i = 0; i < projectResources.size(); i++) {
            ProjectResource projectResource = projectResources.get(i);
            if (projectResource.getAbsolutePath().endsWith(BUILD_FILE)) {
                List<Xml.Document> mavenPoms = parser.parse(List.of(projectResource.getAbsolutePath()), null, executionContext);
                Xml.Document mavenPom = mavenPoms.get(0);
                OpenRewriteMavenBuildFile buildFile = new OpenRewriteMavenBuildFile(absoluteProjectDir, mavenPom, eventPublisher, javaParser, executionContext);
                projectResources.replace(i, buildFile);
                result.add(buildFile);
            }
        }
        return result;
    }
}
