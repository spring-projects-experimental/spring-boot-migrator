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

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.Setter;

import java.util.Optional;

@Setter
public class AddMavenDependencyManagementAction extends AbstractAction {

    private String groupId;
    private String artifactId;
    private String version;
    private String dependencyType;
    private String scope;

    @Override
    public void apply(ProjectContext context) {
        verifyNoConflictingManagedDependencyExists(context);

        Dependency dependency = Dependency.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .scope(scope)
                .type(dependencyType)
                .build();
        BuildFile rootBuildFile = context.getApplicationModules().getRootModule().getBuildFile();
        rootBuildFile.addToDependencyManagement(dependency);
    }

    @NotNull
    private void verifyNoConflictingManagedDependencyExists(ProjectContext context) {
        BuildFile rootBuildFile = context.getApplicationModules().getRootModule().getBuildFile();
        Optional<Dependency> managedSpringDep = rootBuildFile
                .getRequestedDependencyManagement()
                .stream()
                .filter(this::matchingDependencyManagementSection)
                .findFirst();

        if(managedSpringDep.isPresent()) {
            Dependency managedDep = managedSpringDep.get();
            int comparisonResult = compareVersions(this.version, managedDep.getVersion());
            if(managedDependencyHasSameVersion(comparisonResult) || managedDependencyHasHigherVersion(comparisonResult)) {
                String message = String.format(
                        "Failed to add a managed dependency %s with version %s. This managed dependency already exists in %s in version %s.",
                        this.groupId + ":" + this.artifactId,
                        this.version,
                        rootBuildFile.getAbsolutePath(),
                        managedDep.getVersion()
                );
                throw new IllegalStateException(message);
            }
        }
    }

    private boolean managedDependencyHasSameVersion(int comparisonResult) {
        return comparisonResult == 0;
    }

    private boolean managedDependencyHasHigherVersion(int comparisonResult) {
        return comparisonResult == -1;
    }

    private int compareVersions(String newVersion, String existingVersion) {
        return new ComparableVersion(newVersion).compareTo(new ComparableVersion(existingVersion));
    }

    private boolean matchingDependencyManagementSection(Dependency dependency) {
        return dependency.getGroupId().equals(groupId) &&
                dependency.getArtifactId().equals(artifactId);
    }
}
