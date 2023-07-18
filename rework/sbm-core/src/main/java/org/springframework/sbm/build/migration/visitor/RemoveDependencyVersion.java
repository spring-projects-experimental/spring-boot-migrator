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
package org.springframework.sbm.build.migration.visitor;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.*;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.RemoveContentVisitor;
import org.openrewrite.xml.tree.Xml;
import org.openrewrite.xml.tree.Xml.Tag;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = true)
public class RemoveDependencyVersion extends Recipe {

    @Option(displayName = "Group",
            description = "The first part of a dependency coordinate 'com.google.guava:guava:VERSION'.",
            example = "com.google.guava")
    String groupId;

    @Option(displayName = "Artifact",
            description = "The second part of a dependency coordinate 'com.google.guava:guava:VERSION'.",
            example = "guava")
    String artifactId;

    @Option(displayName = "Scope",
            description = "Only remove dependencies if they are in this scope. If 'runtime', this will" +
                    "also remove dependencies in the 'compile' scope because 'compile' dependencies are part of the runtime dependency set",
            valid = {"compile", "test", "runtime", "provided"},
            example = "compile",
            required = false)
    @Nullable
    String scope;

    @Override
    public String getDisplayName() {
        return "Remove Maven dependency";
    }

    @Override
    public String getDescription() {
        return "Removes a single dependency from the <dependencies> section of the pom.xml.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new RemoveDependencyVersionVisitor();
    }

    @Override
    public Validated validate() {
        return super.validate().and(Validated.test("scope", "Scope must be one of compile, runtime, test, or provided",
                scope, s -> !Scope.Invalid.equals(Scope.fromName(s))));
    }

    private class RemoveDependencyVersionVisitor extends MavenVisitor<ExecutionContext> {


        @Override
        public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
            if (isDependencyTag(groupId, artifactId)) {
                ResolvedDependency dependency = findDependency(tag);
                Optional<Tag> versionTag = tag.getChild("version");
                if (dependency != null && versionTag.isPresent()) {
                    Scope checkScope = scope != null ? Scope.fromName(scope) : null;
                    if (checkScope == null ||
                            checkScope.equals(dependency.getRequested().getScope()) ||
                            (dependency.getRequested().getScope() != null && Scope.fromName(dependency.getRequested().getScope()).isInClasspathOf(checkScope))) {
                        doAfterVisit(new RemoveContentVisitor<>(versionTag.get(), true));
                    }
                }
            }

            return super.visitTag(tag, ctx);
        }
    }
}
