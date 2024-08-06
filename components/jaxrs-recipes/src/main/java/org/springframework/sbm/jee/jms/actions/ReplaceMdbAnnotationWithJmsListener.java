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
package org.springframework.sbm.jee.jms.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.Annotation;
import org.springframework.sbm.java.api.Expression;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplaceMdbAnnotationWithJmsListener extends AbstractAction {

    @Override
    public void apply(ProjectContext context) {
        for (JavaSource js : context.getProjectJavaSources().list()) {
            for (Type t : js.getTypes()) {
                if (t.hasAnnotation("javax.ejb.MessageDriven")
                        && t.isTypeOf("javax.jms.MessageListener")) {
                    transform(js, t);
                }
            }
            js.replaceLiteral(String.class, (old) -> {
                if (old instanceof String) {
                    return old.replace("java:app/jms/", "");
                }
                return old;
            });
        }
    }

    private void transform(JavaSource js, Type t) {
        Map<String, Expression> mdbProperties = new HashMap<>();
        t.getAnnotations().stream().filter(a -> "javax.ejb.MessageDriven".equals(a.getFullyQualifiedName())).findFirst().ifPresent(a -> {
            Expression e = a.getAttribute("activationConfig");
            if (e != null) {
                List<Annotation> annotations = js.getAnnotations("javax.ejb.ActivationConfigProperty", e);
                for (Annotation annotation : annotations) {
                    Expression key = annotation.getAttribute("propertyName");
                    Expression value = annotation.getAttribute("propertyValue");
                    mdbProperties.put(key.getAssignmentRightSide().print(), value.getAssignmentRightSide());
                }
            }
        });

        t.removeAnnotation("javax.ejb.MessageDriven");

        t.removeImplements("javax.jms.MessageListener");

        t.addAnnotation("@Component", "org.springframework.stereotype.Component");

        t.getMethods().stream()
                .filter(method -> "onMessage".equals(method.getName()) && method.getParams().size() == 1)
                .findFirst()
                .ifPresent(method -> {
                    boolean shouldAddJmsListener = true;
                    for (Annotation oldAnnotation : method.getAnnotations()) {
                        switch (oldAnnotation.getFullyQualifiedName()) {
                            case "java.lang.Override":
                                method.removeAnnotation(oldAnnotation);
                                break;
                            case "org.springframework.jms.annotation.JmsListener":
                                shouldAddJmsListener = false;
                                break;
                            default:
                        }
                    }
                    if (shouldAddJmsListener) {
                        Expression e = mdbProperties.containsKey("\"destination\"") ? mdbProperties.get("\"destination\"") : mdbProperties.get("\"destinationLookup\"");
                        if (e != null) {
                            method.addAnnotation("@JmsListener(destination = " + e.print() + ")", "org.springframework.jms.annotation.JmsListener");
                        } else {
                            method.addAnnotation("@JmsListener", "org.springframework.jms.annotation.JmsListener");
                        }
                    }
                });

    }

}
