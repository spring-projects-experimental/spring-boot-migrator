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
package org.springframework.sbm.common.migration.actions;

import org.springframework.rewrite.resource.ProjectResource;
import org.springframework.rewrite.resource.finder.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Moves files to a directory
 */
@Getter
@Setter
public class MoveFilesAction extends AbstractAction {

    /**
     * Ant-style path patterns to filter resources.
     */
    @NotEmpty
    private String fromPattern;

    /**
     * Target directory where filtered files will be moved to.
     */
    @NotNull
    private Path toDir;

    @Override
    public void apply(ProjectContext context) {
        List<String> matchingPatterns = List.of(fromPattern);
        List<ProjectResource> resources = context.search(new PathPatternMatchingProjectResourceFinder(matchingPatterns));
        resources.forEach(r -> this.move(context, r));
    }

    private void move(ProjectContext context, ProjectResource projectResource) {
        Path newPath = context.getProjectRootDirectory().resolve(toDir).normalize();
        maybeCreateTargetDirectory();
        projectResource.moveTo(newPath);
    }

    // FIXME: Move to Serialization phase
    private void maybeCreateTargetDirectory() {
        try {
            Files.createDirectories(toDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory " + toDir.toString(), e);
        }
    }
}
