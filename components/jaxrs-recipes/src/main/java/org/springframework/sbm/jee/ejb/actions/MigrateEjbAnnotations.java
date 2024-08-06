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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Migrates {@code @EJB} annotations to {@code @Autowired}.
 */
public class MigrateEjbAnnotations extends AbstractAction {

    public static final String EJB_ANNOTATION = "javax.ejb.EJB";
    private static final String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";
    private static final String QUALIFIER_ANNOTATION = "org.springframework.beans.factory.annotation.Qualifier";

    @Override
    public void apply(ProjectContext context) {
        context.getProjectJavaSources().list().stream()
                .map(JavaSource::getTypes)
                .flatMap(List::stream)
                .forEach(this::migrateEJBAnnotations);
    }

    private void migrateEJBAnnotations(Type type) {

        // if a setter is annotated migrate it and annotate the matching member
        List<String> fieldsWithMethodInjection = new ArrayList<>();
        type.getMethods().stream()
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> method.hasAnnotation(EJB_ANNOTATION))
                .forEach(method -> {
                    String affectedMemberName = this.migrateEjbAnnotation(method);
                    fieldsWithMethodInjection.add(affectedMemberName);
                });

        type.getMembers().stream()
                .filter(member -> member.hasAnnotation(EJB_ANNOTATION))
                .filter(member -> !fieldsWithMethodInjection.contains(member.getName()))
                .forEach(member -> this.migrateEjbAnnotation(member));

    }

    private String migrateEjbAnnotation(Method method) {
        Annotation annotation = method.getAnnotation(EJB_ANNOTATION).get();

        String autowiredAnnotationString = renderAutowiredAnnotation(annotation);
        method.removeAnnotation(annotation);
        method.addAnnotation(autowiredAnnotationString, AUTOWIRED_ANNOTATION);

        Optional<String> qualifierAnnotation = buildQualifierAnnotation(annotation);
        if (qualifierAnnotation.isPresent()) {
            method.addAnnotation(qualifierAnnotation.get(), QUALIFIER_ANNOTATION);
        }
        String memberName = method.getName().substring(3);
        memberName = Character.toLowerCase(memberName.charAt(0)) + memberName.substring(1);
        return memberName;
    }

    private void migrateEjbAnnotation(Member member) {
        Annotation annotation = member.getAnnotation(EJB_ANNOTATION);

        String autowiredAnnotationString = renderAutowiredAnnotation(annotation);
        member.removeAnnotation(annotation);
        member.addAnnotation(autowiredAnnotationString, AUTOWIRED_ANNOTATION);

        Optional<String> qualifierAnnotation = buildQualifierAnnotation(annotation);
        if (qualifierAnnotation.isPresent()) {
            member.addAnnotation(qualifierAnnotation.get(), QUALIFIER_ANNOTATION);
        }
    }

    private Optional<String> buildQualifierAnnotation(Annotation annotation) {
        if (annotation.getAttribute("beanName") != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String beanName = annotation.getAttribute("beanName").getAssignmentRightSide().printVariable();
            stringBuilder.append("@Qualifier(").append("\"").append(beanName).append("\"").append(")");
            return Optional.of(stringBuilder.toString());
        }
        return Optional.empty();
    }

    private String renderAutowiredAnnotation(Annotation annotation) {
        StringBuilder autowiredSb = new StringBuilder();
        Optional<String> comment = renderComment(annotation);

        if (comment.isPresent()) {
            autowiredSb.append(comment.get());
        }
        autowiredSb.append("@Autowired");
        return autowiredSb.toString();
    }

    private Optional<String> renderComment(Annotation annotation) {
        List<String> commentLines = new ArrayList<>();
        StringBuilder autowiredSb = new StringBuilder();

        if (annotation.getAttribute("description") != null) {
            String description = annotation.getAttribute("description").getAssignmentRightSide().printVariable();
            commentLines.add(description.trim());
        }
        if (annotation.getAttribute("lookup") != null) {
            String lookup = annotation.getAttribute("lookup").getAssignmentRightSide().printVariable();
            commentLines.add("SBM-TODO: lookup was '" + lookup.trim() + "'");
        }
        if (annotation.getAttribute("beanInterface") != null) {
            String beanInterface = annotation.getAttribute("beanInterface").getAssignmentRightSide().printVariable();
            commentLines.add("SBM-TODO: beanInterface was '" + beanInterface.trim() + "'");
        }
        if (!commentLines.isEmpty()) {
            autowiredSb.append("/*\n");
            commentLines.forEach(c -> autowiredSb.append(" * ").append(c).append("\n"));
            autowiredSb.append(" */\n");
            return Optional.of(autowiredSb.toString());
        }

        return Optional.empty();
    }

}
