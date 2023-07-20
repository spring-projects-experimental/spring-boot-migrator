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

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.git.Commit;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.project.RewriteSourceFileWrapper;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectContextInitializer {

    private final ProjectContextFactory projectContextFactory;
    private final MavenProjectParser mavenProjectParser;
    private final GitSupport gitSupport;
    private final RewriteSourceFileWrapper rewriteSourceFileWrapper;
    private final ExecutionContext executionContext;

    public ProjectContext initProjectContext(Path projectDir, List<Resource> resources) {
        final Path absoluteProjectDir = projectDir.toAbsolutePath().normalize();
        // TODO: remove git initialization, handled by precondition check
        initializeGitRepoIfNoneExists(absoluteProjectDir);

        List<SourceFile> parsedResources = mavenProjectParser.parse(absoluteProjectDir, resources);
        List<RewriteSourceFileHolder<? extends SourceFile>> rewriteSourceFileHolders = rewriteSourceFileWrapper.wrapRewriteSourceFiles(absoluteProjectDir, parsedResources);

        ProjectResourceSet projectResourceSet = new ProjectResourceSet(rewriteSourceFileHolders, executionContext);
        ProjectContext projectContext = projectContextFactory.createProjectContext(projectDir, projectResourceSet);

        storeGitCommitHash(projectDir, projectContext);

        return projectContext;
    }

    public void storeGitCommitHash(Path projectDir, ProjectContext projectContext) {
        if (gitSupport.repoExists(projectDir.toFile())) {
            Optional<Commit> latestCommit = gitSupport.getLatestCommit(projectDir.toFile());
            String latestHash = null;
            if (latestCommit.isPresent()) {
                latestHash = latestCommit.get().getHash();
            }
            projectContext.setRevision(latestHash);
        }
    }

    void initializeGitRepoIfNoneExists(Path absoluteProjectDir) {
        if (!gitSupport.repoExists(absoluteProjectDir.toFile())) {
            gitSupport.initGit(absoluteProjectDir.toFile());
        }
    }

}
