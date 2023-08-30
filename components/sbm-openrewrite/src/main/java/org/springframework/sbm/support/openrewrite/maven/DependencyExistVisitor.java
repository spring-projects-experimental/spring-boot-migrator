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
package org.springframework.sbm.support.openrewrite.maven;

import lombok.Data;
import org.openrewrite.ExecutionContext;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;

@Data
/**
 * TODO(497) remove class
 * No usages found
 * @deprecated
 */
@Deprecated(forRemoval = true)
public class DependencyExistVisitor extends MavenVisitor {

    private static final XPathMatcher DEPENDENCY_MATCHER = new XPathMatcher("//dependency");
    private static final String FOUND_DEPENDENCY_MSG = "plugin-dependency-found";

    private final Xml.Tag scope;
    private String groupId;
    private String artifactId;

    public DependencyExistVisitor(Xml.Tag tag) {
        this.scope = tag;
    }

    // FIXME(#7): Api changed
    public Xml visitTag(Xml.Tag tag, ExecutionContext ctx) {
        if (getCursor().isScopeInPath(scope)) {
            // Only for each dependency tag
            if (DEPENDENCY_MATCHER.matches(getCursor()) && hasGroupAndArtifact(groupId, artifactId)) {
                ctx.putMessage(FOUND_DEPENDENCY_MSG, true);
            }
        }
        return super.visitTag(tag, ctx);
    }

    private boolean hasGroupAndArtifact(String groupId, String artifactId) {
        Xml.Tag tag = getCursor().getValue();
        return groupId.equals(tag.getChildValue("groupId")) &&
                tag.getChildValue("artifactId")
                        .map(a -> a.equals(artifactId))
                        .orElse(artifactId == null);
    }

}