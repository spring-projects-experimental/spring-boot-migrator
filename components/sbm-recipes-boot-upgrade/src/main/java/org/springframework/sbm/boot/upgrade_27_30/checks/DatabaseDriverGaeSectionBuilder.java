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

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheck;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheckResult;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.precondition.PreconditionCheck;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseDriverGaeSectionBuilder implements Sbu30_PreconditionCheck, Sbu30_UpgradeSectionBuilder {

    private final DatabaseDriverGaeFinder finder;

    @Override
    public Sbu30_PreconditionCheckResult run(ProjectContext context) {

        Set<Module> collect = finder.findMatches(context);

        if(collect.isEmpty()) {
            return  new Sbu30_PreconditionCheckResult(PreconditionCheck.ResultState.PASSED, "No dependency to Google AppEngine's AppEngineDriver found.");
        } else {
            String message = "Dependencies containing 'com.google.appengine.api.rdbms.AppEngineDriver' were found in these modules: '" + collect.stream().map(m -> m.getBuildFile().getCoordinates()).collect(Collectors.joining("', '")) + "'";
            return new Sbu30_PreconditionCheckResult(PreconditionCheck.ResultState.FAILED, message);
        }
    }


    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return ! finder.findMatches(projectContext).isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {
        return ChangeSection.RelevantChangeSection.builder()
                .title("DatabaseDriver.GAE was deprecated in Spring Boot 2.7")
                .paragraph("Support for GAE database driver has been removed in 3.0.0 without replacement following the removal of AppEngineDriver from version 2.0 of the AppEngine API SDK.")
                .relevanceSection()
                .paragraph("The scan found `com.google.appengine.api.rdbms.AppEngineDriver` on the classpath.")
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                                .text("You'll need to find a replacement for Google App Engine.")
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
