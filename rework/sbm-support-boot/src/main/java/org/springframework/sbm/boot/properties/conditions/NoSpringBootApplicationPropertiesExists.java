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
package org.springframework.sbm.boot.properties.conditions;

import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;

public class NoSpringBootApplicationPropertiesExists implements Condition {

    public static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

    @Override
    public String getDescription() {
        return "Checks if '" + APPLICATION_PROPERTIES + "' exists.";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.search(new SpringBootApplicationPropertiesResourceListFilter()).isEmpty();
    }
}
