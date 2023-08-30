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

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;

import javax.validation.constraints.NotNull;

@Slf4j
public class BumpParentPomVersion extends AbstractAction {

    @Setter
    @NotNull
    private String groupId;
    @Setter
    @NotNull
    private String artifactId;
    @Setter
    @NotNull
    private String toVersion;

    @Override
    public void apply(ProjectContext context) {
        context.getApplicationModules().stream()
                .map(Module::getBuildFile)
                .filter(BuildFile::hasParent)
                .filter(b -> b.getParentPomDeclaration().get().getGroupId().equals(groupId))
                .filter(b -> b.getParentPomDeclaration().get().getArtifactId().equals(artifactId))
                .forEach(b -> b.upgradeParentVersion(toVersion));
    }

    private boolean hasParentPom(ProjectContext context) {
        return context.getBuildFile().hasParent();
    }
}
