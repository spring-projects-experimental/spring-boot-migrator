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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.ejb.api.SessionBeanType;
import org.springframework.sbm.jee.ejb.api.EjbJarXml;
import org.springframework.sbm.jee.ejb.filter.EjbJarXmlResourceFilter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@SuperBuilder
public class MigrateEjbDeploymentDescriptor extends AbstractAction {
    @Override
    public void apply(ProjectContext context) {
        Optional<EjbJarXml> ejbJarXml = context.search(new EjbJarXmlResourceFilter());
        if (ejbJarXml.isPresent()) {
            migrateEjbDeploymentDescriptor(context, ejbJarXml.get());
        }
    }

    private void migrateEjbDeploymentDescriptor(ProjectContext context, EjbJarXml ejbJarXml) {

        List<SessionBeanType> sessionBeansToRemove = new ArrayList<>();

        if (ejbJarXml.getEjbJarXml().getEnterpriseBeans() != null) {
            ejbJarXml.getEjbJarXml().getEnterpriseBeans().getSessionOrEntityOrMessageDriven()
                    .forEach(bean -> {
                        if (bean.getClass().isAssignableFrom(SessionBeanType.class)) {
                            SessionBeanType sbt = (SessionBeanType) bean;
                            String sessionBeanType = sbt.getSessionType().getValue();
                            if ("Stateless".equals(sessionBeanType)) {
                                handleStatelessSessionBean(context, sbt);
                                // FIXME: #469
                                sessionBeansToRemove.add(sbt);
                            }
                        }
                    });
        }
        ejbJarXml.removeSessionBeans(sessionBeansToRemove);

        ejbJarXml.getEjbJarXml().getEnterpriseBeans().getSessionOrEntityOrMessageDriven()
                .forEach(bean -> {
                    if (bean.getClass().isAssignableFrom(SessionBeanType.class)) {
                        SessionBeanType sbt = (SessionBeanType) bean;
                        ejbJarXml.removeSessionBean(sbt);
                    }
                });


        // delete deployment descriptor when empty
        if (ejbJarXml.isEmpty()) {
            ejbJarXml.delete();
        }
    }

    private void handleStatelessSessionBean(ProjectContext context, SessionBeanType sbt) {
        String fqName = sbt.getEjbClass().getValue();
        Optional<? extends JavaSource> javaSourceDeclaringType = context.getProjectJavaSources().findJavaSourceDeclaringType(fqName);
        if (javaSourceDeclaringType.isPresent()) {
            JavaSource js = javaSourceDeclaringType.get();
            Type type = js.getType(fqName);

            // remove existing @Stateless annotation if exists
            String statelessAnnotationFqName = "javax.ejb.Stateless";
            if (type.hasAnnotation(statelessAnnotationFqName)) {
                type.removeAnnotation(statelessAnnotationFqName);
            }

            // add @Stateless annotation as defined in deployment descriptor
            String snippet = "@Stateless";
            if (ejbNameGiven(sbt)) {
                // TODO: #470
                snippet += "(name=\"" + getEjbName(sbt) + "\")";
            }
            type.addAnnotation(snippet, "javax.ejb.Stateless");

        } else {
            throw new RuntimeException("Could not find any Java file declaring type '" + fqName + "'");
        }
    }

    private String getEjbName(SessionBeanType sbt) {
        return sbt.getEjbName().getValue();
    }

    private boolean ejbNameGiven(SessionBeanType sbt) {
        return sbt.getEjbName() != null && !sbt.getEjbName().getValue().isEmpty();
    }
}
