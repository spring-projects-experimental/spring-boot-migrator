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
import org.springframework.rewrite.project.resource.ProjectResource;
import org.springframework.rewrite.project.resource.finder.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.ChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(25_003)
public class Boot_24_25_SchemaSqlAndDataSqlFiles implements UpgradeSectionBuilder {

    private List<ProjectResource> dbInitFiles;

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        dbInitFiles = projectContext.search(new PathPatternMatchingProjectResourceFinder(List.of("/**/resources/**/schema.sql", "/**/resources/**/data.sql")));
        return !dbInitFiles.isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {

        // render
        return ChangeSection.RelevantChangeSection.builder()
                .title("schema.sql and data.sql Files")
                .paragraph(
                        "With Spring Boot 2.5.1 and above, the new SQL initialization properties support detection of embedded datasources for JDBC and R2DBC. +\n" +
                        "By default, SQL database initialization is only performed when using an embedded in-memory database. +\n" +
                        "To always initialize a SQL database, irrespective of its jpaRepositoryInterface, set spring.sql.init.mode to always. +\n" +
                        "Similarly, to disable initialization, set spring.sql.init.mode to never."
                )
                .relevanceSection()
                .paragraph(
                        String.format("The scan found a dependency to H2 in-memory database in `./pom.xml` and the file%s `%s`",
                                dbInitFiles.size() > 1 ? "s" : "",
                                dbInitFiles.stream().map(f -> Path.of(".").toAbsolutePath().relativize(f.getAbsolutePath()).toString()).collect(Collectors.joining("`, `"))
                        )
                )
                .todoSection()
                .todoList(
                        TodoList.builder()
                                .todo(
                                        TodoList.Todo.builder()
                                        .text("Verify if you need to set the spring.sql.init.mode property to either `always` or `never`, `embedded` being the default.")
                                        .build()
                                )
                        .build()
                )
                .build();
    }
}
