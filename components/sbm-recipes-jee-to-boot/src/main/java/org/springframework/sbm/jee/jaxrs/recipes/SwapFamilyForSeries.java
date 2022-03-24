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
import org.springframework.sbm.java.impl.JavaParserFactory;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;

public class SwapFamilyForSeries extends Recipe {

    public SwapFamilyForSeries() {

        // All constants seem to match on both types - let ChangeType take care of type changing for field accesses
        JavaParser javaParser = JavaParserFactory.getCurrentJavaParser();
        doNext(new RewriteMethodInvocation(
                        RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.Response.Status.Family familyOf(int)"),
                        (v, m, addImport) -> {
                            JavaTemplate template = JavaTemplate.builder(() -> v.getCursor(), "HttpStatus.Series.resolve(#{any(int)})").build();
                            // v.maybeAddImport("org.springframework.http.HttpStatus.Series");
                            addImport.accept("org.springframework.http.HttpStatus");
                            return m.withTemplate(template, m.getCoordinates().replace(), m.getArguments().get(0));
                        }
                )
        );

        doNext(new ChangeType("javax.ws.rs.core.Response.Status.Family", "org.springframework.http.HttpStatus.Series", false));

    }

    @Override
    public String getDisplayName() {
        return "Swap JAX-RS Family with Spring HttpStaus.Series";
    }

}
