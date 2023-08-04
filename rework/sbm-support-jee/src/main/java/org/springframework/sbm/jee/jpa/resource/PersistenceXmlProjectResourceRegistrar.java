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
package org.springframework.sbm.jee.jpa.resource;

import org.openrewrite.ExecutionContext;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.springframework.sbm.jee.jpa.api.PersistenceXml;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import lombok.RequiredArgsConstructor;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.search.FindTags;
import org.openrewrite.xml.tree.Xml;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PersistenceXmlProjectResourceRegistrar implements ProjectResourceWrapper<RewriteSourceFileHolder<Xml.Document>> {

    public static final String PERSISTENCE_XML_PATH = "META-INF/persistence.xml";
    private final ExecutionContext executionContext;

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        if (isXmlDocument(rewriteSourceFileHolder)) {
            Xml.Document xml = Xml.Document.class.cast(rewriteSourceFileHolder.getSourceFile());
            if (hasCorrectFileName(xml)) {
                return hasPersistenceRootTag(xml);
            }
        }
        return false;
    }

    private boolean hasPersistenceRootTag(Xml.Document xml) {
        List<Result> results = new FindTags("/persistence").run(new InMemoryLargeSourceSet(List.of(xml)), executionContext).getChangeset().getAllResults();
        return ! results.isEmpty();
    }

    private boolean hasCorrectFileName(Xml.Document xml) {
        return xml.getSourcePath().endsWith(PERSISTENCE_XML_PATH);
    }

    private boolean isXmlDocument(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return Xml.Document.class.isAssignableFrom(rewriteSourceFileHolder.getType());
    }

    @Override
    // FIXME: wrap or replace?!
    public RewriteSourceFileHolder<Xml.Document> wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return new PersistenceXml(rewriteSourceFileHolder.getAbsoluteProjectDir(), (Xml.Document) rewriteSourceFileHolder.getSourceFile());
    }

}
