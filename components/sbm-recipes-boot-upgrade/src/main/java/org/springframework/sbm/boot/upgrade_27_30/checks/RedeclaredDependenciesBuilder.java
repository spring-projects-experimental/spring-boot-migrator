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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheck;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_PreconditionCheckResult;
import org.springframework.sbm.boot.upgrade_27_30.Sbu30_UpgradeSectionBuilder;
import org.springframework.sbm.engine.context.ProjectContext;

public class RedeclaredDependenciesBuilder implements Sbu30_PreconditionCheck, Sbu30_UpgradeSectionBuilder {
    private final RedeclaredDependenciesFinder finder;

    public RedeclaredDependenciesBuilder(RedeclaredDependenciesFinder finder) {
        this.finder = finder;
    }

    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return !finder.findMatches(projectContext).isEmpty();
    }

    @Override
    public Section build(ProjectContext projectContext) {
        return null;
    }

    @Override
    public Sbu30_PreconditionCheckResult run(ProjectContext context) {
        return null;
    }
}
