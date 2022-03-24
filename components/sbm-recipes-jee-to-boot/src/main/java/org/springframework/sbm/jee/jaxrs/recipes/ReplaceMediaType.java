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

import org.openrewrite.Recipe;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeUtils;
import org.springframework.sbm.java.impl.JavaParserFactory;
import org.springframework.sbm.java.migration.recipes.FindReplaceFieldAccessors;
import org.springframework.sbm.java.migration.recipes.RewriteConstructorInvocation;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.sbm.java.migration.recipes.RewriteConstructorInvocation.constructorMatcher;

public class ReplaceMediaType extends Recipe {

    public ReplaceMediaType() {

        // Constants
        Map<String, String> mappings = new HashMap<>();
        mappings.put("APPLICATION_ATOM_XML", "APPLICATION_ATOM_XML_VALUE");
        mappings.put("APPLICATION_ATOM_XML_TYPE", "APPLICATION_ATOM_XML");

        mappings.put("APPLICATION_FORM_URLENCODED", "APPLICATION_FORM_URLENCODED_VALUE");
        mappings.put("APPLICATION_FORM_URLENCODED_TYPE", "APPLICATION_FORM_URLENCODED");

        mappings.put("APPLICATION_JSON", "APPLICATION_JSON_VALUE");
        mappings.put("APPLICATION_JSON_TYPE", "APPLICATION_JSON");

        mappings.put("APPLICATION_JSON_PATCH_JSON", "APPLICATION_JSON_PATCH_JSON_VALUE");
        mappings.put("APPLICATION_JSON_PATCH_JSON_TYPE", "APPLICATION_JSON_PATCH_JSON");

        mappings.put("APPLICATION_OCTET_STREAM", "APPLICATION_OCTET_STREAM_VALUE");
        mappings.put("APPLICATION_OCTET_STREAM_TYPE", "APPLICATION_OCTET_STREAM");

        mappings.put("APPLICATION_SVG_XML", "APPLICATION_SVG_XML_VALUE");
        mappings.put("APPLICATION_SVG_XML_TYPE", "APPLICATION_SVG_XML");

        mappings.put("APPLICATION_XHTML_XML", "APPLICATION_XHTML_XML_VALUE");
        mappings.put("APPLICATION_XHTML_XML_TYPE", "APPLICATION_XHTML_XML");

        mappings.put("APPLICATION_XML", "APPLICATION_XML_VALUE");
        mappings.put("APPLICATION_XML_TYPE", "APPLICATION_XML");

        mappings.put("MULTIPART_FORM_DATA", "MULTIPART_FORM_DATA_VALUE");
        mappings.put("MULTIPART_FORM_DATA_TYPE", "MULTIPART_FORM_DATA");

        mappings.put("SERVER_SENT_EVENTS", "TEXT_EVENT_STREAM_VALUE");
        mappings.put("SERVER_SENT_EVENTS_TYPE", "TEXT_EVENT_STREAM");

        mappings.put("TEXT_HTML", "TEXT_HTML_VALUE");
        mappings.put("TEXT_HTML_TYPE", "TEXT_HTML");

        mappings.put("TEXT_PLAIN", "TEXT_PLAIN_VALUE");
        mappings.put("TEXT_PLAIN_TYPE", "TEXT_PLAIN");

        mappings.put("TEXT_XML", "TEXT_XML_VALUE");
        mappings.put("TEXT_XML_TYPE", "TEXT_XML");

        mappings.put("WILDCARD", "ALL_VALUE");
        mappings.put("WILDCARD_TYPE", "ALL");

        doNext(new FindReplaceFieldAccessors(() -> JavaParserFactory.getCurrentJavaParser(), "javax.ws.rs.core.MediaType", "org.springframework.http.MediaType", mappings));

        doNext(new FindReplaceFieldAccessors(() -> JavaParserFactory.getCurrentJavaParser(), "javax.ws.rs.core.MediaType", "org.springframework.util.MimeType", Map.of(
                "CHARSET_PARAMETER", "PARAM_CHARSET",
                "MEDIA_TYPE_WILDCARD", "WILDCARD_TYPE"
        )));


        // instance methods
        JavaParser javaParser = JavaParserFactory.getCurrentJavaParser();
        // #isCompatible(MediaType)
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.MediaType isCompatible(javax.ws.rs.core.MediaType)"), (v, m, addImport) -> {
            JavaType type = JavaType.buildType("org.springframework.http.MediaType");
            return m
                    .withName(m.getName().withSimpleName("isCompatibleWith"))
                    .withSelect(m.getSelect().withType(type))
                    .withType(m.withDeclaringType(TypeUtils.asFullyQualified(type)).getType())
                    .withArguments(List.of(m.getArguments().get(0).withType(type)));
        }));

        // #withCharset(String)
        doNext(new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.MediaType withCharset(java.lang.String)"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "new MediaType(#{any(org.springframework.http.MediaType)}, Charset.forName(#{any(java.lang.String)}))")
                    .imports("org.springframework.http.MediaType", "java.nio.charset.Charset")
                    .build();
            addImport.accept("java.nio.charset.Charset");
            addImport.accept("org.springframework.http.MediaType");

            return m.withTemplate(template, m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
        }));

        // #getParameters() - comes with org.springframework.util.MimeType#getParameters()
        // #getSubtype() - comes with org.springframework.util.MimeType#getSubtype()
        // #getType() - comes with org.springframework.util.MimeType#getType()
        // #isWildcardSubtype() - comes with org.springframework.util.MimeType#isWildcardSubtype()
        // #isWildcardType() - comes with org.springframework.util.MimeType#isWildcardType()

        // static methods

        // #valueOf(String) present on Spring MediaType

        // constructors

        // MediaType() -> new MediaType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE)
        doNext(new RewriteConstructorInvocation(constructorMatcher("javax.ws.rs.core.MediaType"), (v, m, addImport) -> {
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "new MediaType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE)")
                    .imports("org.springframework.http.MediaType", "org.springframework.util.MimeType")
                    .build();
            addImport.accept("org.springframework.util.MimeType");
            addImport.accept("org.springframework.http.MediaType");

            return m.withTemplate(template, m.getCoordinates().replace());
        }));

        // MediaType(String, String) - present on Spring MediaType
        doNext(new RewriteConstructorInvocation(constructorMatcher("javax.ws.rs.core.MediaType", "java.lang.String", "java.lang.String"), (v, m, addImport) -> {
            JavaType type = JavaType.buildType("org.springframework.http.MediaType");
            return m.withType(type).withConstructorType(m.getConstructorType().withDeclaringType(TypeUtils.asFullyQualified(type)));
        }));

        // MediaType(String, String, String) -> MediaType(String, String, Charset)
        doNext(new RewriteConstructorInvocation(constructorMatcher("javax.ws.rs.core.MediaType", "java.lang.String", "java.lang.String", "java.lang.String"), (v, m, addImport) -> {
            List<Expression> arguments = m.getArguments();
            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "new MediaType(#{any(java.lang.String)}, #{any(java.lang.String)}, Charset.forName(#{any(java.lang.String)}))")
                    .imports("org.springframework.http.MediaType", "java.nio.charset.Charset")
                    .build();
            addImport.accept("java.nio.charset.Charset");
            addImport.accept("org.springframework.http.MediaType");

            return m.withTemplate(template, m.getCoordinates().replace(), arguments.get(0), arguments.get(1), arguments.get(2));
        }));

        // MediaType(String, String, Map<String, String>) - present on Spring MediaType
        doNext(new RewriteConstructorInvocation(constructorMatcher("javax.ws.rs.core.MediaType", "java.lang.String", "java.lang.String", "java.util.Map"), (v, m, addImport) -> {
            JavaType type = JavaType.buildType("org.springframework.http.MediaType");
            return m.withType(type).withConstructorType(m.getConstructorType().withDeclaringType(TypeUtils.asFullyQualified(type)));
        }));

        // Type references
        doNext(new ChangeType("javax.ws.rs.core.MediaType", "org.springframework.http.MediaType", false));
    }

    @Override
    public String getDisplayName() {
        return "Replace JAX-RS MediaType with Spring MediaType";
    }

}
