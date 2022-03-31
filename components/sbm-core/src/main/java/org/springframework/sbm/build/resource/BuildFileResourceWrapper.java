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
package org.springframework.sbm.build.resource;

import lombok.RequiredArgsConstructor;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order
public class BuildFileResourceWrapper implements ProjectResourceWrapper<OpenRewriteMavenBuildFile> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return Xml.Document.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass()) && rewriteSourceFileHolder.getAbsolutePath().endsWith("pom.xml");
    }

    @Override
    public OpenRewriteMavenBuildFile wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        Xml.Document maven = (Xml.Document) rewriteSourceFileHolder.getSourceFile();
        return new OpenRewriteMavenBuildFile(rewriteSourceFileHolder.getAbsoluteProjectDir(), maven, eventPublisher, new RewriteExecutionContext(eventPublisher));
    }

}
