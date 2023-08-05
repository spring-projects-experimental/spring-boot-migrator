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
package org.springframework.sbm.boot.upgrade_24_25.conditions;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.boot.upgrade_24_25.filter.SqlScriptDataSourceInitializationPropertiesAnalyzer;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

public class Boot_24_25_SqlScriptDataSourceInitializationCondition implements Condition {
    @Override
    public String getDescription() {
        return "Check if deprecated Datasource init properties exist.";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        List<SpringBootApplicationProperties> filteredResources = context.search(new SpringBootApplicationPropertiesResourceListFilter());
        List<SqlScriptDataSourceInitializationPropertiesAnalyzer.DeperecatedPropertyMatch> properties = new SqlScriptDataSourceInitializationPropertiesAnalyzer().findDeprecatedProperties(filteredResources);
        return !properties.isEmpty();
    }
}
