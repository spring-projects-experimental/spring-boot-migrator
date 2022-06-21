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

package org.springframework.sbm.boot.upgrade.common.actions;

import lombok.Setter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceJavaxWithJakartaAction extends AbstractAction {

    @Setter
    private List<String> javaxPackagePatterns = new ArrayList<>();

    @Override
    public void apply(ProjectContext context) {
        context.getProjectJavaSources()
                .asStream()
                .forEach(js -> {
                    List<String> matchingPackages = javaxPackagePatterns.stream().filter(p -> js.hasImportStartingWith(p)).collect(Collectors.toList());
                    matchingPackages.forEach(p -> js.replaceImport(p, p.replace("javax.", "jakarta.")));
                });
    }
}
