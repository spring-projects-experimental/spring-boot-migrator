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

package org.springframework.sbm.maven;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.MavenIsoVisitor;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.xml.ChangeTagValueVisitor;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;

@AllArgsConstructor
public class UpgradeUnmanagedSpringProject extends Recipe {

    private String springVersion;

    @Override
    public String getDisplayName() {
        return "Upgrade unmanaged spring project";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new MavenIsoVisitor<ExecutionContext>() {
            @Override
            public Xml.Tag visitTag(Xml.Tag tag, ExecutionContext executionContext) {

                if (isDependencyTag()) {
                    ResolvedDependency dependency = findDependency(tag);

                    if (dependency.getArtifactId().equals("spring-boot-starter-web")) {
                        Optional<Xml.Tag> version = tag.getChild("version");
                        if (version.isPresent()) {

                            doAfterVisit(new ChangeTagValueVisitor(version.get(), "3.0.0-M3"));
                        }
                    }
                    if (dependency.getArtifactId().equals("metrics-annotation")) {
                        Optional<Xml.Tag> version = tag.getChild("version");
                        if (version.isPresent()) {

                            doAfterVisit(new ChangeTagValueVisitor(version.get(), "4.2.9"));
                        }
                    }
                    if (dependency.getArtifactId().equals("spring-boot-starter-test")) {
                        Optional<Xml.Tag> version = tag.getChild("version");
                        if (version.isPresent()) {

                            doAfterVisit(new ChangeTagValueVisitor(version.get(), "3.0.0-M3"));
                        }
                    }
                }
                return super.visitTag(tag, executionContext);
            }
        };
    }
}
