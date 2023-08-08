/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.gradle.tooling;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.openrewrite.gradle.JavaVersionInfo;
import org.openrewrite.gradle.ProjectInfo;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

@Value
@AllArgsConstructor
public class SourceSetImpl implements ProjectInfo.SourceSetInfo, Serializable {

    private final String name;
    private final Collection<File> sources;
    private final Collection<File> sourceDirectories;
    private final Collection<File> java;
    private final Collection<File> classesDirs;
    private final Collection<File> compileClasspath;
    private final Collection<File> implementationClasspath;
    private final JavaVersionInfo javaVersionInfo;

    static SourceSetImpl from(ProjectInfo.SourceSetInfo info) {
        return new SourceSetImpl(
          info.getName(),
          info.getSources(),
          info.getSourceDirectories(),
          info.getJava(),
          info.getClassesDirs(),
          info.getCompileClasspath(),
          info.getImplementationClasspath(),
          JavaVersionInfoImpl.from(info.getJavaVersionInfo())
        );
    }
}
