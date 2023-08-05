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
package org.springframework.sbm.engine.git;

import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ProjectSyncVerifier {

    private final GitSupport gitSupport;
    private final ScanCommand scanCommand;
    private final SbmApplicationProperties sbmApplicationProperties;
    private final ProjectContextHolder projectContextHolder;

    public void rescanWhenProjectIsOutOfSyncAndGitAvailable(ProjectContext context) {
        if (sbmApplicationProperties.isGitSupportEnabled()) {
            File repo = context.getProjectRootDirectory().toFile();
            if (gitSupport.repoExists(repo)) {
                String expectedRevision = context.getRevision();
                if (expectedRevision != null && !isProjectInSync(repo, expectedRevision)) {
                    ProjectContext projectContext = scanCommand.execute(context.getProjectRootDirectory().toString());
                    projectContextHolder.setProjectContext(projectContext);
                }
            }
        }
    }

    public void verifyProjectIsInSyncWhenGitAvailable(ProjectContext context) {
        if (sbmApplicationProperties.isGitSupportEnabled()) {
            File repo = context.getProjectRootDirectory().toFile();
            if (gitSupport.repoExists(repo)) {
                String expectedRevision = context.getRevision();
                boolean projectInSync = isProjectInSync(repo, expectedRevision);
                if (expectedRevision != null && !projectInSync) {
                    throw new ProjectOutOfSyncException("It seems that the project was changed while running the recipe. The project was scanned again but you'll need to run the recipe again.");
                }
            }
        }
    }


    boolean isProjectInSync(File repo, String expectedRevision) {
        Assert.notNull(expectedRevision, "Revision must not be null. This might be due to a problem retrieving the last commit hash from git.");
        return !gitSupport.hasUncommittedChangesOrDifferentRevision(repo, expectedRevision);
    }


}
