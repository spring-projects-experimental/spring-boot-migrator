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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.asciidoctor.IntroductionSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.build.api.BuildFile;

import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

public class Boot_24_25_Introduction implements UpgradeSectionBuilder {
    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return projectContext.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.springframework\\.boot\\:.*\\:2\\.4\\..*");
    }

    @Override
    public Section build(ProjectContext projectContext) {
        BuildFile buildFile = projectContext.getBuildFile();
        String applicationName = buildFile.getName().orElse(buildFile.getArtifactId());
        String gitRevision = projectContext.getRevision();
        Path projectRoot = projectContext.getProjectRootDirectory();
        String groupId = buildFile.getGroupId();
        String artifactId = buildFile.getArtifactId();
        String version = buildFile.getVersion();
        Optional<String> foundSpringVersion = buildFile.getRequestedDependencies().stream().filter(d -> d.getGroupId().equals("org.springframework.boot")).map(d -> d.getVersion()).findFirst();
        if(foundSpringVersion.isEmpty()) {
            throw new RuntimeException(String.format("Could not retrieve Spring version from declared dependencies in %s", buildFile.getAbsolutePath()));
        }
        String bootVersion = foundSpringVersion.get();

        IntroductionSection introductionSection = IntroductionSection.builder()
                .projectName(applicationName)
                .revision(gitRevision)
                .projectRoot(projectRoot)
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .bootVersion(bootVersion)
                .datetime(new Date())
                .build();

        return introductionSection;
    }
}
