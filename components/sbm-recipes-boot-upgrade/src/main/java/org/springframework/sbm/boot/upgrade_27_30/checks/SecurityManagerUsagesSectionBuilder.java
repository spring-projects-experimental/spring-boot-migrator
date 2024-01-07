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
package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.openrewrite.java.tree.J;
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.boot.upgrade_27_30.openrewrite.SecurityManagerUsagesFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityManagerUsagesSectionBuilder implements Sbu30_UpgradeSectionBuilder {

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return ! projectContext.getProjectJavaSources().find(new SecurityManagerUsagesFinder()).isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {
        List<RewriteSourceFileHolder<J.CompilationUnit>> rewriteSourceFileHolders = projectContext
                .getProjectJavaSources()
                .find(new SecurityManagerUsagesFinder());

        return ChangeSection.RelevantChangeSection.builder()
                .title("Deprecation of `SecurityManager`")
                .paragraph("Support for Java’s `SecurityManager` has been removed following its deprecation in the JDK")
                .relevanceSection()
                .paragraph("We found either calls to `System.getSecurityManager()` or imports of `" + SecurityManagerUsagesFinder.JAVA_SECURITY_ACCESS_CONTROL_EXCEPTION + "` which indicates the usage of SecurityManager.")
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                                .text("Remove all usages of `SecurityManager` that exist.")
                                                .build()
                                )
                                .build()
                )
                .build();

    }
}
