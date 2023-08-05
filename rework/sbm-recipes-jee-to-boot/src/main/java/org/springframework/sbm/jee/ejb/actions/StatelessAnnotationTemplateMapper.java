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

import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;

import java.util.Map;

/**
 * Helps with the migration of {@code @Stateless} annotation to {@code @Service} annotation.
 */
public class StatelessAnnotationTemplateMapper {

    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String EJB_STATELESS_ANNOTATAION_FQN = "javax.ejb.Stateless";

    public String mapToServiceAnnotation(Annotation annotation) {

        if (!EJB_STATELESS_ANNOTATAION_FQN.equals(annotation.getFullyQualifiedName())) {
            throw new IllegalArgumentException("Passed invalid annotation '" + annotation.getFullyQualifiedName() + "'");
        }

        StringBuilder serviceAnnotationBuilder = new StringBuilder();

        Map<String, Expression> attributes = annotation.getAttributes();
        if (attributes.containsKey(DESCRIPTION_ATTRIBUTE)) {
            serviceAnnotationBuilder.append("/**").append("\n");
            String descriptionValue = attributes.get(DESCRIPTION_ATTRIBUTE).getAssignmentRightSide().printVariable();
            serviceAnnotationBuilder.append("* ").append(descriptionValue).append("\n");
            serviceAnnotationBuilder.append("*/").append("\n");
        }

        serviceAnnotationBuilder.append("@Service");

        if (attributes.containsKey(NAME_ATTRIBUTE)) {
            Expression expression = attributes.get(NAME_ATTRIBUTE);
            String nameAttributeValue = expression.getAssignmentRightSide().printVariable();
            serviceAnnotationBuilder.append("(\"").append(nameAttributeValue).append("\")");
        }

        return serviceAnnotationBuilder.toString();
    }

}
