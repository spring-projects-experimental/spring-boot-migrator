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
package org.springframework.sbm.jee.web.conditions;

import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.migration.conditions.HasTypeAnnotation;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.ArrayList;
import java.util.List;

public class ShouldAddServletComponentScan implements Condition {

    @Override
    public String getDescription() {
        return "It returns true if the application:\n " +
                "\t1. Is Spring boot application\n" +
                "\t2. Uses any of these annotations [@WebListener, @WebServlet, @WebFilter]\n" +
                "\t3. Doesn't contain @ServletComponentScan";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        Condition isSpringBootApplicationCondition = createIsSpringBootApplicationCondition();
        Condition annotatedWithServletScanComponentCondition = createAnnotatedWithServletScanComponentCondition();
        List<? extends Condition> hasWebComponentAnnotationConditions = createHasWebComponentAnnotation();

        boolean isSpringBootApplication = isSpringBootApplicationCondition.evaluate(context);
        boolean isNotAnnotatedWithServletComponent = !annotatedWithServletScanComponentCondition.evaluate(context);
        boolean anyWebComponentAnnotationExists = hasWebComponentAnnotationConditions.stream().anyMatch(c -> c.evaluate(context));

        return isSpringBootApplication && isNotAnnotatedWithServletComponent && anyWebComponentAnnotationExists;
    }

    HasTypeAnnotation createIsSpringBootApplicationCondition() {
        HasTypeAnnotation hasTypeAnnotation = new HasTypeAnnotation();
        hasTypeAnnotation.setAnnotation("org.springframework.boot.autoconfigure.SpringBootApplication");
        return hasTypeAnnotation;
    }

    HasTypeAnnotation createAnnotatedWithServletScanComponentCondition() {
        HasTypeAnnotation hasTypeAnnotation = new HasTypeAnnotation();
        hasTypeAnnotation.setAnnotation("org.springframework.boot.web.servlet.ServletComponentScan");
        return hasTypeAnnotation;
    }

    List<HasTypeAnnotation> createHasWebComponentAnnotation() {
        List<String> webComponentAnnotations = List.of(
                "javax.servlet.annotation.WebListener",
                "javax.servlet.annotation.WebServlet",
                "javax.servlet.annotation.WebFilter");

        List<HasTypeAnnotation> conditions = new ArrayList<>();
        for (String webComponentAnnotation : webComponentAnnotations) {
            HasTypeAnnotation hasTypeAnnotation = new HasTypeAnnotation();
            hasTypeAnnotation.setAnnotation(webComponentAnnotation);
            conditions.add(hasTypeAnnotation);
        }
        return conditions;
    }


}
