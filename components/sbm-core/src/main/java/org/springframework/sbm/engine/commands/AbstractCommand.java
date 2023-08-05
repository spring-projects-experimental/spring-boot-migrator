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
package org.springframework.sbm.engine.commands;

import java.io.File;
import java.nio.file.Path;

public abstract class AbstractCommand<T> implements Command<T> {

    private final String name;

    protected AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean isApplicable(String commandName) {
        return name.equals(commandName);
    }


    /**
     * Use {@code ProjectRootPathResolver} instead
     */
    @Deprecated(forRemoval = true)
    protected void verifyPathExists(Path projectRootPath) {
        final File pathAsFile = projectRootPath.toFile();
        if (!pathAsFile.exists()) {
            throw new IllegalArgumentException("Given project root path '" + projectRootPath + "' does not exist.");
        }
        if (!pathAsFile.isDirectory()) {
            throw new IllegalArgumentException("Given project root path '" + projectRootPath + "' is not a directory.");
        }
    }

    /**
     * Use {@code ProjectRootPathResolver} instead
     */
    @Deprecated(forRemoval = true)
    protected Path getProjectRootFromArgumentsOrDefault(String[] arguments) {
        String givenPath = "./";
        if (arguments.length > 0) {
            givenPath = arguments[0];
        }
        return Path.of(givenPath);
    }

}
