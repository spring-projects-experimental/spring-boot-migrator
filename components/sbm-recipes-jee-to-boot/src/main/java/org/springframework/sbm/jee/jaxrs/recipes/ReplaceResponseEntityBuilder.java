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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaTemplate.Builder;
import org.openrewrite.java.MethodMatcher;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;
import org.springframework.sbm.java.migration.visitor.VisitorUtils;
import org.springframework.sbm.java.migration.visitor.VisitorUtils.AdjustTypesFromExpressionMarkers;
import org.springframework.sbm.java.migration.visitor.VisitorUtils.MarkReturnType;
import org.springframework.sbm.java.migration.visitor.VisitorUtils.MarkWithTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation.methodInvocationMatcher;
import static org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation.renameMethodInvocation;

public class ReplaceResponseEntityBuilder extends Recipe {

    public ReplaceResponseEntityBuilder() {
        // #allow(String...)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder allow(java.lang.String...)"),
                        (v, m, addImport) -> {
                            String transformedArgs = m.getArguments().stream().map(arg -> "HttpMethod.resolve(#{any()})").collect(Collectors.joining(", "));
                            JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.allow(" + transformedArgs + ")").imports("org.springframework.http.HttpMethod", "org.springframework.http.ResponseEntity.HeadersBuilder").build();
//			v.maybeAddImport("org.springframework.http.HttpMethod");
                            addImport.accept("org.springframework.http.HttpMethod");
                            List<Object> parameters = new ArrayList<Object>();
                            parameters.add(m.getSelect());
                            parameters.addAll(m.getArguments());
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), parameters.toArray());
                        }
                )
        );

        // #allow(Set<String>)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder allow(java.util.Set)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate
                                    .builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.allow(#{any()}.stream().map(HttpMethod::resolve).toArray(String[]::new))")
                                    .imports("org.springframework.http.HttpMethod", "org.springframework.http.ResponseEntity.HeadersBuilder")
                                    .build();

                            addImport.accept("org.springframework.http.HttpMethod");
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #cacheControl(CacheControl)

        // #encoding(String)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder encoding(java.lang.String)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate
                                    .builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.header(HttpHeaders.CONTENT_ENCODING, #{any()})")
                                    .imports("org.springframework.http.HttpHeaders", "org.springframework.http.ResponseEntity.HeadersBuilder")
                                    .build();
                            addImport.accept("org.springframework.http.HttpHeaders");
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments());
                        }
                )
        );

        // #contentLocation(URI)
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder contentLocation(java.net.URI)"), "location", "org.springframework.http.ResponseEntity.HeadersBuilder"));

        // #tag(String)
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder tag(java.lang.String)"), "eTag", "org.springframework.http.ResponseEntity.HeadersBuilder"));

        // #entity(Object)
        // #entity(Object, Annotation[])
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder entity(java.lang.Object, ..)"),
                        (v, m, addImport) -> {
                            VisitorUtils.markWrappingInvocationWithTemplate(v, m, new MethodMatcher("javax.ws.rs.core.Response.ResponseBuilder build()"), m.getArguments().get(0).print(), this);
                            JavaTemplate javaTemplate = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}").build();
                            return javaTemplate.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect());
                        }
                )
        );

        // #expires(Date)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder expires(java.util.Date)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate
                                    .builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.headers(h -> h.setExpires(#{any()}.toInstant()))")
                                    .build();
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #language(String)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder language(java.lang.String)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.headers(h -> h.set(HttpHeaders.CONTENT_LANGUAGE, #{any()}))")
                                    .imports("org.springframework.http.HttpHeaders")
                                    .build();
                            addImport.accept("org.springframework.http.HttpHeaders");
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #language(Locale)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder language(java.util.Locale)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.headers(h -> h.setContentLanguage(#{any()}))")
                                    .build();
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #lastModified(Date)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder lastModified(java.util.Date)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.lastModified(#{any()}.toInstant())")
                                    .build();
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #location(URI) - present on Spring ResponseEntity builder classes, nothing to do

        // replaceAll(MultivaluedMap)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder replaceAll(javax.ws.rs.core.MultivaluedMap)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate.builder("""
                                            #{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.headers(h -> {
                                                h.clear();
                                                h.addAll(#{any()});
                                            })
                                            """)
                                    .imports("org.springframework.util.MultiValueMap", "org.springframework.http.ResponseEntity.HeadersBuilder")
                                    .build();
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #type(String)
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder type(java.lang.String)"),
                        (v, m, addImport) -> {
                            JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.headers(h -> h.set(HttpHeaders.CONTENT_TYPE, #{any()}))")
                                    .imports("org.springframework.http.HttpHeaders", "org.springframework.http.ResponseEntity.HeadersBuilder")
                                    .build();
                            return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                        }
                )
        );

        // #type(MediaType)
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder type(javax.ws.rs.core.MediaType)"), "contentType", "org.springframework.http.ResponseEntity.HeadersBuilder"));

        // #build()
        // FIXME: org.springframework.http.ResponseEntity.build() does not exist. Invalid: ResponseEntity r = ResponseEntity.ok("...").build();
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.ResponseBuilder build()"),
                        (v, m, addImport) -> {
                            MarkWithTemplate marker = m.getMarkers().findFirst(MarkWithTemplate.class).orElse(null);
                            if (marker != null) {
                                m = VisitorUtils.removeMarker(m, marker);
                                JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.body(#{})").build();
                                m = t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), marker.getTemplate());
                            }
                            return m.withMarkers(m.getMarkers().computeByType(new MarkReturnType(Tree.randomId(), this, "ResponseEntity", "org.springframework.http.ResponseEntity"), (o1, o2) -> o2));
                        }
                )
        );

        /*
         * NOT SUPPORTED:
         * - cookie(NewCookie)
         * - link(String, String)
         * - link(URI, String)
         * - links(Link...)
         * - status(int)
         * - status(int, String)
         * - status(Status)
         * - status(StatusType)
         * - tag(EntityTag)
         * - variant(Variant)
         * - variants(List<Variant>)
         * - variants(Variant...)
         */


        // Always should be the last for method call migration
        // Take care of #body(Object) calls that were marked and should go at the end of method invocation chain
        doNext(new RewriteMethodInvocation(
                        m -> m.getMarkers().findFirst(MarkWithTemplate.class).isPresent(),
                        (v, m, addImport) -> {
                            MarkWithTemplate marker = m.getMarkers().findFirst(MarkWithTemplate.class).orElse(null);
                            m = VisitorUtils.removeMarker(m, marker);
                            JavaTemplate javaTemplate = JavaTemplate.builder("#{any(org.springframework.http.ResponseEntity.HeadersBuilder)}.body(#{}").build();
                            return javaTemplate.apply(v.getCursor(),
                                            m.getCoordinates().replace(),
                                            m,
                                            marker.getTemplate())
                                    .withMarkers(m.getMarkers().computeByType(new MarkReturnType(Tree.randomId(), this, "ResponseEntity", "org.springframework.http.ResponseEntity"), (o1, o2) -> o2));
                        }
                )
        );

        doNext(new AdjustTypesFromExpressionMarkers());

        // Finally replace type with BodyBuilder if nothing else replaced it previously
        doNext(new ChangeType("javax.ws.rs.core.Response$ResponseBuilder", "org.springframework.http.ResponseEntity$BodyBuilder", true));

    }

    private void doNext(Recipe recipe) {
        getRecipeList().add(recipe);
    }

    @Override
    public String getDisplayName() {
        return "Replace references to JAX-RS ReplaceResponseEntityBuilder";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

}
