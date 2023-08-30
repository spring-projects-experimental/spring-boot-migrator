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
import org.springframework.sbm.boot.asciidoctor.*;
import org.springframework.sbm.boot.upgrade_24_25.conditions.Boot_24_25_CreateDatasourceInitializerCondition;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

@Component
@Order(25_004)
public class Boot_24_25_SeparateCredentials implements UpgradeSectionBuilder {

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return new Boot_24_25_CreateDatasourceInitializerCondition().evaluate(projectContext);
    }

    @Override
    public Section build(ProjectContext projectContext) {
        String code =
                "\n" +
                "[source,java]\n\n" +
                "----\n" +
                "@Bean\n" +
                "public DataSourceInitializer dataSourceInitializer() {\n" +
                "   // code with relevant information extracted from the application during scan...\n" +
                "   // ...\n" +
                "}\n" +
                "----\n";

        return ChangeSection.RelevantChangeSection.builder()
                .title("Separate Credentials")
                .paragraph(
                        "The new script-based SQL database initialization does not support using separate credentials for schema (DDL) and data (DML) changes. +\n" +
                        "This reduces complexity and aligns its capabilities with Flyway and Liquibase. +\n" +
                        "If you require separate credentials for schema and data initialization, define your own org.springframework.jdbc.datasource.init.DataSourceInitializer beans."
                )
                .relevanceSection()
                .paragraph("The scan found the properties spring.datasource.data-username and spring.datasource.schema-username in ./path/to/application.properties.")
                .todoSection()
                .paragraph("You have two options.")
                .todoList(
                        TodoList.builder()
                                .paragraph(
                                        Paragraph.builder()
                                                .text("**1. Use same credentials for data and schema creation (preferred)**")
                                        .build()
                                )
                                .todo(TodoList.Todo.builder().text("Change credentials and provide required access to the database user in all affected databases.").build())
                                .todo(TodoList.Todo.builder().text("Replace the property spring.datasource.data-username with spring.sql.init.username and update to the new credentials.").build())
                                .todo(TodoList.Todo.builder().text("Remove the property spring.datasource.schema-username.").build())
                                .build()
                )
                .todoList(
                        TodoList.builder()
                                .paragraph(
                                        Paragraph.builder()
                                                .text("**2. Provide a DataSourceInitializer bean**")
                                                .build()
                                )
                                .todo(TodoList.Todo.builder().text("Add this bean definition to a bean configuration class (annotated with `@Configuration`). +\n" + code).build())
                                .recipeName("boot-2.4-2.5-datasource-initializer")
                                .build()
                )
                .build();
    }
}
