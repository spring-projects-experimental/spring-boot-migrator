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
package org.springframework.sbm.boot.common.conditions;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.regex.Pattern;

public class HasSpringBootDependencyImport implements Condition {
    private Pattern versionPattern = Pattern.compile(".*");

    @Override
    public String getDescription() {
        return String.format("Check if any Build file has a spring-boot-dependencies import with a version matching pattern '%s'.", versionPattern);
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = Pattern.compile(versionPattern);
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        return context.getApplicationModules()
                .stream()
                .map(Module::getBuildFile)
                .anyMatch(this::hasBuildFileDependencyImport);
    }

    private boolean hasBuildFileDependencyImport(BuildFile buildFile) {
        return buildFile.getRequestedManagedDependencies().stream()
                .map( md -> resolveCoordinates(buildFile, md))
                .anyMatch(c -> c.matches("org.springframework.boot:spring-boot-dependencies:"
                        + versionPattern));
    }

    private String resolveCoordinates(BuildFile buildFile, Dependency md) {
        String coordinates = md.getCoordinates();
        if (md.getVersion().startsWith("${")) {
            String version = buildFile.getProperty(md.getVersion().substring(2, md.getVersion().length() - 1));
            if (version != null) {
                // TODO: check into account properties imported from parent poms
                coordinates = md.getGroupId() + ":" + md.getArtifactId() + ":" + version;
            }
        }
        return coordinates;
    }
}
