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
package org.springframework.sbm.jee.wls.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.jee.wls.EjbDeploymentDescriptor;
import org.springframework.sbm.jee.wls.WlsEjbDeploymentDescriptor;
import org.springframework.sbm.jee.wls.finder.JeeWlsEjbDeploymentDescriptorFilter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class MigrateWlsEjbDeploymentDescriptor extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        AtomicBoolean addDataJpaDependency = new AtomicBoolean();
        WlsEjbDeploymentDescriptor wlsEjbDeploymentDescriptor = context.search(new JeeWlsEjbDeploymentDescriptorFilter()).orElseThrow(() -> new IllegalArgumentException("No weblogic EJB deployment descriptor exists."));
        EjbDeploymentDescriptor ejbDeploymentDescriptor = wlsEjbDeploymentDescriptor.getEjbDeploymentDescriptor();
        ejbDeploymentDescriptor.getEnterpriseBeans().stream()
                .forEach(ejb -> {
                    List<? extends JavaSource> javaSources = context.getProjectJavaSources().list();
                    JavaSourceWithEjb javaSource = findEjbByName(javaSources, ejb);
                    boolean addDep = incorporateIntoSourceFile(context.getBuildFile(), javaSource, wlsEjbDeploymentDescriptor, ejb);
                    addDataJpaDependency.set(addDataJpaDependency.get() || addDep);
                });
        if (addDataJpaDependency.get()) {
            addSpringBootDataJpaDependency(context.getBuildFile());
        }
    }

    private boolean incorporateIntoSourceFile(BuildFile buildFile, JavaSourceWithEjb javaSourceWithEjb, WlsEjbDeploymentDescriptor wlsEjbDeploymentDescriptor, EjbDeploymentDescriptor.EnterpriseBean ejb) {
        boolean addDataJpaDependency = false;
        if (ejb.getTransactionDescriptor() != null) {
            Integer transactionTimeoutSeconds = ejb.getTransactionDescriptor().getTransactionTimeoutSeconds();
            if (transactionTimeoutSeconds != null) {
                int timeoutInMillis = calclateTimeoutForSpringBoot(transactionTimeoutSeconds);
                if (!javaSourceWithEjb.getType().hasAnnotation("org.springframework.transaction.annotation.Transactional")) {

                    this.startProcess("Annotate " + javaSourceWithEjb.getType().getFullyQualifiedName() + " with @Transactional");

                    // FIXME: #466
                    if (!buildFile.hasDeclaredDependencyMatchingRegex("org\\.springframework\\:spring-tx\\:.*")) {
                        addDataJpaDependency = true;
                    }
                    javaSourceWithEjb.getType().addAnnotation("@Transactional(timeout=" + timeoutInMillis + ")", "org.springframework.transaction.annotation.Transactional");

                    this.endProcess();

                } else {
                    Annotation annotation = javaSourceWithEjb.getType().getAnnotation("org.springframework.transaction.annotation.Transactional");
                    annotation.setAttribute("timeout", timeoutInMillis, Integer.class);
                }
            }
        }
        return addDataJpaDependency;
    }

    private int calclateTimeoutForSpringBoot(Integer transactionTimeoutSeconds) {
        return Integer.valueOf(transactionTimeoutSeconds) * 1000;
    }

    private void addSpringBootDataJpaDependency(BuildFile buildFile) {
        buildFile.addDependency(
                Dependency.builder()
                        .groupId("org.springframework.boot")
                        .artifactId("spring-boot-starter-data-jpa")
                        .version("2.6.3")
                        .build()
        );
    }

    private JavaSourceWithEjb findEjbByName(List<? extends JavaSource> javaSources, EjbDeploymentDescriptor.EnterpriseBean ejb) {

        for (JavaSource js : javaSources) {
            Optional<JavaSourceWithEjb> sourceWithMatchingEjb = findEjbByName(ejb, js);
            if (sourceWithMatchingEjb.isPresent()) {
                return sourceWithMatchingEjb.get();
            }
        }

        throw new RuntimeException("EJB with name '" + ejb.getEjbName() + "' could not be found.");
    }

    private Optional<JavaSourceWithEjb> findEjbByName(EjbDeploymentDescriptor.EnterpriseBean ejb, JavaSource js) {
        return js.getTypes().stream()
                .filter(type -> isEjbWithName(type, ejb.getEjbName()))
                .map(type -> new JavaSourceWithEjb(js, type))
                .findFirst();
    }

    private boolean isEjbWithName(Type type, String ejbName) {
        Set<String> relevantEjbTypes = Set.of("javax.ejb.Stateless", "javax.ejb.Stateful"); // TODO: handle Message Driven Beans, @LocalBean, @Singleton,... ?
        return type.getAnnotations().stream()
                .filter(a -> a.getFullyQualifiedName() != null)
                .filter(a -> relevantEjbTypes.contains(a.getFullyQualifiedName()))
                .anyMatch(a -> this.filterByEjbName(ejbName, type, a));
    }

    private boolean filterByEjbName(String ejbName, Type type, Annotation a) {
        if (a.getAttribute("name") != null) {
            return a.getAttribute("name").printAssignmentValue().equals(ejbName);
        } else {
            String name = type.getSimpleName();
            String ejbNameSanitized = StringUtils.capitalize(ejbName.replace("-", "_"));
            return name.equals(ejbNameSanitized);
        }
    }

    @Getter
    @RequiredArgsConstructor
    private class JavaSourceWithEjb {
        private final JavaSource javaSource;
        private final Type type;
    }
}
