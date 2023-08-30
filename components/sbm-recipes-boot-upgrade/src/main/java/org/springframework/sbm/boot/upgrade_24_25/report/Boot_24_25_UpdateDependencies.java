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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.springframework.core.annotation.Order;
import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade.common.conditions.HasSpringBootParentOfVersion;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Order(25_001)
public class Boot_24_25_UpdateDependencies implements UpgradeSectionBuilder {
    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        HasSpringBootParentOfVersion condition = new HasSpringBootParentOfVersion();
        condition.setVersionStartingWith("2.4.");
        return condition.evaluate(projectContext);
    }

    @Override
    public Section build(ProjectContext projectContext) {
        OpenRewriteMavenBuildFile buildFile = (OpenRewriteMavenBuildFile) projectContext.getBuildFile();
        String version = buildFile.getPom().getPom().getRequested().getParent().getVersion();
        Path pathToPom = buildFile.getSourcePath();

        return ChangeSection.RelevantChangeSection.builder()
                .title("Update dependencies")
                .paragraph("The Spring Boot version must be updated to 2.5.6.") // TODO: make target version configurable
                .relevanceSection()
                .paragraph(String.format("The scan found a dependency to Spring Boot %s as parent to the Maven build file link:%s[`pom.xml`, window=_blank].", version, pathToPom))
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                                .text(String.format("Change the version of the referenced spring-boot-starter-parent in link:%s[`pom.xml`] from %s to 2.5.6.", pathToPom, version))
                                                .build()
                                )
                                .recipeName("boot-2.4-2.5-dependency-version-update")
                                .build()
                )
                .build();

    }
}
