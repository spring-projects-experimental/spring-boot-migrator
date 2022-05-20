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
import org.springframework.sbm.jee.ejb.api.DescriptionType;
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
            final StringBuilder statelessSnippet =
                    new StringBuilder("@Stateless(")
                            .append(getEjbName(sbt)
                                    .map(n -> "name=" + n + ",")
                                    .orElse("")
                            ).append(getMappedName(sbt)
                                    .map(m -> "mappedName=" + m + ",")
                                    .orElse("")
                            ).append(getDescription(sbt)
                                    .map(d -> "description=" + d)
                                    .orElse("")
                            ).append(")");

            if(statelessSnippet.length() == "@Statlesss()".length())
                statelessSnippet.deleteCharAt("@Statlesss".length() - 1);

            type.addAnnotation(statelessSnippet.toString(), "javax.ejb.Stateless");

            getRemote(sbt)
                    .map( r -> "@Remote(value=" + r + ".class)")
                    .ifPresent( a -> type.addAnnotation(a,"javax.ejb.Remote"));

            getRemoteHome(sbt)
                    .map( r -> "@RemoteHome(value=" + r + ".class)")
                    .ifPresent( a -> type.addAnnotation(a,"javax.ejb.RemoteHome"));

            getLocal(sbt)
                    .map( r -> "@Local(value=" + r + ".class)")
                    .ifPresent( a -> type.addAnnotation(a,"javax.ejb.Local"));

            getLocalHome(sbt)
                    .map( r -> "@LocalHome(value=" + r + ".class)")
                    .ifPresent( a -> type.addAnnotation(a,"javax.ejb.LocalHome"));

            getTransactionType(sbt)
                    .map( r -> "@TransactionManagement(value=javax.ejb.TransactionManagementType." + r.toUpperCase() + ")")
                    .ifPresent( a -> type.addAnnotation(a,"javax.ejb.TransactionManagement"));

        } else {
            throw new RuntimeException("Could not find any Java file declaring type '" + fqName + "'");
        }
    }

    private Optional<String> getEjbName(SessionBeanType sbt) {
        return sbt.getEjbName() != null && !sbt.getEjbName().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getEjbName().getValue())
                : Optional.empty();
    }
    private Optional<String> getMappedName(SessionBeanType sbt){
        return sbt.getMappedName() != null  && !sbt.getMappedName().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getMappedName().getValue())
                : Optional.empty();

    }

    //FIXME Add support for other languages in description. Currently description attribute of
    // @Stateless annotation is not a list
    private Optional<String> getDescription(SessionBeanType sbt) {
        return sbt.getDescription() != null ? sbt.getDescription()
                                                 .stream()
                                                 .filter( d ->"en".equalsIgnoreCase(d.getLang()))
                                                 .map(DescriptionType::getValue)
                                                 .findFirst()
                                            : Optional.empty();
    }

    private Optional<String> getRemote(SessionBeanType sbt){
        return sbt.getRemote() != null && !sbt.getRemote().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getRemote().getValue())
                : Optional.empty();
    }

    private Optional<String> getRemoteHome(SessionBeanType sbt){
        return sbt.getHome() != null && !sbt.getHome().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getHome().getValue())
                : Optional.empty();
    }

    private Optional<String> getLocal(SessionBeanType sbt){
        return sbt.getLocal() != null && !sbt.getLocal().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getLocal().getValue())
                : Optional.empty();
    }

    private Optional<String> getLocalHome(SessionBeanType sbt){
        return sbt.getLocalHome() != null && !sbt.getLocalHome().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getLocalHome().getValue())
                : Optional.empty();
    }

    private Optional<String> getTransactionType(SessionBeanType sbt){
        return sbt.getTransactionType() != null && !sbt.getTransactionType().getValue().isEmpty()
                ? Optional.ofNullable(sbt.getTransactionType().getValue())
                : Optional.empty();
    }
}
