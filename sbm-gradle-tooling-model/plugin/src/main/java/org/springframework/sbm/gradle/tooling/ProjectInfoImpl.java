/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.gradle.tooling;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.openrewrite.gradle.ProjectInfo;
import org.openrewrite.gradle.marker.GradleSettings;
import org.openrewrite.gradle.toolingapi.GradleProject;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class ProjectInfoImpl implements ProjectInfo, Serializable {

    private static Class<?>[] SUPPORTED_GRADLE_PROPERTY_VALUE_TYPES =new Class<?>[] {
        Number.class,
        Boolean.class,
        String.class,
        Character.class
    };

    private final GradleSettings gradleSettings;
    private final String gradleVersion;
    private final boolean rootProject;
    private final File rootProjectDir;
    private final Collection<ProjectInfo> subprojects;
    private final File projectDir;
    private final File buildDir;
    private final File buildscriptFile;
    private final File settingsBuildscriptFile;
    private final Map<String, ?> properties;
    private final Collection<SourceSetInfo> sourceSets;
    private final GradleProject gradleProjectToolingModel;
    private final boolean multiPlatformKotlinProject;
    private final Collection<KotlinSourceSetInfo> kotlinSourceSets;
    private final Collection<File> buildscriptClasspath;
    private final Collection<File> settingsClasspath;

    @Override
    public <T> T getExtensionByType(Class<T> extClass) {
        return null;
    }

    static ProjectInfoImpl from(ProjectInfo info) {
        return new ProjectInfoImpl(
                info.getGradleSettings(),
                info.getGradleVersion(),
                info.isRootProject(),
                info.getRootProjectDir(),
                info.getSubprojects().stream().map(ProjectInfoImpl::from).collect(Collectors.toList()),
                info.getProjectDir(),
                info.getBuildDir(),
                info.getBuildscriptFile(),
                info.getSettingsBuildscriptFile(),
                info.getProperties().entrySet()
                        .stream()
                        .filter(e -> Arrays.stream(SUPPORTED_GRADLE_PROPERTY_VALUE_TYPES).anyMatch(c -> c.isInstance(e.getValue())))
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())),
                info.getSourceSets().stream().map(SourceSetImpl::from).collect(Collectors.toList()),
                info.getGradleProjectToolingModel(),
                info.isMultiPlatformKotlinProject(),
                info.getKotlinSourceSets().stream().map(KotlinSourceSetImpl::from).collect(Collectors.toList()),
                info.getBuildscriptClasspath(),
                info.getSettingsClasspath()
        );
    }

}
