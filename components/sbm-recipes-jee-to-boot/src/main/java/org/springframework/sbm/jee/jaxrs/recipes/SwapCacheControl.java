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
import org.openrewrite.java.JavaTemplate;
import org.springframework.sbm.java.migration.recipes.RewriteConstructorInvocation;
import org.springframework.sbm.java.migration.recipes.RewriteMethodInvocation;

import java.util.List;

public class SwapCacheControl extends Recipe {

    public SwapCacheControl() {

    }

    @Override
    public List<Recipe> getRecipeList() {
        return List.of(

                /*
                 * NOT SUPPORTED:
                 * - valueOf(String)
                 * - getCacheExtension()
                 * - getMaxAge()
                 * - getNoCacheFields()
                 * - getPrivateFields()
                 * - getSMaxAge()
                 * - isMustRevalidate()
                 * - isNoCache()
                 * - isNoStore()
                 * - isNoTransform()
                 * - isPrivate()
                 * - isProxyRevalidate()
                 * - setMaxAge(int)
                 * - setMustRevalidate(boolean)
                 * - setNoCache(boolean)
                 * - setNoStore(boolean)
                 * - setNoTransform(boolean)
                 * - setPrivate(boolean)
                 * - setProxyRevalidate(boolean)
                 */

                // setSMaxAge(int)
                new RewriteMethodInvocation(RewriteMethodInvocation.methodInvocationMatcher("javax.ws.rs.core.CacheControl setSMaxAge(int)"), (v, m, addImport) -> {
                    JavaTemplate t = JavaTemplate.builder("#{any(org.springframework.http.CacheControl)}.sMaxAge(#{any(int)}, TimeUnit.SECONDS)")
                            .imports("java.util.concurrent.TimeUnit", "org.springframework.http.CacheControl")
                            .build();
                    addImport.accept("java.util.concurrent.TimeUnit");
                    return t.apply(v.getCursor(), m.getCoordinates().replace(), m.getSelect(), m.getArguments().get(0));
                }),

                // constructor
                new RewriteConstructorInvocation(RewriteConstructorInvocation.constructorMatcher("javax.ws.rs.core.CacheControl"), (v, c, addImport) -> {
                    JavaTemplate t = JavaTemplate.builder("CacheControl.empty()")
                            .imports("org.springframework.http.CacheControl")
                            .build();
                    addImport.accept("org.springframework.http.CacheControl");
                    v.maybeRemoveImport("javax.ws.rs.core.CacheControl");
                    return t.apply(v.getCursor(), c.getCoordinates().replace());
                }),

                new ChangeType("javax.ws.rs.core.CacheControl", "org.springframework.http.CacheControl", false)
        );
    }

    @Override
    public String getDisplayName() {
        return "Swap JAX-RS CacheControl with Spring CacheControl";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

}
