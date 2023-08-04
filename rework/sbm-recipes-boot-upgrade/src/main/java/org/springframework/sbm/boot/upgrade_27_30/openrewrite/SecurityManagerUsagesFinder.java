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

package org.springframework.sbm.boot.upgrade_27_30.openrewrite;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.search.FindTypes;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Statement;
import org.openrewrite.marker.SearchResult;

import java.util.UUID;

public class SecurityManagerUsagesFinder extends Recipe {

    public static final String JAVA_SECURITY_ACCESS_CONTROL_EXCEPTION = "java.security.AccessControlException";

    @Override
    public String getDisplayName() {
        return "Finds usages of SecurityManager";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<>() {
            @Override
            public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {
                // find System.getSecurityManager()

                J.MethodInvocation m = super.visitMethodInvocation(method, executionContext);
                if(m.getMethodType() != null && "java.lang.System".equals(m.getMethodType().getDeclaringType().getFullyQualifiedName()) && "getSecurityManager".equals(m.getName().getSimpleName())) {
                    return m.withMarkers(m.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "Indicator for usage for SecurityManager: calls to System.getSecurityManager()")));
                }
                return m;
            }

            @Override
            public J.Import visitImport(J.Import _import, ExecutionContext executionContext) {
                J.Import imp = super.visitImport(_import, executionContext);
                if(JAVA_SECURITY_ACCESS_CONTROL_EXCEPTION.equals(imp.getTypeName())) {
                    return imp.withMarkers(imp.getMarkers().addIfAbsent(new SearchResult(UUID.randomUUID(), "Indicator for usage for SecurityManager: import of " + JAVA_SECURITY_ACCESS_CONTROL_EXCEPTION)));
                }
                return imp;
            }
        };
    }
}
