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
package org.springframework.sbm.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

public class ProjectUtils {
    
    public static File initTestProject(File containerFolder, String name) {
        try {
            Path initialPath = Path.of("src/test/resources/test-code/").resolve(name);

            FileUtils.copyDirectory(initialPath.toFile(), containerFolder);
            
            return containerFolder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
