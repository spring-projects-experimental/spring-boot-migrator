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
package org.springframework.sbm.actions.spring.xml.migration;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.rewrite.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
public
class MigrateXmlToJavaConfigurationActionHelper {
    List<RewriteSourceFileHolder> getXmlBeanDefinitionFiles(ProjectContext context) {
        List<Path> resourceFolders = context.getBuildFile().getResourceFolders();
        List<RewriteSourceFileHolder> xmlBeanFiles = context.getProjectResources().stream()
                .filter(this::isXmlFile)
                .filter(pr -> isInMainResourceFolder(resourceFolders, pr))
                .filter(pr -> isSpringBeanConfiguration(pr))
                .map(RewriteSourceFileHolder.class::cast) // FIXME :/
                .collect(Collectors.toList());
        return xmlBeanFiles;
    }

    private boolean isInMainResourceFolder(List<Path> mainResourceFolders, ProjectResource resource) {
        return mainResourceFolders.stream()
                .anyMatch(mrf -> resource.getAbsolutePath().startsWith(mrf.toAbsolutePath()));
    }

    private boolean isSpringBeanConfiguration(ProjectResource pr) {
        return pr.print().contains("www.springframework.org/schema/beans");
    }

    private boolean isXmlFile(ProjectResource pr) {
        return pr.getAbsolutePath().toString().endsWith(".xml");
    }
}
