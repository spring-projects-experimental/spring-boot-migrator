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
package org.springframework.sbm.jee.jsf.actions;

import org.springframework.sbm.build.MultiModuleApplicationNotSupportedException;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AddJoinfacesDependencies extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        if (context.getApplicationModules().isSingleModuleApplication()) {
            Module module = context.getApplicationModules().getRootModule();
            applyToModule(module);
        } else {
            throw new MultiModuleApplicationNotSupportedException("Action can only be applied to applications with single module.");
        }
    }

    private void applyToModule(Module module) {
        JsfImplementation jsfImplementation = getJsfImplementationInUse(module);

        if (jsfImplementation.equals(JsfImplementation.UNKNOWN)) {
            log.warn("Could not find used JSF implementation. Currently supported implementations are [" + JsfImplementation.supportedImplementations() + "]");
            return;
        }

        addJoinfacesDependencyManagement(module);
        addJoinfacesDependencies(jsfImplementation, module);
    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        if (context.getApplicationModules().isSingleModuleApplication()) {
            return hasJsfImport(context.getApplicationModules().getRootModule());
        }
        return false;
    }

    private void addJoinfacesDependencies(JsfImplementation jsfImplementation, Module module) {
        if (jsfImplementation.equals(JsfImplementation.APACHE_MYFACES)) {
            addMyFacesDependencies(module);
        } else if (jsfImplementation.equals(JsfImplementation.MOJARRA)) {
            addMojarraDependencies(module);
        }
    }

    private void addMojarraDependencies(Module context) {
        Dependency joinfacesStarter = Dependency.builder()
                .groupId("org.joinfaces")
                .artifactId("jsf-spring-boot-starter")
                .version("4.4.10")
                .build();

        context.getBuildFile().addDependencies(List.of(joinfacesStarter));
    }

    private void addMyFacesDependencies(Module context) {
        Dependency joinfacesStarter = Dependency.builder()
                .groupId("org.joinfaces")
                .artifactId("jsf-spring-boot-starter")
                .version("4.4.10")
                .exclusions(List.of(Dependency.builder()
                        .groupId("org.joinfaces")
                        .artifactId("mojarra-spring-boot-starter")
                        .build())
                )
                .build();

        Dependency myfacesStarter = Dependency.builder()
                .groupId("org.joinfaces")
                .artifactId("myfaces-spring-boot-starter")
                .version("4.4.10")
                .build();

        context.getBuildFile().addDependencies(List.of(joinfacesStarter, myfacesStarter));
    }

    private void addJoinfacesDependencyManagement(Module context) {
        Dependency joinfacesDependencyManagement = Dependency.builder()
                .groupId("org.joinfaces")
                .artifactId("joinfaces-dependencies")
                .version("4.4.10")
                .type("pom")
                .scope("import")
                .build();
        context.getBuildFile().addToDependencyManagement(joinfacesDependencyManagement);
    }

    private JsfImplementation getJsfImplementationInUse(Module module) {
        if (usesMyFaces(module)) {
            return JsfImplementation.APACHE_MYFACES;
        } else if (usesMojarra(module)) {
            return JsfImplementation.MOJARRA;
        } else {
            return JsfImplementation.UNKNOWN;
        }
    }

    private boolean usesMojarra(Module module) {
        return module.getBuildFile().hasDeclaredDependencyMatchingRegex(
                "org\\.glassfish\\:javax\\.faces.*",
                "org\\.glassfish\\:jakarta\\.faces.*");
    }

    private boolean usesMyFaces(Module module) {
        return module.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.apache\\.myfaces.*");
    }

    private boolean hasJsfImport(Module module) {
        return module.getMainJavaSourceSet().hasImportStartingWith("javax.faces");
    }

    private enum JsfImplementation {
        APACHE_MYFACES,
        MOJARRA,
        UNKNOWN;

        public static String supportedImplementations() {
            return Arrays.asList(JsfImplementation.values()).stream()
                    .map(jsfimpl -> jsfimpl.name())
                    .collect(Collectors.joining(", "));
        }
    }
}
