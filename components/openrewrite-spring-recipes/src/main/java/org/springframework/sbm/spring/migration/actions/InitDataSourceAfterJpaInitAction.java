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
package org.springframework.sbm.spring.migration.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.common.migration.conditions.FileExist;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;

import java.nio.file.Path;
import java.util.List;

public class InitDataSourceAfterJpaInitAction extends AbstractAction {

    public static final String QUESTION = "Would you rather run the SQL init scripts after JPA initialization?";

    private final UserInteractions ui;

    public InitDataSourceAfterJpaInitAction(UserInteractions ui) {
        this.ui = ui;
        setCondition(FileExist.builder().fileName("data.sql").build().or(FileExist.builder().fileName("schema.sql").build()));
        setDescription("If you use both JPA initialization and Spring Boot’s SQL script support in the same application you may find an ordering issue with Spring Boot 2.5. By default, we now run the schema.sql and data.sql scripts before JPA initialization occurs.");
    }

    @Override
    public void apply(ProjectContext context) {
        if (ui.askUserYesOrNo(QUESTION)) {
            List<SpringBootApplicationProperties> filteredResources = context.search(new SpringBootApplicationPropertiesResourceListFilter());
            SpringBootApplicationProperties applicationProperties;
            if (filteredResources.isEmpty()) {
                Path path = context.getBuildFile().getResourceFolders().get(0).resolve("application.properties");
				applicationProperties = SpringBootApplicationProperties.newApplicationProperties(context.getProjectRootDirectory(), path);
                context.getProjectResources().add(applicationProperties);
            } else {
                applicationProperties = filteredResources.get(0);
            }
            applicationProperties.setProperty("spring.datasource.initialization-order", "after-jpa");
        }
    }

}
