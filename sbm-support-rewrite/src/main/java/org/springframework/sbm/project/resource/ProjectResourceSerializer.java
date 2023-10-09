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
package org.springframework.sbm.project.resource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectResourceSerializer {

    public void writeChanges(InternalProjectResource projectResource) {

        if (projectResource != null && projectResource.hasChanges()) {

            Path absolutePath = projectResource.getAbsolutePath();

            if (projectResource.isDeleted()) {
                try {
                    if(Files.exists(absolutePath)) {
                        Files.delete(absolutePath);
                    }
                } catch (IOException ioe) {
                    throw new RuntimeException("Can't delete file [" + absolutePath + "]", ioe);
                }
            } else {
                try {
                    Files.createDirectories(absolutePath.getParent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try (BufferedWriter sourceFileWriter = Files.newBufferedWriter(absolutePath)) {
                    String newSource = projectResource.print();
                    sourceFileWriter.write(newSource);
                    projectResource.resetHasChanges();
                } catch (IOException ioe) {
                    throw new RuntimeException("Can't write back changes in [" + absolutePath + "]", ioe);
                }
            }

        }
    }
}
