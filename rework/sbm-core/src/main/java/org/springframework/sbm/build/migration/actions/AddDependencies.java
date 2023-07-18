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

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.DependencyChangeResolver;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.sbm.engine.recipe.MultiModuleAwareAction;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Setter
@Getter
@SuperBuilder
public class AddDependencies extends MultiModuleAwareAction {

    public AddDependencies() {
        super(builder());
        dependencies = new ArrayList<>();
    }

    public AddDependencies(List<Dependency> dependencies) {
        super(builder());
        this.dependencies = dependencies;
    }

    @Valid
    private final List<Dependency> dependencies;

    @Override
    public void apply(ProjectContext context) {
        BuildFile buildFile = context.getBuildFile();
        List<Pair<List<Dependency>, Optional<Dependency>>> pairs = dependencies.stream()
                .map(d -> new DependencyChangeResolver(buildFile, d))
                .map(DependencyChangeResolver::apply)
                .collect(Collectors.toList());

        List<Dependency> removeList = pairs.stream()
                .map(Pair::getLeft)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Dependency> addList = pairs.stream()
                .map(Pair::getRight)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        buildFile.removeDependencies(removeList);
        buildFile.addDependencies(addList);
    }
}
