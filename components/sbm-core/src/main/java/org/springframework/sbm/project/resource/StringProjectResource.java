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

import org.openrewrite.ExecutionContext;
import org.openrewrite.text.PlainText;
import org.openrewrite.text.PlainTextParser;
import org.springframework.core.io.DefaultResourceLoader;

import java.nio.file.Path;
import java.util.List;

/**
 * A project resource with {@code String} as content type.
 * <p>
 * See also: {@link RewriteSourceFileHolder}
 */
public class StringProjectResource extends RewriteSourceFileHolder<PlainText> {

    private final ExecutionContext executionContext;
    private String content;
    private final ResourceHelper resourceHelper = new ResourceHelper(new DefaultResourceLoader());

    /**
     * Create a new instance with given {@code Path} and blank content.
     */
    public StringProjectResource(Path absolutePath, ExecutionContext executionContext) {
        //super(new RewriteSourceFileHolder<>(new PlainTextParser().parse(List.of(absolutePath), null, new RewriteExecutionContext()).get(0)));
        super(absolutePath, new PlainTextParser().parse(List.of(absolutePath), null, executionContext).get(0));
        this.executionContext = executionContext;
    }

    /**
     * Create a new instance with given {@code Path} and given {@code String} content and marks it as changed.
     */
    public StringProjectResource(Path projectRoot, Path absolutePath, String content, ExecutionContext executionContext) {
        // FIXME: absolutePath, sourcePath, modulePath ?!
        super(projectRoot, new PlainTextParser().parse(content).get(0).withSourcePath(projectRoot.relativize(absolutePath)));
        this.content = content;
        this.executionContext = executionContext;
        markAsChanged();
    }

    @Override
    public String toString() {
        return "StringProjectResource(" + getAbsolutePath() + ")";
    }

}
