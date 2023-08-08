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

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.gradle.DefaultRewriteExtension;
import org.openrewrite.gradle.ProjectInfo;
import org.openrewrite.gradle.isolated.DefaultProjectParser;
import org.openrewrite.internal.lang.Nullable;

import java.io.File;
import java.util.stream.Stream;

public class GradleProjectParser {

    public Stream<SourceFile> parse(File projectDir, @Nullable File buildFile, ExecutionContext ctx) {
        ProjectInfo projectInfo = ModelBuilder.forProjectDirectory(projectDir, buildFile, ProjectInfo.class);
        DefaultProjectParser gradlePluginParser = new DefaultProjectParser(projectInfo, new DefaultRewriteExtension(projectInfo));
        return gradlePluginParser.parse(ctx);
    }


}
