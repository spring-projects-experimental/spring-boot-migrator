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
package org.springframework.sbm.build.api;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.springframework.sbm.build.impl.MavenBuildFileUtil;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationModules {
    private final List<ApplicationModule> modules;

    public ApplicationModules(List<ApplicationModule> modules) {
        this.modules = modules;
    }

    public Stream<ApplicationModule> stream() {
        return modules.stream();
    }

    public ApplicationModule getRootModule() {
        return modules.stream()
                .sorted((m2, m1) -> m1.getBuildFile().getAbsolutePath().toString().compareTo(m2.getBuildFile().getAbsolutePath().toString()))
                .findFirst()
                .orElse(modules.get(0));
    }

    public List<ApplicationModule> list() {
        return stream().collect(Collectors.toUnmodifiableList());
    }

    public ApplicationModule getModule(Path name) {
        return modules.stream()
                .filter(m -> m.getModulePath().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find module with name '" + name + "'"));
    }

    public ApplicationModule getModule(String name) {
        return getModule(Path.of(name));
    }

    public List<ApplicationModule> getModules(ApplicationModule module) {
        MavenResolutionResult mavenResolutionResult = MavenBuildFileUtil.findMavenResolution(((OpenRewriteMavenBuildFile) module.getBuildFile()).getSourceFile()).get();
        List<MavenResolutionResult> modulesMarker = mavenResolutionResult.getModules();
        if (!modulesMarker.isEmpty()) {
            return filterModulesContainingMavens(modulesMarker);
        } else {
            return new ArrayList<>();
        }
    }

    @NotNull
    private List<ApplicationModule> filterModulesContainingMavens(List<MavenResolutionResult> modulesMarker) {
        List<String> collect = modulesMarker.stream()
                .map(m -> m.getPom().getGroupId() + ":" + m.getPom().getArtifactId())
                .collect(Collectors.toList());

        return modules.stream()
                .filter(module -> {
                    String groupAndArtifactId = module.getBuildFile().getGroupId() + ":" + module.getBuildFile().getArtifactId();
                    return collect.contains(groupAndArtifactId);
                })
                .collect(Collectors.toList());
    }

    public List<ApplicationModule> getTopmostApplicationModules() {
        List<ApplicationModule> topmostModules = new ArrayList<>();
        modules.forEach(module -> {
            // is jar
            if ("jar".equals(module.getBuildFile().getPackaging())) { // FIXME: other types could be topmost too, e.g. 'war'
                // no other pom depends on this pom in its dependency section
                if (noOtherPomDependsOn(module.getBuildFile())) {
                    // has no parent or parent has packaging pom
                    Optional<ParentDeclaration> parentPomDeclaration = module.getBuildFile().getParentPomDeclaration();
                    if (parentPomDeclaration.isEmpty()) {
                        topmostModules.add(module);
                    } else if (isDeclaredInProject(parentPomDeclaration.get()) && isPackagingOfPom(parentPomDeclaration.get())) {
                        topmostModules.add(module);
                    } else if (!isDeclaredInProject(parentPomDeclaration.get())) {
                        topmostModules.add(module);
                    }
                }
            }
        });
        return topmostModules;
    }

    private boolean isPackagingOfPom(ParentDeclaration parentPomDeclaration) {
        Optional<ApplicationModule> applicationModule = this.modules.stream()
                .filter(module -> module.getBuildFile().getCoordinates().equals(parentPomDeclaration.getCoordinates()))
                .findFirst();
        if (applicationModule.isPresent()) {
            BuildFile buildFile = applicationModule.get().getBuildFile();
            return "pom".equals(buildFile.getPackaging());
        }
        return true;
    }

    private boolean isDeclaredInProject(ParentDeclaration parentPomDeclaration) {
        return this.modules.stream()
                .anyMatch(module -> module.getBuildFile().getCoordinates().equals(parentPomDeclaration.getCoordinates()));
    }

    private boolean noOtherPomDependsOn(BuildFile buildFile) {
        return !this.modules.stream()
                .anyMatch(module -> module.getBuildFile().getDeclaredDependencies().stream().anyMatch(d -> d.getCoordinates().equals(buildFile.getCoordinates())));
    }

    public boolean isSingleModuleApplication() {
        return modules.size() == 1;
    }
}
