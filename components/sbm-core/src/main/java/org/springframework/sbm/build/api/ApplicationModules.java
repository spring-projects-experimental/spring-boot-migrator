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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * Represents all modules in the {@code ProjectCOntext}.
 */
public class ApplicationModules {
    private final List<Module> modules;

    public ApplicationModules(List<Module> modules) {
        this.modules = modules;
    }

    public Stream<Module> stream() {
        return modules.stream();
    }

    public Module getRootModule() {
        return modules.stream()
                .filter(m -> m.getBuildFile().isRootBuildFile())
                .findFirst()
                .orElseThrow(() -> new RootBuildFileNotFoundException("Module with root build file is missing"));
    }

    public List<Module> list() {
        return stream().collect(Collectors.toUnmodifiableList());
    }

    public Module getModule(Path modulePath) {
        return modules.stream()
                .filter(m -> m.getModulePath().equals(modulePath))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find module with modulePath '" + modulePath + "'"));
    }

    public Optional<Module> findModule(String coordinate) {
        return modules.stream().filter(m -> m.getBuildFile().getCoordinates().equals(coordinate)).findFirst();
    }

    public Module getModule(String name) {
        if("root".equals(name)) name = "";
        return getModule(Path.of(name));
    }

    /**
     * Searches in all modules for a resource with given {@code resourcePath} and returns first match.
     *
     * @param resourcePath must be an <b>absolute path</b> of the resource
     */
    public Optional<Module> findModuleContaining(Path resourcePath) {
        return modules.stream().filter(m -> m.contains(resourcePath)).findFirst();
    }

    public List<Module> getModules(Module module) {
        MavenResolutionResult mavenResolutionResult = MavenBuildFileUtil.findMavenResolution(((OpenRewriteMavenBuildFile) module.getBuildFile()).getSourceFile()).get();
        List<MavenResolutionResult> modulesMarker = mavenResolutionResult.getModules();
        if (!modulesMarker.isEmpty()) {
            return getModulesContainingMavens(modulesMarker);
        } else {
            return new ArrayList<>();
        }
    }

    /**
    * Takes a list of {@code MavenResolutionResult}s and returns the modules with matching {@code groupId:artifactId}.
    */
    @NotNull
    private List<Module> getModulesContainingMavens(List<MavenResolutionResult> mavens) {
        List<String> relevantGroupAndArtifactIds = mavens.stream()
                .map(m -> m.getPom().getGroupId() + ":" + m.getPom().getArtifactId())
                .collect(Collectors.toList());

        return modules.stream()
                .filter(module -> {
                    String groupAndArtifactId = module.getBuildFile().getGroupId() + ":" + module.getBuildFile().getArtifactId();
                    return relevantGroupAndArtifactIds.contains(groupAndArtifactId);
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns the list of application modules.
     *
     * An application module is a module that no other module depends on and which has a parent with packaging of type pom.
     */
    public List<Module> getTopmostApplicationModules() {
        List<Module> topmostModules = new ArrayList<>();
        Set<String> packagingTypes = Set.of("jar","war","mule-application");
        modules.forEach(module -> {
            if (packagingTypes.contains(module.getBuildFile().getPackaging())) {
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

    /**
     * Returns the list of component modules.
     *
     * A component module is a module that another module depends on and that thus will be part of another application module.
     */
    public List<Module> getComponentModules() {
        return modules.stream()
                .filter(this::isDependencyOfAnotherModule)
                .collect(Collectors.toList());
    }

    private boolean isDependencyOfAnotherModule(Module applicationModule) {
        return ! noOtherPomDependsOn(applicationModule.getBuildFile());
    }

    private boolean isPackagingOfPom(ParentDeclaration parentPomDeclaration) {
        Optional<Module> applicationModule = this.modules.stream()
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
                .anyMatch(module -> module.getBuildFile().getRequestedDependencies().stream().anyMatch(d -> d.getCoordinates().equals(buildFile.getCoordinates())));
    }

    public boolean isSingleModuleApplication() {
        return modules.size() == 1;
    }

}
