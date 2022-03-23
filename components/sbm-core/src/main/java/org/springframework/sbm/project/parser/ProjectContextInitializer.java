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
import org.openrewrite.SourceFile;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.git.Commit;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectContextInitializer {

    private final ProjectContextFactory projectContextFactory;
    private final RewriteMavenParserFactory rewriteMavenParserFactory;
    private final GitSupport gitSupport;

    public ProjectContext initProjectContext(Path projectDir, List<Resource> resources, RewriteExecutionContext rewriteExecutionContext) {
        final Path absoluteProjectDir = projectDir.toAbsolutePath().normalize();

        initializeGitRepoIfNoneExists(absoluteProjectDir);
        MavenProjectParser mavenProjectParser = rewriteMavenParserFactory.createRewriteMavenParser(absoluteProjectDir, rewriteExecutionContext);

        List<SourceFile> parsedResources = mavenProjectParser.parse(absoluteProjectDir, resources);
        List<RewriteSourceFileHolder<? extends SourceFile>> rewriteSourceFileHolders = wrapRewriteSourceFiles(absoluteProjectDir, parsedResources);

        ProjectResourceSet projectResourceSet = new ProjectResourceSet(rewriteSourceFileHolders);
        ProjectContext projectContext = projectContextFactory.createProjectContext(projectDir, projectResourceSet);

        storeGitCommitHash(projectDir, projectContext);

        return projectContext;
    }

    private List<RewriteSourceFileHolder<? extends SourceFile>> wrapRewriteSourceFiles(Path absoluteProjectDir, List<SourceFile> parsedByRewrite) {
        List<RewriteSourceFileHolder<?>> rewriteProjectResources = parsedByRewrite.stream()
                .map(sf -> wrapRewriteSourceFile(absoluteProjectDir, sf))
                .collect(Collectors.toList());
        return rewriteProjectResources;
    }

    private RewriteSourceFileHolder<?> wrapRewriteSourceFile(Path absoluteProjectDir, SourceFile sourceFile) {
        RewriteSourceFileHolder<?> rewriteSourceFileHolder = new RewriteSourceFileHolder<>(absoluteProjectDir, sourceFile);
        return rewriteSourceFileHolder;
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
