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
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.*;
import org.springframework.sbm.engine.recipe.MultiModuleAwareAction;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@SuperBuilder
public class RemoveDependenciesMatchingRegex extends MultiModuleAwareAction {

    @Valid
    @NotEmpty
    private List<String> dependenciesRegex;

    public RemoveDependenciesMatchingRegex() {
        super(builder());
        this.dependenciesRegex = new ArrayList<>();
    }

    public RemoveDependenciesMatchingRegex(List<String> dependenciesRegex) {
        super(builder());
        this.dependenciesRegex = dependenciesRegex;
    }

    @Override
    public void apply(ProjectContext context) {
        context.getModules().stream()
                .map(Module::getBuildFile)
                .filter(b -> b.hasDeclaredDependencyMatchingRegex(dependenciesRegex.toArray(new String[0])))
                .forEach(b -> b.removeDependenciesMatchingRegex(dependenciesRegex.toArray(new String[0])));
    }
}
