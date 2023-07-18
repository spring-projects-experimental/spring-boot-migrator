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
package org.springframework.sbm.java.impl;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaParser;
import org.springframework.sbm.project.resource.SbmApplicationProperties;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 */
@Deprecated(since = "0.12.0", forRemoval = true)
public class JavaParserFactory {

    @Deprecated
    public static @NotNull JavaParser getInitialJavaParser(ExecutionContext executionContext) {
        Set<Path> dependencies = ClasspathRegistry.getInstance().getInitialDependencies();
        JavaParser javaParser = new RewriteJavaParser(new SbmApplicationProperties(), executionContext);
        javaParser.setClasspath(new ArrayList<>(dependencies));
        return javaParser;
    }

    @Deprecated
    public static @NotNull JavaParser getCurrentJavaParser(ExecutionContext executionContext) {
        Set<Path> dependencies = ClasspathRegistry.getInstance().getCurrentDependencies();
        JavaParser javaParser = new RewriteJavaParser(new SbmApplicationProperties(), executionContext);
        javaParser.setClasspath(new ArrayList<>(dependencies));
        return javaParser;
    }
}
