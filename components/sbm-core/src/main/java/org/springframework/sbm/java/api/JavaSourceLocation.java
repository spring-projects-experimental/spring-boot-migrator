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
package org.springframework.sbm.java.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.nio.file.Path;

@Getter
@EqualsAndHashCode
final public class JavaSourceLocation {

    private final Path sourceFolder;
    private final String packageName;

    /**
     * @param sourceFolder absolute path of source folder
     */
    public JavaSourceLocation(Path sourceFolder, String packageName) {
        Assert.isTrue(sourceFolder.isAbsolute(), "path not absolute");
        this.sourceFolder = sourceFolder;
        this.packageName = packageName;
    }

}
