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
package org.springframework.sbm.java.migration.visitor;

import org.openrewrite.*;
import org.openrewrite.internal.lang.NonNull;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.MethodDeclaration;
import org.openrewrite.java.tree.J.MethodInvocation;
import org.openrewrite.java.tree.J.Return;
import org.openrewrite.java.tree.JRightPadded;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.marker.SearchResult;
import org.springframework.sbm.java.migration.recipes.ChangeMethodReturnTypeRecipe;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class VisitorUtils {

    public static class MarkWithTemplate implements Marker {

		private final SearchResult searchResult;
		private String template;

        public MarkWithTemplate(UUID id, Recipe recipe, String template) {
			this.searchResult = new SearchResult(id, "MarkWithTemplate");
            this.template = template;
        }

        public String getTemplate() {
            return template;
        }

        public boolean equals(@Nullable final Object o) {
        	return searchResult.equals(o);
        }

        public int hashCode() {
            return searchResult.hashCode();
        }

        @NonNull
        public String toString() {
            return searchResult.toString();
        }

        @NonNull
        public SearchResult withId(final UUID id) {
            return searchResult.withId(id);
        }

        @NonNull
        public SearchResult withDescription(@Nullable final String description) {
            return searchResult.withDescription(description);
        }


        @Override
        public UUID getId() {
            return searchResult.getId();
        }
    }

    public static class AddTemplateMark extends JavaVisitor<ExecutionContext> {

        private UUID addTo;
        private String template;
        private Recipe recipe;

        public AddTemplateMark(UUID addTo, String template, Recipe recipe) {
            this.addTo = addTo;
            this.template = template;
            this.recipe = recipe;
        }

        @Override
        public @Nullable J postVisit(J tree, ExecutionContext p) {
            if (tree.getId().equals(addTo)) {
                return tree.withMarkers(tree.getMarkers().computeByType(new MarkWithTemplate(Tree.randomId(), recipe, template), (m1, m2) -> m2));
            }
            return super.postVisit(tree, p);
        }

    }

    public static class MarkReturnType implements Marker {

        private UUID id;
        private final SearchResult searchResult;
        private String expression;
        private String[] imports;

        // TODO: Remove recipe parameter
        public MarkReturnType(UUID id, Recipe recipe, @Nullable String expression, String... imports) {
            this.id = id;
            this.searchResult = new SearchResult(id, "MarkReturnType");
            this.expression = expression;
            this.imports = imports;
        }

        public MarkReturnType(UUID id, Recipe recipe, String description, String expression, String[] imports) {
            this.id = id;
            this.searchResult = new SearchResult(id, "MarkReturnType");
            this.expression = expression;
            this.imports = imports;
        }

        public String getExpression() {
            return expression;
        }

        public String[] getImports() {
            return imports;
        }

        @Override
        public UUID getId() {
            return searchResult.getId();
        }

        @Override
        public <T extends Tree> T withId(final UUID id) {
            MarkReturnType commentJavaSearchResult = this.id == id ? this : new MarkReturnType(id, null, searchResult.getDescription(), expression, imports);
            return (T) commentJavaSearchResult;
        }

    }

    public static class AdjustTypesFromExpressionMarkers extends Recipe {

        @Override
        public String getDisplayName() {
            return "Adjustt type based on marked expressions";
        }

        @Override
        protected TreeVisitor<?, ExecutionContext> getVisitor() {
            return new JavaIsoVisitor<ExecutionContext>() {

                @Override
                public Return visitReturn(Return _return, ExecutionContext p) {
                    Return r = super.visitReturn(_return, p);
                    MarkReturnType marker = r.getExpression().getMarkers().findFirst(MarkReturnType.class).orElse(null);
                    if (marker != null) {
                        removeMarker(r.getExpression(), marker);
                        MethodDeclaration method = getCursor().firstEnclosing(MethodDeclaration.class);
                        if (method != null) {
                            doAfterVisit(new ChangeMethodReturnTypeRecipe(m -> m.getId().equals(method.getId()), marker.getExpression(), marker.getImports()));
                        }
                    }
                    return r;
                }


            };
        }


    }


    public static MethodInvocation findWrappingMethodInvocation(JavaVisitor<?> visitor, MethodInvocation m, MethodMatcher matcher) {
        MethodInvocation top = m;
        Cursor c = visitor.getCursor();
        while (c.getParent().getValue() instanceof JRightPadded<?> || c.getParent().getValue() instanceof MethodInvocation) {
            c = c.getParent();
            Object o = c.getValue();
            if (o instanceof MethodInvocation) {
                MethodInvocation current = (MethodInvocation) o;
                if (matcher != null && matcher.matches(current)) {
                    return current;
                } else {
                    top = current;
                }
            }
        }
        return top;
    }

    public static void markWrappingInvocationWithTemplate(JavaVisitor<ExecutionContext> v, MethodInvocation m, MethodMatcher matcher, String template, Recipe r) {
        MethodInvocation top = findWrappingMethodInvocation(v, m, matcher);
        if (top != null && top != m) {
            doAfterVisit(v, new AddTemplateMark(top.getId(), template, r));
        }
    }

    public static void doAfterVisit(TreeVisitor<?, ?> host, TreeVisitor<?, ?> v) {
        try {
            Method m = TreeVisitor.class.getDeclaredMethod("doAfterVisit", TreeVisitor.class);
            if (m != null) {
                m.setAccessible(true);
                m.invoke(host, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends J> T removeMarker(T node, Marker marker) {
        return node.withMarkers(Markers.build(node.getMarkers().entries().stream().filter(m -> !Objects.equals(m, marker)).collect(Collectors.toList())));
    }

}
