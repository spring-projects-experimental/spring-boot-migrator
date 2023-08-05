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
package org.springframework.sbm.build.migration.conditions;

import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Setter;

import jakarta.validation.Valid;

public class MavenPluginDoesNotExist implements Condition {
    @Setter
    @Valid
    private OpenRewriteMavenPlugin plugin;

    @Override
    public String getDescription() {
        return "Verify that the given Plugin (" + plugin + ") is not yet defined";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return false == context.getBuildFile().hasPlugin(plugin);
    }
}
