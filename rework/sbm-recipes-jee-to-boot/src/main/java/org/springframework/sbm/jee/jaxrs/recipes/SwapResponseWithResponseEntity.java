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
package org.springframework.sbm.jee.jaxrs.recipes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.Tree;
import org.openrewrite.java.*;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.MethodInvocation;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;
import org.springframework.sbm.java.migration.visitor.VisitorUtils;
import org.springframework.sbm.search.recipe.CommentJavaSearchResult;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation.methodInvocationMatcher;
import static org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation.renameMethodInvocation;

public class SwapResponseWithResponseEntity extends Recipe {

    public SwapResponseWithResponseEntity(Supplier<JavaParser> javaParserSupplier) {

        doNext(new SwapStatusForHttpStatus(javaParserSupplier));
        // #status(int)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response status(int)"), (v, m, addImport) -> {
            String args = m.getArguments().stream().map(a -> "#{any()}").collect(Collectors.joining(", "));
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(" + args + ")")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            addImport.accept("org.springframework.http.ResponseEntity");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().toArray());
        }));

        // #status(int, String)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response status(int, java.lang.String)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(#{any()})")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0)).withMarkers(m.getMarkers().add(new CommentJavaSearchResult(Tree.randomId(), "SBM FIXME: Couldn't find exact replacement for status(int, java.lang.String) - dropped java.lang.String argument")));
        }));

        // #status(Status)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response status(javax.ws.rs.core.Response.Status)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(#{any()})")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0)).withMarkers(m.getMarkers().add(new CommentJavaSearchResult(Tree.randomId(), "SBM FIXME: Couldn't find exact replacement for status(javax.ws.rs.core.Response.StatusType) - replaced with status(int)")));
        }));

        // #status(StatusType)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response status(javax.ws.rs.core.Response.StatusType)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(#{()})")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0)).withMarkers(m.getMarkers().add(new CommentJavaSearchResult(Tree.randomId(), "SBM FIXME: Couldn't find exact replacement for status(javax.ws.rs.core.Response.StatusType) - replaced with status(int)")));
        }));

        // #ok()
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response ok()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.ok()")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // #ok(Object)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response ok(java.lang.Object)"), (v, m, addImport) -> {
            List<Expression> args = m.getArguments();
            if(J.Literal.class.isInstance(m.getArguments().get(0))) {
                JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.ok()")
                        .imports("org.springframework.http.ResponseEntity")
                        .build();
                addImport.accept("org.springframework.http.ResponseEntity");
                v.maybeRemoveImport("javax.ws.rs.core.Response");
                m = m.withTemplate(template, m.getCoordinates().replace());
                markTopLevelInvocationWithTemplate(v, m, args.get(0).print());
            }
            return m;
        }));

        // #ok(Object, MediaType)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response ok(java.lang.Object, javax.ws.rs.core.MediaType)"), (v, m, addImport) -> {
            List<Expression> args = m.getArguments();
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.ok().contentType(#{any()})")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            markTopLevelInvocationWithTemplate(v, m, args.get(0).print());
            return m.withTemplate(template, m.getCoordinates().replace(), args.get(1));
        }));

        // #ok(Object, String)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response ok(java.lang.Object, java.lang.String)"), (v, m, addImport) -> {
            List<Expression> args = m.getArguments();
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.ok().contentType(MediaType.parseMediaType(#{any()}))")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.MediaType")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            addImport.accept("org.springframework.http.MediaType");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            markTopLevelInvocationWithTemplate(v, m, args.get(0).print());
            return m.withTemplate(template, m.getCoordinates().replace(), args.get(1));
        }));

        // #accepted()
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response accepted()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.accepted()")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // #accepted(Object)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response accepted(java.lang.Object)"), (v, m, addImport) -> {
            List<Expression> args = m.getArguments();
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.accepted()")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            m = m.withTemplate(template, m.getCoordinates().replace());
            markTopLevelInvocationWithTemplate(v, m, args.get(0).print());
            return m;
        }));

        // #created(URI)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response created(java.net.URI)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.created(#{any()})")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0));
        }));

        // #fromResponse(Response)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response fromResponse(javax.ws.rs.core.Response)"), (v, m, addImport) -> {
            Expression e = m.getArguments().get(0);
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(#{any()}.getStatusCode()).headers(#{any()}.getHeaders())")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            markTopLevelInvocationWithTemplate(v, m, e.print() + ".getBody()");
            return m.withTemplate(template, m.getCoordinates().replace(), e, e);
        }));

        // #noContent()
        // TODO: returns HeadersBuilder
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response noContent()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.noContent()")
                    .imports("org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // #notAcceptable(List<Variant) - migration not supported!

        // #notModified()
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response notModified()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(HttpStatus.NOT_MODIFIED)")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.HttpStatus")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // notModified(String)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response notModified(java.lang.String)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(#{any()})")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.HttpStatus")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            addImport.accept("org.springframework.http.HttpStatus");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0));
        }));

        // notModified(EntityTag) - migration not supported

        // #seeOther(URI)
        // TODO: Returns BodyBuilder
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response seeOther(java.net.URI)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(HttpStatus.SEE_OTHER).location(#{any()})")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.HttpStatus")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            addImport.accept("org.springframework.http.HttpStatus");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0));
        }));

        // #serverError()
        // Returns BodyBuilder
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response serverError()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(HttpStatus.SERVER_ERROR)")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.HttpStatus")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            addImport.accept("org.springframework.http.HttpStatus");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // #temporaryRedirect(URI)
        // TODO: Returns BodyBuilder
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response temporaryRedirect(java.net.URI)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(#{any()})")
                    .imports("org.springframework.http.ResponseEntity", "org.springframework.http.HttpStatus")
                    .build();
            addImport.accept("org.springframework.http.ResponseEntity");
            addImport.accept("org.springframework.http.HttpStatus");
            v.maybeRemoveImport("javax.ws.rs.core.Response");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0));
        }));

        // INSTANCE METHODS

        // #getAllowedMethods()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getAllowedMethods()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getAllow().stream().map(m -> m.toString()).collect(Collectors.toList())")
                    .imports("java.util.stream.Collectors", "org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("java.util.stream.Collectors");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getDate()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getDate()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "new Date(#{any(org.springframework.http.ResponseEntity)}.getHeaders().getDate())")
                    .imports("java.util.Date", "org.springframework.http.ResponseEntity")
                    .build();
            addImport.accept("java.util.Date");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getEntity()
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getEntity()"), "getBody", "org.springframework.http.ResponseEntity"));

        // #getEntityTag()
        // TODO: return type not EntityTag but String after migration
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getEntityTag()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getETag()")
                    .build();
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getHeaderString(String)
        doNext(new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getHeaderString(java.lang.String)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().get(#{any()}).stream().collect(Collectors.joining(\", \"))")
                    .imports("java.util.stream.Collectors")
                    .build();
            v.maybeAddImport("java.util.stream.Collectors");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
        }));

        // #getMetadata()
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getMetadata()"), "getHeaders", "org.springframework.http.ResponseEntity"));

        // #getLanguage()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getLanguage()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getContentLanguage()")
                    .build();
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getLastModified()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getLastModified()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "new Date(#{any(org.springframework.http.ResponseEntity)}.getHeaders().getLastModified())")
                    .imports("java.util.Date")
                    .build();
            addImport.accept("java.util.Date");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getLength()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getLength()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getContentLength()")
                    .build();
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getLocation()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getLocation()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getLocation()")
                    .build();
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getMediaType()
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response getMediaType()"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getHeaders().getContentType()")
                    .build();
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getStatus()
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getStatus()"), "getStatusCodeValue", "org.springframework.http.ResponseEntity"));

        // #getStatusInfo()
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getStatusInfo()"), "getStatusCode", "org.springframework.http.ResponseEntity"));

        // #getStringHeaders()
        // TODO: different return type
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response getStringHeaders()"), "getHeaders", "org.springframework.http.ResponseEntity"));

        // #hasEntity()
        doNext(renameMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response hasEntity()"), "hasBody", "org.springframework.http.ResponseEntity"));

        // #readEntity(..)
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response readEntity(..)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "#{any(org.springframework.http.ResponseEntity)}.getBody())")
                    .build();
            v.maybeRemoveImport("java.lang.annotation.Annotation");
            v.maybeRemoveImport("javax.ws.rs.core.GenericType");
            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect());
        }));

        // #getHeaders() - present on ResponseEntity but different return type. Nothing can be done about it for now...

        // #getCookies() - not implemented
        // #bufferEntity() - not implemented
        // #close() - not implemented
        // #getLink(String) - not implemented
        // #getLinkBuilder(String) - not implemented
        // #getLinks() - not implemented
        // #hasLink() - not implemented


        doNext(new ReplaceResponseEntityBuilder());

        doNext(new ChangeType("javax.ws.rs.core.Response", "org.springframework.http.ResponseEntity", false));
    }

    private void markTopLevelInvocationWithTemplate(JavaVisitor<ExecutionContext> v, MethodInvocation m, String template) {
        VisitorUtils.markWrappingInvocationWithTemplate(v, m, new MethodMatcher("javax.ws.rs.core.Response.ResponseBuilder build()"), template, this);
    }

    @Override
    public String getDisplayName() {
        return "Replace JAX-RS Response with Spring ResponseEntity";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

}
