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
package org.springframework.sbm.common.migration.actions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.common.filter.PathMatchingProjectResourceFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.InternalProjectResource;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MoveFilesActionTest {

    // TODO: test moving outside project root

    @Test
    @DisplayName("Move two files to a new dir")
    void moveFile() {

        Path projectRoot = TestProjectContext.getDefaultProjectRoot();

        // file 1
        String someFilePath = Path.of("src/main/resources/a/SomeFile.foo").toString();
        String fileContent1 = "file content";

        // file 2
        String anotherFilePath = Path.of("src/main/resources/b/AnotherFile.foo").toString();
        String fileContent2 = "file content 2";

        // target dir
        Path target = projectRoot.resolve("src/main/resources/new-location");

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectRoot(projectRoot)
                .addProjectResource(someFilePath, fileContent1)
                .addProjectResource(anotherFilePath, fileContent2)
                .build();

        ProjectResource someFile = projectContext.search(new PathMatchingProjectResourceFilter("/**/SomeFile.foo")).get(0);
        ProjectResource anotherFile = projectContext.search(new PathMatchingProjectResourceFilter("/**/AnotherFile.foo")).get(0);

        verifyPrecondition(someFile, projectRoot.resolve(someFilePath), fileContent1);
        verifyPrecondition(anotherFile, projectRoot.resolve(anotherFilePath), fileContent2);

        // TODO: #486
        System.out.println("Current ProjectContext: " + projectContext.getProjectResources().stream()
                .map(pr -> pr.getAbsolutePath().toString()).collect(Collectors.joining("\n")));

        MoveFilesAction sut = new MoveFilesAction();
        sut.setFromPattern("/**/*File.foo");
        sut.setToDir(target);

        sut.apply(projectContext);

        // TODO: #486
        System.out.println("Migrated ProjectContext: " + projectContext.getProjectResources().stream()
                .map(pr -> pr.getAbsolutePath().toString()).collect(Collectors.joining("\n")));

//        verifyResult(projectContext, target.resolve("SomeFile.foo"), someFile); // TODO: #486
        verifyResult(projectContext, target.resolve("AnotherFile.foo"), anotherFile);
        assertThat(projectContext.search(new PathMatchingProjectResourceFilter(someFilePath))).isEmpty();
        assertThat(projectContext.search(new PathMatchingProjectResourceFilter(anotherFilePath))).isEmpty();
    }

    private void verifyResult(ProjectContext projectContext, Path newPath, ProjectResource resource) {
        InternalProjectResource newResource = (InternalProjectResource) projectContext.search(new PathMatchingProjectResourceFilter(newPath.toString())).get(0);
        assertThat(newResource.hasChanges()).isTrue();
        assertThat(newResource.getAbsolutePath()).isEqualTo(newPath);
        assertThat(newResource.print()).isEqualTo(resource.print());
    }

    private void verifyPrecondition(ProjectResource resourceToMove1, Path absolutePath, String fileContent1) {
        assertThat(resourceToMove1.print()).isEqualTo(fileContent1);
        assertThat(resourceToMove1.isDeleted()).isFalse();
        assertThat(resourceToMove1.getAbsolutePath()).isEqualTo(absolutePath);
    }

}