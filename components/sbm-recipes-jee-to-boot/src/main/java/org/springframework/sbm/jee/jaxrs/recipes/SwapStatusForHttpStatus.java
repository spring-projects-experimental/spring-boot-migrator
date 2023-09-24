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
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;
import org.springframework.sbm.java.migration.recipes.openrewrite.ReplaceConstantWithAnotherConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation.methodInvocationMatcher;

public class SwapStatusForHttpStatus extends Recipe {

    private final Map<String, String> fieldsMapping;

    public SwapStatusForHttpStatus() {
        fieldsMapping = new HashMap<>();
        fieldsMapping.put("OK", "OK");
        fieldsMapping.put("ACCEPTED", "ACCEPTED");
        fieldsMapping.put("BAD_GATEWAY", "BAD_GATEWAY");
        fieldsMapping.put("BAD_REQUEST", "BAD_REQUEST");
        fieldsMapping.put("CONFLICT", "CONFLICT");
        fieldsMapping.put("CREATED", "CREATED");
        fieldsMapping.put("EXPECTATION_FAILED", "EXPECTATION_FAILED");
        fieldsMapping.put("FORBIDDEN", "FORBIDDEN");
        fieldsMapping.put("FOUND", "FOUND");
        fieldsMapping.put("GATEWAY_TIMEOUT", "GATEWAY_TIMEOUT");
        fieldsMapping.put("GONE", "GONE");
        fieldsMapping.put("HTTP_VERSION_NOT_SUPPORTED", "HTTP_VERSION_NOT_SUPPORTED");
        fieldsMapping.put("INTERNAL_SERVER_ERROR", "INTERNAL_SERVER_ERROR");
        fieldsMapping.put("LENGTH_REQUIRED", "LENGTH_REQUIRED");
        fieldsMapping.put("METHOD_NOT_ALLOWED", "METHOD_NOT_ALLOWED");
        fieldsMapping.put("MOVED_PERMANENTLY", "MOVED_PERMANENTLY");
        fieldsMapping.put("NETWORK_AUTHENTICATION_REQUIRED", "NETWORK_AUTHENTICATION_REQUIRED");
        fieldsMapping.put("NO_CONTENT", "NO_CONTENT");
        fieldsMapping.put("NOT_ACCEPTABLE", "NOT_ACCEPTABLE");
        fieldsMapping.put("NOT_FOUND", "NOT_FOUND");
        fieldsMapping.put("NOT_IMPLEMENTED", "NOT_IMPLEMENTED");
        fieldsMapping.put("NOT_MODIFIED", "NOT_MODIFIED");
        fieldsMapping.put("PARTIAL_CONTENT", "PARTIAL_CONTENT");
        fieldsMapping.put("PAYMENT_REQUIRED", "PAYMENT_REQUIRED");
        fieldsMapping.put("PRECONDITION_FAILED", "PRECONDITION_FAILED");
        fieldsMapping.put("PRECONDITION_REQUIRED", "PRECONDITION_REQUIRED");
        fieldsMapping.put("PROXY_AUTHENTICATION_REQUIRED", "PROXY_AUTHENTICATION_REQUIRED");

        // Different !!!
        fieldsMapping.put("REQUEST_ENTITY_TOO_LARGE", "PAYLOAD_TOO_LARGE");

        fieldsMapping.put("REQUEST_HEADER_FIELDS_TOO_LARGE", "REQUEST_HEADER_FIELDS_TOO_LARGE");
        fieldsMapping.put("REQUEST_TIMEOUT", "REQUEST_TIMEOUT");
        fieldsMapping.put("REQUEST_URI_TOO_LONG", "REQUEST_URI_TOO_LONG");
        fieldsMapping.put("REQUESTED_RANGE_NOT_SATISFIABLE", "REQUESTED_RANGE_NOT_SATISFIABLE");
        fieldsMapping.put("RESET_CONTENT", "RESET_CONTENT");
        fieldsMapping.put("SEE_OTHER", "SEE_OTHER");
        fieldsMapping.put("SERVICE_UNAVAILABLE", "SERVICE_UNAVAILABLE");
        fieldsMapping.put("TEMPORARY_REDIRECT", "TEMPORARY_REDIRECT");
        fieldsMapping.put("TOO_MANY_REQUESTS", "TOO_MANY_REQUESTS");
        fieldsMapping.put("UNAUTHORIZED", "UNAUTHORIZED");
        fieldsMapping.put("UNSUPPORTED_MEDIA_TYPE", "UNSUPPORTED_MEDIA_TYPE");
        fieldsMapping.put("USE_PROXY", "USE_PROXY");

        fieldsMapping.forEach((key, value) -> new ReplaceConstantWithAnotherConstant("javax.ws.rs.core.Response$Status." + key, "org.springframework.http.HttpStatus." + value));
    }

    @Override
    public List<Recipe> getRecipeList() {

        return List.of(
                // Switch JAX-RS Family to Spring HttpStatus.Series
                new SwapFamilyForSeries(),


                // Instance methods
                new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.StatusType getStatusCode()")
                        .or(methodInvocationMatcher("javax.ws.rs.core.Response.Status getStatusCode()")),
                        (v, m, addImport) -> {
                            return m.withName(m.getName().withSimpleName("getValue"));
                        }),

                // Remove #toEnum() method calls - these shouldn't appear as we migrate both Jax-Rs Status and StatusType to the same HttpStatus
                new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.StatusType toEnum()").or(methodInvocationMatcher("javax.ws.rs.core.Response.Status toEnum()")), (v, m, addImport) -> {
                    JavaTemplate template = JavaTemplate.builder("#{any(org.springframework.http.HttpStatus)}").build();
                    return template.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect());
                }),

                // Switch Family to Series
                new RewriteMethodInvocation(methodInvocationMatcher("javax.ws.rs.core.Response.StatusType getFamily()").or(methodInvocationMatcher("javax.ws.rs.core.Response.Status getFamily()")), (v, m, addImport) -> {
                    return m.withName(m.getName().withSimpleName("series"));
                }),

                // getReasonPhrase() doesn't need to be migrated - same named method returning the same type

                // Type reference replacement

                new ChangeType("javax.ws.rs.core.Response$StatusType", "org.springframework.http.HttpStatus", false),
                new ChangeType("javax.ws.rs.core.Response$Status", "org.springframework.http.HttpStatus", false)
        );
    }

    @Override
    public String getDisplayName() {
        return "Swap Jax-RS Status with Spring HttpStatus";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

}
