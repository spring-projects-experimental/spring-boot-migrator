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
package org.springframework.sbm.properties.parser;

import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class RewritePropertiesParser extends PropertiesParser {
    private PropertiesParser wrappedParser = new PropertiesParser();

    @Deprecated(forRemoval = true, since = "0.12.0")
    public boolean shouldBeParsedAsProperties(Resource resource) {
        return resource.getFilename().endsWith(".properties");
    }

    public RewriteSourceFileHolder<Properties.File> parse(Path projectRoot, Path resourcePath, String resourceContent) {
        Properties.File parse = wrappedParser.parse(resourceContent).get(0).withSourcePath(resourcePath);
        return wrapRewriteSourceFile(projectRoot, parse);
    }

    private RewriteSourceFileHolder<Properties.File> wrapRewriteSourceFile(Path absoluteProjectDir, Properties.File sourceFile) {
        RewriteSourceFileHolder<Properties.File> rewriteSourceFileHolder = new RewriteSourceFileHolder<>(absoluteProjectDir, sourceFile);
        return rewriteSourceFileHolder;
    }
}
