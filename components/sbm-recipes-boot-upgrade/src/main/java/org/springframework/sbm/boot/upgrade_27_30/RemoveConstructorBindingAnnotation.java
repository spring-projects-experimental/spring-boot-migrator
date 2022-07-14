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
package org.springframework.sbm.boot.upgrade_27_30;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.Comment;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Javadoc;
import org.openrewrite.java.tree.TypeUtils;
import org.openrewrite.marker.Markers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.openrewrite.Tree.randomId;

/**
 * @author Alex Boyko
 */
public class RemoveConstructorBindingAnnotation extends Recipe {

    private static final String ANNOTATION_CONSTRUCTOR_BINDING = "org.springframework.boot.context.properties.ConstructorBinding";
    private static final String ANNOTATION_CONFIG_PROPERTIES = "org.springframework.boot.context.properties.ConfigurationProperties";

    @Override
    public String getDisplayName() {
        return "Remove Unnecessary @ConstructorBinding";
    }

    @Override
    public String getDescription() {
        return "As of Boot 3.0 @ConstructorBinding is no longer needed at the type level on @ConfigurationProperties classes and should be removed.";
    }

    @Override
    public JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                List<J.MethodDeclaration> constructors = classDecl.getBody().getStatements().stream()
                        .filter(o -> o instanceof J.MethodDeclaration)
                        .map(o -> (J.MethodDeclaration) o)
                        .filter(J.MethodDeclaration::isConstructor)
                        .collect(Collectors.toList());

                if (constructors.size() == 1 &&
                        constructors.get(0).getLeadingAnnotations().stream()
                                .anyMatch(a -> TypeUtils.isOfClassType(a.getType(), ANNOTATION_CONSTRUCTOR_BINDING))) {
                    getCursor().putMessage("CONSTRUCTOR_TO_UPDATE", constructors.get(0));
                }

                J.ClassDeclaration c = super.visitClassDeclaration(classDecl, executionContext);
                if (c.getLeadingAnnotations().stream().anyMatch(a -> TypeUtils.isOfClassType(a.getType(), ANNOTATION_CONFIG_PROPERTIES))) {
                    c = c.withLeadingAnnotations(ListUtils.map(c.getLeadingAnnotations(), anno -> {
                        if (TypeUtils.isOfClassType(anno.getType(), ANNOTATION_CONSTRUCTOR_BINDING)) {
                            if (constructors.size() <= 1) {
                                maybeRemoveImport(ANNOTATION_CONSTRUCTOR_BINDING);
                                return null;
                            }

                            return anno.withComments(maybeAddComment(anno.getComments()));
                        }
                        return anno;
                    }));
                }
                return c;
            }

            private List<Comment> maybeAddComment(List<Comment> comments) {
                String message = "You need to remove ConstructorBinding on class level and move it to appropriate";
                if (comments.isEmpty() || comments.stream()
                        .filter(o -> o instanceof Javadoc.DocComment)
                        .map(o -> (Javadoc.DocComment) o)
                        .flatMap(o -> o.getBody().stream().filter(j -> j instanceof Javadoc.Text))
                        .noneMatch(o -> o.print(getCursor()).equals(message))) {

                    List<Javadoc> javadoc = new ArrayList<>();
                    javadoc.add(new Javadoc.LineBreak(randomId(), "\n * ", Markers.EMPTY));
                    javadoc.add(new Javadoc.Text(randomId(), Markers.EMPTY, "TODO:"));
                    javadoc.add(new Javadoc.LineBreak(randomId(), "\n * ", Markers.EMPTY));
                    javadoc.add(new Javadoc.Text(randomId(), Markers.EMPTY, message));
                    javadoc.add(new Javadoc.LineBreak(randomId(), "\n * ", Markers.EMPTY));
                    javadoc.add(new Javadoc.Text(randomId(), Markers.EMPTY, "constructor."));
                    javadoc.add(new Javadoc.LineBreak(randomId(), "\n ", Markers.EMPTY));

                    List<Comment> newComments = new ArrayList<>(comments);
                    newComments.add(new Javadoc.DocComment(randomId(), Markers.EMPTY, javadoc, "\n"));
                    return newComments;
                }
                return comments;
            }

            @Override
            public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, ExecutionContext executionContext) {
                J.MethodDeclaration methodDeclaration = super.visitMethodDeclaration(method, executionContext);

                J.MethodDeclaration constructorToUpdate = getCursor().getNearestMessage("CONSTRUCTOR_TO_UPDATE");
                if (method == constructorToUpdate) {
                    methodDeclaration = methodDeclaration.withLeadingAnnotations(ListUtils.map(method.getLeadingAnnotations(), anno -> {
                        if (TypeUtils.isOfClassType(anno.getType(), ANNOTATION_CONSTRUCTOR_BINDING)) {
                            maybeRemoveImport(ANNOTATION_CONSTRUCTOR_BINDING);
                            return null;
                        }
                        return anno;
                    }));
                    getCursor().pollNearestMessage("CONSTRUCTOR_TO_UPDATE");
                }
                return methodDeclaration;
            }
        };
    }

}

