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
package org.springframework.sbm.support.openrewrite.maven;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.maven.AddDependency;
import org.openrewrite.maven.AddDependencyVisitor;
import org.openrewrite.maven.tree.Maven;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Overwrites {@code AddDependency} to allow adding dependencies without defining {@code onlyIfUsing}.
 */
public class AlwaysAddDependency extends AddDependency {

    public AlwaysAddDependency(
            String groupId,
            String artifactId,
            String version,
            @Nullable String versionPattern,
            @Nullable String scope,
            @Nullable boolean releasesOnly,
//            String onlyIfUsing,
            @Nullable String type,
            @Nullable String classifier,
            @Nullable Boolean optional,
            @Nullable String familyPattern
    ) {
        super(
                groupId,
                artifactId,
                version,
                versionPattern,
                scope,
                releasesOnly,
                "*",
                type,
                classifier,
                optional,
                familyPattern
        );
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return null;
    }

    @Override
    protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {
        Map<JavaProject, String> scopeByProject = new HashMap();
        Iterator iterator = before.iterator();

//        while(iterator.hasNext()) {
//            SourceFile source = (SourceFile)iterator.next();
//            source.getMarkers().findFirst(JavaProject.class).ifPresent((javaProject) -> {
//                source.getMarkers().findFirst(JavaSourceSet.class).ifPresent((sourceSet) -> {
//                    if (true) { //source != (new UsesType(this.onlyIfUsing)).visit(source, ctx)) {
//                        scopeByProject.compute(javaProject, (jp, scope) -> {
//                            return "compile".equals(scope) ? scope : (sourceSet.getName().equals("test") ? "test" : "compile");
//                        });
//                    }
//
//                });
//            });
//        }

//        if (scopeByProject.isEmpty()) {
//            return before;
//        } else {
        Pattern familyPatternCompiled = getFamilyPattern() == null ? null : Pattern.compile(getFamilyPattern().replace("*", ".*"));
        return ListUtils.map(before, (s) -> {
            return (SourceFile) s.getMarkers().findFirst(JavaProject.class).map((javaProject) -> {
                if (!(s instanceof Maven)) {
                    return s;
                } else {
                    for (Pom ancestor = ((Maven) s).getMavenModel().getPom(); ancestor != null; ancestor = ancestor.getParent()) {
                        Iterator var7 = ancestor.getDependencies(Scope.Compile).iterator();

                        Pom.Dependency d;
                        while (var7.hasNext()) {
                            d = (Pom.Dependency) var7.next();
                            if (getGroupId().equals(d.getGroupId()) && getArtifactId().equals(d.getArtifactId())) {
                                return s;
                            }
                        }

                        var7 = ancestor.getDependencies(Scope.Test).iterator();

                        while (var7.hasNext()) {
                            d = (Pom.Dependency) var7.next();
                            if (getGroupId().equals(d.getGroupId()) && getArtifactId().equals(d.getArtifactId())) {
                                return s;
                            }
                        }
                    }

                    String scope = getScope() == null ? (String) scopeByProject.get(javaProject) : getScope();
                    return scope == null ? s : (SourceFile) (new AddDependencyVisitor(getGroupId(), getArtifactId(), getVersion(), getVersionPattern(), scope, getReleasesOnly(), getType(), getClassifier(), getOptional(), familyPatternCompiled)).visit(s, ctx);
                }
            }).orElse(s);
        });
//        }
    }

}
