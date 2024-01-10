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
package org.springframework.sbm.mule.resource;

import lombok.RequiredArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MuleXmlProjectResourceRegistrar implements ProjectResourceWrapper<MuleXml> {

    private final ExecutionContext executionContext;

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return isMuleXmlResource(rewriteSourceFileHolder);
    }

    @Override
    public MuleXml wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {


        Parser.Input input = new Parser.Input(rewriteSourceFileHolder.getAbsolutePath(), () -> new ByteArrayInputStream(rewriteSourceFileHolder.print().getBytes(StandardCharsets.UTF_8)));
        List<Xml.Document> documents = new XmlParser().parseInputs(List.of(input), rewriteSourceFileHolder.getAbsoluteProjectDir(), executionContext);
        return new MuleXml(rewriteSourceFileHolder.getAbsoluteProjectDir(), documents.get(0));
    }

    private boolean isMuleXmlResource(RewriteSourceFileHolder<?> sourceFileHolder) {
        return sourceFileHolder.getAbsolutePath().toString().endsWith(".xml") &&
                sourceFileHolder.print().contains("<mule");
    }
}
