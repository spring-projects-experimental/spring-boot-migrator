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
package org.springframework.sbm.build.migration.actions;

import lombok.experimental.SuperBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Builder;
import lombok.Setter;
import org.springframework.sbm.engine.recipe.Condition;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Setter
@SuperBuilder
public class RemovePluginsMatchingRegex extends AbstractAction {

    private List<String> pluginsRegex;

    public RemovePluginsMatchingRegex() {
        super(builder());
    }

    public RemovePluginsMatchingRegex(List<String> pluginsRegex) {
        super(builder());
        this.pluginsRegex = pluginsRegex;
    }

    @Override
    public void apply(ProjectContext context) {
        BuildFile buildFile = context.getBuildFile();
        buildFile.removePluginsMatchingRegex(pluginsRegex.toArray(new String[]{}));
    }
}
