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

import lombok.Setter;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

/**
 * @author Fabian Krüger
 */
@Setter
public class NoMoreRecentManagedDependencyExists implements Condition {

    private String groupId;
    private String artifactId;
    private String version;


    @Override
    public String getDescription() {
        return "Check that no more recent managed dependency exists";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getApplicationModules().stream().map(Module::getBuildFile)
                .noneMatch(this::hasConflictingManagedDependency);
    }

    private boolean hasConflictingManagedDependency(BuildFile buildFile) {
        List<Dependency> requestedDependencyManagement = buildFile.getRequestedDependencyManagement();
        if(requestedDependencyManagement == null || requestedDependencyManagement.isEmpty()) {
            return false;
        }
        return buildFile.getRequestedDependencyManagement().stream()
                .anyMatch(this::isConflictingDEpendency);
    }

    private boolean isConflictingDEpendency(Dependency dependency) {
        boolean matchingGA = groupId.equals(dependency.getGroupId()) &&
                artifactId.equals(dependency.getArtifactId());

        if(matchingGA) {
            return hasConflictingVersion(dependency);
        } else {
            return false;
        }
    }

    private boolean hasConflictingVersion(Dependency dependency) {
        int comparisonResult = compareVersions(version, dependency.getVersion());
        return managedDependencyHasHigherVersion(comparisonResult) || managedDependencyHasSameVersion(comparisonResult);
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
}
