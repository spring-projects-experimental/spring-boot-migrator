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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.RelevantChangeSection;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.Table;
import org.springframework.sbm.boot.asciidoctor.TodoList;
import org.springframework.sbm.boot.upgrade_24_25.conditions.Boot_24_25_SqlScriptDataSourceInitializationCondition;
import org.springframework.sbm.boot.upgrade_24_25.filter.SqlScriptDataSourceInitializationPropertiesAnalyzer;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;

import java.util.Comparator;
import java.util.List;

public class Boot_24_25_SqlScriptDataSourceInitialization implements UpgradeSectionBuilder {


    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return new Boot_24_25_SqlScriptDataSourceInitializationCondition().evaluate(projectContext);
//        List<SpringBootApplicationProperties> filteredResources = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
//        List<SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch> properties = new SqlScriptDataSourceInitializationPropertiesAnalyzer().findDeprecatedProperties(filteredResources);
//        return !properties.isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {

        List<SpringBootApplicationProperties> filteredResources = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
        List<SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch> deprecatedProperties = new SqlScriptDataSourceInitializationPropertiesAnalyzer().findDeprecatedProperties(filteredResources);

            Table.Builder tableBuilder = Table.builder();
            tableBuilder.headerCols("File", "Old (2.4)", "New (2.5)");
            deprecatedProperties.sort(Comparator.comparing(SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch::getDeprecatedPropery));
            for (SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch match : deprecatedProperties) {
                tableBuilder.row("`" + match.getPath() + "`", "`" + match.getDeprecatedPropery() + "`", "`" + match.getNewProperty() + "`");
            }

            return RelevantChangeSection.builder()
                    .title("SQL Script DataSource Initialization")
                    .paragraph(
                            "The underlying method used to support schema.sql and data.sql scripts has been redesigned in Spring Boot 2.5. +\n " +
                            "spring.datasource.* properties related to DataSource initialization have been deprecated in favor of new spring.sql.init.* properties. +\n" +
                            "These properties can also be used to initialize an SQL database accessed via R2DBC."
                    )
                    .relevanceSection()
                    .paragraph(
                            "The scan found properties in your application that need to be changed as these were deprecated in Spring Boot 2.5. +\n" +
                            "Please see the table below for more information about what needs to be changed."
                    )
                    .table(
                            tableBuilder.build()
                    )
                    .todoSection()
                    .todoList(TodoList.builder()
                            .todo(TodoList.Todo.builder()
                                    .text("Replace the old property names to the new names in the given files.")
                                    .build())
                            .recipeName("boot-2.4-2.5-sql-init-properties")
                            .build())
                    .build();
    }
}
