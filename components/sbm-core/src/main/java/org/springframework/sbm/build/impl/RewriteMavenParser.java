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
package org.springframework.sbm.build.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.project.parser.RewriteMavenSettingsInitializer;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Class to parse Maven build files.
 *
 * It reads {@code .mvn/maven.config when a project root dir is provided.
 * Maven's {@code settings.xml} is read when it exits. The result is stored in {@code ExecutionContext}.
 * When an {@code ExecutionContext} is provided the MavenSettings are initilized again to guarantee that
 * all settings from {@code settings.xml} are respected.
 */
@Component
@RequiredArgsConstructor
public class RewriteMavenParser implements Parser<Xml.Document> {

    private final ExecutionContext executionContext;
    private MavenParser parser;

//    @Deprecated
//    public RewriteMavenParser(RewriteMavenSettingsInitializer mavenSettingsInitializer, ExecutionContext executionContext) {
//        this.executionContext = executionContext;
//        initMavenParser(this.executionContext, null);
//    }

    @NotNull
    private void initMavenParser(ExecutionContext executionContext, Path projectRoot) {
        MavenParser.Builder builder = MavenParser.builder();
        if (projectRoot != null && projectRoot.resolve(".mvn/maven.config").toFile().exists()) {
            builder.mavenConfig(projectRoot.resolve(".mvn/maven.config"));
        }
        this.parser = builder.build();
//        if(executionContext.getMavenProfiles().length > 0) {
//            builder.activeProfiles(executionContext.getMavenProfiles());
//        }
//        if(executionContext.getMavenConfig() != null) {
//            builder.mavenConfig(executionContext.getMavenConfig());
//        }
//        if(executionContext.getMavenPomCache() != null) {
//            builder.cache(executionContext.getMavenPomCache());
//        }
    }

    @Override
    public List<Xml.Document> parse(String... sources) {
        return parser.parse(sources);
    }

    @Override
    public List<Xml.Document> parse(ExecutionContext ctx, String... sources) {
        return parser.parse(ctx, sources);
    }

    /**
     * @param sources    the sources to parse
     * @param relativeTo the project root, if null is given .mvn/maven.config will not be read
     * @param ctx        the ExecutionContext,
     */
    @Override
    public List<Xml.Document> parseInputs(Iterable<Input> sources, @Nullable Path relativeTo, ExecutionContext ctx) {
        if (relativeTo != null) {
            initMavenParser(ctx, relativeTo);
        }
        return parser.parseInputs(sources, relativeTo, ctx);
    }

    @Override
    public boolean accept(Path path) {
        return parser.accept(path);
    }

    @Override
    public Path sourcePathFromSourceText(Path prefix, String sourceCode) {
        return parser.sourcePathFromSourceText(prefix, sourceCode);
    }
}
