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
package org.springframework.sbm.jee.ejb.resource;

import org.springframework.sbm.jee.ejb.api.EjbJarXml;
import org.springframework.sbm.project.resource.ProjectResourceWrapper;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.openrewrite.SourceFile;
import org.openrewrite.xml.tree.Xml;
import org.springframework.stereotype.Component;

@Component
public class JeeEjbJarXmlProjectResourceRegistrar implements ProjectResourceWrapper<EjbJarXml> {

    private static final String EJB_JAR_XML = "ejb-jar.xml";
    private static final String NAMESPACE_PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";

//    @EventListener
//    public void onProjectContextBuiltEvent(ProjectContextBuiltEvent projectContextBuiltEvent) {
//        ProjectContext projectContext = projectContextBuiltEvent.getProjectContext();
//        registerResources(projectContext);
//    }
//
//    private void registerResources(ProjectContext projectContext) {
//        ProjectResourceSet projectResources = projectContext.getProjectResources();
//        projectResources.stream()
//                .filter(pr -> pr.getSourcePath().getFileName().toString().equals(EJB_JAR_XML))
//                .filter(pr -> Xml.Document.class.isAssignableFrom(pr.getRewriteResource().getClass()))
//                .map(pr -> pr.getRewriteResource())
//                .map(Xml.Document.class::cast)
//                .forEach(pr -> createEjbJarXml(projectContext.getProjectRootDirectory(), projectResources, pr));
//    }
//
//    void createEjbJarXml(Path projectRootDirectory, ProjectResourceSet projectResources, Xml.Document xml) {
//        EjbJarXml ejbJarXml = new EjbJarXml(projectRootDirectory, xml);
//        projectResources.replace(ejbJarXml.getAbsolutePath(), ejbJarXml);
//    }

    @Override
    public boolean shouldHandle(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return rewriteSourceFileHolder.getSourcePath().getFileName().toString().equals(EJB_JAR_XML) &&
                Xml.Document.class.isAssignableFrom(rewriteSourceFileHolder.getSourceFile().getClass());
    }

    @Override
    public EjbJarXml wrapRewriteSourceFileHolder(RewriteSourceFileHolder<? extends SourceFile> rewriteSourceFileHolder) {
        return new EjbJarXml(rewriteSourceFileHolder.getAbsoluteProjectDir(), (Xml.Document) rewriteSourceFileHolder.getSourceFile());
    }
}
