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
package org.springframework.sbm.jee.web.api;

import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.web.api.WebAppType;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.search.FindTags;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.List;

@Configuration
public class JeeWebXmlProjectResourceRegistrar implements ProjectResourceWrapper<RewriteSourceFileHolder<Xml.Document>> {

//    @EventListener(ProjectContextBuiltEvent.class)
//    public void onProjectContextBuiltEvent(ProjectContextBuiltEvent projectContextBuiltEvent) {
//        ProjectContext projectContext = projectContextBuiltEvent.getProjectContext();
//        this.registerResources(projectContext);
//    }

    private WebAppType parseXml(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WebAppType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement element = (JAXBElement) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
            return (WebAppType) element.getValue();
        } catch (JAXBException e) {
            throw new RuntimeException("Given xml could not be parsed as web.xml.", e);
        }
    }

//    public void registerResources(ProjectContext projectContext) {
//        ProjectResourceSet projectResources = projectContext.getProjectResources();
//
//        projectResources.stream()
//                .filter(r -> r.getAbsolutePath().getFileName().endsWith(Path.of("web.xml")))
//                .forEach(w -> {
//                    WebAppType webAppType = parseXml(w.print());
//                    WebXml webXml = new WebXml(w.getAbsolutePath(), webAppType);
//                    // FIXME: remove comment after ProjectResourceSet holds ProjectResource
////                    projectResources.replace(w.getAbsolutePath(), webXml);
//                });
//    }

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return (
                Xml.Document.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass()) &&
                rewriteSourceFileHolder.getAbsolutePath().getFileName().endsWith("web.xml") &&
                ! new FindTags("/web-app").run(List.of(rewriteSourceFileHolder.getSourceFile())).isEmpty());
    }

    @Override
    public RewriteSourceFileHolder<Xml.Document> wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        Xml.Document document = Xml.Document.class.cast(rewriteSourceFileHolder.getSourceFile());
        return new WebXml(rewriteSourceFileHolder.getAbsoluteProjectDir(), document);
    }

//    @Override
//    public <T extends SourceFile> RewriteSourceFileHolder<T> wrapRewriteSourceFileHolder(RewriteSourceFileHolder<T> rewriteSourceFileHolder) {
//        SourceFile rewriteResource = rewriteSourceFileHolder.getRewriteResource();
//        return new RewriteSourceFileHolder<Xml.Document>(rewriteResource);
//    }
}
