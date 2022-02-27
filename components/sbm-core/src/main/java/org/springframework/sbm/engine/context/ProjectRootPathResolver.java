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
package org.springframework.sbm.engine.context;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ProjectRootPathResolver {

    public Path getProjectRootOrDefault(String argument) {
        String givenPath = "./";
        if (argument != null && !argument.isBlank()) {
            givenPath = argument;
        }
        givenPath = givenPath.replace("\\", "/");
        Path projectRoot = Paths.get(givenPath);
        verifyPathExists(projectRoot);
        try {
            return projectRoot.toRealPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyPathExists(Path projectRootPath) {
        final File pathAsFile = projectRootPath.toFile();
        if (!pathAsFile.exists()) {
            throw new IllegalArgumentException("Given project root path '" + projectRootPath + "' does not exist.");
        }
        if (!pathAsFile.isDirectory()) {
            throw new IllegalArgumentException("Given project root path '" + projectRootPath + "' is not a directory.");
        }
    }
}
