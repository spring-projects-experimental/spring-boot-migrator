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
package org.springframework.sbm.build.api;

import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.StringProjectResource;
import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;

import java.nio.file.Path;

@RequiredArgsConstructor
public class ResourceSet {

    private final ProjectResourceSet projectResourceSet;
    private final Path projectRoot;
    private final Path modulePath;
    private final Path resourceSetPath;

    public void addStringResource(String filePath, String content) {
        Path absFilePath = getAbsolutePath().resolve(filePath);
        StringProjectResource resource = new StringProjectResource(projectRoot, absFilePath, content);
        resource.markAsChanged();
        projectResourceSet.add(resource);
    }

    public void addResource(RewriteSourceFileHolder<? extends SourceFile> resource) {
        resource.markChanged();
        projectResourceSet.add(resource);
    }

    public Path getAbsolutePath() {
        return projectRoot.resolve(resourceSetPath);
    }
}
