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

import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheck;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheckResult;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.boot.upgrade_27_30.filter.LoggingDateFormatPropertyFinder;
import org.springframework.sbm.engine.context.ProjectContext;

import static org.springframework.sbm.engine.precondition.PreconditionCheck.ResultState.PASSED;

public class LoggingDateFormatOverrideSectionBuilder implements Sbu30_PreconditionCheck, Sbu30_UpgradeSectionBuilder {

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return projectContext
                .search(new LoggingDateFormatPropertyFinder())
                .isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {
        return ChangeSection.RelevantChangeSection.builder()
                .title("Changes in logging date format of log messages to align with the ISO-8601")
                .paragraph("The new default format yyyy-MM-dd’T’HH:mm:ss.SSSXXX uses a T to separate the date and time instead of a space character and adds the timezone offset to the end. ")
                .relevanceSection()
                .paragraph("The scan found there is no existing override. Hence a default boot property will be created with date format set to old style i.e. yyyy-MM-dd HH:mm:ss.SSSXXX")
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                                .text("If you wish to continue using new style remove the logging.pattern.dateformat from the applications.properties file.")
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    @Override
    public Sbu30_PreconditionCheckResult run(ProjectContext context) {
        return isApplicable(context) ? new Sbu30_PreconditionCheckResult(PASSED, "Override logging date format to yyyy-MM-dd HH:mm:ss.SSSXXX")
                                     : new Sbu30_PreconditionCheckResult(PASSED, "Already logging format provided. No overrides required.");
    }
}
