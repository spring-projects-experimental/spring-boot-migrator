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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class RewriteMavenParser implements Parser<Xml.Document> {

    private final MavenParser parser;

    public RewriteMavenParser() {
        this.parser = initMavenParser(new RewriteExecutionContext());
    }

    @NotNull
    private MavenParser initMavenParser(RewriteExecutionContext executionContext) {
        MavenParser.Builder builder = MavenParser.builder();
//        if(executionContext.getMavenProfiles().length > 0) {
//            builder.activeProfiles(executionContext.getMavenProfiles());
//        }
//        if(executionContext.getMavenConfig() != null) {
//            builder.mavenConfig(executionContext.getMavenConfig());
//        }
//        if(executionContext.getMavenPomCache() != null) {
//            builder.cache(executionContext.getMavenPomCache());
//        }
        return builder.build();
    }

    @Override
    public List<Xml.Document> parse(String... sources) {
        return parser.parse(sources);
    }

    @Override
    public List<Xml.Document> parseInputs(Iterable<Input> sources, @Nullable Path relativeTo, ExecutionContext ctx) {
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
