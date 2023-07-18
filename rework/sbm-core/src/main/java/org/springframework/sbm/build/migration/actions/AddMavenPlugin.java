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
package org.springframework.sbm.build.migration.actions;

import lombok.experimental.SuperBuilder;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.sbm.engine.recipe.MultiModuleAwareAction;
import org.springframework.sbm.engine.recipe.MultiModuleHandler;

import javax.validation.Valid;

@Getter
@SuperBuilder
public class AddMavenPlugin extends MultiModuleAwareAction {

    @Setter
    @Valid
    private OpenRewriteMavenPlugin plugin;

    public AddMavenPlugin() {
        super(builder());
        setMultiModuleHandler(new DefaultMultiModuleHandler());
    }

    @Override
    public void apply(ProjectContext context) {
        context.getBuildFile().addPlugin(plugin);
    }

    private class DefaultMultiModuleHandler implements MultiModuleHandler {
        @Override
        public void handle(ProjectContext context) {
            apply(context);
        }

        @Override
        public void setAction(MultiModuleAwareAction action) {

        }
    }
}
